package com.alibaba.android.arouter.launcher


import android.app.Activity
import android.app.Application
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.alibaba.android.arouter.core.LogisticsCenter
import com.alibaba.android.arouter.exception.HandlerException
import com.alibaba.android.arouter.exception.InitException
import com.alibaba.android.arouter.exception.NoRouteFoundException
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.callback.NavigationCallback
import com.alibaba.android.arouter.facade.enums.RouteType
import com.alibaba.android.arouter.facade.service.DegradeService
import com.alibaba.android.arouter.facade.service.InterceptorService
import com.alibaba.android.arouter.facade.service.PathReplaceService
import com.alibaba.android.arouter.utils.Consts
import com.alibaba.android.arouter.utils.TextUtils
import name.zeno.android.core.navigator

/**
 * [_ARouter] 不允许修改，copy一份修改内容
 *
 * @version 1.0
 * @since 16/8/16 14:39
 */
class _ARouterX private constructor() {

  /**
   * Build postcard by path and default group
   */
  internal fun build(path: String): Postcard {
    var path = path
    if (TextUtils.isEmpty(path)) {
      throw HandlerException(Consts.TAG + "Parameter is invalid!")
    } else {
      val pService = ARouter.getInstance().navigation(PathReplaceService::class.java)
      if (null != pService) {
        path = pService.forString(path)
      }
      return build(path, extractGroup(path))
    }
  }

  /**
   * Build postcard by path and group
   */
  internal fun build(path: String, group: String?): Postcard {
    var path = path
    if (TextUtils.isEmpty(path) || TextUtils.isEmpty(group)) {
      throw HandlerException(Consts.TAG + "Parameter is invalid!")
    } else {
      val pService = ARouter.getInstance().navigation(PathReplaceService::class.java)
      if (null != pService) {
        path = pService.forString(path)
      }
      return Postcard(path, group)
    }
  }

  /**
   * Extract the default group from path.
   */
  private fun extractGroup(path: String): String? {
    if (TextUtils.isEmpty(path) || !path.startsWith("/")) {
      throw HandlerException(Consts.TAG + "Extract the default group failed, the path must be start with '/' and contain more than 2 '/'!")
    }

    try {
      val defaultGroup = path.substring(1, path.indexOf("/", 1))
      return if (TextUtils.isEmpty(defaultGroup)) {
        throw HandlerException(Consts.TAG + "Extract the default group failed! There's nothing between 2 '/'!")
      } else {
        defaultGroup
      }
    } catch (e: Exception) {
      ARouter.logger.warning(Consts.TAG, "Failed to extract default group! " + e.message)
      return null
    }
  }

  internal fun navigation(
      fragment: Fragment,
      postcard: Postcard,
      callback: NavigationCallback?,
      onResult: ((Boolean, Intent?) -> Unit)? = null
  ) {
    try {
      LogisticsCenter.completion(postcard)
    } catch (ex: NoRouteFoundException) {
      ARouter.logger.warning(Consts.TAG, ex.message)

      if (ARouter.debuggable()) { // Show friendly tips for user.
        Toast.makeText(mContext, "There's no route matched!\n" +
            " Path = [" + postcard.path + "]\n" +
            " Group = [" + postcard.group + "]", Toast.LENGTH_LONG).show()
      }

      if (null != callback) {
        callback.onLost(postcard)
      } else {    // No callback for this invoke, then we use the global degrade service.
        val degradeService = ARouter.getInstance().navigation(DegradeService::class.java)
        degradeService?.onLost(fragment.activity, postcard)
      }

      return
    }

    callback?.onFound(postcard)

    if (postcard.isGreenChannel) {
      _navigation(fragment, postcard, callback, onResult)
    } else {
      // It must be run in async thread, maybe interceptor cost too mush time made ANR.
      interceptorService.doInterceptions(postcard, object : InterceptorCallback {
        override fun onContinue(postcard: Postcard) {
          _navigation(fragment, postcard, callback, onResult)
        }

        override fun onInterrupt(exception: Throwable) {
          callback?.onInterrupt(postcard)
          ARouter.logger.info(Consts.TAG, "Navigation failed, termination by interceptor : " + exception.message)
        }
      })
    }
  }

  private fun _navigation(
      fragment: Fragment,
      postcard: Postcard,
      callback: NavigationCallback?,
      onResult: ((Boolean, Intent?) -> Unit)? = null
  ) {
    val currentContext = fragment.activity ?: mContext

    if (postcard.type != RouteType.ACTIVITY) {
      TODO("only support nav activity")
    }

    // Build intent
    val intent = Intent(currentContext, postcard.destination)
    intent.putExtras(postcard.extras)

    // Set flags.
    val flags = postcard.flags
    if (-1 != flags) {
      intent.flags = flags
    } else if (currentContext !is Activity) {    // Non activity, need less one flag.
      intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }

    // Navigation in main looper.
    Handler(Looper.getMainLooper()).post {
      if (onResult != null) {  // Need start for result
        val next = { ok: Boolean, intentData: Intent? -> onResult(ok, intentData) }
        fragment.navigator().startActivityForResult(intent, next)
      } else {
        fragment.startActivity(intent)
      }

      if ((0 != postcard.enterAnim || 0 != postcard.exitAnim) && currentContext is Activity) {    // Old version.
        currentContext.overridePendingTransition(postcard.enterAnim, postcard.exitAnim)
      }

      callback?.onArrival(postcard)
    }
  }

  companion object {
    @Volatile
    private var instance: _ARouterX? = null
    @Volatile
    private var hasInit = false
    private var mContext: Context? = null

    private lateinit var interceptorService: InterceptorService

    @Synchronized
    internal fun init(application: Application): Boolean {
      mContext = application
      hasInit = true
      // Trigger interceptor init, use byName.
      interceptorService = ARouter.getInstance().build("/arouter/service/interceptor").navigation() as InterceptorService
      return true
    }

    internal fun getInstance(): _ARouterX {
      if (!hasInit) {
        throw InitException("ARouterCore::Init::Invoke init(context) first!")
      } else {
        return instance ?: synchronized(_ARouterX::class.java) {
          val routerX: _ARouterX = instance ?: _ARouterX()
          instance = routerX
          routerX
        }
      }
    }
  }
}


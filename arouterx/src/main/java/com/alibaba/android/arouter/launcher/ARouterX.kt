package com.alibaba.android.arouter.launcher

import android.app.Application
import android.app.Fragment
import android.content.Intent
import com.alibaba.android.arouter.exception.InitException
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavigationCallback
import name.zeno.android.core.initInIOThread

/**
 * 扩展 [ARouter], 使其支持 zako 库的跳转
 * @since 2018-05-28 10:30:25
 */
class ARouterX private constructor() {
  private fun build(path: String): Postcard {
    return _ARouterX.getInstance().build(path)
  }

  fun navigation(
      fragment: Fragment,
      postcard: Postcard,
      callback: NavigationCallback?,
      onResult: ((Boolean, Intent?) -> Unit)? = null
  ): Any? {
    return _ARouterX.getInstance().navigation(fragment, postcard, callback, onResult)
  }

  companion object {
    @Volatile
    private var instance: ARouterX? = null
    @Volatile
    private var hasInit = false

    /**
     * Init, it must be call before used router.
     */
    fun init(application: Application) {
      initInIOThread("ARouter") {
        ARouter.init(application)
        _ARouterX.init(application)
        hasInit = true
      }
    }

    /**
     * Build the roadmap, draw a postcard.
     *
     * @param path Where you go.
     */
    fun build(path: String): Postcard = getInstance().build(path)

    /**
     * Get instance of router. A
     * All feature U use, will be starts here.
     */
    fun getInstance(): ARouterX {
      if (!hasInit) {
        throw InitException("ARouterX::Init::Invoke init(context) first!")
      } else {
        if (instance == null) {
          synchronized(ARouterX::class.java) {
            if (instance == null) {
              instance = ARouterX()
            }
          }
        }
        return instance!!
      }
    }
  }
}

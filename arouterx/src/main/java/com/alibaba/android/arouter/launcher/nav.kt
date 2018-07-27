package com.alibaba.android.arouter.launcher

import android.app.Fragment
import android.content.Intent
import android.os.Parcelable
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavigationCallback
import com.orhanobut.logger.Logger

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2018/5/26
 */
fun Postcard.nav(
    fragment: Fragment,
    callback: NavigationCallback? = null
) = ARouterX.getInstance().navigation(fragment, this, callback)

fun String.nav(
    fragment: Fragment,
    data: (Postcard.() -> Unit)? = null,
    callback: NavigationCallback? = null,
    onResult: ((Boolean, Intent?) -> Unit)? = null
) {
  val postcard = ARouter.getInstance().build(this)
  data?.invoke(postcard)
  ARouterX.getInstance().navigation(fragment, postcard, callback, onResult)
}

fun String.fragment(): Fragment? = ARouterX.build(this).navigation() as? Fragment

fun String.nav(fragment: Fragment) = ARouterX.build(this).nav(fragment)
fun String.nav(withData: (Postcard.() -> Unit)? = null) {
  try {
    val postcard = ARouterX.build(this)
    withData?.invoke(postcard)
    postcard.navigation()
  } catch (e: Exception) {
    Logger.e(e.message.orEmpty(), e)
  }
}

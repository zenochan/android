package com.alibaba.android.arouter.launcher

import android.app.Fragment
import android.os.Parcelable
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavigationCallback

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2018/5/26
 */
fun Postcard.nav(
    fragment: Fragment,
    callback: NavigationCallback? = null
) = nav<Parcelable>(fragment, callback)

fun <T : Parcelable> Postcard.nav(
    fragment: Fragment,
    callback: NavigationCallback? = null,
    onResult: ((Boolean, T?) -> Unit)? = null
) {
  ARouterX.getInstance().navigation(fragment, this, callback, onResult)
}


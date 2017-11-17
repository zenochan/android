/**
 * RxPermission Kotlin 化
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/11/17
 */

@file:Suppress("HasPlatformType", "unused")

package name.zeno.android.core

import android.app.Activity
import android.app.Fragment
import com.tbruyelle.rxpermissions2.RxPermissions

fun Activity.requestPermissions(vararg permissions: String) = RxPermissions(this).request(*permissions)

/**
 * - 不能在 onCreateView 中调用
 * - 不能在 onActivityCreated 中调用
 */
fun Fragment.requestPermissions(vararg permissions: String) = RxPermissions(activity).request(*permissions)

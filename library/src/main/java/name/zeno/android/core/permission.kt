/**
 * RxPermission Kotlin 化
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/11/17
 */

package name.zeno.android.core

import android.annotation.TargetApi
import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.content.ContextCompat
import io.reactivex.Observable
import name.zeno.android.presenter.LifecycleListener
import name.zeno.android.presenter.ZFragment
import name.zeno.android.system.ZPermission
import name.zeno.android.system.permission.RxPermissions

fun Activity.rxPermissions(vararg permissions: String) = RxPermissions(this).request(*permissions)

fun Fragment.rxPermissions(vararg permissions: String): Observable<Boolean> {
  return Observable.create<Boolean> { e ->
    // 确保在正确的周期执行请求
    if (isResumed) {
      e.onNext(true)
      e.onComplete()
    } else {

      val nav = when {
        this is ZFragment -> this
        sdkInt < O -> navigator()
        else -> throw IllegalStateException("you can't request permission before onResume() or after onPaused()")
      }

      nav.registerLifecycleListener(object : LifecycleListener {
        override fun onResume() {
          nav.unregisterLifecycleListener(this)
          e.onNext(true)
          e.onComplete()
        }
      })
    }
  }.flatMap {
    RxPermissions(this).request(*permissions)
  }
}

fun Fragment.isPermissionRevoked(permission: String) = activity.isPermissionRevoked(permission)

/**
 * 检测指定权限是否被策略撤销。通常，设备主人或配置文件拥有者(如各种厂商)可能会使用某些策略
 * (如华为不允许其他应用商店安装应用)。用户无法授予被策略取消的权限，要获得权限只能通过改变策略
 * - see [PackageManager.isPermissionRevokedByPolicy]
 */
@TargetApi(Build.VERSION_CODES.M)
fun Activity.isPermissionRevoked(permission: String): Boolean =
    marshmallow && packageManager.isPermissionRevokedByPolicy(permission, packageName)


fun Fragment.isPermissionGranted(vararg permissions: String) = activity.isPermissionGranted(*permissions)
fun Context.isPermissionGranted(vararg permissions: String) = permissions.all {
  ContextCompat.checkSelfPermission(this, it) == ZPermission.GRANTED
}


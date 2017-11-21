/**
 * RxPermission Kotlin 化
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/11/17
 */

package name.zeno.android.core

import android.app.Activity
import android.app.Fragment
import io.reactivex.Observable
import name.zeno.android.presenter.LifecycleListener
import name.zeno.android.system.permission.RxPermissions

fun Activity.rxPermissions(vararg permissions: String) = RxPermissions(this).request(*permissions)

fun Fragment.rxPermissions(vararg permissions: String): Observable<Boolean> {
  if (isAdded) {
    return RxPermissions(this).request(*permissions)
  } else {
    return Observable.create<Boolean> { e ->
      // 确保在正确的周期执行请求
      if (isAdded) {
        e.onNext(true)
        e.onComplete()
      } else {
        val nav = navigator()
        nav.registerLifecycleListener(object : LifecycleListener {
          override fun onResume() {
            nav.unregisterLifecycleListener(this)
            e.onNext(true)
            e.onComplete()
          }
        })
      }
    }.flatMap {
      RxPermissions(activity).request(*permissions)
    }
  }
}


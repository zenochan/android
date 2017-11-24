/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package name.zeno.android.system.permission

import android.annotation.TargetApi
import android.app.Activity
import android.app.Fragment
import android.app.FragmentManager
import android.os.Build
import android.text.TextUtils
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.functions.Function
import io.reactivex.subjects.PublishSubject
import name.zeno.android.core.JELLY_BEAN_MR1
import name.zeno.android.core.marshmallow
import name.zeno.android.core.sdkInt
import java.util.*

class RxPermissions(fragmentManager: FragmentManager) {

  constructor(activity: Activity) : this(activity.fragmentManager)
  constructor(fragment: Fragment) : this(when {
    sdkInt > JELLY_BEAN_MR1 -> fragment.childFragmentManager
    else -> fragment.fragmentManager
  })

  private var mRxPermissionsFragment: RxPermissionsFragment

  init {
    mRxPermissionsFragment = getRxPermissionsFragment(fragmentManager)
  }

  private fun getRxPermissionsFragment(fragmentManager: FragmentManager): RxPermissionsFragment {
    var rxPermissionsFragment: RxPermissionsFragment? = fragmentManager.findFragmentByTag(TAG) as? RxPermissionsFragment
    if (rxPermissionsFragment == null) {
      rxPermissionsFragment = RxPermissionsFragment()
      fragmentManager.beginTransaction()
          .add(rxPermissionsFragment, TAG)
          .commitAllowingStateLoss()
      fragmentManager.executePendingTransactions()
    }

    return rxPermissionsFragment
  }

  fun logging(logging: Boolean) = apply {
    mRxPermissionsFragment.log = logging
  }

  /**
   * Map emitted items from the source observable into `true` if permissions in parameters
   * are granted, or `false` if not.
   *
   *
   * If one or several permissions have never been requested, invoke the related framework method
   * to ask the user if he allows the permissions.
   */
  fun <T> ensure(vararg permissions: String): ObservableTransformer<T, Boolean> {
    return ObservableTransformer { o ->
      request(o, *permissions)
          // Transform Observable<Permission> to Observable<Boolean>
          .buffer(permissions.size)
          .flatMap(Function<List<Permission>, ObservableSource<Boolean>> { permissions ->
            if (permissions.isEmpty()) {
              // Occurs during orientation change, when the subject receives onComplete.
              // In that case we don't want to propagate that empty list to the
              // subscriber, only the onComplete.
              return@Function Observable.empty()
            }
            // Return true if all permissions are granted.
            for (p in permissions) {
              if (!p.granted) {
                return@Function Observable.just(false)
              }
            }
            Observable.just(true)
          })
    }
  }

  /**
   * Map emitted items from the source observable into [Permission] objects for each
   * permission in parameters.
   *
   *
   * If one or several permissions have never been requested, invoke the related framework method
   * to ask the user if he allows the permissions.
   */
  fun <T> ensureEach(vararg permissions: String): ObservableTransformer<T, Permission> {
    return ObservableTransformer { o -> request(o, *permissions) }
  }

  /**
   * Request permissions immediately, **must be invoked during initialization phase
   * of your application**.
   */
  fun request(vararg permissions: String): Observable<Boolean> {
    return Observable.just(TRIGGER).compose(ensure(*permissions))
  }

  /**
   * Request permissions immediately, **must be invoked during initialization phase
   * of your application**.
   */
  fun requestEach(vararg permissions: String): Observable<Permission> {
    return Observable.just(TRIGGER).compose<Permission>(ensureEach(*permissions))
  }

  private fun request(trigger: Observable<*>, vararg permissions: String): Observable<Permission> {
    if (permissions.isEmpty()) {
      throw IllegalArgumentException("RxPermissions.request/requestEach requires at least one input permission")
    }

    return oneOf(trigger, pending(*permissions)).flatMap { requestImplementation(*permissions) }
  }

  private fun pending(vararg permissions: String): Observable<*> {
    for (p in permissions) {
      if (!mRxPermissionsFragment.containsByPermission(p)) {
        return Observable.empty<Any>()
      }
    }
    return Observable.just(TRIGGER)
  }

  private fun oneOf(trigger: Observable<*>?, pending: Observable<*>): Observable<*> =
      when (trigger) {
        null -> Observable.just(TRIGGER)
        else -> Observable.merge<Any>(trigger, pending)
      }

  @TargetApi(Build.VERSION_CODES.M)
  private fun requestImplementation(vararg permissions: String): Observable<Permission> {
    val list = ArrayList<Observable<Permission>>(permissions.size)
    val unrequestedPermissions = ArrayList<String>()

    // In case of multiple permissions, we create an Observable for each of them.
    // At the end, the observables are combined to have a unique response.
    // 多个权限申请，为每个权限创建一个 Observable， 最后，observables 组合成响应序列
    permissions.forEach { permission ->
      mRxPermissionsFragment.log("Requesting permission " + permission)

      if (isGranted(permission)) {
        // Already granted, or not Android M
        // Return a granted Permission object.
        // 已经授权，获取系统低于 Android M，添加到已授权权限列表
        list.add(Observable.just(Permission(permission, true)))
      } else if (isRevoked(permission)) {
        // Revoked by a policy, return a denied Permission object.
        // 被策略撤销权限
        list.add(Observable.just(Permission(permission, false)))
      } else {
        // 需要用户授权
        var subject: PublishSubject<Permission>? = mRxPermissionsFragment.getSubjectByPermission(permission)
        // Create a new subject if not exists
        if (subject == null) {
          unrequestedPermissions.add(permission)
          subject = PublishSubject.create<Permission>()
          mRxPermissionsFragment.setSubjectForPermission(permission, subject!!)
        }

        list.add(subject)
      }

    }

    if (!unrequestedPermissions.isEmpty()) {
      val unrequestedPermissionsArray = unrequestedPermissions.toTypedArray()
      requestPermissionsFromFragment(unrequestedPermissionsArray)
    }
    return Observable.concat(Observable.fromIterable(list))
  }

  /**
   * Invokes Activity.shouldShowRequestPermissionRationale and wraps
   * the returned value in an observable.
   *
   *
   * In case of multiple permissions, only emits true if
   * Activity.shouldShowRequestPermissionRationale returned true for
   * all revoked permissions.
   *
   *
   * You shouldn't call this method if all permissions have been granted.
   *
   *
   * For SDK < 23, the observable will always emit false.
   */
  fun shouldShowRequestPermissionRationale(activity: Activity, vararg permissions: String): Observable<Boolean> =
      when {
        marshmallow -> Observable.just(shouldShowRequestPermissionRationaleImplementation(activity, *permissions))
        else -> Observable.just(false)
      }

  @TargetApi(Build.VERSION_CODES.M)
  private fun shouldShowRequestPermissionRationaleImplementation(activity: Activity, vararg permissions: String): Boolean {
    for (p in permissions) {
      if (!isGranted(p) && !activity.shouldShowRequestPermissionRationale(p)) {
        return false
      }
    }
    return true
  }

  @TargetApi(Build.VERSION_CODES.M)
  internal fun requestPermissionsFromFragment(permissions: Array<String>) {
    mRxPermissionsFragment.log("requestPermissionsFromFragment " + TextUtils.join(", ", permissions))
    mRxPermissionsFragment.requestPermissions(permissions)
  }

  /**
   * Returns true if the permission is already granted.
   */
  fun isGranted(permission: String): Boolean = mRxPermissionsFragment.isGranted(permission)

  /**
   * Returns true if the permission has been revoked by a policy.
   *
   *
   * Always false if SDK < 23.
   */
  fun isRevoked(permission: String): Boolean = marshmallow && mRxPermissionsFragment.isRevoked(permission)

  internal fun onRequestPermissionsResult(permissions: Array<String>, grantResults: IntArray) {
    mRxPermissionsFragment.onRequestPermissionsResult(permissions, grantResults, BooleanArray(permissions.size))
  }

  companion object {
    internal val TAG = "RxPermissions"
    internal val TRIGGER = Any()
  }
}

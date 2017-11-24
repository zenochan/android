package name.zeno.android.system.permission

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Fragment
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import io.reactivex.subjects.PublishSubject
import name.zeno.android.core.JELLY_BEAN_MR1
import name.zeno.android.core.isPermissionGranted
import name.zeno.android.core.isPermissionRevoked
import name.zeno.android.core.sdkInt
import java.util.*

/**
 * - [shouldShowRequestPermissionRationale]
 *    - 弹出的对话框会有一个类似于“拒绝后不再询问”的勾选项
 *    - 若用户打了勾，并选择拒绝，那么下次程序调用Activity.requestPermissions()方法时，将不会弹出对话框
 *    - 提示用户类似于“您已经拒绝了使用该功能所需要的权限，若需要使用该功能，请手动开启权限”的信息
 * - [onRequestPermissionsResult] 接收授权结果
 */
class RxPermissionsFragment : Fragment() {

  // Contains all the current permission requests.
  // Once granted or denied, they are removed from it.
  private val mSubjects = HashMap<String, PublishSubject<Permission>>()
  var log: Boolean = false

  @SuppressLint("NewApi")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // Cannot retain Fragment that is nested in other Fragment
    if (sdkInt < JELLY_BEAN_MR1 || parentFragment == null) {
      retainInstance = true
    }
  }

  @TargetApi(Build.VERSION_CODES.M)
  internal fun requestPermissions(permissions: Array<String>) {
    requestPermissions(permissions, PERMISSIONS_REQUEST_CODE)
  }

  @TargetApi(Build.VERSION_CODES.M)
  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    if (requestCode != PERMISSIONS_REQUEST_CODE) return


    val shouldShowRequestPermissionRationale = permissions.map {
      shouldShowRequestPermissionRationale(it)
    }.toBooleanArray()

    onRequestPermissionsResult(permissions, grantResults, shouldShowRequestPermissionRationale)
  }

  internal fun onRequestPermissionsResult(permissions: Array<String>, grantResults: IntArray, shouldShowRequestPermissionRationale: BooleanArray) {
    var i = 0
    val size = permissions.size
    while (i < size) {
      log("onRequestPermissionsResult  " + permissions[i])
      // Find the corresponding subject
      val subject = mSubjects[permissions[i]]
      if (subject == null) {
        // No subject found
        Log.e(RxPermissions.TAG, "RxPermissions.onRequestPermissionsResult invoked but didn't find the corresponding permission request.")
        return
      }
      mSubjects.remove(permissions[i])
      val granted = grantResults[i] == PackageManager.PERMISSION_GRANTED
      subject.onNext(Permission(permissions[i], granted, shouldShowRequestPermissionRationale[i]))
      subject.onComplete()
      i++
    }
  }

  internal fun isGranted(permission: String): Boolean = activity.isPermissionGranted(permission)

  @TargetApi(Build.VERSION_CODES.M)
  internal fun isRevoked(permission: String): Boolean = activity.isPermissionRevoked(permission)

  fun getSubjectByPermission(permission: String): PublishSubject<Permission>? = mSubjects[permission]
  fun containsByPermission(permission: String): Boolean = mSubjects.containsKey(permission)

  fun setSubjectForPermission(permission: String, subject: PublishSubject<Permission>): PublishSubject<Permission>?
      = mSubjects.put(permission, subject)

  internal fun log(message: String) {
    if (log) {
      Log.d(RxPermissions.TAG, message)
    }
  }

  companion object {
    private val PERMISSIONS_REQUEST_CODE = 42
  }

}

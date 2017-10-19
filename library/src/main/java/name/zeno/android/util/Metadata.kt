package name.zeno.android.util

import android.app.Activity
import android.app.Service
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/12/29.
 */
object Metadata {
  /**
   * # 获取 activity 的metadata
   * ```
   * <activity...>
   *    <meta-setData android:name="data_Name" android:value="hello my activity"></meta-setData>
   * </activity>
   * ```
   * */
  fun inActivity(activity: Activity, name: String): String? {
    try {
      val pm = activity.packageManager
      val info = pm.getActivityInfo(activity.componentName, PackageManager.GET_META_DATA)
      return info.metaData.getString(name)
    } catch (e: PackageManager.NameNotFoundException) {
      return null
    }
  }

  /**
   * ```
   * <application...>
   *   <meta-setData
   *     android:value="hello my application"
   *     android:name="data_Name"/>
   * </application>
   * ```
   */
  fun inApp(context: Context, name: String): String? {
    try {
      val pm = context.packageManager
      val appInfo = pm.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
      return appInfo.metaData.getString(name)
    } catch (e: PackageManager.NameNotFoundException) {
      return null
    }

  }

  /**
   * ```
   * <service android:name="MetaDataService">
   *   <meta-setData android:value="hello my service" android:name="data_Name"></meta-setData>
   * </service>
   * ```
   */
  fun <T : Service> inService(context: Context, tClass: Class<T>, name: String): String? {

    try {
      val cn = ComponentName(context, tClass)
      val pm = context.packageManager
      val serviceInfo = pm.getServiceInfo(cn, PackageManager.GET_META_DATA)
      return serviceInfo.metaData.getString(name)
    } catch (e: PackageManager.NameNotFoundException) {
      return null
    }

  }

  /**
   * ```
   * <receiver android:name="MetaDataReceiver">
   *   <meta-setData android:value="hello my receiver" android:name="data_Name"></meta-setData>
   *   <intent-filter>
   *     <action android:name="android.intent.action.PHONE_STATE"></action>
   *   </intent-filter>
   * </receiver>
   *  ```
   */
  fun <T : BroadcastReceiver> inReceiver(context: Context, tClass: Class<T>, name: String): String? {
    try {
      val cn = ComponentName(context, tClass)
      val pm = context.packageManager
      val receiverInfo = pm.getReceiverInfo(cn, PackageManager.GET_META_DATA)
      return receiverInfo.metaData.getString(name)
    } catch (e: PackageManager.NameNotFoundException) {
      return null
    }

  }
}

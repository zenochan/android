package name.zeno.android.util;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/12/29.
 */
public class Metadata
{
  /**
   * 获取 activity 的metadata
   * <activity...>
   * <meta-setData android:name="data_Name" android:value="hello my activity"></meta-setData>
   * </activity>
   */
  public static String inActivity(Activity activity, String name)
  {
    try {
      PackageManager pm   = activity.getPackageManager();
      ActivityInfo   info = pm.getActivityInfo(activity.getComponentName(), PackageManager.GET_META_DATA);
      return info.metaData.getString(name);
    } catch (PackageManager.NameNotFoundException e) {
      return null;
    }
  }

  /**
   * <pre><code>
   * &lt;application...&gt;
   *   &lt;meta-setData
   *     android:value="hello my application"
   *     android:name="data_Name"&gt;&lt;/meta-setData&gt;
   * &lt;/application&gt;
   * </code></pre>
   */
  public static String inApp(Context context, String name)
  {
    try {
      PackageManager  pm      = context.getPackageManager();
      ApplicationInfo appInfo = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
      return appInfo.metaData.getString(name);
    } catch (PackageManager.NameNotFoundException e) {
      return null;
    }
  }

  /**
   * <service android:name="MetaDataService">
   * <meta-setData android:value="hello my service" android:name="data_Name"></meta-setData>
   * </service>
   */
  public static <T extends Service> String inService(Context context, Class<T> tClass, String name)
  {

    try {
      ComponentName  cn          = new ComponentName(context, tClass);
      PackageManager pm          = context.getPackageManager();
      ServiceInfo    serviceInfo = pm.getServiceInfo(cn, PackageManager.GET_META_DATA);
      return serviceInfo.metaData.getString(name);
    } catch (PackageManager.NameNotFoundException e) {
      return null;
    }
  }

  /**
   * <receiver android:name="MetaDataReceiver">
   * <meta-setData android:value="hello my receiver" android:name="data_Name"></meta-setData>
   * <intent-filter>
   * <action android:name="android.intent.action.PHONE_STATE"></action>
   * </intent-filter>
   * </receiver>
   */
  public static <T extends BroadcastReceiver> String inReceiver(Context context, Class<T> tClass, String name)
  {
    try {
      ComponentName  cn           = new ComponentName(context, tClass);
      PackageManager pm           = context.getPackageManager();
      ActivityInfo   receiverInfo = pm.getReceiverInfo(cn, PackageManager.GET_META_DATA);
      return receiverInfo.metaData.getString(name);
    } catch (PackageManager.NameNotFoundException e) {
      return null;
    }
  }
}

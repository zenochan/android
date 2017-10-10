package name.zeno.android.util;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.RequiresPermission;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("unused")
public class SystemUtils
{
  private static final String TAG = "SystemUtils";

  /**
   * 是否有网络
   */
  @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
  public static boolean isNetworkConnected(Context context)
  {
    if (context != null) {
      ConnectivityManager cm = (ConnectivityManager) context
          .getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo mNetworkInfo = cm.getActiveNetworkInfo();
      if (mNetworkInfo != null) {
        return mNetworkInfo.isAvailable();
      }
    }
    return false;
  }

  /**
   * 已连接wifi
   */
  @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
  public static boolean isWifiConnected(Context context)
  {
    return isNetworkConnected(context, ConnectivityManager.TYPE_WIFI);
  }

  /**
   * 已连接移动网络
   */
  @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
  public static boolean isMobileConnected(Context context)
  {
    return isNetworkConnected(context, ConnectivityManager.TYPE_MOBILE);
  }

  private static boolean isNetworkConnected(Context context, int networkType)
  {
    if (context != null) {
      ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
      @SuppressWarnings("deprecation")
      NetworkInfo mMobileNetworkInfo = cm.getNetworkInfo(networkType);
      if (mMobileNetworkInfo != null) {
        return mMobileNetworkInfo.isAvailable();
      }
    }
    return false;
  }

  /**
   * @return one of
   * <ul>
   * <li>{@link ConnectivityManager#TYPE_MOBILE}</li>
   * <li>{@link ConnectivityManager#TYPE_WIFI}</li>
   * <li>{@link ConnectivityManager#TYPE_WIMAX}</li>
   * <li>{@link ConnectivityManager#TYPE_ETHERNET}</li>
   * <li>{@link ConnectivityManager#TYPE_BLUETOOTH}</li>
   * </ul>
   * or other types defined by {@link ConnectivityManager}
   */
  @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
  public static int getConnectedType(Context context)
  {
    if (context != null) {
      ConnectivityManager mConnectivityManager = (ConnectivityManager) context
          .getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
      if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
        return mNetworkInfo.getType();
      }
    }
    return -1;
  }

  @RequiresPermission(anyOf = {
      "android.permission.ACCESS_WIFI_STATE",
      "android.permission.INTERNET"
  })

  /**
   * @see <a href="http://www.cnblogs.com/android100/p/Android-get-ip.html">android手机两种方式获取IP地址</a>
   */
  public static String getIP(Application context)
  {
    String ip = null;

    //获取wifi服务
    WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    //判断wifi是否开启
    if (wifiManager.isWifiEnabled()) {
//      wifiManager.setWifiEnabled(true);
      WifiInfo wifiInfo  = wifiManager.getConnectionInfo();
      int      ipAddress = wifiInfo.getIpAddress();

      ip = String.format(Locale.CHINA, "%d.%d.%d.%d",
          ipAddress & 0xFF, (ipAddress >> 8) & 0xFF, (ipAddress >> 16) & 0xFF, ipAddress >> 24 & 0xFF);
    } else {
      try {
        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); )
        {
          NetworkInterface intf = en.nextElement();
          for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); )
          {
            InetAddress inetAddress = enumIpAddr.nextElement();
            if (!inetAddress.isLoopbackAddress()) {
              ip = inetAddress.getHostAddress();
            }
          }
        }
      } catch (SocketException ex) {
        Log.e(TAG, ex.getMessage());
      }
    }

    return ip;
  }

  /** @deprecated use {@link ZEditor#toggleInputMethod(View)} */
  public static void toggleInputMethod(View view)
  {
    ZEditor.toggleInputMethod(view);
  }

  /** @deprecated use {@link ZEditor#showInputMethod(EditText)} (View)} */
  public static void showInputMethod(EditText view)
  {
    ZEditor.showInputMethod(view);
  }

  /** @deprecated use {@link ZEditor#hideInputMethod(View)} (EditText)} (View)} */
  public static void hideInputMethod(View view)
  {
    ZEditor.hideInputMethod(view);
  }

  /**
   * 获取 AppName
   */
  public static String getApplicationName(Context context)
  {
    PackageManager  packageManager = null;
    ApplicationInfo appInfo;
    try {
      packageManager = context.getApplicationContext().getPackageManager();
      appInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
    } catch (PackageManager.NameNotFoundException e) {
      appInfo = null;
    }
    return (String) packageManager.getApplicationLabel(appInfo);
  }


  //禁止截屏
  public static void disableScreenShot(Activity activity)
  {
    disableScreenShot(activity.getWindow());
  }

  //禁止截屏
  public static void disableScreenShot(Window window)
  {
    window.addFlags(WindowManager.LayoutParams.FLAG_SECURE);
  }

  /**
   * @return 0:没有运行，1：前台运行，2：后台运行
   */
  public static int isBackground(Context context)
  {
    ActivityManager                             activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    List<ActivityManager.RunningAppProcessInfo> appProcesses    = activityManager.getRunningAppProcesses();
    for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
      if (appProcess.processName.equals(context.getPackageName())) {
        if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
          ZLog.v(TAG, "前台运行:" + appProcess.processName);
          return 1;
        } else {
          ZLog.v(TAG, "后台后台:" + appProcess.processName);
          return 2;
        }
      }
    }
    return 0;
  }

  @RequiresPermission(Manifest.permission.EXPAND_STATUS_BAR)
  public static void collapsingNotification(Context context)
  {
    @SuppressWarnings("WrongConstant")
    Object service = context.getSystemService("statusbar");
    if (null == service)
      return;

    try {
      Class<?> clazz      = Class.forName("android.app.StatusBarManager");
      int      sdkVersion = Build.VERSION.SDK_INT;
      Method   collapse;
      if (sdkVersion <= 16) {
        collapse = clazz.getMethod("collapse");
      } else {
        collapse = clazz.getMethod("collapsePanels");
      }

      collapse.setAccessible(true);
      collapse.invoke(service);
    } catch (Exception e) {
      ZLog.e(TAG, e.getMessage(), e);
    }
  }

  @RequiresPermission(Manifest.permission.EXPAND_STATUS_BAR)
  public static void expandNotification(Context context)
  {
    @SuppressWarnings("WrongConstant")
    Object service = context.getSystemService("statusbar");
    if (null == service)
      return;

    try {
      Class<?> clazz      = Class.forName("android.app.StatusBarManager");
      int      sdkVersion = Build.VERSION.SDK_INT;
      Method   expand;
      if (sdkVersion <= 16) {
        expand = clazz.getMethod("expand");
      } else {
        //Android SDK 16之后的版本展开通知栏有两个接口可以处理
        //expandNotificationsPanel()
        //expandSettingsPanel()
        //expand =clazz.getMethod("expandNotificationsPanel");
        expand = clazz.getMethod("expandSettingsPanel");
      }

      expand.setAccessible(true);
      expand.invoke(service);
    } catch (Exception e) {
      ZLog.e(TAG, e.getMessage(), e);
    }
  }
}

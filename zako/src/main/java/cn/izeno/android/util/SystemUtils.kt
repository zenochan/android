package cn.izeno.android.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Build
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import androidx.annotation.RequiresPermission
import java.lang.reflect.Method
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*

object SystemUtils {
  private val TAG = "SystemUtils"

  /**
   * 是否有网络
   */
  @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
  fun isNetworkConnected(context: Context?): Boolean {
    if (context != null) {
      val cm = context
          .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
      val mNetworkInfo = cm.activeNetworkInfo
      if (mNetworkInfo != null) {
        return mNetworkInfo.isAvailable
      }
    }
    return false
  }

  /**
   * 已连接wifi
   */
  @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
  fun isWifiConnected(context: Context): Boolean {
    return isNetworkConnected(context, ConnectivityManager.TYPE_WIFI)
  }

  /**
   * 已连接移动网络
   */
  @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
  fun isMobileConnected(context: Context): Boolean {
    return isNetworkConnected(context, ConnectivityManager.TYPE_MOBILE)
  }

  @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
  private fun isNetworkConnected(context: Context?, networkType: Int): Boolean {
    if (context != null) {
      val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
      val mMobileNetworkInfo = cm.getNetworkInfo(networkType)
      if (mMobileNetworkInfo != null) {
        return mMobileNetworkInfo.isAvailable
      }
    }
    return false
  }

  /**
   * @return one of
   *
   *  * [ConnectivityManager.TYPE_MOBILE]
   *  * [ConnectivityManager.TYPE_WIFI]
   *  * [ConnectivityManager.TYPE_WIMAX]
   *  * [ConnectivityManager.TYPE_ETHERNET]
   *  * [ConnectivityManager.TYPE_BLUETOOTH]
   *
   * or other types defined by [ConnectivityManager]
   */
  @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
  fun getConnectedType(context: Context?): Int {
    if (context != null) {
      val mConnectivityManager = context
          .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
      val mNetworkInfo = mConnectivityManager.activeNetworkInfo
      if (mNetworkInfo != null && mNetworkInfo.isAvailable) {
        return mNetworkInfo.type
      }
    }
    return -1
  }

  @RequiresPermission(anyOf = arrayOf("android.permission.ACCESS_WIFI_STATE", "android.permission.INTERNET"))
      /**
       * @see [android手机两种方式获取IP地址](http://www.cnblogs.com/android100/p/Android-get-ip.html)
       */
  fun getIP(context: Application): String? {
    var ip: String? = null

    //获取wifi服务
    val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
    //判断wifi是否开启
    if (wifiManager.isWifiEnabled) {
      //      wifiManager.setWifiEnabled(true);
      val wifiInfo = wifiManager.connectionInfo
      val ipAddress = wifiInfo.ipAddress

      ip = String.format(Locale.CHINA, "%d.%d.%d.%d",
          ipAddress and 0xFF, ipAddress shr 8 and 0xFF, ipAddress shr 16 and 0xFF, ipAddress shr 24 and 0xFF)
    } else {
      try {
        val en = NetworkInterface.getNetworkInterfaces()
        while (en.hasMoreElements()) {
          val intf = en.nextElement()
          val enumIpAddr = intf.inetAddresses
          while (enumIpAddr.hasMoreElements()) {
            val inetAddress = enumIpAddr.nextElement()
            if (!inetAddress.isLoopbackAddress) {
              ip = inetAddress.hostAddress
            }
          }
        }
      } catch (ex: SocketException) {
        Log.e(TAG, ex.message)
      }

    }

    return ip
  }


  @Deprecated("use {@link ZEditor#toggleInputMethod(View)} ")
  fun toggleInputMethod(view: View) {
    ZEditor.toggleInputMethod(view)
  }


  @Deprecated("use {@link ZEditor#showInputMethod(EditText)} (View)} ")
  fun showInputMethod(view: EditText) {
    ZEditor.showInputMethod(view)
  }


  @Deprecated("use {@link ZEditor#hideInputMethod(View)} (EditText)} (View)} ")
  fun hideInputMethod(view: View) {
    ZEditor.hideInputMethod(view)
  }

  /**
   * 获取 AppName
   */
  fun getApplicationName(context: Context): String {
    var packageManager: PackageManager? = null
    var appInfo: ApplicationInfo?
    try {
      packageManager = context.applicationContext.packageManager
      appInfo = packageManager!!.getApplicationInfo(context.packageName, 0)
    } catch (e: PackageManager.NameNotFoundException) {
      appInfo = null
    }

    return packageManager!!.getApplicationLabel(appInfo) as String
  }


  //禁止截屏
  fun disableScreenShot(activity: Activity) {
    disableScreenShot(activity.window)
  }

  //禁止截屏
  fun disableScreenShot(window: Window) {
    window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
  }

  /**
   * @return 0:没有运行，1：前台运行，2：后台运行
   */
  fun isBackground(context: Context): Int {
    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val appProcesses = activityManager.runningAppProcesses
    for (appProcess in appProcesses) {
      if (appProcess.processName == context.packageName) {
        if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
          ZLog.v(TAG, "前台运行:" + appProcess.processName)
          return 1
        } else {
          ZLog.v(TAG, "后台后台:" + appProcess.processName)
          return 2
        }
      }
    }
    return 0
  }

  @SuppressLint("WrongConstant")
  @RequiresPermission(Manifest.permission.EXPAND_STATUS_BAR)
  fun collapsingNotification(context: Context) {
    val service = context.getSystemService("statusbar") ?: return

    try {
      val clazz = Class.forName("android.app.StatusBarManager")
      val sdkVersion = Build.VERSION.SDK_INT
      val collapse: Method
      if (sdkVersion <= 16) {
        collapse = clazz.getMethod("collapse")
      } else {
        collapse = clazz.getMethod("collapsePanels")
      }

      collapse.isAccessible = true
      collapse.invoke(service)
    } catch (e: Exception) {
      ZLog.e(TAG, e.message, e)
    }

  }

  @SuppressLint("WrongConstant")
  @RequiresPermission(Manifest.permission.EXPAND_STATUS_BAR)
  fun expandNotification(context: Context) {
    val service = context.getSystemService("statusbar") ?: return

    try {
      val clazz = Class.forName("android.app.StatusBarManager")
      val sdkVersion = Build.VERSION.SDK_INT
      val expand: Method
      if (sdkVersion <= 16) {
        expand = clazz.getMethod("expand")
      } else {
        //Android SDK 16之后的版本展开通知栏有两个接口可以处理
        //expandNotificationsPanel()
        //expandSettingsPanel()
        //expand =clazz.getMethod("expandNotificationsPanel");
        expand = clazz.getMethod("expandSettingsPanel")
      }

      expand.isAccessible = true
      expand.invoke(service)
    } catch (e: Exception) {
      ZLog.e(TAG, e.message, e)
    }

  }
}

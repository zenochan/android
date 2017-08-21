package name.zeno.android.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

import java.security.Permission;
import java.util.jar.Manifest;

import name.zeno.android.system.ZPermission;
import name.zeno.android.util.PrintUtils;
import name.zeno.android.util.SystemUtils;
import name.zeno.android.util.ZLog;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Create Date: 16/6/9
 *
 * @author 陈治谋 (513500085@qq.com)
 */
@SuppressWarnings("WeakerAccess")
public class AppInfo
{
  private static final String TAG = "AppInfo";

  public static String dType;
  public static String dVersion;
  public static String imei;
  public static String phoneNumber;

  public static String appName;
  public static String packageName;
  public static int    versionCode;
  public static String versionName;

  /** 屏幕宽度 */
  public static int   width;
  /** 屏幕高度 */
  public static int   height;
  public static float density;
  public static int   densityDpi;

  public static String btAddress; //蓝牙地址
  public static String btName;    //蓝牙名称

  static {
    initDisplay();
  }

  @SuppressLint("HardwareIds")
  public static void init(Context context)
  {
    dType = Build.MODEL;
    dVersion = Build.VERSION.SDK_INT + "_" + Build.VERSION.RELEASE;

    try {
      TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
      if (ContextCompat.checkSelfPermission(context, ZPermission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
        imei = tm.getDeviceId();
        phoneNumber = tm.getLine1Number();
      }
    } catch (Exception ignore) { }

    PackageInfo pi = null;
    try {
      pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
    } catch (Exception e) {
      ZLog.e(TAG, e.getMessage(), e);
    }
    if (null != pi) {
      versionName = pi.versionName;
      versionCode = pi.versionCode;
      packageName = pi.packageName;
    } else {
      versionName = "";
      versionCode = 0;
    }
    appName = SystemUtils.getApplicationName(context);
    btAddress = PrintUtils.getDefaultBluetoothDeviceAddress(context);
    btName = PrintUtils.getDefaultBluetoothDeviceName(context);

    printAppInfo();
  }

  public static void initDisplay()
  {
    DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
    width = metrics.widthPixels;
    height = metrics.heightPixels;
    density = metrics.density;
    densityDpi = metrics.densityDpi;
  }

  public static void printAppInfo()
  {
    String appInfo =
        "-------------   app info  -------------------\r\n" +
            "dType" + ":" + dType + "\r\n" +
            "dVersion:" + dVersion + "\r\n" +
            "versionCode:" + versionCode + "\r\n" +
            "versionName:" + versionName + "\r\n" +
            "width:" + width + "\r\n" +
            "height:" + height + "\r\n" +
            "density:" + density + "\r\n" +
            "densityDpi:" + densityDpi + "\r\n" +
            "--------------------------------\r\n";

    Log.d(TAG, appInfo);
  }
}


package name.zeno.android.third.umeng;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import name.zeno.android.app.ZApplication;
import name.zeno.android.util.ZLog;
import name.zeno.android.util.ZString;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/9/7
 */
public abstract class ZUmeng
{
  public static final Boolean supported;
  private static final String TAG = ZUmeng.class.getSimpleName();

  static {
    boolean support = false;
    try {
      Class clazz = Class.forName("com.umeng.analytics.MobclickAgent");
      support = true;

      //2.5.2  包含Activity、Fragment或View的应用
      //统计应用中包含Fragment的情况比较复杂，首先要明确一些概念。
      //1. MobclickAgent.onResume()  和MobclickAgent.onPause()  方法是用来统计应用时长的(也就是Session时长,当然还包括一些其他功能)
      //2.MobclickAgent.onPageStart() 和MobclickAgent.onPageEnd() 方法是用来统计页面跳转的
      //在仅有Activity的应用中，SDK 自动帮助开发者调用了 2  中的方法，并把Activity 类名作为页面名称统计。但是在包含fragment的程序中我们希望统计更详细的页面，所以需要自己调用方法做更详细的统计。
      //首先，需要在程序入口处，调用 MobclickAgent.openActivityDurationTrack(false)  禁止默认的页面统计方式，这样将不会再自动统计Activity。
      //然后需要手动添加以下代码：
      //1. 使用 MobclickAgent.onResume 和 MobclickAgent.onPause 方法统计时长, 这和基本统计中的情况一样(针对Activity)
      //2. 使用 MobclickAgent.onPageStart 和 MobclickAgent.onPageEnd 方法统计页面(针对页面,页面可能是Activity 也可能是Fragment或View)
      MobclickAgent.openActivityDurationTrack(false);
    } catch (ClassNotFoundException e) {
      ZLog.i(TAG, "umeng module not supported");
    }

    supported = support;
  }

  //<editor-fold desc="友盟页面基础统计">
  public static void onResume(Activity activity)
  {
    if (supported) MobclickAgent.onResume(activity);
  }

  public static void onPause(Activity activity)
  {
    if (supported) MobclickAgent.onPause(activity);
  }

  public static void onPageStart(String page)
  {

    if (supported) MobclickAgent.onPageStart(page);
  }

  public static void onPageEnd(String page)
  {
    if (supported) MobclickAgent.onPageEnd(page);
  }
  //</editor-fold>

  //友盟事件,带参数
  public static void onEvent(String event, String... param)
  {
    if (param == null || param.length < 2) {
      MobclickAgent.onEvent(ZApplication.getApplication(), event);
    } else {
      Map<String, String> params = new HashMap<>();
      for (int i = 0; i < param.length / 2; i++) {
        params.put(param[i * 2], param[i * 2 + 1]);
      }
      MobclickAgent.onEvent(ZApplication.getApplication(), event, params);
    }
  }

  // 获取设备信息
  @SuppressLint({"HardwareIds", "MissingPermission"})
  @SuppressWarnings({"unused", "ConstantConditions"})
  public static String getDeviceInfo(Context context)
  {
    try {
      JSONObject json = new JSONObject();
      android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
          .getSystemService(Context.TELEPHONY_SERVICE);
      String device_id = null;
      if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
        device_id = tm.getDeviceId();
      }
      String     mac = null;
      FileReader fstream;
      try {
        fstream = new FileReader("/sys/class/net/wlan0/address");
      } catch (FileNotFoundException e) {
        fstream = new FileReader("/sys/class/net/eth0/address");
      }
      BufferedReader in = null;
      if (fstream != null) {
        try {
          in = new BufferedReader(fstream, 1024);
          mac = in.readLine();
        } finally {
          if (fstream != null) {
            try {
              fstream.close();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
          if (in != null) {
            try {
              in.close();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        }
      }
      json.put("mac", mac);
      if (ZString.INSTANCE.isEmpty(device_id)) {
        device_id = mac;
      }
      if (ZString.INSTANCE.isEmpty(device_id)) {
        device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
            android.provider.Settings.Secure.ANDROID_ID);
      }
      json.put("device_id", device_id);
      return json.toString();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private static boolean checkPermission(Context context, String permission)
  {
    boolean result = false;
    if (Build.VERSION.SDK_INT >= 23) {
      try {
        Class<?> clazz  = Class.forName("android.content.Context");
        Method   method = clazz.getMethod("checkSelfPermission", String.class);
        int      rest   = (Integer) method.invoke(context, permission);
        result = rest == PackageManager.PERMISSION_GRANTED;
      } catch (Exception e) {
        result = false;
      }
    } else {
      PackageManager pm = context.getPackageManager();
      if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
        result = true;
      }
    }
    return result;
  }
}

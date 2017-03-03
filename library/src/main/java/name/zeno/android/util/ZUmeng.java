package name.zeno.android.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.util.ArrayMap;

import com.umeng.analytics.MobclickAgent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

import name.zeno.android.app.ZApplication;

/**
 * Create Date: 16/7/15
 *
 * @author 陈治谋 (513500085@qq.com)
 */
public class ZUmeng
{
  @SuppressWarnings("unused")
  private static final String TAG = "ZUmeng";

  //友盟事件,带参数
  public static void onEvent(String event, String... param)
  {
    if (param == null || param.length < 2) {
      MobclickAgent.onEvent(ZApplication.getApplication(), event);
    } else {
      Map<String, String> params = new ArrayMap<>();
      for (int i = 0; i < param.length / 2; i++) {
        params.put(param[i * 2], param[i * 2 + 1]);
      }
      MobclickAgent.onEvent(ZApplication.getApplication(), event, params);
    }
  }

  // 获取设备信息
  @SuppressLint("HardwareIds") @SuppressWarnings({"unused", "ConstantConditions"})
  public static String getDeviceInfo(Context context)
  {
    try {
      org.json.JSONObject json = new org.json.JSONObject();
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
      if (ZString.isEmpty(device_id)) {
        device_id = mac;
      }
      if (ZString.isEmpty(device_id)) {
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

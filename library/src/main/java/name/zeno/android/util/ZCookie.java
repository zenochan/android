package name.zeno.android.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Create Date: 16/6/1
 *
 * @author 陈治谋 (513500085@qq.com)
 */
@SuppressWarnings("unused")
public class ZCookie
{
  private static final String TAG = "ZCookie";

  private static SharedPreferences sp;

  public static void init(@NonNull Application application)
  {
    sp = application.getSharedPreferences(TAG, Context.MODE_MULTI_PROCESS);
  }

  /**
   * @return versionCode 版本第一次打开
   */
  public static boolean firstOpen(int versionCode)
  {
    boolean isFirstOpen = false;

    if (!sp.getBoolean("versionCode_" + versionCode, false)) {
      SharedPreferences.Editor editor = sp.edit();
      editor.putBoolean("versionCode_" + versionCode, true);
      editor.apply();
      isFirstOpen = true;
    }

    return isFirstOpen;
  }

  @SuppressWarnings("unchecked")
  public static <T> T get(@NonNull String key, Class<T> clazz)
  {
    T t;
    try {
      if (!spInited()) {
        t = null;
      } else if (clazz == Long.class) {
        t = (T) (Long) sp.getLong(key, 0);
      } else if (clazz == String.class) {
        t = (T) sp.getString(key, null);
      } else if (clazz == Float.class) {
        t = (T) (Float) sp.getFloat(key, 0);
      } else if (clazz == Integer.class) {
        t = (T) (Integer) sp.getInt(key, 0);
      } else if (clazz == Boolean.class) {
        t = (T) (Boolean) sp.getBoolean(key, false);
      } else {
        String jsonStr = sp.getString(key, null);
        t = jsonStr == null ? null : JSON.parseObject(jsonStr, clazz);
      }
    } catch (Exception e) {
      ZLog.e(TAG, e.getMessage(), e);
      t = null;
    }

    return t;
  }

  //获取列表
  @Nullable @SuppressWarnings("unchecked")
  public static <T> List<T> getList(@NonNull String key, Class<T> clazz)
  {
    if (!spInited()) {
      return null;
    }

    String  jsonStr = sp.getString(key, null);
    List<T> list    = null;
    try {
      list = jsonStr == null ? null : JSON.parseArray(jsonStr, clazz);
    } catch (Exception e) {
      ZLog.e("数据格式过期", e.getMessage());
    }

    return list;
  }

  public static void put(@NonNull String k, Object v)
  {
    if (!spInited()) {
      return;
    }

    SharedPreferences.Editor editor = sp.edit();
    if (v == null) {
      editor.remove(k).apply();
      return;
    }

    if (v instanceof Long) {
      editor.putLong(k, (Long) v);
    } else if (v instanceof String) {
      editor.putString(k, (String) v);
    } else if (v instanceof Float) {
      editor.putFloat(k, (Float) v);
    } else if (v instanceof Integer) {
      editor.putInt(k, (Integer) v);
    } else if (v instanceof Boolean) {
      editor.putBoolean(k, (Boolean) v);
    } else {
      String jsonStr = JSON.toJSONString(v);
      editor.putString(k, jsonStr);
    }

    editor.apply();
  }

  public static boolean getBoolean(String key)
  {
    return sp.getBoolean(key, false);
  }

  public static void putBoolean(String key, boolean v)
  {
    sp.edit().putBoolean(key, v).apply();
  }

  private static boolean spInited()
  {
    if (sp == null) {
      ZLog.e("calling method init(Application) before use ZCookie");
    }
    return sp != null;
  }

}

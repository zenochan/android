package name.zeno.android.util;

import android.util.Log;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

/**
 * Create Date: 16/6/9
 *
 * @author 陈治谋 (513500085@qq.com)
 */
@SuppressWarnings("unused")
public class ZLog
{

  public static final String  TAG   = "ZLog";
  private static      boolean DEBUG = true;

  static {
    Logger.init(TAG).hideThreadInfo().methodCount(1).logLevel(LogLevel.FULL);
  }

  public static void debug(boolean debug)
  {
    if (DEBUG != debug) {
      DEBUG = debug;
      Logger.init(TAG).hideThreadInfo().methodCount(1).logLevel(DEBUG ? LogLevel.FULL : LogLevel.NONE);
    }
  }

  public static void v(String msg)
  {
    v(TAG, msg);
  }

  public static void v(String tag, String msg)
  {
    Log.v(TAG + "-" + tag, msg);
  }

  public static void d(String msg)
  {
    d(TAG, msg);
  }

  public static void d(String tag, String msg)
  {
    Log.d(TAG + "-" + tag, msg);
  }


  public static void i(String msg)
  {
    i(TAG, msg);
  }

  public static void i(String tag, String msg)
  {
    Log.i(TAG + "-" + tag, msg);
  }

  public static void w(String msg)
  {
    w(TAG, msg);
  }

  public static void w(String tag, String msg)
  {
    Log.w(TAG + "-" + tag, msg);
  }

  public static void e(String msg)
  {
    e(TAG, msg);
  }

  public static void e(String tag, String msg)
  {
    Log.e(TAG + "-" + tag, msg);
  }

  public static void e(String tag, String msg, Throwable tr)
  {
    Log.e(TAG + "-" + tag, msg, tr);
  }
}

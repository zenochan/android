package name.zeno.android.data.models;

import android.os.Build;

import com.alibaba.fastjson.JSONObject;

import lombok.Data;
import name.zeno.android.app.AppInfo;
import name.zeno.android.exception.ZException;
import name.zeno.android.util.ZDate;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/8/19
 */
@Data
public class Crash
{
  private final String appName     = AppInfo.appName;
  private final String packageName = AppInfo.packageName;
  private final int    versionCode = AppInfo.versionCode;
  private final String versionName = AppInfo.versionName;

  // 设备信息
  private final String manufacturer = Build.MANUFACTURER;
  private final String mode         = Build.MODEL;
  private final String version      = Build.VERSION.RELEASE;
  private final int    sdkInt       = Build.VERSION.SDK_INT;
  private final String imei         = AppInfo.imei;
  private final String phoneNumber  = AppInfo.phoneNumber;

  // 账户
  private JSONObject account;

  // 错误信息
  private String type;
  private String message;
  private String stack;
  private final String time = ZDate.nowString("E yyyy/MM/dd HH:mm:ss");

  public Crash() {}

  public Crash(Throwable throwable)
  {
    type = throwable.getClass().getName();
    message = throwable.getMessage();
    stack = ZException.stack(throwable);
  }
}

package name.zeno.android.data.models;

import android.os.Build;

import com.alibaba.fastjson.JSONObject;

import name.zeno.android.app.AppInfo;
import name.zeno.android.exception.ZException;
import name.zeno.android.util.ZDate;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/8/19
 */
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

  public String getAppName()
  {
    return appName;
  }

  public String getPackageName()
  {
    return packageName;
  }

  public int getVersionCode()
  {
    return versionCode;
  }

  public String getVersionName()
  {
    return versionName;
  }

  public String getManufacturer()
  {
    return manufacturer;
  }

  public String getMode()
  {
    return mode;
  }

  public String getVersion()
  {
    return version;
  }

  public int getSdkInt()
  {
    return sdkInt;
  }

  public String getImei()
  {
    return imei;
  }

  public String getPhoneNumber()
  {
    return phoneNumber;
  }

  public JSONObject getAccount()
  {
    return account;
  }

  public void setAccount(JSONObject account)
  {
    this.account = account;
  }

  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    this.type = type;
  }

  public String getMessage()
  {
    return message;
  }

  public void setMessage(String message)
  {
    this.message = message;
  }

  public String getStack()
  {
    return stack;
  }

  public void setStack(String stack)
  {
    this.stack = stack;
  }

  public String getTime()
  {
    return time;
  }

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

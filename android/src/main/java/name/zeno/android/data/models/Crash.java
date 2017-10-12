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

  public String getAppName()
  {return this.appName;}

  public String getPackageName()
  {return this.packageName;}

  public int getVersionCode()
  {return this.versionCode;}

  public String getVersionName()
  {return this.versionName;}

  public String getManufacturer()
  {return this.manufacturer;}

  public String getMode()
  {return this.mode;}

  public String getVersion()
  {return this.version;}

  public int getSdkInt()
  {return this.sdkInt;}

  public String getImei()
  {return this.imei;}

  public String getPhoneNumber()
  {return this.phoneNumber;}

  public JSONObject getAccount()
  {return this.account;}

  public String getType()
  {return this.type;}

  public String getMessage()
  {return this.message;}

  public String getStack()
  {return this.stack;}

  public String getTime()
  {return this.time;}

  public void setAccount(JSONObject account)
  {this.account = account; }

  public void setType(String type)
  {this.type = type; }

  public void setMessage(String message)
  {this.message = message; }

  public void setStack(String stack)
  {this.stack = stack; }

  public boolean equals(Object o)
  {
    if (o == this) return true;
    if (!(o instanceof Crash)) return false;
    final Crash other = (Crash) o;
    if (!other.canEqual((Object) this)) return false;
    final Object this$appName  = this.getAppName();
    final Object other$appName = other.getAppName();
    if (this$appName == null ? other$appName != null : !this$appName.equals(other$appName))
      return false;
    final Object this$packageName  = this.getPackageName();
    final Object other$packageName = other.getPackageName();
    if (this$packageName == null ? other$packageName != null : !this$packageName.equals(other$packageName))
      return false;
    if (this.getVersionCode() != other.getVersionCode()) return false;
    final Object this$versionName  = this.getVersionName();
    final Object other$versionName = other.getVersionName();
    if (this$versionName == null ? other$versionName != null : !this$versionName.equals(other$versionName))
      return false;
    final Object this$manufacturer  = this.getManufacturer();
    final Object other$manufacturer = other.getManufacturer();
    if (this$manufacturer == null ? other$manufacturer != null : !this$manufacturer.equals(other$manufacturer))
      return false;
    final Object this$mode  = this.getMode();
    final Object other$mode = other.getMode();
    if (this$mode == null ? other$mode != null : !this$mode.equals(other$mode)) return false;
    final Object this$version  = this.getVersion();
    final Object other$version = other.getVersion();
    if (this$version == null ? other$version != null : !this$version.equals(other$version))
      return false;
    if (this.getSdkInt() != other.getSdkInt()) return false;
    final Object this$imei  = this.getImei();
    final Object other$imei = other.getImei();
    if (this$imei == null ? other$imei != null : !this$imei.equals(other$imei)) return false;
    final Object this$phoneNumber  = this.getPhoneNumber();
    final Object other$phoneNumber = other.getPhoneNumber();
    if (this$phoneNumber == null ? other$phoneNumber != null : !this$phoneNumber.equals(other$phoneNumber))
      return false;
    final Object this$account  = this.getAccount();
    final Object other$account = other.getAccount();
    if (this$account == null ? other$account != null : !this$account.equals(other$account))
      return false;
    final Object this$type  = this.getType();
    final Object other$type = other.getType();
    if (this$type == null ? other$type != null : !this$type.equals(other$type)) return false;
    final Object this$message  = this.getMessage();
    final Object other$message = other.getMessage();
    if (this$message == null ? other$message != null : !this$message.equals(other$message))
      return false;
    final Object this$stack  = this.getStack();
    final Object other$stack = other.getStack();
    if (this$stack == null ? other$stack != null : !this$stack.equals(other$stack)) return false;
    final Object this$time  = this.getTime();
    final Object other$time = other.getTime();
    if (this$time == null ? other$time != null : !this$time.equals(other$time)) return false;
    return true;
  }

  public int hashCode()
  {
    final int    PRIME    = 59;
    int          result   = 1;
    final Object $appName = this.getAppName();
    result = result * PRIME + ($appName == null ? 43 : $appName.hashCode());
    final Object $packageName = this.getPackageName();
    result = result * PRIME + ($packageName == null ? 43 : $packageName.hashCode());
    result = result * PRIME + this.getVersionCode();
    final Object $versionName = this.getVersionName();
    result = result * PRIME + ($versionName == null ? 43 : $versionName.hashCode());
    final Object $manufacturer = this.getManufacturer();
    result = result * PRIME + ($manufacturer == null ? 43 : $manufacturer.hashCode());
    final Object $mode = this.getMode();
    result = result * PRIME + ($mode == null ? 43 : $mode.hashCode());
    final Object $version = this.getVersion();
    result = result * PRIME + ($version == null ? 43 : $version.hashCode());
    result = result * PRIME + this.getSdkInt();
    final Object $imei = this.getImei();
    result = result * PRIME + ($imei == null ? 43 : $imei.hashCode());
    final Object $phoneNumber = this.getPhoneNumber();
    result = result * PRIME + ($phoneNumber == null ? 43 : $phoneNumber.hashCode());
    final Object $account = this.getAccount();
    result = result * PRIME + ($account == null ? 43 : $account.hashCode());
    final Object $type = this.getType();
    result = result * PRIME + ($type == null ? 43 : $type.hashCode());
    final Object $message = this.getMessage();
    result = result * PRIME + ($message == null ? 43 : $message.hashCode());
    final Object $stack = this.getStack();
    result = result * PRIME + ($stack == null ? 43 : $stack.hashCode());
    final Object $time = this.getTime();
    result = result * PRIME + ($time == null ? 43 : $time.hashCode());
    return result;
  }

  protected boolean canEqual(Object other)
  {return other instanceof Crash;}

  public String toString()
  {return "Crash(appName=" + this.getAppName() + ", packageName=" + this.getPackageName() + ", versionCode=" + this.getVersionCode() + ", versionName=" + this.getVersionName() + ", manufacturer=" + this.getManufacturer() + ", mode=" + this.getMode() + ", version=" + this.getVersion() + ", sdkInt=" + this.getSdkInt() + ", imei=" + this.getImei() + ", phoneNumber=" + this.getPhoneNumber() + ", account=" + this.getAccount() + ", type=" + this.getType() + ", message=" + this.getMessage() + ", stack=" + this.getStack() + ", time=" + this.getTime() + ")";}
}

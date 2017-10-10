package name.zeno.android.app;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Create Date: 16/6/30
 *
 * @author 陈治谋 (513500085@qq.com)
 */
public class ExceptionInfo implements Parcelable
{
  public static final String EXTRA_NAME = "exception_info";

  Throwable                 throwable;
  String                    email;
  Class<? extends Activity> mainActivityClass;
  String                    accountJson;

  public ExceptionInfo() { }

  @Override public int describeContents() { return 0; }

  @Override public void writeToParcel(Parcel dest, int flags)
  {
    dest.writeSerializable(this.throwable);
    dest.writeString(this.email);
    dest.writeSerializable(this.mainActivityClass);
    dest.writeString(this.accountJson);
  }

  protected ExceptionInfo(Parcel in)
  {
    this.throwable = (Throwable) in.readSerializable();
    this.email = in.readString();
    //noinspection unchecked
    this.mainActivityClass = (Class<? extends Activity>) in.readSerializable();
    this.accountJson = in.readString();
  }

  public static final Creator<ExceptionInfo> CREATOR = new Creator<ExceptionInfo>()
  {
    @Override
    public ExceptionInfo createFromParcel(Parcel source) {return new ExceptionInfo(source);}

    @Override public ExceptionInfo[] newArray(int size) {return new ExceptionInfo[size];}
  };

  public Throwable getThrowable()
  {return this.throwable;}

  public String getEmail()
  {return this.email;}

  public Class<? extends Activity> getMainActivityClass()
  {return this.mainActivityClass;}

  public String getAccountJson()
  {return this.accountJson;}

  public void setThrowable(Throwable throwable)
  {this.throwable = throwable; }

  public void setEmail(String email)
  {this.email = email; }

  public void setMainActivityClass(Class<? extends Activity> mainActivityClass)
  {this.mainActivityClass = mainActivityClass; }

  public void setAccountJson(String accountJson)
  {this.accountJson = accountJson; }

  public boolean equals(Object o)
  {
    if (o == this) return true;
    if (!(o instanceof ExceptionInfo)) return false;
    final ExceptionInfo other = (ExceptionInfo) o;
    if (!other.canEqual((Object) this)) return false;
    final Object this$throwable  = this.getThrowable();
    final Object other$throwable = other.getThrowable();
    if (this$throwable == null ? other$throwable != null : !this$throwable.equals(other$throwable))
      return false;
    final Object this$email  = this.getEmail();
    final Object other$email = other.getEmail();
    if (this$email == null ? other$email != null : !this$email.equals(other$email)) return false;
    final Object this$mainActivityClass  = this.getMainActivityClass();
    final Object other$mainActivityClass = other.getMainActivityClass();
    if (this$mainActivityClass == null ? other$mainActivityClass != null : !this$mainActivityClass.equals(other$mainActivityClass))
      return false;
    final Object this$accountJson  = this.getAccountJson();
    final Object other$accountJson = other.getAccountJson();
    if (this$accountJson == null ? other$accountJson != null : !this$accountJson.equals(other$accountJson))
      return false;
    return true;
  }

  public int hashCode()
  {
    final int    PRIME      = 59;
    int          result     = 1;
    final Object $throwable = this.getThrowable();
    result = result * PRIME + ($throwable == null ? 43 : $throwable.hashCode());
    final Object $email = this.getEmail();
    result = result * PRIME + ($email == null ? 43 : $email.hashCode());
    final Object $mainActivityClass = this.getMainActivityClass();
    result = result * PRIME + ($mainActivityClass == null ? 43 : $mainActivityClass.hashCode());
    final Object $accountJson = this.getAccountJson();
    result = result * PRIME + ($accountJson == null ? 43 : $accountJson.hashCode());
    return result;
  }

  protected boolean canEqual(Object other)
  {return other instanceof ExceptionInfo;}

  public String toString()
  {return "ExceptionInfo(throwable=" + this.getThrowable() + ", email=" + this.getEmail() + ", mainActivityClass=" + this.getMainActivityClass() + ", accountJson=" + this.getAccountJson() + ")";}
}

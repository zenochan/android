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

  public Throwable getThrowable()
  {
    return throwable;
  }

  public void setThrowable(Throwable throwable)
  {
    this.throwable = throwable;
  }

  public String getEmail()
  {
    return email;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  public Class<? extends Activity> getMainActivityClass()
  {
    return mainActivityClass;
  }

  public void setMainActivityClass(Class<? extends Activity> mainActivityClass)
  {
    this.mainActivityClass = mainActivityClass;
  }

  public String getAccountJson()
  {
    return accountJson;
  }

  public void setAccountJson(String accountJson)
  {
    this.accountJson = accountJson;
  }

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
}

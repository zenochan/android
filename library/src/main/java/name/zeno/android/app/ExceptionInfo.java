package name.zeno.android.app;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;

/**
 * Create Date: 16/6/30
 *
 * @author 陈治谋 (513500085@qq.com)
 */
@Data
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
}

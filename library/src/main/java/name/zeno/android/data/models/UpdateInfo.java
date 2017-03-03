package name.zeno.android.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/11/21.
 */
@Data
public class UpdateInfo implements Parcelable
{
  private String versionName;
  private int    versionCode;
  private String versionUrl;

  private boolean forceUpdate;

  //android 7.0 后需要
  private String fileProvider;

  public UpdateInfo() {}

  @Override public int describeContents() { return 0; }

  @Override public void writeToParcel(Parcel dest, int flags)
  {
    dest.writeString(this.versionName);
    dest.writeInt(this.versionCode);
    dest.writeString(this.versionUrl);
    dest.writeByte(this.forceUpdate ? (byte) 1 : (byte) 0);
    dest.writeString(this.fileProvider);
  }

  protected UpdateInfo(Parcel in)
  {
    this.versionName = in.readString();
    this.versionCode = in.readInt();
    this.versionUrl = in.readString();
    this.forceUpdate = in.readByte() != 0;
    this.fileProvider = in.readString();
  }

  public static final Creator<UpdateInfo> CREATOR = new Creator<UpdateInfo>()
  {
    @Override public UpdateInfo createFromParcel(Parcel source) {return new UpdateInfo(source);}

    @Override public UpdateInfo[] newArray(int size) {return new UpdateInfo[size];}
  };
}

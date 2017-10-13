package name.zeno.android.data.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/11/21.
 */
public class UpdateInfo implements Parcelable
{
  private String versionName;
  private int    versionCode;
  private String versionInfo;
  private String versionUrl;

  private boolean forceUpdate;

  public UpdateInfo() {}

  @Override public int describeContents() { return 0; }

  @Override public void writeToParcel(Parcel dest, int flags)
  {
    dest.writeString(this.versionName);
    dest.writeInt(this.versionCode);
    dest.writeString(this.versionInfo);
    dest.writeString(this.versionUrl);
    dest.writeByte(this.forceUpdate ? (byte) 1 : (byte) 0);
  }

  protected UpdateInfo(Parcel in)
  {
    this.versionName = in.readString();
    this.versionCode = in.readInt();
    this.versionInfo = in.readString();
    this.versionUrl = in.readString();
    this.forceUpdate = in.readByte() != 0;
  }

  public static final Creator<UpdateInfo> CREATOR = new Creator<UpdateInfo>()
  {
    @Override public UpdateInfo createFromParcel(Parcel source) {return new UpdateInfo(source);}

    @Override public UpdateInfo[] newArray(int size) {return new UpdateInfo[size];}
  };

  public String getVersionName()
  {return this.versionName;}

  public int getVersionCode()
  {return this.versionCode;}

  public String getVersionInfo()
  {return this.versionInfo;}

  public String getVersionUrl()
  {return this.versionUrl;}

  public boolean isForceUpdate()
  {return this.forceUpdate;}

  public void setVersionName(String versionName)
  {this.versionName = versionName; }

  public void setVersionCode(int versionCode)
  {this.versionCode = versionCode; }

  public void setVersionInfo(String versionInfo)
  {this.versionInfo = versionInfo; }

  public void setVersionUrl(String versionUrl)
  {this.versionUrl = versionUrl; }

  public void setForceUpdate(boolean forceUpdate)
  {this.forceUpdate = forceUpdate; }

  public boolean equals(Object o)
  {
    if (o == this) return true;
    if (!(o instanceof UpdateInfo)) return false;
    final UpdateInfo other = (UpdateInfo) o;
    if (!other.canEqual((Object) this)) return false;
    final Object this$versionName  = this.getVersionName();
    final Object other$versionName = other.getVersionName();
    if (this$versionName == null ? other$versionName != null : !this$versionName.equals(other$versionName))
      return false;
    if (this.getVersionCode() != other.getVersionCode()) return false;
    final Object this$versionInfo  = this.getVersionInfo();
    final Object other$versionInfo = other.getVersionInfo();
    if (this$versionInfo == null ? other$versionInfo != null : !this$versionInfo.equals(other$versionInfo))
      return false;
    final Object this$versionUrl  = this.getVersionUrl();
    final Object other$versionUrl = other.getVersionUrl();
    if (this$versionUrl == null ? other$versionUrl != null : !this$versionUrl.equals(other$versionUrl))
      return false;
    if (this.isForceUpdate() != other.isForceUpdate()) return false;
    return true;
  }

  public int hashCode()
  {
    final int    PRIME        = 59;
    int          result       = 1;
    final Object $versionName = this.getVersionName();
    result = result * PRIME + ($versionName == null ? 43 : $versionName.hashCode());
    result = result * PRIME + this.getVersionCode();
    final Object $versionInfo = this.getVersionInfo();
    result = result * PRIME + ($versionInfo == null ? 43 : $versionInfo.hashCode());
    final Object $versionUrl = this.getVersionUrl();
    result = result * PRIME + ($versionUrl == null ? 43 : $versionUrl.hashCode());
    result = result * PRIME + (this.isForceUpdate() ? 79 : 97);
    return result;
  }

  protected boolean canEqual(Object other)
  {return other instanceof UpdateInfo;}

  public String toString()
  {return "UpdateInfo(versionName=" + this.getVersionName() + ", versionCode=" + this.getVersionCode() + ", versionInfo=" + this.getVersionInfo() + ", versionUrl=" + this.getVersionUrl() + ", forceUpdate=" + this.isForceUpdate() + ")";}
}

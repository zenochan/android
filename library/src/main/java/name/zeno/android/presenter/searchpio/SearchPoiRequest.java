package name.zeno.android.presenter.searchpio;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/12/23.
 */
public class SearchPoiRequest implements Parcelable
{
  //填充的值
  private String  fill;
  //是否允许使用用户输入的值作为结果
  private boolean enableOriginInput;

  public SearchPoiRequest() { }

  @Override public int describeContents() { return 0; }

  @Override public void writeToParcel(Parcel dest, int flags)
  {
    dest.writeString(this.fill);
    dest.writeByte(this.enableOriginInput ? (byte) 1 : (byte) 0);
  }

  protected SearchPoiRequest(Parcel in)
  {
    this.fill = in.readString();
    this.enableOriginInput = in.readByte() != 0;
  }

  public static final Parcelable.Creator<SearchPoiRequest> CREATOR = new Parcelable.Creator<SearchPoiRequest>()
  {
    @Override
    public SearchPoiRequest createFromParcel(Parcel source) {return new SearchPoiRequest(source);}

    @Override public SearchPoiRequest[] newArray(int size) {return new SearchPoiRequest[size];}
  };

  public String getFill()
  {return this.fill;}

  public boolean isEnableOriginInput()
  {return this.enableOriginInput;}

  public void setFill(String fill)
  {this.fill = fill; }

  public void setEnableOriginInput(boolean enableOriginInput)
  {this.enableOriginInput = enableOriginInput; }

  public boolean equals(Object o)
  {
    if (o == this) return true;
    if (!(o instanceof SearchPoiRequest)) return false;
    final SearchPoiRequest other = (SearchPoiRequest) o;
    if (!other.canEqual((Object) this)) return false;
    final Object this$fill  = this.getFill();
    final Object other$fill = other.getFill();
    if (this$fill == null ? other$fill != null : !this$fill.equals(other$fill)) return false;
    if (this.isEnableOriginInput() != other.isEnableOriginInput()) return false;
    return true;
  }

  public int hashCode()
  {
    final int    PRIME  = 59;
    int          result = 1;
    final Object $fill  = this.getFill();
    result = result * PRIME + ($fill == null ? 43 : $fill.hashCode());
    result = result * PRIME + (this.isEnableOriginInput() ? 79 : 97);
    return result;
  }

  protected boolean canEqual(Object other)
  {return other instanceof SearchPoiRequest;}

  public String toString()
  {return "SearchPoiRequest(fill=" + this.getFill() + ", enableOriginInput=" + this.isEnableOriginInput() + ")";}
}

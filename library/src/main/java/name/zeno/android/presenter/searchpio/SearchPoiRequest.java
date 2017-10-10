package name.zeno.android.presenter.searchpio;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/12/23.
 */
public class SearchPoiRequest implements Parcelable
{
  public static final String EXTRA_NAME = "SearchPoiRequest";

  //填充的值
  private String  fill;
  //是否允许使用用户输入的值作为结果
  private boolean enableOriginInput;


  public String getFill()
  {
    return fill;
  }

  public void setFill(String fill)
  {
    this.fill = fill;
  }

  public boolean isEnableOriginInput()
  {
    return enableOriginInput;
  }

  public void setEnableOriginInput(boolean enableOriginInput)
  {
    this.enableOriginInput = enableOriginInput;
  }

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
}

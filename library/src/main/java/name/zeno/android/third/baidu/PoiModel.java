package name.zeno.android.third.baidu;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import com.baidu.mapapi.search.core.PoiInfo;

import lombok.Data;

/**
 * 百度地图POI
 */
@Data @SuppressWarnings("WrongConstant")
public class PoiModel implements Parcelable
{
  public static final String COOKIE_LAST_POI = "bd_last_poi";
  private String name;
  private String address;
  private String city;
  @PoiTypeInt
  private int    type;
  private double latitude;
  private double longitude;
  private long   time;

  public PoiModel()
  {
    time = System.currentTimeMillis();
  }

  public PoiModel(@NonNull PoiInfo poiInfo)
  {
    this.name = poiInfo.name;
    this.address = poiInfo.address;
    this.city = poiInfo.city;
    if (poiInfo.type != null) {
      this.type = poiInfo.type.getInt();
    }

    if (poiInfo.location != null) {
      this.latitude = poiInfo.location.latitude;
      this.longitude = poiInfo.location.longitude;
    }
  }

  @IntDef({
      PoiTypeInt.POINT,
      PoiTypeInt.BUS_STATION,
      PoiTypeInt.BUS_LINE,
      PoiTypeInt.SUBWAY_STATION,
      PoiTypeInt.SUBWAY_LINE,
  })
  public @interface PoiTypeInt
  {
    int POINT          = 0;
    int BUS_STATION    = 1;
    int BUS_LINE       = 2;
    int SUBWAY_STATION = 3;
    int SUBWAY_LINE    = 4;
  }


  @Override public int describeContents() { return 0; }

  @Override public void writeToParcel(Parcel dest, int flags)
  {
    dest.writeString(this.name);
    dest.writeString(this.address);
    dest.writeString(this.city);
    dest.writeInt(this.type);
    dest.writeDouble(this.latitude);
    dest.writeDouble(this.longitude);
    dest.writeLong(this.time);
  }

  protected PoiModel(Parcel in)
  {
    this.name = in.readString();
    this.address = in.readString();
    this.city = in.readString();
    this.type = in.readInt();
    this.latitude = in.readDouble();
    this.longitude = in.readDouble();
    this.time = in.readLong();
  }

  public static final Parcelable.Creator<PoiModel> CREATOR = new Parcelable.Creator<PoiModel>()
  {
    @Override public PoiModel createFromParcel(Parcel source) {return new PoiModel(source);}

    @Override public PoiModel[] newArray(int size) {return new PoiModel[size];}
  };
}

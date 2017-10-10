package name.zeno.android.third.baidu;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import com.baidu.mapapi.search.core.PoiInfo;

/**
 * 百度地图POI
 */
@SuppressWarnings("WrongConstant")
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

  public String getName()
  {return this.name;}

  public String getAddress()
  {return this.address;}

  public String getCity()
  {return this.city;}

  public int getType()
  {return this.type;}

  public double getLatitude()
  {return this.latitude;}

  public double getLongitude()
  {return this.longitude;}

  public long getTime()
  {return this.time;}

  public void setName(String name)
  {this.name = name; }

  public void setAddress(String address)
  {this.address = address; }

  public void setCity(String city)
  {this.city = city; }

  public void setType(int type)
  {this.type = type; }

  public void setLatitude(double latitude)
  {this.latitude = latitude; }

  public void setLongitude(double longitude)
  {this.longitude = longitude; }

  public void setTime(long time)
  {this.time = time; }

  public boolean equals(Object o)
  {
    if (o == this) return true;
    if (!(o instanceof PoiModel)) return false;
    final PoiModel other = (PoiModel) o;
    if (!other.canEqual((Object) this)) return false;
    final Object this$name  = this.getName();
    final Object other$name = other.getName();
    if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
    final Object this$address  = this.getAddress();
    final Object other$address = other.getAddress();
    if (this$address == null ? other$address != null : !this$address.equals(other$address))
      return false;
    final Object this$city  = this.getCity();
    final Object other$city = other.getCity();
    if (this$city == null ? other$city != null : !this$city.equals(other$city)) return false;
    if (this.getType() != other.getType()) return false;
    if (Double.compare(this.getLatitude(), other.getLatitude()) != 0) return false;
    if (Double.compare(this.getLongitude(), other.getLongitude()) != 0) return false;
    if (this.getTime() != other.getTime()) return false;
    return true;
  }

  public int hashCode()
  {
    final int    PRIME  = 59;
    int          result = 1;
    final Object $name  = this.getName();
    result = result * PRIME + ($name == null ? 43 : $name.hashCode());
    final Object $address = this.getAddress();
    result = result * PRIME + ($address == null ? 43 : $address.hashCode());
    final Object $city = this.getCity();
    result = result * PRIME + ($city == null ? 43 : $city.hashCode());
    result = result * PRIME + this.getType();
    final long $latitude = Double.doubleToLongBits(this.getLatitude());
    result = result * PRIME + (int) ($latitude >>> 32 ^ $latitude);
    final long $longitude = Double.doubleToLongBits(this.getLongitude());
    result = result * PRIME + (int) ($longitude >>> 32 ^ $longitude);
    final long $time = this.getTime();
    result = result * PRIME + (int) ($time >>> 32 ^ $time);
    return result;
  }

  protected boolean canEqual(Object other)
  {return other instanceof PoiModel;}

  public String toString()
  {return "PoiModel(name=" + this.getName() + ", address=" + this.getAddress() + ", city=" + this.getCity() + ", type=" + this.getType() + ", latitude=" + this.getLatitude() + ", longitude=" + this.getLongitude() + ", time=" + this.getTime() + ")";}

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

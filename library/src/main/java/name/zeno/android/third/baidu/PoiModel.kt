package name.zeno.android.third.baidu

import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.IntDef

import com.baidu.mapapi.search.core.PoiInfo

/**
 * 百度地图POI
 */
class PoiModel(
    var name: String? = null,
    var address: String? = null,
    var city: String? = null,
    @PoiTypeInt
    var type: Int = POINT,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var time: Long = System.currentTimeMillis()
) : Parcelable {
  @IntDef(POINT.toLong(), BUS_STATION.toLong(), BUS_LINE.toLong(), SUBWAY_STATION.toLong(), SUBWAY_LINE.toLong())
  annotation class PoiTypeInt()

  constructor(poiInfo: PoiInfo) : this(
      name = poiInfo.name,
      address = poiInfo.address,
      city = poiInfo.city,
      type = poiInfo.type?.int ?: POINT,
      latitude = poiInfo.location?.latitude ?: 0.0,
      longitude = poiInfo.location?.longitude ?: 0.0
  )

  constructor(source: Parcel) : this(
      source.readString(),
      source.readString(),
      source.readString(),
      source.readInt(),
      source.readDouble(),
      source.readDouble(),
      source.readLong()
  )

  override fun describeContents() = 0

  override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
    writeString(name)
    writeString(address)
    writeString(city)
    writeInt(type)
    writeDouble(latitude)
    writeDouble(longitude)
    writeLong(time)
  }

  companion object {
    const val COOKIE_LAST_POI = "bd_last_poi"

    const val POINT = 0

    const val BUS_STATION = 1

    const val BUS_LINE = 2

    const val SUBWAY_STATION = 3

    const val SUBWAY_LINE = 4

    @JvmField
    val CREATOR: Parcelable.Creator<PoiModel> = object : Parcelable.Creator<PoiModel> {
      override fun createFromParcel(source: Parcel): PoiModel = PoiModel(source)
      override fun newArray(size: Int): Array<PoiModel?> = arrayOfNulls(size)
    }
  }
}

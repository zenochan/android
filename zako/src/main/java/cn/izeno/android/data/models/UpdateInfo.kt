package cn.izeno.android.data.models

import android.os.Parcel
import android.os.Parcelable
import cn.izeno.android.common.annotations.DataClass

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/11/21.
 */
@DataClass data class UpdateInfo(
    var versionName: String? = null,
    var versionCode: Int = 0,
    var versionInfo: String? = null,
    var versionUrl: String? = null,

    var isForceUpdate: Boolean = false
) : Parcelable {
  constructor(source: Parcel) : this(
      source.readString(),
      source.readInt(),
      source.readString(),
      source.readString(),
      1 == source.readInt()
  )

  override fun describeContents() = 0

  override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
    writeString(versionName)
    writeInt(versionCode)
    writeString(versionInfo)
    writeString(versionUrl)
    writeInt((if (isForceUpdate) 1 else 0))
  }

  companion object {
    @JvmField
    val CREATOR: Parcelable.Creator<UpdateInfo> = object : Parcelable.Creator<UpdateInfo> {
      override fun createFromParcel(source: Parcel): UpdateInfo = UpdateInfo(source)
      override fun newArray(size: Int): Array<UpdateInfo?> = arrayOfNulls(size)
    }
  }
}

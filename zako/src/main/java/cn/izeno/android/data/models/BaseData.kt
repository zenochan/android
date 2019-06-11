package cn.izeno.android.data.models

import android.os.Parcel
import android.os.Parcelable
import cn.izeno.android.common.annotations.DataClass

/**
 * # 用于 Intent 传递基础类型数据
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/10/11
 */
@Suppress("unused")
@DataClass data class BaseData(
    val string: String = "",
    val boolean: Boolean = false,
    val int: Int = 0,
    var long: Long = 0L,
    var float: Float = 0F,
    var double: Double = 0.0
) : Parcelable {
  constructor(source: Parcel) : this(
      source.readString(),
      1 == source.readInt(),
      source.readInt(),
      source.readLong(),
      source.readFloat(),
      source.readDouble()
  )

  constructor(value: String) : this(string = value)
  constructor(value: Boolean) : this(boolean = value)
  constructor(value: Int) : this(int = value)
  constructor(value: Long) : this(long = value)
  constructor(value: Float) : this(float = value)
  constructor(value: Double) : this(double = value)

  override fun describeContents() = 0

  override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
    writeString(string)
    writeInt((if (boolean) 1 else 0))
    writeInt(int)
    writeLong(long)
    writeFloat(float)
    writeDouble(double)
  }

  companion object {
    @JvmField
    val CREATOR: Parcelable.Creator<BaseData> = object : Parcelable.Creator<BaseData> {
      override fun createFromParcel(source: Parcel): BaseData = BaseData(source)
      override fun newArray(size: Int): Array<BaseData?> = arrayOfNulls(size)
    }
  }
}

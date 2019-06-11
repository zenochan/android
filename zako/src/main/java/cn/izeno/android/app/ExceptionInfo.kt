package cn.izeno.android.app

import android.app.Activity
import android.os.Parcel
import android.os.Parcelable

/**
 * Create Date: 16/6/30
 *
 * @author 陈治谋 (513500085@qq.com)
 */
class ExceptionInfo(
    var throwable: Throwable,
    var email: String,
    var mainActivityClass: Class<out Activity>,
    var accountJson: String? = null
) : Parcelable {

  @Suppress("UNCHECKED_CAST")
  constructor(source: Parcel) : this(
      source.readSerializable() as Throwable,
      source.readString(),
      source.readSerializable() as Class<out Activity>,
      source.readString()
  )

  override fun describeContents() = 0

  override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
    writeSerializable(throwable)
    writeString(email)
    writeSerializable(mainActivityClass)
    writeString(accountJson)
  }

  companion object {
    @JvmField
    val CREATOR: Parcelable.Creator<ExceptionInfo> = object : Parcelable.Creator<ExceptionInfo> {
      override fun createFromParcel(source: Parcel): ExceptionInfo = ExceptionInfo(source)
      override fun newArray(size: Int): Array<ExceptionInfo?> = arrayOfNulls(size)
    }
  }
}

package name.zeno.android.data.models

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.IntDef
import name.zeno.android.common.annotations.DataClass

/**
 * Create Date: 16/6/16
 *
 * @author 陈治谋 (513500085@qq.com)
 */
@DataClass data class TextData(
    var title: String? = null,
    var type: Int = 0,
    var result: String? = null,
    var preFill: String? = null,
    var hint: String? = null,
    var regex: String? = null,
    var regexHint: String? = null,
    var isWhiteStatusBar: Boolean = false,
    @DrawableRes
    var btnBackground: Int = 0
) : Parcelable {

  @IntDef(TYPE_TEXT, TYPE_NUMBER, TYPE_PHONE, TYPE_CONTENT)
  internal annotation class Type()

  constructor(source: Parcel) : this(
      source.readString(),
      source.readInt(),
      source.readString(),
      source.readString(),
      source.readString(),
      source.readString(),
      source.readString(),
      1 == source.readInt()
  )

  override fun describeContents() = 0

  override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
    writeString(title)
    writeInt(type)
    writeString(result)
    writeString(preFill)
    writeString(hint)
    writeString(regex)
    writeString(regexHint)
    writeInt((if (isWhiteStatusBar) 1 else 0))
  }

  companion object {
    const val TYPE_TEXT = 0     //单行文本
    const val TYPE_NUMBER = 1   //数字
    const val TYPE_PHONE = 2    //手机号
    const val TYPE_CONTENT = 3  //多行文本

    @JvmField
    val CREATOR: Parcelable.Creator<TextData> = object : Parcelable.Creator<TextData> {
      override fun createFromParcel(source: Parcel): TextData = TextData(source)
      override fun newArray(size: Int): Array<TextData?> = arrayOfNulls(size)
    }
  }
}

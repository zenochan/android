package name.zeno.android.presenter.searchpio

import android.os.Parcel
import android.os.Parcelable
import name.zeno.android.common.annotations.DataClass

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/12/23.
 */
@DataClass data class SearchPoiRequest(
    //填充的值
    var fill: String? = null,
    //是否允许使用用户输入的值作为结果
    var isEnableOriginInput: Boolean = false
) : Parcelable {
  constructor(source: Parcel) : this(
      source.readString(),
      1 == source.readInt()
  )

  override fun describeContents() = 0

  override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
    writeString(fill)
    writeInt((if (isEnableOriginInput) 1 else 0))
  }

  companion object {
    @JvmField
    val CREATOR: Parcelable.Creator<SearchPoiRequest> = object : Parcelable.Creator<SearchPoiRequest> {
      override fun createFromParcel(source: Parcel): SearchPoiRequest = SearchPoiRequest(source)
      override fun newArray(size: Int): Array<SearchPoiRequest?> = arrayOfNulls(size)
    }
  }
}

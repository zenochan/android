package name.zeno.android.jiguang

import android.os.Parcel
import android.os.Parcelable

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2018/5/24
 */
data class JPushMsg(
    val id: String,

    /**
     * 保存服务器推送下来的消息的标题。
     * - 对应 API 消息内容的 title 字段。
     * - Portal 推送消息界上不作展示
     */
    val title: String?,

    /**
     * 保存服务器推送下来的消息内容。
     * - 对应 API 消息内容的 message 字段。
     * - 对应 Portal 推送消息界面上的"自定义消息内容”字段。
     */
    val msg: String,

    /**
     * 保存服务器推送下来的附加字段。这是个 JSON 字符串。
     * - 对应 API 消息内容的 extras 字段。
     * - 对应 Portal 推送消息界面上的“可选设置”里的附加字段。
     */
    val extra: String?
) : Parcelable {
  constructor(parcel: Parcel) : this(
      parcel.readString(),
      parcel.readString(),
      parcel.readString(),
      parcel.readString()) {
  }

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeString(id)
    parcel.writeString(title)
    parcel.writeString(msg)
    parcel.writeString(extra)
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Parcelable.Creator<JPushMsg> {
    override fun createFromParcel(parcel: Parcel): JPushMsg {
      return JPushMsg(parcel)
    }

    override fun newArray(size: Int): Array<JPushMsg?> {
      return arrayOfNulls(size)
    }
  }

}
package name.zeno.android.third.wxapi

import android.os.Parcel
import android.os.Parcelable

import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXTextObject

import io.reactivex.Observable
import name.zeno.android.common.annotations.DataClass

/**
 * 发送文本消息
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/4/27
 */
@DataClass data class ZTextMessageReq(
    var text: String? = null,
    @WXScene
    var scene: Int = WXScene.SESSION
) : WxReq(), Parcelable {
  override fun build(): Observable<BaseReq> {
    val textObject = WXTextObject(text)
    val msg = WXMediaMessage(textObject)
    msg.description = text

    val req = SendMessageToWX.Req()
    req.transaction = "Zeno" //transaction 字段用于唯一标识一个请求
    req.message = msg
    req.scene = scene

    return Observable.just(req)
  }

  constructor(source: Parcel) : this(
      source.readString(),
      source.readInt()
  )

  override fun describeContents() = 0

  override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
    writeString(text)
    writeInt(scene)
  }

  companion object {
    @JvmField
    val CREATOR: Parcelable.Creator<ZTextMessageReq> = object : Parcelable.Creator<ZTextMessageReq> {
      override fun createFromParcel(source: Parcel): ZTextMessageReq = ZTextMessageReq(source)
      override fun newArray(size: Int): Array<ZTextMessageReq?> = arrayOfNulls(size)
    }
  }
}

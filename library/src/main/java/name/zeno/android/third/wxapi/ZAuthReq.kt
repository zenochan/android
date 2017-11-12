package name.zeno.android.third.wxapi

import android.os.Parcel
import android.os.Parcelable

import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelmsg.SendAuth

import io.reactivex.Observable

/**
 * 授权登陆
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/4/27
 */
class ZAuthReq(
    val scope: String = "snsapi_userinfo",
    val state: String = "wx_sdk_" + System.currentTimeMillis()
) : WxReq(), Parcelable {
  override fun build(): Observable<BaseReq> {
    val req = SendAuth.Req()
    req.scope = scope
    req.state = state
    return Observable.just(req)
  }

  constructor(source: Parcel) : this(
      source.readString(),
      source.readString()
  )

  override fun describeContents() = 0

  override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
    writeString(scope)
    writeString(state)
  }

  companion object {
    @JvmField
    val CREATOR: Parcelable.Creator<ZAuthReq> = object : Parcelable.Creator<ZAuthReq> {
      override fun createFromParcel(source: Parcel): ZAuthReq = ZAuthReq(source)
      override fun newArray(size: Int): Array<ZAuthReq?> = arrayOfNulls(size)
    }
  }
}

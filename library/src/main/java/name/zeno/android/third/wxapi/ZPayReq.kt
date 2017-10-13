package name.zeno.android.third.wxapi

import android.os.Parcel
import android.os.Parcelable

import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelpay.PayReq

import io.reactivex.Observable

/**
 * 发起支付
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/4/27
 */
class ZPayReq(
    var appId: String? = null,
    var partnerId: String? = null,
    var prepayId: String? = null,
    var packageValue: String? = null,
    var nonceStr: String? = null,
    var timeStamp: String? = null,
    var sign: String? = null
) : AbsReq(), Parcelable {
  override fun build(): Observable<BaseReq> {
    val payReq = PayReq()
    payReq.appId = appId
    payReq.partnerId = partnerId
    payReq.prepayId = prepayId
    payReq.packageValue = packageValue
    payReq.nonceStr = nonceStr
    payReq.timeStamp = timeStamp
    payReq.sign = sign
    return Observable.just(payReq)
  }

  constructor(source: Parcel) : this(
      source.readString(),
      source.readString(),
      source.readString(),
      source.readString(),
      source.readString(),
      source.readString(),
      source.readString()
  )

  override fun describeContents() = 0

  override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
    writeString(appId)
    writeString(partnerId)
    writeString(prepayId)
    writeString(packageValue)
    writeString(nonceStr)
    writeString(timeStamp)
    writeString(sign)
  }

  companion object {
    @JvmField
    val CREATOR: Parcelable.Creator<ZPayReq> = object : Parcelable.Creator<ZPayReq> {
      override fun createFromParcel(source: Parcel): ZPayReq = ZPayReq(source)
      override fun newArray(size: Int): Array<ZPayReq?> = arrayOfNulls(size)
    }
  }
}

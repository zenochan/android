package name.zeno.android.third.wxapi;

import android.os.Parcel;
import android.os.Parcelable;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelpay.PayReq;

import io.reactivex.Observable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 发起支付
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/4/27
 */
@SuppressWarnings("WeakerAccess")
@Data @NoArgsConstructor @EqualsAndHashCode(callSuper = false)
public class ZPayReq extends AbsReq
{
  private String appId;
  private String partnerId;
  private String prepayId;
  private String packageValue;
  private String nonceStr;
  private String timeStamp;
  private String sign;

  @Override public Observable<BaseReq> build()
  {
    PayReq payReq = new PayReq();
    payReq.appId = appId;
    payReq.partnerId = partnerId;
    payReq.prepayId = prepayId;
    payReq.packageValue = packageValue;
    payReq.nonceStr = nonceStr;
    payReq.timeStamp = timeStamp;
    payReq.sign = sign;
    return Observable.just(payReq);
  }

  @Override public int describeContents() { return 0; }

  @Override public void writeToParcel(Parcel dest, int flags)
  {
    dest.writeString(this.appId);
    dest.writeString(this.partnerId);
    dest.writeString(this.prepayId);
    dest.writeString(this.packageValue);
    dest.writeString(this.nonceStr);
    dest.writeString(this.timeStamp);
    dest.writeString(this.sign);
  }

  protected ZPayReq(Parcel in)
  {
    this.appId = in.readString();
    this.partnerId = in.readString();
    this.prepayId = in.readString();
    this.packageValue = in.readString();
    this.nonceStr = in.readString();
    this.timeStamp = in.readString();
    this.sign = in.readString();
  }

  public static final Parcelable.Creator<ZPayReq> CREATOR = new Parcelable.Creator<ZPayReq>()
  {
    @Override public ZPayReq createFromParcel(Parcel source) {return new ZPayReq(source);}

    @Override public ZPayReq[] newArray(int size) {return new ZPayReq[size];}
  };
}

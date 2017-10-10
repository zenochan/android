package name.zeno.android.third.wxapi;

import android.os.Parcel;
import android.os.Parcelable;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelpay.PayReq;

import io.reactivex.Observable;

/**
 * 发起支付
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/4/27
 */
@SuppressWarnings("WeakerAccess")
public class ZPayReq extends AbsReq
{
  private String appId;
  private String partnerId;
  private String prepayId;
  private String packageValue;
  private String nonceStr;
  private String timeStamp;
  private String sign;

  public ZPayReq() {}

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

  public String getAppId()
  {return this.appId;}

  public String getPartnerId()
  {return this.partnerId;}

  public String getPrepayId()
  {return this.prepayId;}

  public String getPackageValue()
  {return this.packageValue;}

  public String getNonceStr()
  {return this.nonceStr;}

  public String getTimeStamp()
  {return this.timeStamp;}

  public String getSign()
  {return this.sign;}

  public void setAppId(String appId)
  {this.appId = appId; }

  public void setPartnerId(String partnerId)
  {this.partnerId = partnerId; }

  public void setPrepayId(String prepayId)
  {this.prepayId = prepayId; }

  public void setPackageValue(String packageValue)
  {this.packageValue = packageValue; }

  public void setNonceStr(String nonceStr)
  {this.nonceStr = nonceStr; }

  public void setTimeStamp(String timeStamp)
  {this.timeStamp = timeStamp; }

  public void setSign(String sign)
  {this.sign = sign; }

  public String toString()
  {return "ZPayReq(appId=" + this.getAppId() + ", partnerId=" + this.getPartnerId() + ", prepayId=" + this.getPrepayId() + ", packageValue=" + this.getPackageValue() + ", nonceStr=" + this.getNonceStr() + ", timeStamp=" + this.getTimeStamp() + ", sign=" + this.getSign() + ")";}

  public boolean equals(Object o)
  {
    if (o == this) return true;
    if (!(o instanceof ZPayReq)) return false;
    final ZPayReq other = (ZPayReq) o;
    if (!other.canEqual((Object) this)) return false;
    final Object this$appId  = this.getAppId();
    final Object other$appId = other.getAppId();
    if (this$appId == null ? other$appId != null : !this$appId.equals(other$appId)) return false;
    final Object this$partnerId  = this.getPartnerId();
    final Object other$partnerId = other.getPartnerId();
    if (this$partnerId == null ? other$partnerId != null : !this$partnerId.equals(other$partnerId))
      return false;
    final Object this$prepayId  = this.getPrepayId();
    final Object other$prepayId = other.getPrepayId();
    if (this$prepayId == null ? other$prepayId != null : !this$prepayId.equals(other$prepayId))
      return false;
    final Object this$packageValue  = this.getPackageValue();
    final Object other$packageValue = other.getPackageValue();
    if (this$packageValue == null ? other$packageValue != null : !this$packageValue.equals(other$packageValue))
      return false;
    final Object this$nonceStr  = this.getNonceStr();
    final Object other$nonceStr = other.getNonceStr();
    if (this$nonceStr == null ? other$nonceStr != null : !this$nonceStr.equals(other$nonceStr))
      return false;
    final Object this$timeStamp  = this.getTimeStamp();
    final Object other$timeStamp = other.getTimeStamp();
    if (this$timeStamp == null ? other$timeStamp != null : !this$timeStamp.equals(other$timeStamp))
      return false;
    final Object this$sign  = this.getSign();
    final Object other$sign = other.getSign();
    if (this$sign == null ? other$sign != null : !this$sign.equals(other$sign)) return false;
    return true;
  }

  public int hashCode()
  {
    final int    PRIME  = 59;
    int          result = 1;
    final Object $appId = this.getAppId();
    result = result * PRIME + ($appId == null ? 43 : $appId.hashCode());
    final Object $partnerId = this.getPartnerId();
    result = result * PRIME + ($partnerId == null ? 43 : $partnerId.hashCode());
    final Object $prepayId = this.getPrepayId();
    result = result * PRIME + ($prepayId == null ? 43 : $prepayId.hashCode());
    final Object $packageValue = this.getPackageValue();
    result = result * PRIME + ($packageValue == null ? 43 : $packageValue.hashCode());
    final Object $nonceStr = this.getNonceStr();
    result = result * PRIME + ($nonceStr == null ? 43 : $nonceStr.hashCode());
    final Object $timeStamp = this.getTimeStamp();
    result = result * PRIME + ($timeStamp == null ? 43 : $timeStamp.hashCode());
    final Object $sign = this.getSign();
    result = result * PRIME + ($sign == null ? 43 : $sign.hashCode());
    return result;
  }

  protected boolean canEqual(Object other)
  {return other instanceof ZPayReq;}
}

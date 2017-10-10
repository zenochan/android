package name.zeno.android.third.wxapi;

import android.os.Parcel;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import io.reactivex.Observable;

/**
 * 授权登陆
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/4/27
 */
public class ZAuthReq extends AbsReq
{
  private String scope = "snsapi_userinfo";
  private String state = "wx_sdk_" + System.currentTimeMillis();

  public String getScope()
  {
    return scope;
  }

  public void setScope(String scope)
  {
    this.scope = scope;
  }

  public String getState()
  {
    return state;
  }

  public void setState(String state)
  {
    this.state = state;
  }

  @Override public Observable<BaseReq> build()
  {
    SendAuth.Req req = new SendAuth.Req();
    req.scope = scope;
    req.state = state;
    return Observable.just(req);
  }

  @Override public int describeContents() { return 0; }

  @Override public void writeToParcel(Parcel dest, int flags)
  {
    dest.writeString(this.scope);
    dest.writeString(this.state);
  }

  protected ZAuthReq(Parcel in)
  {
    this.scope = in.readString();
    this.state = in.readString();
  }

  public static final Creator<ZAuthReq> CREATOR = new Creator<ZAuthReq>()
  {
    @Override public ZAuthReq createFromParcel(Parcel source) {return new ZAuthReq(source);}

    @Override public ZAuthReq[] newArray(int size) {return new ZAuthReq[size];}
  };
}

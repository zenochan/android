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

  public ZAuthReq() {}

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

  public String getScope()
  {return this.scope;}

  public String getState()
  {return this.state;}

  public void setScope(String scope)
  {this.scope = scope; }

  public void setState(String state)
  {this.state = state; }

  public String toString()
  {return "ZAuthReq(scope=" + this.getScope() + ", state=" + this.getState() + ")";}

  public boolean equals(Object o)
  {
    if (o == this) return true;
    if (!(o instanceof ZAuthReq)) return false;
    final ZAuthReq other = (ZAuthReq) o;
    if (!other.canEqual((Object) this)) return false;
    final Object this$scope  = this.getScope();
    final Object other$scope = other.getScope();
    if (this$scope == null ? other$scope != null : !this$scope.equals(other$scope)) return false;
    final Object this$state  = this.getState();
    final Object other$state = other.getState();
    if (this$state == null ? other$state != null : !this$state.equals(other$state)) return false;
    return true;
  }

  public int hashCode()
  {
    final int    PRIME  = 59;
    int          result = 1;
    final Object $scope = this.getScope();
    result = result * PRIME + ($scope == null ? 43 : $scope.hashCode());
    final Object $state = this.getState();
    result = result * PRIME + ($state == null ? 43 : $state.hashCode());
    return result;
  }

  protected boolean canEqual(Object other)
  {return other instanceof ZAuthReq;}
}

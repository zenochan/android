package name.zeno.android.third.wxapi;

import android.os.Parcel;
import android.os.Parcelable;

import com.tencent.mm.sdk.modelbase.BaseResp;

import lombok.Data;

/**
 * 微信响应结果封装
 * Create Date: 16/6/9
 *
 * @author 陈治谋 (513500085@qq.com)
 */
@Data
public class WxRespWrapper implements Parcelable
{
  public static final String                 EXTRA_NAME = "extra_wx_resp";
  public static final Creator<WxRespWrapper> CREATOR    = new Creator<WxRespWrapper>()
  {
    @Override
    public WxRespWrapper createFromParcel(Parcel source) {return new WxRespWrapper(source);}

    @Override public WxRespWrapper[] newArray(int size) {return new WxRespWrapper[size];}
  };

  /**
   * 消息类型
   *
   * @see com.tencent.mm.sdk.constants.ConstantsAPI#COMMAND_SENDAUTH 微信授权登录
   * @see com.tencent.mm.sdk.constants.ConstantsAPI#COMMAND_SENDMESSAGE_TO_WX 发送消息到微信（分享）
   * @see com.tencent.mm.sdk.constants.ConstantsAPI#COMMAND_PAY_BY_WX 微信支付
   */
  private int type;

  /**
   * 错误码
   *
   * @see com.tencent.mm.sdk.modelbase.BaseResp.ErrCode#ERR_OK 0 成功
   * @see com.tencent.mm.sdk.modelbase.BaseResp.ErrCode#ERR_COMM
   * @see com.tencent.mm.sdk.modelbase.BaseResp.ErrCode#ERR_USER_CANCEL -2 用户取消
   * @see com.tencent.mm.sdk.modelbase.BaseResp.ErrCode#ERR_SENT_FAILED -3 发送失败
   * @see com.tencent.mm.sdk.modelbase.BaseResp.ErrCode#ERR_UNSUPPORT -5 不支持此功能
   * @see com.tencent.mm.sdk.modelbase.BaseResp.ErrCode#ERR_BAN
   */
  private int errCode;

  private String code;//授权登录获取的code

  private String desc;//描述

  public WxRespWrapper() {}

  protected WxRespWrapper(Parcel in)
  {
    this.type = in.readInt();
    this.errCode = in.readInt();
    this.code = in.readString();
    this.desc = in.readString();
  }

  public boolean success()
  {
    return errCode == BaseResp.ErrCode.ERR_OK;
  }

  @Override public int describeContents() { return 0; }

  @Override public void writeToParcel(Parcel dest, int flags)
  {
    dest.writeInt(this.type);
    dest.writeInt(this.errCode);
    dest.writeString(this.code);
    dest.writeString(this.desc);
  }

}

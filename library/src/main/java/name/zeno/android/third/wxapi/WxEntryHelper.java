package name.zeno.android.third.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import name.zeno.android.listener.Action1;

/**
 * 微信入口帮助类
 * Create Date: 16/6/8
 * <p>
 * <h3>将下列方法与 Activity 同步</h3>
 * <ul>
 * <li>{@link #onCreate(Activity, String)}</li>
 * <li>{@link #onNewIntent(Intent)}</li>
 * <li>{@link #onResume()}</li>
 * </ul>
 *
 * @author 陈治谋 (513500085@qq.com)
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class WxEntryHelper implements IWXAPIEventHandler
{
  public static final int SHARE_TYPE_SESSION  = SendMessageToWX.Req.WXSceneSession;
  public static final int SHARE_TYPE_TIMELINE = SendMessageToWX.Req.WXSceneTimeline;
  public static final int SHARE_TYPE_FAVORITE = SendMessageToWX.Req.WXSceneFavorite;

  private static final String TAG = "WxEntryHelper";

  private IWXAPI   api;
  private BaseResp resp;

  private Action1<WxRespWrapper> onPayRespListener;

  private Context context;


  public void onCreate(Activity activity, String appId)
  {
    resp = null;
    context = activity.getApplication();
    api = WXAPIFactory.createWXAPI(activity.getApplicationContext(), appId, false);
    api.registerApp(appId);
    api.handleIntent(activity.getIntent(), this);
  }

  public void onNewIntent(Intent intent)
  {
    resp = null;
    api.handleIntent(intent, this);
  }

  public void onResume()
  {
    if (resp == null) {
      return;
    }

    int           type        = resp.getType();
    int           errCode     = resp.errCode;
    WxRespWrapper respWrapper = new WxRespWrapper();
    respWrapper.setType(type);
    respWrapper.setErrCode(errCode);
    switch (type) {
      case ConstantsAPI.COMMAND_PAY_BY_WX://微信支付
        if (onPayRespListener != null) {
          if (errCode == BaseResp.ErrCode.ERR_OK) {
            respWrapper.setDesc("支付成功");
          } else if (errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
            respWrapper.setDesc("取消支付");
          } else {
            respWrapper.setDesc("支付未完成");
          }
          onPayRespListener.call(respWrapper);
        }
        break;
    }
    resp = null;
  }

  @Override public void onReq(BaseReq baseReq) { }

  @Override public void onResp(BaseResp baseResp) { resp = baseResp; }

  //微信支付
  public void pay(String appId, String partnerId, String prepayId, String packageValue, String nonceStr, String timeStamp, String sign)
  {
    PayReq payReq = new PayReq();
    payReq.appId = appId;
    payReq.partnerId = partnerId;
    payReq.prepayId = prepayId;
    payReq.packageValue = packageValue;
    payReq.nonceStr = nonceStr;
    payReq.timeStamp = timeStamp;
    payReq.sign = sign;
    pay(payReq);
  }

  public void pay(PayReq payReq)
  {
    if (isWXAppInstalled()) {
      api.sendReq(payReq);
    } else if (onPayRespListener != null) {
      WxRespWrapper res = new WxRespWrapper();
      res.setType(ConstantsAPI.COMMAND_PAY_BY_WX);
      res.setDesc("未安装微信,不能完成支付");
      res.setErrCode(BaseResp.ErrCode.ERR_UNSUPPORT);
      onPayRespListener.call(res);
    }
  }

  public boolean isWXAppInstalled()
  {
    return api != null && api.isWXAppInstalled();
  }

  // 分享文本消息
  public void shareText(String text)
  {
    WXTextObject textObject = new WXTextObject();
    textObject.text = text;
    WXMediaMessage msg = new WXMediaMessage(textObject);
    msg.description = text;

    SendMessageToWX.Req req = new SendMessageToWX.Req();
    req.transaction = "Zeno"; //transaction 字段用于唯一标识一个请求
    req.message = msg;
    req.scene = WXScene.SESSION;

    api.sendReq(req);
  }

  public Action1<WxRespWrapper> getOnPayRespListener()
  {return this.onPayRespListener;}

  public void setOnPayRespListener(Action1<WxRespWrapper> onPayRespListener)
  {this.onPayRespListener = onPayRespListener; }
}

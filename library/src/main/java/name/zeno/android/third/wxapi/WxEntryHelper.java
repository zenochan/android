package name.zeno.android.third.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import lombok.Getter;
import lombok.Setter;
import name.zeno.android.annotation.WXScene;
import name.zeno.android.listener.Action1;
import name.zeno.android.third.rxjava.RxUtils;
import name.zeno.android.util.ZBitmap;
import name.zeno.android.util.R;

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
@SuppressWarnings("unused")
public class WxEntryHelper implements IWXAPIEventHandler
{
  public static final int SHARE_TYPE_SESSION  = SendMessageToWX.Req.WXSceneSession;
  public static final int SHARE_TYPE_TIMELINE = SendMessageToWX.Req.WXSceneTimeline;
  public static final int SHARE_TYPE_FAVORITE = SendMessageToWX.Req.WXSceneFavorite;

  private static final String TAG = "WxEntryHelper";

  private IWXAPI   api;
  private BaseResp resp;

  @Getter @Setter private Action1<WxRespWrapper> onPayRespListener;
  @Getter @Setter private Action1<WxRespWrapper> onAuthRespListener;
  @Getter @Setter private Action1<WxRespWrapper> onShareRespListener;

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
      case ConstantsAPI.COMMAND_SENDAUTH: //微信登录
        if (onAuthRespListener != null) {
          if (BaseResp.ErrCode.ERR_OK == errCode) {
            respWrapper.setCode(((SendAuth.Resp) resp).code);
            respWrapper.setDesc("登录成功");
          } else if (BaseResp.ErrCode.ERR_USER_CANCEL == errCode) {
            respWrapper.setDesc("取消登录");
          } else if (BaseResp.ErrCode.ERR_AUTH_DENIED == errCode) {
            respWrapper.setDesc("拒绝登录");
          }
          onAuthRespListener.call(respWrapper);
        }
        break;
      case ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX: //发送消息到微信（如 分享）
        if (onShareRespListener != null) {
          if (BaseResp.ErrCode.ERR_OK == errCode) {
            respWrapper.setDesc("分享成功");
          } else if (BaseResp.ErrCode.ERR_USER_CANCEL == errCode) {
            respWrapper.setDesc("取消分享");
          } else {
            respWrapper.setDesc("分享失败");
          }
          onShareRespListener.call(respWrapper);
        }
        break;
    }
    resp = null;
  }

  @Override public void onReq(BaseReq baseReq) { }

  @Override public void onResp(BaseResp baseResp) { resp = baseResp; }

  /** 微信登录 */
  public void auth()
  {
    SendAuth.Req req = new SendAuth.Req();
    req.scope = "snsapi_userinfo";
    req.state = "wx_sdk_" + System.currentTimeMillis();
    api.sendReq(req);
  }

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


  // 分享网址
  public void shareWebpage(String url, String title, String desc, @WXScene int scene, String iconUrl)
  {
    Observable.create((ObservableOnSubscribe<Bitmap>) subscriber -> {
      Bitmap bitmap = ZBitmap.bitmap(iconUrl);
      if (bitmap == null) {
        bitmap = ZBitmap.bitmap(ContextCompat.getDrawable(context, R.mipmap.ic_add), true);
      }
      subscriber.onNext(bitmap);
      subscriber.onComplete();
    })
        .compose(RxUtils.applySchedulers())
        .subscribe(new Observer<Bitmap>()
        {
          @Override public void onSubscribe(Disposable d) { }

          @Override public void onComplete() { }

          @Override public void onError(Throwable e)
          {
            Log.e(TAG, e.getMessage(), e);
          }

          @Override public void onNext(Bitmap bitmap)
          {
            shareWebpage(url, title, desc, scene, bitmap);
          }
        });
  }

  // 分享网址
  public void shareWebpage(String url, String title, String desc, @WXScene int scene, Bitmap bitmap)
  {
    if (bitmap == null) {
      bitmap = ZBitmap.bitmap(ContextCompat.getDrawable(context, R.mipmap.ic_add), true);
    }
    WXWebpageObject webpage = new WXWebpageObject();
    webpage.webpageUrl = url;
    WXMediaMessage msg = new WXMediaMessage(webpage);
    msg.title = title;
    msg.description = desc;
    if (bitmap.getWidth() > 256 || bitmap.getHeight() > 256) {
      bitmap = ZBitmap.zoom(bitmap, 256, 256);
    }
    msg.setThumbImage(bitmap);

    SendMessageToWX.Req req = new SendMessageToWX.Req();
    req.transaction = "Zeno"; //transaction 字段用于唯一标识一个请求
    req.message = msg;
    req.scene = scene;

    api.sendReq(req);
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
}

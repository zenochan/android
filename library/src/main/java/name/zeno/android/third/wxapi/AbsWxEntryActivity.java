package name.zeno.android.third.wxapi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.afollestad.materialdialogs.MaterialDialog;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import io.reactivex.disposables.Disposable;
import name.zeno.android.exception.ZException;
import name.zeno.android.presenter.Extra;
import name.zeno.android.presenter.ZActivity;
import name.zeno.android.third.rxjava.RxUtils;
import name.zeno.android.third.rxjava.ZObserver;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/4/27
 */
public abstract class AbsWxEntryActivity extends ZActivity implements IWXAPIEventHandler
{
  private static final String TAG = "AbsWxEntryActivity";

  private IWXAPI     api;
  private Disposable disposable;

  private boolean finishIfNullRes = false;
  private BaseResp       resp;
  private MaterialDialog dialog;

  /** 微信是否已安装 */
  public static boolean isWxAppInstalled(Context context)
  {
    return WXAPIFactory.createWXAPI(context, null).isWXAppInstalled();
  }

  /** 注册 App 到微信 */
  public static boolean register(Context context, String appId)
  {
    IWXAPI api = WXAPIFactory.createWXAPI(context, appId, true);
    return api.isWXAppInstalled() && api.registerApp(appId);
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    api = WXAPIFactory.createWXAPI(this, getAppId(), true);
    api.registerApp(getAppId());
    AbsReq req = Extra.getData(getIntent());
    if (req != null) {
      dialog = new MaterialDialog.Builder(this)
          .progress(true, 5000, true)
          .content("正在打开微信...")
          .cancelListener((dialogInterface) -> finish())
          .show();

      req.build().compose(RxUtils.applySchedulers()).subscribe(new ZObserver<BaseReq>()
      {
        @Override public void onNext(BaseReq value)
        {
          api.sendReq(value);
          finishIfNullRes = true;
        }

        @Override public void handleError(ZException e)
        {
          super.handleError(e);
          finish();
        }

        @Override public void onSubscribe(Disposable d)
        {
          disposable = d;
        }
      });
    } else {
      finish();
    }
  }

  @Override protected void onNewIntent(Intent intent)
  {
    super.onNewIntent(intent);
    setIntent(intent);
    api.handleIntent(intent, this);
  }

  @Override protected void onResume()
  {
    super.onResume();
    if (resp != null) {

      int           type        = resp.getType();
      int           errCode     = resp.errCode;
      WxRespWrapper respWrapper = new WxRespWrapper();
      respWrapper.setType(type);
      respWrapper.setErrCode(errCode);
      switch (type) {
        case ConstantsAPI.COMMAND_PAY_BY_WX://微信支付
          if (errCode == BaseResp.ErrCode.ERR_OK) {
            respWrapper.setDesc("支付成功");
          } else if (errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
            respWrapper.setDesc("取消支付");
          } else {
            respWrapper.setDesc("支付未完成");
          }
          break;
        case ConstantsAPI.COMMAND_SENDAUTH: //微信登录
          if (BaseResp.ErrCode.ERR_OK == errCode) {
            respWrapper.setCode(((SendAuth.Resp) resp).code);
            respWrapper.setDesc("登录成功");
          } else if (BaseResp.ErrCode.ERR_USER_CANCEL == errCode) {
            respWrapper.setDesc("取消登录");
          } else if (BaseResp.ErrCode.ERR_AUTH_DENIED == errCode) {
            respWrapper.setDesc("拒绝登录");
          }
          break;
        case ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX: //发送消息到微信（如 分享）
          if (BaseResp.ErrCode.ERR_OK == errCode) {
            respWrapper.setDesc("分享成功");
          } else if (BaseResp.ErrCode.ERR_USER_CANCEL == errCode) {
            respWrapper.setDesc("取消分享");
          } else {
            respWrapper.setDesc("分享失败");
          }
          break;
      }
      setResult(RESULT_OK, Extra.setData(respWrapper));
      finish();
    } else if (finishIfNullRes) {
      finish();
    }
  }

  @Override protected void onDestroy()
  {
    super.onDestroy();
    // 切断管道
    if (disposable != null && !disposable.isDisposed()) {
      disposable.dispose();
      disposable = null;
    }

    if (dialog != null && dialog.isShowing()) {
      dialog.dismiss();
      dialog = null;
    }
  }

  @Override public void onReq(BaseReq baseReq)
  {
    // do nothing
    finish();
  }

  @Override public void onResp(BaseResp baseResp)
  {
    this.resp = baseResp;
  }

  /**
   * @return 微信 AppId
   */
  protected abstract String getAppId();

}

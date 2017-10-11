package name.zeno.android.third.wxapi

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle

import com.afollestad.materialdialogs.MaterialDialog
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory

import io.reactivex.disposables.Disposable
import name.zeno.android.exception.ZException
import name.zeno.android.presenter.Extra
import name.zeno.android.presenter.ZActivity
import name.zeno.android.third.rxjava.RxUtils
import name.zeno.android.third.rxjava.ZObserver

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/4/27
 */
abstract class AbsWxEntryActivity : ZActivity(), IWXAPIEventHandler {

  private var api: IWXAPI? = null
  private var disposable: Disposable? = null

  private var finishIfNullRes = false
  private var resp: BaseResp? = null
  private var dialog: MaterialDialog? = null

  /**
   * @return 微信 AppId
   */
  protected abstract val appId: String

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    api = WXAPIFactory.createWXAPI(this, appId, true)
    api!!.registerApp(appId)
    val req = Extra.getData<AbsReq>(intent)
    if (req != null) {
      dialog = MaterialDialog.Builder(this)
          .progress(true, 5000, true)
          .content("正在打开微信...")
          .cancelListener { dialogInterface -> finish() }
          .show()

      req.build().compose(RxUtils.applySchedulers()).subscribe(object : ZObserver<BaseReq>() {
        override fun onNext(value: BaseReq) {
          api!!.sendReq(value)
          finishIfNullRes = true
        }

        override fun handleError(e: ZException) {
          super.handleError(e)
          finish()
        }

        override fun onSubscribe(d: Disposable) {
          disposable = d
        }
      })
    } else {
      finish()
    }
  }

  override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    setIntent(intent)
    api!!.handleIntent(intent, this)
  }

  override fun onResume() {
    super.onResume()
    if (resp != null) {

      val type = resp!!.type
      val errCode = resp!!.errCode
      val respWrapper = WxRespWrapper()
      respWrapper.type = type
      respWrapper.errCode = errCode
      when (type) {
        ConstantsAPI.COMMAND_PAY_BY_WX//微信支付
        -> if (errCode == BaseResp.ErrCode.ERR_OK) {
          respWrapper.desc = "支付成功"
        } else if (errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
          respWrapper.desc = "取消支付"
        } else {
          respWrapper.desc = "支付未完成"
        }
        ConstantsAPI.COMMAND_SENDAUTH //微信登录
        -> if (BaseResp.ErrCode.ERR_OK == errCode) {
          respWrapper.code = (resp as SendAuth.Resp).code
          respWrapper.desc = "登录成功"
        } else if (BaseResp.ErrCode.ERR_USER_CANCEL == errCode) {
          respWrapper.desc = "取消登录"
        } else if (BaseResp.ErrCode.ERR_AUTH_DENIED == errCode) {
          respWrapper.desc = "拒绝登录"
        }
        ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX //发送消息到微信（如 分享）
        -> if (BaseResp.ErrCode.ERR_OK == errCode) {
          respWrapper.desc = "分享成功"
        } else if (BaseResp.ErrCode.ERR_USER_CANCEL == errCode) {
          respWrapper.desc = "取消分享"
        } else {
          respWrapper.desc = "分享失败"
        }
      }
      setResult(Activity.RESULT_OK, Extra.setData(respWrapper))
      finish()
    } else if (finishIfNullRes) {
      finish()
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    // 切断管道
    if (disposable != null && !disposable!!.isDisposed) {
      disposable!!.dispose()
      disposable = null
    }

    if (dialog != null && dialog!!.isShowing) {
      dialog!!.dismiss()
      dialog = null
    }
  }

  override fun onReq(baseReq: BaseReq) {
    // do nothing
    finish()
  }

  override fun onResp(baseResp: BaseResp) {
    this.resp = baseResp
  }

  companion object {
    private val TAG = "AbsWxEntryActivity"

    /** 微信是否已安装  */
    fun isWxAppInstalled(context: Context): Boolean {
      return WXAPIFactory.createWXAPI(context, null).isWXAppInstalled
    }

    /** 注册 App 到微信  */
    fun register(context: Context, appId: String): Boolean {
      val api = WXAPIFactory.createWXAPI(context, appId, true)
      return api.isWXAppInstalled && api.registerApp(appId)
    }
  }

}

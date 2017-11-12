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
import name.zeno.android.core.dataOrNull
import name.zeno.android.exception.ZException
import name.zeno.android.presenter.Extra
import name.zeno.android.presenter.ZActivity
import name.zeno.android.third.rxjava.RxUtils
import name.zeno.android.third.rxjava.ZObserver

/**
 * * [register] 注册 App 到微信
 * * [isWxAppInstalled] 是否安装微信
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/4/27
 */
abstract class AbsWxEntryActivity : ZActivity(), IWXAPIEventHandler {

  private lateinit var api: IWXAPI
  private var disposable: Disposable? = null

  // 处理分享到微信但是没有立马返回 App 的场景
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
    api.registerApp(appId)
    val req: WxReq? = dataOrNull()

    if (req == null) {
      finish()
      return
    }

    dialog = MaterialDialog.Builder(this)
        .progress(true, 5000, true)
        .content("正在打开微信...")
        .cancelListener { finish() }
        .show()

    req.build().compose(RxUtils.applySchedulers()).subscribe(object : ZObserver<BaseReq>() {
      override fun onNext(value: BaseReq) {
        api.sendReq(value)
        finishIfNullRes = true
      }

      override fun handleError(e: ZException) = finish()

      override fun onSubscribe(d: Disposable) {
        disposable = d
      }
    })
  }

  override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    setIntent(intent)
    api.handleIntent(intent, this)
  }

  override fun onResume() {
    super.onResume()
    if (resp != null) {

      val type = resp!!.type
      val errCode = resp!!.errCode
      val respWrapper = WxRespWrapper()
      respWrapper.type = type
      respWrapper.errCode = errCode
      respWrapper.desc = when (type) {

        ConstantsAPI.COMMAND_PAY_BY_WX -> when (errCode) {                //微信支付
          BaseResp.ErrCode.ERR_OK -> "支付成功"
          BaseResp.ErrCode.ERR_USER_CANCEL -> "取消支付"
          else -> "支付未完成"
        }

        ConstantsAPI.COMMAND_SENDAUTH -> when (errCode) {               //微信登录
          BaseResp.ErrCode.ERR_OK -> {
            respWrapper.code = (resp as SendAuth.Resp).code
            "登录成功"
          }
          BaseResp.ErrCode.ERR_USER_CANCEL -> "取消登录"
          BaseResp.ErrCode.ERR_AUTH_DENIED -> "拒绝登录"
          else -> null
        }

        ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX -> when (errCode) {      //发送消息到微信（如 分享）
          BaseResp.ErrCode.ERR_OK -> "分享成功"
          BaseResp.ErrCode.ERR_USER_CANCEL -> "取消分享"
          else -> "分享失败"
        }

        else -> null
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

  override fun onReq(baseReq: BaseReq) = finish()

  override fun onResp(baseResp: BaseResp) {
    this.resp = baseResp
  }

  companion object {
    /** 微信是否已安装  */
    @JvmStatic
    fun isWxAppInstalled(context: Context): Boolean {
      return WXAPIFactory.createWXAPI(context, null).isWXAppInstalled
    }

    /** 注册 App 到微信  */
    @JvmStatic
    fun register(context: Context, appId: String): Boolean {
      val api = WXAPIFactory.createWXAPI(context, appId, true)
      return api.isWXAppInstalled && api.registerApp(appId)
    }
  }

}

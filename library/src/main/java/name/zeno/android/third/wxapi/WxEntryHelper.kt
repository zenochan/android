package name.zeno.android.third.wxapi

import android.app.Activity
import android.content.Context
import android.content.Intent

import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXTextObject
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory

import name.zeno.android.listener.Action1

/**
 * # 微信入口帮助类
 *
 *
 * ### API
 * - [isWXAppInstalled]
 * - [pay]
 * - [shareText]
 *
 * ### 将下列方法与 Activity 同步
 *
 *  - [onCreate]
 *  - [onNewIntent]
 *  - [onResume]
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since  16/6/8
 */
@Deprecated("")
class WxEntryHelper : IWXAPIEventHandler {

  private var api: IWXAPI? = null
  private var resp: BaseResp? = null

  var onPayRespListener: ((WxRespWrapper) -> Unit)? = null

  private var context: Context? = null

  val isWXAppInstalled: Boolean
    get() = api?.isWXAppInstalled ?: false


  fun onCreate(activity: Activity, appId: String) {
    resp = null
    context = activity.application
    api = WXAPIFactory.createWXAPI(activity.applicationContext, appId, false)
    api?.registerApp(appId)
    api?.handleIntent(activity.intent, this)
  }

  fun onNewIntent(intent: Intent) {
    resp = null
    api?.handleIntent(intent, this)
  }

  fun onResume() {
    val resp = resp ?: return
    this.resp = null

    onPayRespListener?.invoke(WxRespWrapper(
        type = resp.type,
        errCode = resp.errCode,
        desc = when {
          resp.type != ConstantsAPI.COMMAND_PAY_BY_WX -> null
          resp.errCode == BaseResp.ErrCode.ERR_OK -> "支付成功"
          resp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL -> "取消支付"
          else -> "支付未完成"
        }
    ))
  }

  override fun onReq(baseReq: BaseReq) {}

  override fun onResp(baseResp: BaseResp) {
    resp = baseResp
  }

  //微信支付
  fun pay(appId: String, partnerId: String, prepayId: String, packageValue: String, nonceStr: String, timeStamp: String, sign: String) {
    val payReq = PayReq()
    payReq.appId = appId
    payReq.partnerId = partnerId
    payReq.prepayId = prepayId
    payReq.packageValue = packageValue
    payReq.nonceStr = nonceStr
    payReq.timeStamp = timeStamp
    payReq.sign = sign
    pay(payReq)
  }

  fun pay(payReq: PayReq) {
    if (isWXAppInstalled) {
      api?.sendReq(payReq)
    } else {
      onPayRespListener?.invoke(WxRespWrapper(
          type = ConstantsAPI.COMMAND_PAY_BY_WX,
          desc = "未安装微信,不能完成支付",
          errCode = BaseResp.ErrCode.ERR_UNSUPPORT
      ))
    }
  }

  // 分享文本消息
  fun shareText(text: String) {
    val textObject = WXTextObject()
    textObject.text = text
    val msg = WXMediaMessage(textObject)
    msg.description = text

    val req = SendMessageToWX.Req()
    req.transaction = "Zeno" //transaction 字段用于唯一标识一个请求
    req.message = msg
    req.scene = WXScene.SESSION

    api?.sendReq(req)
  }

  companion object {
    const val SHARE_TYPE_SESSION = SendMessageToWX.Req.WXSceneSession
    const val SHARE_TYPE_TIMELINE = SendMessageToWX.Req.WXSceneTimeline
    const val SHARE_TYPE_FAVORITE = SendMessageToWX.Req.WXSceneFavorite

    private const val TAG = "WxEntryHelper"
  }
}

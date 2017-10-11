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
 * 微信入口帮助类
 * Create Date: 16/6/8
 *
 *
 * <h3>将下列方法与 Activity 同步</h3>
 *
 *  * [.onCreate]
 *  * [.onNewIntent]
 *  * [.onResume]
 *
 *
 * @author 陈治谋 (513500085@qq.com)
 */
class WxEntryHelper : IWXAPIEventHandler {

  private var api: IWXAPI? = null
  private var resp: BaseResp? = null

  var onPayRespListener: Action1<WxRespWrapper>? = null

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
    if (resp == null) {
      return
    }

    val type = resp!!.type
    val errCode = resp!!.errCode
    val respWrapper = WxRespWrapper()
    respWrapper.type = type
    respWrapper.errCode = errCode
    when (type) {
      ConstantsAPI.COMMAND_PAY_BY_WX//微信支付
      -> if (onPayRespListener != null) {
        if (errCode == BaseResp.ErrCode.ERR_OK) {
          respWrapper.desc = "支付成功"
        } else if (errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
          respWrapper.desc = "取消支付"
        } else {
          respWrapper.desc = "支付未完成"
        }
        onPayRespListener!!.call(respWrapper)
      }
    }
    resp = null
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
      api!!.sendReq(payReq)
    } else if (onPayRespListener != null) {
      val res = WxRespWrapper()
      res.type = ConstantsAPI.COMMAND_PAY_BY_WX
      res.desc = "未安装微信,不能完成支付"
      res.errCode = BaseResp.ErrCode.ERR_UNSUPPORT
      onPayRespListener!!.call(res)
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

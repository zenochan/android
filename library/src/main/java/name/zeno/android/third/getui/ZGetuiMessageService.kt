package name.zeno.android.third.getui

import android.content.Context
import android.support.annotation.CallSuper
import android.util.Log

import com.igexin.sdk.GTIntentService
import com.igexin.sdk.message.GTCmdMessage
import com.igexin.sdk.message.GTTransmitMessage

import name.zeno.android.data.models.GetuiMessage

/**
 *  * [.onReceiveClientId] 接受 cid
 *  * [.onReceiveMessageData] 接收透传消息
 *  * [.onReceiveOnlineState] 离线上线通知
 *  * [.getCid] 获取缓存中的 cid
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/8/21
 */
abstract class ZGetuiMessageService : GTIntentService() {

  override fun onReceiveServicePid(context: Context, i: Int) {}

  /**
   * <h1>当获取到 client id 时调用</h1>
   * 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
   */
  @CallSuper
  override fun onReceiveClientId(context: Context, cid: String) {
    Log.e(TAG, String.format("ClientId: %s", cid))
    saveCid(context, cid)
  }

  override fun onReceiveMessageData(context: Context, gtTransmitMessage: GTTransmitMessage) {
    onReceiveMessageData(context, GetuiMessage(gtTransmitMessage))
  }

  override fun onReceiveOnlineState(context: Context, b: Boolean) {}

  abstract fun onReceiveMessageData(context: Context, message: GetuiMessage)

  override fun onReceiveCommandResult(context: Context, gtCmdMessage: GTCmdMessage) {}

  /** 保存cid  */
  private fun saveCid(context: Context, cid: String) {
    context.getSharedPreferences(TAG, Context.MODE_PRIVATE).edit().putString(TAG, cid).apply()
  }

  companion object {
    private val TAG = "ZGetuiMessageService"

    /** 获取保存的cid  */
    fun getCid(context: Context): String? {
      return context.getSharedPreferences(TAG, Context.MODE_PRIVATE).getString(TAG, null)
    }
  }
}

package name.zeno.android.data.models

import com.igexin.sdk.message.GTTransmitMessage
import name.zeno.android.common.annotations.DataClass

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/8/21
 */
@DataClass data class GetuiMessage(
    // base
    var appid: String? = null,
    var pkgName: String? = null,
    var clientId: String? = null,

    // 透传
    var messageId: String? = null, // 消息id
    var taskId: String? = null,   // 推送任务id
    var payLoadId: String? = null, // 透传id
    var payLoad: String? = null  // 透传内容
) {
  constructor(gtTransmitMessage: GTTransmitMessage) : this(
      appid = gtTransmitMessage.appid,
      pkgName = gtTransmitMessage.pkgName,
      clientId = gtTransmitMessage.clientId,
      messageId = gtTransmitMessage.messageId,
      taskId = gtTransmitMessage.taskId,
      payLoadId = gtTransmitMessage.payloadId
  ) {
    if (gtTransmitMessage.payload != null) {
      payLoad = String(gtTransmitMessage.payload)
    }
  }
}

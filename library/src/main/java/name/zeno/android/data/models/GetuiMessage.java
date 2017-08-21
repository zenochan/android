package name.zeno.android.data.models;

import com.igexin.sdk.message.GTTransmitMessage;

import lombok.Data;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/8/21
 */
@Data
public class GetuiMessage
{
  // base
  private String appid;
  private String pkgName;
  private String clientId;

  // 透传
  private String messageId; // 消息id
  private String taskId;    // 推送任务id
  private String payLoadId; // 透传id
  private String payLoad;   // 透传内容

  public GetuiMessage() { }

  public GetuiMessage(GTTransmitMessage gtTransmitMessage)
  {
    appid = gtTransmitMessage.getAppid();
    pkgName = gtTransmitMessage.getPkgName();
    clientId = gtTransmitMessage.getClientId();

    messageId = gtTransmitMessage.getMessageId();
    taskId = gtTransmitMessage.getTaskId();
    payLoadId = gtTransmitMessage.getPayloadId();
    if (gtTransmitMessage.getPayload() != null) {
      payLoad = new String(gtTransmitMessage.getPayload());
    }
  }
}

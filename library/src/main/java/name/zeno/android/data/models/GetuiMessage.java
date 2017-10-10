package name.zeno.android.data.models;

import com.igexin.sdk.message.GTTransmitMessage;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/8/21
 */
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

  public String getAppid()
  {
    return appid;
  }

  public void setAppid(String appid)
  {
    this.appid = appid;
  }

  public String getPkgName()
  {
    return pkgName;
  }

  public void setPkgName(String pkgName)
  {
    this.pkgName = pkgName;
  }

  public String getClientId()
  {
    return clientId;
  }

  public void setClientId(String clientId)
  {
    this.clientId = clientId;
  }

  public String getMessageId()
  {
    return messageId;
  }

  public void setMessageId(String messageId)
  {
    this.messageId = messageId;
  }

  public String getTaskId()
  {
    return taskId;
  }

  public void setTaskId(String taskId)
  {
    this.taskId = taskId;
  }

  public String getPayLoadId()
  {
    return payLoadId;
  }

  public void setPayLoadId(String payLoadId)
  {
    this.payLoadId = payLoadId;
  }

  public String getPayLoad()
  {
    return payLoad;
  }

  public void setPayLoad(String payLoad)
  {
    this.payLoad = payLoad;
  }

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

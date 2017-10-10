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

  public String getAppid()
  {return this.appid;}

  public String getPkgName()
  {return this.pkgName;}

  public String getClientId()
  {return this.clientId;}

  public String getMessageId()
  {return this.messageId;}

  public String getTaskId()
  {return this.taskId;}

  public String getPayLoadId()
  {return this.payLoadId;}

  public String getPayLoad()
  {return this.payLoad;}

  public void setAppid(String appid)
  {this.appid = appid; }

  public void setPkgName(String pkgName)
  {this.pkgName = pkgName; }

  public void setClientId(String clientId)
  {this.clientId = clientId; }

  public void setMessageId(String messageId)
  {this.messageId = messageId; }

  public void setTaskId(String taskId)
  {this.taskId = taskId; }

  public void setPayLoadId(String payLoadId)
  {this.payLoadId = payLoadId; }

  public void setPayLoad(String payLoad)
  {this.payLoad = payLoad; }

  public boolean equals(Object o)
  {
    if (o == this) return true;
    if (!(o instanceof GetuiMessage)) return false;
    final GetuiMessage other = (GetuiMessage) o;
    if (!other.canEqual((Object) this)) return false;
    final Object this$appid  = this.getAppid();
    final Object other$appid = other.getAppid();
    if (this$appid == null ? other$appid != null : !this$appid.equals(other$appid)) return false;
    final Object this$pkgName  = this.getPkgName();
    final Object other$pkgName = other.getPkgName();
    if (this$pkgName == null ? other$pkgName != null : !this$pkgName.equals(other$pkgName))
      return false;
    final Object this$clientId  = this.getClientId();
    final Object other$clientId = other.getClientId();
    if (this$clientId == null ? other$clientId != null : !this$clientId.equals(other$clientId))
      return false;
    final Object this$messageId  = this.getMessageId();
    final Object other$messageId = other.getMessageId();
    if (this$messageId == null ? other$messageId != null : !this$messageId.equals(other$messageId))
      return false;
    final Object this$taskId  = this.getTaskId();
    final Object other$taskId = other.getTaskId();
    if (this$taskId == null ? other$taskId != null : !this$taskId.equals(other$taskId))
      return false;
    final Object this$payLoadId  = this.getPayLoadId();
    final Object other$payLoadId = other.getPayLoadId();
    if (this$payLoadId == null ? other$payLoadId != null : !this$payLoadId.equals(other$payLoadId))
      return false;
    final Object this$payLoad  = this.getPayLoad();
    final Object other$payLoad = other.getPayLoad();
    if (this$payLoad == null ? other$payLoad != null : !this$payLoad.equals(other$payLoad))
      return false;
    return true;
  }

  public int hashCode()
  {
    final int    PRIME  = 59;
    int          result = 1;
    final Object $appid = this.getAppid();
    result = result * PRIME + ($appid == null ? 43 : $appid.hashCode());
    final Object $pkgName = this.getPkgName();
    result = result * PRIME + ($pkgName == null ? 43 : $pkgName.hashCode());
    final Object $clientId = this.getClientId();
    result = result * PRIME + ($clientId == null ? 43 : $clientId.hashCode());
    final Object $messageId = this.getMessageId();
    result = result * PRIME + ($messageId == null ? 43 : $messageId.hashCode());
    final Object $taskId = this.getTaskId();
    result = result * PRIME + ($taskId == null ? 43 : $taskId.hashCode());
    final Object $payLoadId = this.getPayLoadId();
    result = result * PRIME + ($payLoadId == null ? 43 : $payLoadId.hashCode());
    final Object $payLoad = this.getPayLoad();
    result = result * PRIME + ($payLoad == null ? 43 : $payLoad.hashCode());
    return result;
  }

  protected boolean canEqual(Object other)
  {return other instanceof GetuiMessage;}

  public String toString()
  {return "GetuiMessage(appid=" + this.getAppid() + ", pkgName=" + this.getPkgName() + ", clientId=" + this.getClientId() + ", messageId=" + this.getMessageId() + ", taskId=" + this.getTaskId() + ", payLoadId=" + this.getPayLoadId() + ", payLoad=" + this.getPayLoad() + ")";}
}

package name.zeno.android.third.wxapi;

import android.os.Parcel;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;

import io.reactivex.Observable;

/**
 * 发送文本消息
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/4/27
 */
public class ZTextMessageReq extends AbsReq
{
  private String text;
  @WXScene
  private int scene = WXScene.SESSION;

  public ZTextMessageReq() {}

  @Override public Observable<BaseReq> build()
  {
    WXTextObject   textObject = new WXTextObject(text);
    WXMediaMessage msg        = new WXMediaMessage(textObject);
    msg.description = text;

    SendMessageToWX.Req req = new SendMessageToWX.Req();
    req.transaction = "Zeno"; //transaction 字段用于唯一标识一个请求
    req.message = msg;
    req.scene = scene;

    return Observable.just(req);
  }

  @Override public int describeContents() { return 0; }

  @Override public void writeToParcel(Parcel dest, int flags) {dest.writeString(this.text);}

  protected ZTextMessageReq(Parcel in) {this.text = in.readString();}

  public static final Creator<ZTextMessageReq> CREATOR = new Creator<ZTextMessageReq>()
  {
    @Override
    public ZTextMessageReq createFromParcel(Parcel source) {return new ZTextMessageReq(source);}

    @Override public ZTextMessageReq[] newArray(int size) {return new ZTextMessageReq[size];}
  };

  public String getText()
  {return this.text;}

  public int getScene()
  {return this.scene;}

  public void setText(String text)
  {this.text = text; }

  public String toString()
  {return "ZTextMessageReq(text=" + this.getText() + ", scene=" + this.getScene() + ")";}

  public boolean equals(Object o)
  {
    if (o == this) return true;
    if (!(o instanceof ZTextMessageReq)) return false;
    final ZTextMessageReq other = (ZTextMessageReq) o;
    if (!other.canEqual((Object) this)) return false;
    final Object this$text  = this.getText();
    final Object other$text = other.getText();
    if (this$text == null ? other$text != null : !this$text.equals(other$text)) return false;
    if (this.getScene() != other.getScene()) return false;
    return true;
  }

  public int hashCode()
  {
    final int    PRIME  = 59;
    int          result = 1;
    final Object $text  = this.getText();
    result = result * PRIME + ($text == null ? 43 : $text.hashCode());
    result = result * PRIME + this.getScene();
    return result;
  }

  protected boolean canEqual(Object other)
  {return other instanceof ZTextMessageReq;}

  @WXScene public void setScene(int scene)
  {this.scene = scene; }
}

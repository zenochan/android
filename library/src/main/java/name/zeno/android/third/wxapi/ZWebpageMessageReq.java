package name.zeno.android.third.wxapi;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.support.v4.content.ContextCompat;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import name.zeno.android.app.ZApplication;
import name.zeno.android.util.R;
import name.zeno.android.util.ZBitmap;

/**
 * 发送网页消息
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/4/27
 */
public class ZWebpageMessageReq extends AbsReq
{
  private String webpageUrl;
  private String title;
  private String description;
  private String thumbImageUrl;
  private Bitmap thumbImage;
  @WXScene
  private int    scene;

  public ZWebpageMessageReq() {}

  @Override public Observable<BaseReq> build()
  {
    return Observable.create((ObservableOnSubscribe<Bitmap>) subscriber -> {
      // 选择分享主图
      if (thumbImage != null) {
        subscriber.onNext(thumbImage);
        subscriber.onComplete();
        return;
      }

      if (thumbImageUrl != null) {
        Bitmap bitmap = ZBitmap.bitmap(thumbImageUrl);
        if (bitmap != null) {
          subscriber.onNext(bitmap);
          subscriber.onComplete();
          return;
        }
      }

      Bitmap bitmap = ZBitmap.bitmap(ContextCompat.getDrawable(ZApplication.getApplication(), R.mipmap.ic_add), true);
      subscriber.onNext(bitmap);
      subscriber.onComplete();
    }).map(bitmap -> {
      // 对大图片处理
      if (bitmap.getWidth() > 256 || bitmap.getHeight() > 256) {
        bitmap = ZBitmap.zoom(bitmap, 256, 256);
      }
      return bitmap;
    }).map(bitmap -> {
      // 构建 req
      WXWebpageObject webpage = new WXWebpageObject();
      webpage.webpageUrl = webpageUrl;
      WXMediaMessage msg = new WXMediaMessage(webpage);
      msg.title = title;
      msg.description = description;
      msg.setThumbImage(bitmap);

      SendMessageToWX.Req req = new SendMessageToWX.Req();
      req.transaction = "Zeno"; //transaction 字段用于唯一标识一个请求
      req.message = msg;
      req.scene = scene;
      return req;
    });
  }

  @Override public int describeContents() { return 0; }

  @Override public void writeToParcel(Parcel dest, int flags)
  {
    dest.writeString(this.webpageUrl);
    dest.writeString(this.title);
    dest.writeString(this.description);
    dest.writeString(this.thumbImageUrl);
    dest.writeParcelable(this.thumbImage, flags);
    dest.writeInt(this.scene);
  }

  protected ZWebpageMessageReq(Parcel in)
  {
    this.webpageUrl = in.readString();
    this.title = in.readString();
    this.description = in.readString();
    this.thumbImageUrl = in.readString();
    this.thumbImage = in.readParcelable(Bitmap.class.getClassLoader());
    //noinspection WrongConstant
    this.scene = in.readInt();
  }

  public static final Creator<ZWebpageMessageReq> CREATOR = new Creator<ZWebpageMessageReq>()
  {
    @Override
    public ZWebpageMessageReq createFromParcel(Parcel source) {return new ZWebpageMessageReq(source);}

    @Override public ZWebpageMessageReq[] newArray(int size) {return new ZWebpageMessageReq[size];}
  };

  public String getWebpageUrl()
  {return this.webpageUrl;}

  public String getTitle()
  {return this.title;}

  public String getDescription()
  {return this.description;}

  public String getThumbImageUrl()
  {return this.thumbImageUrl;}

  public Bitmap getThumbImage()
  {return this.thumbImage;}

  public int getScene()
  {return this.scene;}

  public void setWebpageUrl(String webpageUrl)
  {this.webpageUrl = webpageUrl; }

  public void setTitle(String title)
  {this.title = title; }

  public void setDescription(String description)
  {this.description = description; }

  public void setThumbImageUrl(String thumbImageUrl)
  {this.thumbImageUrl = thumbImageUrl; }

  public void setThumbImage(Bitmap thumbImage)
  {this.thumbImage = thumbImage; }

  public String toString()
  {return "ZWebpageMessageReq(webpageUrl=" + this.getWebpageUrl() + ", title=" + this.getTitle() + ", description=" + this.getDescription() + ", thumbImageUrl=" + this.getThumbImageUrl() + ", thumbImage=" + this.getThumbImage() + ", scene=" + this.getScene() + ")";}

  public boolean equals(Object o)
  {
    if (o == this) return true;
    if (!(o instanceof ZWebpageMessageReq)) return false;
    final ZWebpageMessageReq other = (ZWebpageMessageReq) o;
    if (!other.canEqual((Object) this)) return false;
    final Object this$webpageUrl  = this.getWebpageUrl();
    final Object other$webpageUrl = other.getWebpageUrl();
    if (this$webpageUrl == null ? other$webpageUrl != null : !this$webpageUrl.equals(other$webpageUrl))
      return false;
    final Object this$title  = this.getTitle();
    final Object other$title = other.getTitle();
    if (this$title == null ? other$title != null : !this$title.equals(other$title)) return false;
    final Object this$description  = this.getDescription();
    final Object other$description = other.getDescription();
    if (this$description == null ? other$description != null : !this$description.equals(other$description))
      return false;
    final Object this$thumbImageUrl  = this.getThumbImageUrl();
    final Object other$thumbImageUrl = other.getThumbImageUrl();
    if (this$thumbImageUrl == null ? other$thumbImageUrl != null : !this$thumbImageUrl.equals(other$thumbImageUrl))
      return false;
    final Object this$thumbImage  = this.getThumbImage();
    final Object other$thumbImage = other.getThumbImage();
    if (this$thumbImage == null ? other$thumbImage != null : !this$thumbImage.equals(other$thumbImage))
      return false;
    if (this.getScene() != other.getScene()) return false;
    return true;
  }

  public int hashCode()
  {
    final int    PRIME       = 59;
    int          result      = 1;
    final Object $webpageUrl = this.getWebpageUrl();
    result = result * PRIME + ($webpageUrl == null ? 43 : $webpageUrl.hashCode());
    final Object $title = this.getTitle();
    result = result * PRIME + ($title == null ? 43 : $title.hashCode());
    final Object $description = this.getDescription();
    result = result * PRIME + ($description == null ? 43 : $description.hashCode());
    final Object $thumbImageUrl = this.getThumbImageUrl();
    result = result * PRIME + ($thumbImageUrl == null ? 43 : $thumbImageUrl.hashCode());
    final Object $thumbImage = this.getThumbImage();
    result = result * PRIME + ($thumbImage == null ? 43 : $thumbImage.hashCode());
    result = result * PRIME + this.getScene();
    return result;
  }

  protected boolean canEqual(Object other)
  {return other instanceof ZWebpageMessageReq;}

  public void setScene(@WXScene int scene)
  {this.scene = scene; }
}

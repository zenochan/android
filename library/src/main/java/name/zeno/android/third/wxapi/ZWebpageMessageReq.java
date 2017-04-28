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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import name.zeno.android.app.ZApplication;
import name.zeno.android.util.R;
import name.zeno.android.util.ZBitmap;

/**
 * 发送网页消息
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/4/27
 */
@Data @NoArgsConstructor @EqualsAndHashCode(callSuper = false)
public class ZWebpageMessageReq extends AbsReq
{
  private String webpageUrl;
  private String title;
  private String description;
  private String thumbImageUrl;
  private Bitmap thumbImage;
  @WXScene @Setter(onMethod = @__({@WXScene}))
  private int    scene;

  @Override public Observable<BaseReq> build()
  {
    return Observable.create((ObservableOnSubscribe<Bitmap>) subscriber -> {
      // 选择分享主图
      if (thumbImage != null) {
        subscriber.onNext(thumbImage);
        subscriber.onComplete();
      } else if (thumbImageUrl != null) {
        Bitmap bitmap = ZBitmap.bitmap(thumbImageUrl);
        subscriber.onNext(bitmap);
        subscriber.onComplete();
      } else {
        Bitmap bitmap = ZBitmap.bitmap(ContextCompat.getDrawable(ZApplication.getApplication(), R.mipmap.ic_add), true);
        subscriber.onNext(bitmap);
        subscriber.onComplete();
      }
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
}

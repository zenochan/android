package name.zeno.android.util;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.Observable;


/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/10/13.
 */
public class IOUtils
{
  public static Observable<String> readString(Context context, String file)
  {
    return Observable.create(subscriber -> {
      InputStream is = null;
      try {
        is = context.getAssets().open(file);
      } catch (IOException e) {
        subscriber.onError(e);
      }

      if (is == null) {
        subscriber.onError(new IllegalArgumentException("file not found"));
        return;
      }

      try {
        subscriber.onNext(readString(is));
        subscriber.onComplete();
        is.close();
      } catch (IOException e) {
        subscriber.onError(e);
      }
    });
  }

  /**
   * 将InputStream转换成某种字符编码的String
   */
  public static String readString(InputStream is) throws IOException
  {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    int i;
    while ((i = is.read()) != -1) {
      baos.write(i);
    }
    return baos.toString();
  }
}

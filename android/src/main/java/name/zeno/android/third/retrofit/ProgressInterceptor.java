package name.zeno.android.third.retrofit;

import java.io.IOException;

import name.zeno.android.listener.Action3;
import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/10/31.
 */
public class ProgressInterceptor implements Interceptor
{
  private Action3<Long, Long, Boolean> onProgressChanged;

  public ProgressInterceptor(Action3<Long, Long, Boolean> onProgressChanged)
  {
    this.onProgressChanged = onProgressChanged;
  }

  @Override
  public Response intercept(Interceptor.Chain chain) throws IOException
  {
    Response             originalResponse     = chain.proceed(chain.request());
    ProgressResponseBody progressResponseBody = new ProgressResponseBody(originalResponse.body());
    progressResponseBody.setOnProgressChanged(onProgressChanged);
    return originalResponse.newBuilder().body(progressResponseBody).build();
  }
}

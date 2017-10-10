package name.zeno.android.third.retrofit;

import java.io.IOException;

import name.zeno.android.listener.Action3;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/10/31.
 */
public class ProgressResponseBody extends ResponseBody
{
  private final ResponseBody                 responseBody;
  private       BufferedSource               bufferedSource;
  private       Action3<Long, Long, Boolean> onProgressChanged;

  public ProgressResponseBody(ResponseBody responseBody)
  {
    this.responseBody = responseBody;
  }

  @Override
  public MediaType contentType()
  {
    return responseBody.contentType();
  }

  @Override
  public long contentLength()
  {
    return responseBody.contentLength();
  }

  @Override
  public BufferedSource source()
  {
    if (bufferedSource == null) {
      bufferedSource = Okio.buffer(source(responseBody.source()));
    }
    return bufferedSource;
  }

  private Source source(Source source)
  {
    return new ForwardingSource(source)
    {
      long totalBytesRead = 0L;

      @Override
      public long read(Buffer sink, long byteCount) throws IOException
      {
        long bytesRead = super.read(sink, byteCount);
        totalBytesRead += bytesRead != -1 ? bytesRead : 0;
        if (onProgressChanged != null) {
          onProgressChanged.call(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
        }
        return bytesRead;
      }
    };
  }

  public void setOnProgressChanged(Action3<Long, Long, Boolean> onProgressChanged)
  {this.onProgressChanged = onProgressChanged; }
}

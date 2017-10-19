package name.zeno.android.third.retrofit

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.io.IOException

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/10/31.
 */
class ProgressResponseBody(
    private val responseBody: ResponseBody,
    private val onProgress: ((received: Long, total: Long, completed: Boolean) -> Unit)? = null
) : ResponseBody() {
  private val bufferedSource: BufferedSource by lazy {
    Okio.buffer(source(responseBody.source()))
  }

  override fun contentType(): MediaType? {
    return responseBody.contentType()
  }

  override fun contentLength(): Long {
    return responseBody.contentLength()
  }

  override fun source(): BufferedSource = bufferedSource

  private fun source(source: Source): Source {
    return object : ForwardingSource(source) {
      internal var totalBytesRead = 0L

      @Throws(IOException::class)
      override fun read(sink: Buffer, byteCount: Long): Long {
        val bytesRead = super.read(sink, byteCount)
        totalBytesRead += if (bytesRead != -1L) bytesRead else 0
        onProgress?.invoke(totalBytesRead, responseBody.contentLength(), bytesRead == -1L)
        return bytesRead
      }
    }
  }
}

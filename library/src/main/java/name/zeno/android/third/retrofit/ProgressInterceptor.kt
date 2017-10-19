package name.zeno.android.third.retrofit

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/10/31.
 */
class ProgressInterceptor(private val onProgressChanged: ((received: Long, total: Long, completed: Boolean) -> Unit)?) : Interceptor {

  @Throws(IOException::class)
  override fun intercept(chain: Interceptor.Chain): Response {
    val originalResponse = chain.proceed(chain.request())
    val progressResponseBody = ProgressResponseBody(originalResponse.body()!!, onProgressChanged)
    return originalResponse.newBuilder().body(progressResponseBody).build()
  }
}

package name.zeno.android.third.retrofit

import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2018/1/24
 */

object RequestBodyBuilder {
  fun image(file: File) = RequestBody.create(MediaType.parse("image/*"), file)
}


/**
 * 将 [RequestBody] 转为可监听精度的 [ProcessingRequestBody]
 */
fun RequestBody.processing() = ProcessingRequestBody(this)

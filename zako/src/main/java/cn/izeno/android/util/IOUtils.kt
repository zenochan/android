package cn.izeno.android.util

import android.content.Context
import io.reactivex.Observable
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream


/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/10/13.
 */
object IOUtils {
  fun readString(context: Context, file: String): Observable<String> {
    return Observable.create souse@{ emitter ->
      var inputStream: InputStream? = null
      try {
        inputStream = context.assets.open(file)
      } catch (e: IOException) {
        emitter.onError(e)
      }

      if (inputStream == null) {
        emitter.onError(IllegalArgumentException("file not found"))
        return@souse
      }

      try {
        emitter.onNext(readString(inputStream))
        emitter.onComplete()
        inputStream.close()
      } catch (e: IOException) {
        emitter.onError(e)
      }
    }
  }

  /** # 将InputStream转换成某种字符编码的String */
  @Throws(IOException::class)
  fun readString(inputStream: InputStream): String {
    val oStream = ByteArrayOutputStream()

    var i: Int
    do {
      i = inputStream.read()
      if (i != -1) oStream.write(i)
    } while (i != -1)
    return oStream.toString()
  }
}

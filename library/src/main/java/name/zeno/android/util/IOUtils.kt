package name.zeno.android.util

import android.content.Context
import io.reactivex.Emitter

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

import io.reactivex.Observable


/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/10/13.
 */
object IOUtils {
  fun readString(context: Context, file: String): Observable<String> {
    return Observable.create(souse@ { subscriber: Emitter<String> ->
      var inputStream: InputStream? = null
      try {
        inputStream = context.assets.open(file)
      } catch (e: IOException) {
        subscriber.onError(e)
      }

      if (inputStream == null) {
        subscriber.onError(IllegalArgumentException("file not found"))
        return@souse
      }

      try {
        subscriber.onNext(readString(inputStream))
        subscriber.onComplete()
        inputStream.close()
      } catch (e: IOException) {
        subscriber.onError(e)
      }
    })
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

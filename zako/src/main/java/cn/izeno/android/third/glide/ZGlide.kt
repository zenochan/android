package cn.izeno.android.third.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import io.reactivex.Emitter
import io.reactivex.Observable
import cn.izeno.android.third.glide.ZGlide.Companion.clearCache
import cn.izeno.android.third.glide.ZGlide.Companion.getDiskCacheSize
import cn.izeno.android.third.rxjava.RxUtils
import okhttp3.OkHttpClient
import java.io.File
import java.io.InputStream
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.*


/**
 *  * [getDiskCacheSize] 获取磁盘缓存大小
 *  * [clearCache] 清除内存 & 磁盘缓存
 */
@GlideModule
@Suppress("unused")
class ZGlide : AppGlideModule() {
  override fun applyOptions(context: Context, builder: GlideBuilder) {
    // Glide使用bitmap的编码为RGB565，所以有时的时候由于过度压缩导致了图片变绿。
    // 所以要改变一下Glide的bitmap编码。
    val options = RequestOptions().format(DecodeFormat.PREFER_ARGB_8888)
    builder.setDefaultRequestOptions(options)
  }

  override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
    super.registerComponents(context, glide, registry)
    //配置glide网络加载框架
//    registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory())
    registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory(createOkHttpClient()))
  }

  override fun isManifestParsingEnabled(): Boolean {
    //不使用清单配置的方式,减少初始化时间
    return false
  }

  companion object {

    /** Glide 磁盘缓存大小 */
    @JvmStatic
    fun getDiskCacheSize(context: Context): String {
      val file = GlideApp.getPhotoCacheDir(context)
      return if (file != null) {
        formatMemorySize(getFolderSize(file))
      } else {
        "0 Byte"
      }
    }

    /** 异步清除内存&磁盘缓存 */
    @JvmStatic
    fun clearCache(context: Context): Observable<Boolean> {
      val glide = GlideApp.get(context)
      // 清除内存缓存必须在UI线程
      glide.clearMemory()
      return Observable.create { subscriber: Emitter<Boolean> ->
        // 清除磁盘缓存必须在UI线程
        glide.clearDiskCache()
        subscriber.onNext(true)
        subscriber.onComplete()
      }.compose(RxUtils.applySchedulers())
    }

    private fun getFolderSize(directory: File): Long {
      var size = 0L
      try {
        val fileList = directory.listFiles()
        for (file in fileList) {
          if (file.isDirectory) {
            size += getFolderSize(file)
          } else {
            size += file.length()
          }
        }
      } catch (ignore: Exception) {
      }

      return size
    }

    /** 格式化单位 */
    private fun formatMemorySize(bytes: Long): String {
      val unit = arrayOf("Byte", "KB", "MB", "GB", "TB")
      var value = bytes.toDouble()
      var level = 0
      while (level < unit.size && value > 1024) {
        level++
        value /= 1024.0
      }

      return String.format(Locale.CHINA, "%.02f %s", value, unit[level])
    }
  }

  private fun createOkHttpClient(): OkHttpClient {
//    val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
//    trustManagerFactory.init(null as KeyStore?)
//    val trustManagers = trustManagerFactory.trustManagers
//    if (trustManagers.size != 1 || trustManagers[0] !is X509TrustManager) {
//      throw IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers))
//    }
//    val trustManager = trustManagers[0] as X509TrustManager
//    val sslContext = SSLContext.getInstance("SSL")
//    sslContext.init(null, arrayOf(trustManager), null)
//    val sslSocketFactory = sslContext.socketFactory


    // Create a trust manager that does not validate certificate chains
    val trustAllCert = object : X509TrustManager {
      override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()
      override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
      override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
    }

    // Install the all-trusting trust manager
    val sslContext = SSLContext.getInstance("SSL")
    sslContext.init(null, arrayOf(trustAllCert), null)
    // Create an ssl socket factory with our all-trusting manager
    val sslSocketFactory = sslContext.socketFactory

    return OkHttpClient.Builder()
        .hostnameVerifier { _, _ -> true }
        .sslSocketFactory(sslSocketFactory, trustAllCert)
        .build()
  }
}

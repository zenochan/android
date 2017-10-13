package name.zeno.android.webkit

import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Build
import android.webkit.*
import com.alibaba.fastjson.JSON
import name.zeno.android.util.ZLog

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/12/2.
 */
open class ZWebViewClient @JvmOverloads constructor(
    var onPageStarted: ((url: String) -> Unit)? = null,
    var onPageFinished: ((url: String) -> Unit)? = null,
    var shouldOverride: ((url: String) -> Boolean)? = null
) : WebViewClient() {

  override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
    super.onPageStarted(view, url, favicon)
    ZLog.v(TAG, "page started: " + url)
    onPageStarted?.invoke(url)
  }

  override fun onPageFinished(view: WebView, url: String) {
    super.onPageFinished(view, url)
    ZLog.v(TAG, "page finished: " + url)
    onPageFinished?.invoke(url)
  }

  override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
    }
    return super.shouldOverrideUrlLoading(view, request)
  }

  override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
    val suplerHandle = super.shouldOverrideUrlLoading(view, url)
    ZLog.v(TAG, "override url: " + url)
    return shouldOverride?.invoke(url) ?: suplerHandle
  }

  /** 打印错误日志 */
  override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
    super.onReceivedError(view, request, error)
    ZLog.v(TAG, "on error: " + JSON.toJSONString(error))
  }

  /** 忽略 ssl 证书错误 */
  override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
    //super.onReceivedSslError(view, handler, error);
    ZLog.e(TAG, "SSL 证书错误:" + error.url)
    //忽略 ssl 证书错误,继续加载
    handler.proceed()
  }

  companion object {
    private val TAG = "ZWebViewClient"
  }
}

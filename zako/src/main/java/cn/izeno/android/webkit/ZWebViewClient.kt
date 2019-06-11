package cn.izeno.android.webkit

import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Build
import android.webkit.*
import com.alibaba.fastjson.JSON
import cn.izeno.android.util.ZLog

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/12/2.
 */
open class ZWebViewClient : WebViewClient() {
  lateinit var view: WebView
  private var onPageStarted: (ZWebViewClient.(url: String) -> Unit)? = null
  private var onPageFinished: (ZWebViewClient.(url: String) -> Unit)? = null
  private var shouldOverride: (ZWebViewClient.(url: String) -> Boolean)? = null

  fun onStart(action: (ZWebViewClient.(url: String) -> Unit)) = apply {
    this.onPageStarted = action
  }

  fun onFinish(action: ZWebViewClient.(url: String) -> Unit) = apply {
    this.onPageFinished = action
  }

  fun override(action: ZWebViewClient.(url: String) -> Boolean) = apply {
    this.shouldOverride = action
  }

  override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
    super.onPageStarted(view, url, favicon)
    ZLog.v(TAG, "page started: " + url)
    this.view = view
    onPageStarted?.invoke(this, url)
  }

  override fun onPageFinished(view: WebView, url: String) {
    super.onPageFinished(view, url)
    ZLog.v(TAG, "page finished: " + url)
    this.view = view
    onPageFinished?.invoke(this, url)
  }

  override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
    }
    return super.shouldOverrideUrlLoading(view, request)
  }

  override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
    ZLog.v(TAG, "override url: " + url)
    this.view = view
    return shouldOverride?.invoke(this, url) ?: false || super.shouldOverrideUrlLoading(view, url)
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

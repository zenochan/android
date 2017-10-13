package name.zeno.android.util

import android.os.Build
import android.webkit.WebView

/**
 * <h1>加载 html 内容, 根据版本设置 MIME 和 ENCODING</h1>
 *
 * - [NO_PADDING] 去除默认 padding
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 16/6/30
 */
object WebViewHelper {
  val MIME: String
  val ENCODING: String?
  const val DEFAULT_STYLE =
      """
      <style type="text/css">
        *{font-size:13px;color:rgba(0,0,0,0.87);}
        img{width: 100%;height:auto;display:block;margin-top:-1px}
        p{padding:0;margin:0}
        body{padding:8px;margin:0px}
      </style>
      """

  const val NO_PADDING =
      """
      <style type="text/css">
        body{padding:0px;margin:0px}
      </style>
      """

  init {
    val lollipop = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    if (lollipop) {
      MIME = "text/html; charset=UTF-8"
      ENCODING = null
    } else {
      MIME = "text/html"
      ENCODING = "utf-8"
    }
  }

  fun loadData(webView: WebView, data: String?) {
    webView.loadData(DEFAULT_STYLE + (data ?: ""), MIME, ENCODING)
  }

  fun loadData(webView: WebView, data: String?, baseUrl: String) {
    webView.loadDataWithBaseURL(baseUrl, DEFAULT_STYLE + (data ?: ""), MIME, ENCODING, null)
  }
}

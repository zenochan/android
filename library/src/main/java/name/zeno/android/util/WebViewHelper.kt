package name.zeno.android.util

import android.webkit.WebView
import name.zeno.android.core.lollipop
import name.zeno.android.util.WebViewHelper.NO_PADDING

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
        img{width: 100%;height:auto;margin-top:-1px}
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
    if (lollipop) {
      MIME = "text/html; charset=UTF-8"
      ENCODING = null
    } else {
      MIME = "text/html"
      ENCODING = "utf-8"
    }
  }

  fun loadData(webView: WebView, data: String?, baseUrl: String? = null) {
    /**
     * [WebView.loadData] 在 5.0 以下，中文显示乱码,
     * 统一使用 [WebView.loadDataWithBaseURL]
     */
    webView.loadDataWithBaseURL(baseUrl, DEFAULT_STYLE + (data ?: ""), MIME, ENCODING, null)
  }
}

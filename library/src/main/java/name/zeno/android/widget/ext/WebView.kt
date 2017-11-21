@file:Suppress("unused")

package name.zeno.android.widget.ext

import android.os.Build
import android.webkit.WebView
import name.zeno.android.core.lollipop
import name.zeno.android.webkit.ZWebViewClient


val WebView.MIME: String
  get() = when {
    lollipop -> "text/html; charset=UTF-8"
    else -> "text/html"
  }

val WebView.ENCODING
  get() = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> null
    else -> "utf-8"
  }


val WebView.DEFAULT_STYLE
  get() = STYLE

/**
 * @param shouldOverride (url:[String])  ->  shouldOverride:[Boolean]
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/10/12
 */
fun WebView.handlerUrl(shouldOverride: ZWebViewClient.(url: String) -> Boolean) {
  val client = ZWebViewClient().override(shouldOverride)
  this.webViewClient = client
}

/**
 * @param data h5data
 * @param baseUrl 主要影响一些不完整链接 src 的引用
 * @param style 样式
 */
fun WebView.load(
    data: String?,
    baseUrl: String? = null,
    style: () -> String? = { DEFAULT_STYLE }
) {
  val css =
      """
      <style type="text/css">
        ${style()}
      </style>
      """
  loadDataWithBaseURL(baseUrl, css + (data ?: ""), MIME, ENCODING, null)
}

private const val STYLE = """
*{
  font-size:13px;
  color:rgba(0,0,0,.87)
}
p{
  overflow:hidden
}
img{
  max-width:100%;
  height:auto;
  margin-top:-1px
}
body{
  padding:0;
  margin:0
}
"""

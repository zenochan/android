package name.zeno.android.widget.ext

import android.os.Build
import android.webkit.WebView
import name.zeno.android.webkit.ZWebViewClient

private val lollipop = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

val WebView.MIME: String
  get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
    "text/html; charset=UTF-8"
  } else {
    "text/html"
  }

val WebView.ENCODING
  get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
    null
  } else {
    "utf-8"
  }


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


/**
 * @param action (url:[String])  ->  shouldOverride:[Boolean]
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/10/12
 */
fun WebView.handlerUrl(action: (url: String) -> Boolean) {
  val client = ZWebViewClient(shouldOverride = action)
  this.webViewClient = client
}

fun WebView.loadData(data: String?, baseUrl: String? = null, style: () -> String? = { DEFAULT_STYLE }) {
  if (baseUrl == null) {
    loadData(style() + (data ?: ""), MIME, ENCODING)
  } else {
    loadDataWithBaseURL(baseUrl, style() + (data ?: ""), MIME, ENCODING, null)
  }
}


package name.zeno.android.widget.ext

import android.os.Build
import android.webkit.WebView
import azadev.kotlin.css.*
import azadev.kotlin.css.colors.rgba
import azadev.kotlin.css.dimens.percent
import azadev.kotlin.css.dimens.px
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


val DEFAULT_STYLE =
    Stylesheet {
      "*"{
        fontSize = 13.px
        color = rgba(0, 0, 0, 0.87)
      }
      p {
        overflow = HIDDEN
      }

      img {
        maxWidth = 100.percent
        height = AUTO
        marginTop = (-1).px
      }
      body {
        padding = 0.px
        margin = 0.px
      }
    }

val BODY_PADDING_8PX =
    Stylesheet {
      body {
        padding = 8.px
      }
    }


/**
 * @param shouldOverride (url:[String])  ->  shouldOverride:[Boolean]
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/10/12
 */
fun WebView.handlerUrl(shouldOverride: (url: String) -> Boolean) {
  val client = ZWebViewClient(shouldOverride = shouldOverride)
  this.webViewClient = client
}

/**
 * @param data h5data
 * @param baseUrl 主要影响一些不完整链接 src 的引用
 * @param style 样式
 */
fun WebView.loadData(
    data: String?,
    baseUrl: String? = null,
    style: () -> Stylesheet? = { DEFAULT_STYLE }
) {
  val css =
      """
      <style type="text/css">
        ${style()?.render()}
      </style>
      """
  if (baseUrl == null) {
    loadData(css + (data ?: ""), MIME, ENCODING)
  } else {
    loadDataWithBaseURL(baseUrl, css + (data ?: ""), MIME, ENCODING, null)
  }
}

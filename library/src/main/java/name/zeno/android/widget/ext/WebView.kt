package name.zeno.android.widget.ext

import android.webkit.WebView
import name.zeno.android.webkit.ZWebViewClient

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/10/12
 */

fun WebView.handlerUrl(action: (String) -> Boolean) {
  val client = ZWebViewClient(shouldOverride = action)
  this.webViewClient = client
}


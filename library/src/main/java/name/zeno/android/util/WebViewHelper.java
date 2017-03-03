package name.zeno.android.util;

import android.os.Build;
import android.webkit.WebView;

/**
 * Create Date: 16/6/30
 *
 * @author 陈治谋 (513500085@qq.com)
 */
public class WebViewHelper
{
  public static final String MIME          = "text/html; charset=UTF-8";
  public static final String DEFAULT_STYLE =
      "<style type=\"text/css\">" +
          "*{font-size:13px;color:rgba(0,0,0,0.87);}" +
          "img{width: 100%;height:auto;margin-top:-1px}" +
          "body{padding:8px;margin:0px}" +
          "</style>";

  public static void loadData(WebView webView, String data)
  {
    webView.loadData(DEFAULT_STYLE + data, MIME, null);
  }

  public static void loadData(WebView webView, String data, String baseUrl)
  {
    if (data == null) data = "";
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      webView.loadDataWithBaseURL(baseUrl, DEFAULT_STYLE + data, "text/html; charset=UTF-8", null, null);
    } else {
      //低版本不支持混写的 mimeType
      webView.loadDataWithBaseURL(baseUrl, DEFAULT_STYLE + data, "text/html", "utf-8", null);
    }
  }
}

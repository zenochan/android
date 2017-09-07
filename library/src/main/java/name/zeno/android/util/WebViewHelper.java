package name.zeno.android.util;

import android.os.Build;
import android.webkit.WebView;

/**
 * <h1>加载 html 内容, 根据版本设置 MIME 和 ENCODING</h1>
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 16/6/30
 */
public class WebViewHelper
{
  public static final String MIME;
  public static final String ENCODING;
  public static final String DEFAULT_STYLE =
      "<style type=\"text/css\">" +
          "*{font-size:13px;color:rgba(0,0,0,0.87);}" +
          "img{width: 100%;height:auto;margin-top:-1px}" +
          "body{padding:8px;margin:0px}" +
          "</style>";

  static {
    boolean lollipop = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    if (lollipop) {
      MIME = "text/html; charset=UTF-8";
      ENCODING = null;
    } else {
      MIME = "text/html";
      ENCODING = "utf-8";
    }
  }

  public static void loadData(WebView webView, String data)
  {
    webView.loadData(DEFAULT_STYLE + data, MIME, ENCODING);
  }

  public static void loadData(WebView webView, String data, String baseUrl)
  {
    if (data == null) data = "";
    webView.loadDataWithBaseURL(baseUrl, DEFAULT_STYLE + data, MIME, ENCODING, null);
  }
}

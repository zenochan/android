package name.zeno.android.webkit;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.fastjson.JSON;

import name.zeno.android.util.ZLog;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/12/2.
 */
public class ZWebViewClient extends WebViewClient
{
  private static final String TAG = "ZWebViewClient";

  @Override public void onPageStarted(WebView view, String url, Bitmap favicon)
  {
    super.onPageStarted(view, url, favicon);
    ZLog.v(TAG, "page started: " + url);
  }

  @Override public void onPageFinished(WebView view, String url)
  {
    super.onPageFinished(view, url);
    ZLog.v(TAG, "page finished: " + url);
  }

  @Override public boolean shouldOverrideUrlLoading(WebView view, String url)
  {
    ZLog.v(TAG, "override url: " + url);
    return super.shouldOverrideUrlLoading(view, url);
  }

  @Override
  public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error)
  {
    super.onReceivedError(view, request, error);
    ZLog.v(TAG, "on error: " + JSON.toJSONString(error));
  }

  @Override public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error)
  {
    //super.onReceivedSslError(view, handler, error);
    ZLog.e(TAG, "SSL 证书错误:" + error.getUrl());
    //忽略 ssl 证书错误,继续加载
    handler.proceed();
  }
}

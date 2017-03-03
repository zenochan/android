package name.zeno.android.util;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pManager;
import android.provider.Settings;
import android.support.annotation.RequiresPermission;
import android.widget.Toast;

/**
 * <h1>跳转工具类</h1>
 * <p>
 * <h5>原文:</h5>
 *
 * @author ZenoChan (513500085@qq.com)
 * @version 2015-10-19 14:09:05
 * @see <a href="https://github.com/TakWolf/CNode-Material-Design/blob/master/app/src/main/java/org/cnodejs/android/md/util/ShipUtils.java">
 * ZAction.java
 * </a>
 */
@SuppressWarnings("unused")
public final class ZAction
{
  private ZAction() {}

  /**
   * 打开应用商店
   */
  public static void openAppStore(Context context)
  {
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.setData(Uri.parse("market://details?id=" + context.getPackageName()));
    if (intent.resolveActivity(context.getPackageManager()) != null) {
      context.startActivity(intent);
    } else {
      Toast.makeText(context, "您的系统中没有安装应用商店", Toast.LENGTH_SHORT).show();
    }
  }

  public static void openUrl(Context context, String url)
  {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    if (intent.resolveActivity(context.getPackageManager()) != null) {
      context.startActivity(intent);
    } else {
      Toast.makeText(context, "您的系统中没有安装浏览器", Toast.LENGTH_SHORT).show();
    }
  }

  /**
   * 发送邮件
   *
   * @param context 上下文
   * @param email   mailTo
   * @param subject 主题
   * @param text    内容
   */
  public static void sendEmail(Context context, String email, String subject, String text)
  {
    Intent intent = new Intent(Intent.ACTION_SENDTO);
    intent.setData(Uri.parse("mailto:" + email));
    if (intent.resolveActivity(context.getPackageManager()) != null) {
      intent.putExtra(Intent.EXTRA_SUBJECT, subject);
      intent.putExtra(Intent.EXTRA_TEXT, text);
      context.startActivity(intent);
    } else {
      Toast.makeText(context, "您的系统中没有安装邮件客户端", Toast.LENGTH_SHORT).show();
    }
  }

  /**
   * 需要拨号权限
   */
  @RequiresPermission(Manifest.permission.CALL_PHONE)
  public static void call(Context context, String number)
  {
    Uri    telUri = Uri.parse(String.format("tel:%s", number));
    Intent intent = new Intent("android.intent.action.CALL", telUri);
    context.startActivity(intent);
  }

  /**
   * 打开应用设置页面
   */
  public static void appDetailSetting(Context context)
  {
    Uri    uri    = Uri.parse("package:" + context.getPackageName());
    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri);
    if (intent.resolveActivity(context.getPackageManager()) != null) {
      context.startActivity(intent);
    }
  }

  public static void sendText(Context context, String text)
  {
    if (ZString.isEmpty(text)) {
      return;
    }

    Intent intent = new Intent(Intent.ACTION_SEND);
    intent.setType("text/plain");
    intent.putExtra(Intent.EXTRA_TEXT, text);
    Intent chooserIntent = Intent.createChooser(intent, "选择分享方式");
    if (chooserIntent != null) {
      context.startActivity(chooserIntent);
    }
  }
}

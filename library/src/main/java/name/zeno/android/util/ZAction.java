package name.zeno.android.util;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pManager;
import android.provider.Settings;
import android.support.annotation.RequiresPermission;
import android.widget.Toast;

import java.util.List;

/**
 * <h1>跳转工具类</h1>
 * <p>
 * <h5>原文:</h5>
 * <p>
 * <li>{@link #isAppInstalled(Context, String)} 应用是否安装</li>
 * <li>{@link #launchApp(Context, String, String)}</li>
 * <li>{@link App} 常用的App 包名和 Activity</li>
 *
 * @author ZenoChan (513500085@qq.com)
 * @version 2015-10-19 14:09:05
 * @see <a href="http://www.jianshu.com/p/1e92c2b0d773">android在项目中启动微信</a>
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


  /**
   * @param packageName 包名
   */
  public static boolean isAppInstalled(Context context, String packageName)
  {
    final PackageManager packageManager = context.getPackageManager();
    List<PackageInfo>    packages       = packageManager.getInstalledPackages(0);
    if (packages != null) {
      for (PackageInfo packageInfo : packages) {
        if (packageInfo.packageName.equals(packageName))
          return true;
      }
    }
    return false;
  }

  /**
   * @param packageName 报名
   * @param className   要启动的 Activity 完整类名
   */
  public static void launchApp(Context context, String packageName, String className)
  {
    Intent        intent = new Intent();
    ComponentName cmp    = new ComponentName(packageName, className);
    intent.setAction(Intent.ACTION_MAIN);
    intent.addCategory(Intent.CATEGORY_LAUNCHER);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.setComponent(cmp);
    context.startActivity(intent);
  }

  public interface App
  {
    String[] WECHAT = {"com.tencent.mm", "com.tencent.mm.ui.LauncherUI"};
    String[] WEIBO  = {"com.sina.weibo", "com.sina.weibo.EditActivity"};
    String[] W_BLOG = {"com.tencent.WBlog", "com.tencent.WBlog.activity.MicroblogInput"};
    String[] QQ     = {"com.tencent.mobileqq", "com.tencent.mobileqq.activity.HomeActivity"};
  }
}

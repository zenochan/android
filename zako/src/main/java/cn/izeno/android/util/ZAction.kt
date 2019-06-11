package cn.izeno.android.util

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresPermission
import cn.izeno.android.util.ZAction.App
import cn.izeno.android.util.ZAction.isAppInstalled
import cn.izeno.android.util.ZAction.launchApp
import java.io.File

/**
 * <h1>跳转工具类</h1>
 *
 *
 * <h5>原文:</h5>
 *
 *
 *  * [isAppInstalled] 应用是否安装
 *  * [launchApp]
 *  * [App] 常用的App 包名和 Activity
 *
 * @author ZenoChan (513500085@qq.com)
 * @version 2015-10-19 14:09:05
 * @see [android在项目中启动微信](http://www.jianshu.com/p/1e92c2b0d773)
 */
object ZAction {

  /**
   * 打开应用商店
   */
  fun openAppStore(context: Context) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.data = Uri.parse("market://details?id=" + context.packageName)
    if (intent.resolveActivity(context.packageManager) != null) {
      context.startActivity(intent)
    } else {
      Toast.makeText(context, "您的系统中没有安装应用商店", Toast.LENGTH_SHORT).show()
    }
  }

  fun openUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    if (intent.resolveActivity(context.packageManager) != null) {
      context.startActivity(intent)
    } else {
      Toast.makeText(context, "您的系统中没有安装浏览器", Toast.LENGTH_SHORT).show()
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
  fun sendEmail(context: Context, email: String, subject: String, text: String) {
    val intent = Intent(Intent.ACTION_SENDTO)
    intent.data = Uri.parse("mailto:" + email)
    if (intent.resolveActivity(context.packageManager) != null) {
      intent.putExtra(Intent.EXTRA_SUBJECT, subject)
      intent.putExtra(Intent.EXTRA_TEXT, text)
      context.startActivity(intent)
    } else {
      Toast.makeText(context, "您的系统中没有安装邮件客户端", Toast.LENGTH_SHORT).show()
    }
  }

  /**
   * 需要拨号权限
   */
  @RequiresPermission(Manifest.permission.CALL_PHONE)
  fun call(context: Context, number: String) {
    val telUri = Uri.parse(String.format("tel:%s", number))
    val intent = Intent("android.intent.action.CALL", telUri)
    context.startActivity(intent)
  }

  /**
   * 打开应用设置页面
   */
  fun appDetailSetting(context: Context) {
    val uri = Uri.parse("package:" + context.packageName)
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri)
    if (intent.resolveActivity(context.packageManager) != null) {
      context.startActivity(intent)
    }
  }


  fun sendText(context: Context, text: String) {
    if (text.isEmpty()) {
      return
    }

    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_TEXT, text)
    val chooserIntent = Intent.createChooser(intent, "选择分享方式")
    if (chooserIntent != null) {
      context.startActivity(chooserIntent)
    }
  }

  /**
   * # 分享文本 or 图片
   *
   * @param context             上下文
   * @param shareTitle          dialog 标题
   * @param msgTitle            消息标题
   * @param msgText             消息内容
   * @param imgPath             图片路径，不分享图片则传null
   * @author [陈治谋](mailto:513500085@qq.com)
   * @since 2017/11/21
   */
  fun shareMsg(context: Context, shareTitle: String, msgTitle: String, msgText: String, imgPath: String? = null) {
    val intent = Intent(Intent.ACTION_SEND)
    if (imgPath.isNullOrEmpty()) {
      intent.type = "text/plain" // 纯文本
    } else {
      val f = File(imgPath)
      if (f.exists() && f.isFile) {
        intent.type = "image/jpg"
        val u = Uri.fromFile(f)
        intent.putExtra(Intent.EXTRA_STREAM, u)
      }
    }
    intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle)
    intent.putExtra(Intent.EXTRA_TEXT, msgText)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    if (intent.resolveActivity(context.packageManager) != null) {
      context.startActivity(Intent.createChooser(intent, shareTitle))
    }
  }


  /**
   * @param packageName 包名
   */
  fun isAppInstalled(context: Context, packageName: String): Boolean {
    val packageManager = context.packageManager
    val packages = packageManager.getInstalledPackages(0)
    if (packages != null) {
      for (packageInfo in packages) {
        if (packageInfo.packageName == packageName)
          return true
      }
    }
    return false
  }

  /**
   * @param packageName 报名
   * @param className   要启动的 Activity 完整类名
   */
  fun launchApp(context: Context, packageName: String, className: String) {
    val intent = Intent()
    val cmp = ComponentName(packageName, className)
    intent.action = Intent.ACTION_MAIN
    intent.addCategory(Intent.CATEGORY_LAUNCHER)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.component = cmp
    context.startActivity(intent)
  }

  interface App {
    companion object {
      val WECHAT = arrayOf("com.tencent.mm", "com.tencent.mm.ui.LauncherUI")
      val WEIBO = arrayOf("com.sina.weibo", "com.sina.weibo.EditActivity")
      val W_BLOG = arrayOf("com.tencent.WBlog", "com.tencent.WBlog.activity.MicroblogInput")
      val QQ = arrayOf("com.tencent.mobileqq", "com.tencent.mobileqq.activity.HomeActivity")
    }
  }
}

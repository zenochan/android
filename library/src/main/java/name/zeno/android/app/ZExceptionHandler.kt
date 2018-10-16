package name.zeno.android.app

import android.app.Activity
import android.app.Application
import android.content.Intent
import name.zeno.android.util.ZLog

/**
 * # 异常捕获上报
 *
 * ### USAGE
 * ```kotlin
 *     val account = if (LocalData.isLogin) JSON.toJSONString(LocalData.buyer) else null
 *     ZExceptionHandler(
 *     app = this,
 *     mainClass = MainActivity::class.java,
 *     email = ChurgoConf.SUBSCRIBE_EMAIL,
 *     accountJson = account
 *     ).apply()
 * ```
 *
 * @param context [Application]
 * @param mainClass 点击关闭或回到什么页面
 * @param email 用户发送邮件的接收地址
 * @param accountJson 用户数据 JSON 串
 * @param onError 错误回调
 *
 * @author [陈治谋](mailto:513500085@qq.com)
 * @since 16/6/30
 */
class ZExceptionHandler(
    private val context: Application,
    private var mainClass: Class<out Activity>,
    private var email: String,
    private var accountJson: String? = null,
    private var onError: ((Throwable) -> Unit)? = null
) : Thread.UncaughtExceptionHandler {
  private val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()

  override fun uncaughtException(thread: Thread, throwable: Throwable) {
    ZLog.e("CRASH", throwable.message, throwable)

    onError?.invoke(throwable)


    val info = ExceptionInfo(throwable, email, mainClass, accountJson)

    val intent = Intent(context, CrashLogActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION)
    intent.putExtra("data", info)
    context.startActivity(intent)

    System.exit(1)
  }

  fun apply() {
    Thread.setDefaultUncaughtExceptionHandler(this)
  }
}

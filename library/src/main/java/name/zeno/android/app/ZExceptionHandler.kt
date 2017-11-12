package name.zeno.android.app

import android.app.Activity
import android.app.Application
import android.content.Intent
import com.umeng.analytics.MobclickAgent
import name.zeno.android.presenter.Extra
import name.zeno.android.third.umeng.ZUmeng
import name.zeno.android.util.ZLog

/**
 * Create Date: 16/6/30
 *
 * @author 陈治谋 (513500085@qq.com)
 */
class ZExceptionHandler @JvmOverloads constructor(
    private val app: Application,
    private var mainClass: Class<out Activity>,
    private var email: String,
    private var accountJson: String? = null,
    private var onError: ((Throwable) -> Unit)? = null
) : Thread.UncaughtExceptionHandler {
  private val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()

  override fun uncaughtException(thread: Thread, throwable: Throwable) {
    ZLog.e("CRASH", throwable.message, throwable)

    onError?.invoke(throwable)

    if (ZUmeng.supported) {
      //友盟错误上报
      MobclickAgent.reportError(app, throwable)
      MobclickAgent.onKillProcess(app)
    }

    val info = ExceptionInfo(throwable, email, mainClass, accountJson)

    val intent = Intent(app, CrashLogActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    Extra.setData(intent, info)
    app.startActivity(intent)

    System.exit(1)
  }

  fun apply() {
    Thread.setDefaultUncaughtExceptionHandler(this)
  }
}

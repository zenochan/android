package cn.izeno.android.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_crash_log.*
import cn.izeno.android.data.CommonConnector
import cn.izeno.android.exception.ZException
import cn.izeno.android.presenter.ZActivity
import cn.izeno.android.util.R
import cn.izeno.android.util.ZAction
import cn.izeno.android.util.ZLog
import cn.izeno.android.widget.SimpleActionbar

/**
 * <h1>程序奔溃</h1>
 *
 *
 * 点击发送奔溃日志邮件
 *
 * @author [陈治谋](513500085@qq.com)
 * @since 1.3.0
 */
class CrashLogActivity : ZActivity() {

  private lateinit var crashLog: String

  private lateinit var info: ExceptionInfo

  override val ctx: Context = this

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    try {
      setContentView(R.layout.activity_crash_log)
      info = intent.getParcelableExtra("data")

      val actionbar = findViewById<SimpleActionbar>(R.id.layout_actionbar)
      actionbar.onAction { this.sendLog() }

      crashLog = ZException.info(info.throwable)
      CommonConnector.sendCrash(info.throwable, info.accountJson)
      tv_crash_log.text = crashLog
      ZLog.e(TAG, crashLog)
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  private fun sendLog() {
    val subject = String.format("来自 %s-%s(%s)的客户端奔溃日志", AppInfo.appName, AppInfo.versionName, AppInfo.versionCode)
    ZAction.sendEmail(this, info.email, subject, crashLog)
  }

  override fun finish() {
    val intent = Intent(this, info.mainActivityClass)
    intent.putExtra("exit", true)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
    super.finish()
  }
}

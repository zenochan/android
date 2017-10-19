package name.zeno.android.app

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import name.zeno.android.core.data
import name.zeno.android.data.CommonConnector
import name.zeno.android.exception.ZException
import name.zeno.android.presenter.ZActivity
import name.zeno.android.util.R
import name.zeno.android.util.ZAction
import name.zeno.android.util.ZLog
import name.zeno.android.widget.SimpleActionbar

/**
 * <h1>程序奔溃</h1>
 *
 *
 * 点击发送奔溃日志邮件
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 1.3.0
 */
class CrashLogActivity : ZActivity() {

  internal lateinit var tvInfo: TextView
  private lateinit var crashLog: String

  private var info: ExceptionInfo? = null


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    try {
      setContentView(R.layout.activity_crash_log)
      info = this.data()

      tvInfo = findViewById(R.id.tv_crash_log)
      val actionbar = findViewById<SimpleActionbar>(R.id.layout_actionbar)
      actionbar.onAction { this.sendLog() }

      crashLog = ZException.info(info!!.throwable)
      CommonConnector.sendCrash(info!!.throwable, info!!.accountJson)
      tvInfo.text = crashLog
      ZLog.e(TAG, crashLog)
    } catch (e: Exception) {
      e.printStackTrace()
    }

  }

  private fun sendLog() {
    val subject = String.format("来自 %s-%s(%s)的客户端奔溃日志", AppInfo.appName, AppInfo.versionName, AppInfo.versionCode)
    ZAction.sendEmail(this, info!!.email, subject, crashLog)
  }

  override fun finish() {
    val intent = Intent(this, info!!.mainActivityClass)
    intent.putExtra("exit", true)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
    super.finish()
  }
}
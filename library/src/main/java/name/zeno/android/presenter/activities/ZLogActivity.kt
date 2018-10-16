package name.zeno.android.presenter.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import name.zeno.android.util.ZLog

/**
 *  * 周期日志
 *  * 友盟 session 统计
 *
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 16/8/22
 */
abstract class ZLogActivity : AppCompatActivity() {
  protected val TAG: String = javaClass.simpleName

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    ZLog.v(TAG, "onCreate()")
  }

  override fun onResume() {
    super.onResume()
    ZLog.v(TAG, "onResume()")
  }

  override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    ZLog.v(TAG, "onNewIntent()")
  }

  override fun onPause() {
    super.onPause()
    ZLog.v(TAG, "onPause()")
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    ZLog.v(TAG, "onSaveInstanceState()")
  }

  override fun onDestroy() {
    super.onDestroy()
    ZLog.v(TAG, "onDestroy()")
  }
}

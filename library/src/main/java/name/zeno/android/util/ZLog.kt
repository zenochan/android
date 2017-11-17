package name.zeno.android.util

import android.util.Log

import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

/**
 * Create Date: 16/6/9
 *
 * @author 陈治谋 (513500085@qq.com)
 */
object ZLog {

  const val TAG = "ZLog"
  var DEBUG = true

  init {
    Logger.addLogAdapter(object : AndroidLogAdapter() {
      override fun isLoggable(priority: Int, tag: String?): Boolean {
        return DEBUG || priority >= Logger.WARN
      }
    })
  }

  fun v(msg: String?) {
    v(TAG, msg)
  }

  fun v(tag: String, msg: String?) {
    Log.v(TAG + "-" + tag, msg)
  }

  fun d(msg: String?) {
    d(TAG, msg)
  }

  fun d(tag: String, msg: String?) {
    Log.d(TAG + "-" + tag, msg)
  }


  fun i(msg: String) {
    i(TAG, msg)
  }

  fun i(tag: String, msg: String?) {
    Log.i(TAG + "-" + tag, msg)
  }

  fun w(msg: String) {
    w(TAG, msg)
  }

  fun w(tag: String, msg: String?) {
    Log.w(TAG + "-" + tag, msg)
  }

  fun e(msg: String) {
    e(TAG, msg)
  }

  fun e(tag: String, msg: String?) {
    Log.e(TAG + "-" + tag, msg)
  }

  fun e(tag: String, msg: String?, tr: Throwable) {
    Log.e(TAG + "-" + tag, msg, tr)
  }
}

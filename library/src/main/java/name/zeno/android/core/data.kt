package name.zeno.android.core

import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.os.Bundle

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/10/13
 */

/**
 * 把 Activity 的参数 copy 一份, 方便配合 ARouter 的传参
 */
fun <T : Fragment, A : Activity> T.args(activity: A): T {
  val args = this.arguments ?: Bundle()
  val actArgs = activity.intent.extras
  if (actArgs != null) args.putAll(actArgs)
  arguments = args
  return this
}

fun Fragment.cancel(finish: Boolean = true, data: (Intent.() -> Unit)? = null) = activity?.cancel(finish, data)
fun Activity.cancel(finish: Boolean = true, data: (Intent.() -> Unit)? = null) {
  if (data == null) {
    setResult(Activity.RESULT_CANCELED)
  } else {
    val intentData = Intent()
    data(intentData)
    setResult(Activity.RESULT_CANCELED, intentData)
  }

  if (finish) finish()
}

fun Fragment.ok(finish: Boolean = true, data: (Intent.() -> Unit)? = null) = activity?.ok(finish, data)
fun Activity.ok(finish: Boolean = true, data: (Intent.() -> Unit)? = null) {
  if (data == null) {
    setResult(Activity.RESULT_OK)
  } else {
    val intentData = Intent()
    data(intentData)
    setResult(Activity.RESULT_OK, intentData)
  }

  if (finish) finish()
}

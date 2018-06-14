package name.zeno.android.core

import android.app.Activity
import android.app.Fragment
import android.os.Bundle
import android.os.Parcelable
import name.zeno.android.presenter.Extra

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/10/13
 */

fun <R : Parcelable> Activity.data(): R = Extra.getData(intent)
    ?: throw IllegalStateException("获取参数失败")

fun <R : Parcelable> Fragment.data(): R = Extra.getData(arguments)
    ?: throw IllegalStateException("获取参数失败")

fun <R : Parcelable> Activity.dataOrNull(): R? = Extra.getData(intent)
fun <R : Parcelable> Fragment.dataOrNull(): R? = Extra.getData(arguments)

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

fun Fragment.cancel(data: Parcelable? = null, finish: Boolean = true) = activity?.cancel(data, finish)
fun Activity.cancel(data: Parcelable? = null, finish: Boolean = true) {
  if (data == null) {
    setResult(Activity.RESULT_CANCELED)
  } else {
    setResult(Activity.RESULT_CANCELED, Extra.setData(data))
  }

  if (finish) finish()
}

fun Fragment.ok(data: Parcelable? = null, finish: Boolean = true) = activity?.ok(data, finish)
fun Activity.ok(data: Parcelable? = null, finish: Boolean = true) {
  if (data == null) {
    setResult(Activity.RESULT_OK)
  } else {
    setResult(Activity.RESULT_OK, Extra.setData(data))
  }

  if (finish) finish()
}

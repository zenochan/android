package name.zeno.android.core

import android.app.Activity
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import name.zeno.android.presenter.Extra

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/10/13
 */

fun now() = System.currentTimeMillis()

fun <R : Parcelable> Activity.data(): R = Extra.getData(intent)!!

fun <R : Parcelable> Fragment.data(): R = Extra.getData(arguments)!!

fun <R : Parcelable> Activity.dataOrNull(): R? = Extra.getData(intent)
fun <R : Parcelable> Fragment.dataOrNull(): R? = Extra.getData(arguments)


fun <T : Fragment, A : Activity> T.args(activity: A): T = args<T>(activity.data())
fun <T : Fragment> T.args(data: Parcelable?): T {
  if (data != null) {
    val args = this.arguments ?: Bundle()
    Extra.setData(args, data)
    this.arguments = args
  }
  return this
}


fun Fragment.cancel(data: Parcelable?, finish: Boolean = false) {
  if (data == null) {
    activity?.setResult(Activity.RESULT_CANCELED)
  } else {
    activity?.setResult(Activity.RESULT_CANCELED, Extra.setData(data))
  }

  if (finish) activity?.finish()
}

fun Fragment.ok(data: Parcelable?, finish: Boolean = false) {
  if (data == null) {
    activity?.setResult(Activity.RESULT_OK)
  } else {
    activity?.setResult(Activity.RESULT_OK, Extra.setData(data))
  }

  if (finish) activity?.finish()
}

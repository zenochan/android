package name.zeno.android.presenter

import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.View
import android.widget.Toast

import com.afollestad.materialdialogs.MaterialDialog

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/9/27.
 */
open class ToastFragment : Fragment() {
  private var fragmentView: View? = null
  private var progressDialog: MaterialDialog? = null

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    fragmentView = view
  }

  override fun onDestroy() {
    super.onDestroy()
    if (progressDialog != null) {
      progressDialog!!.dismiss()
      progressDialog = null
    }
    fragmentView = null
  }

  //<editor-fold desc="snack & toast">
  open fun snack(msg: String?) {
    val msg = msg ?: ""
    try {
      Snackbar.make(fragmentView!!, msg, Snackbar.LENGTH_LONG).show()
    } catch (e: IllegalStateException) {
      //java.lang.IllegalStateException: Fatal Exception thrown on Scheduler.
      //Caused by: java.lang.IllegalArgumentException: No suitable parent found from the given view. Please provide a valid view.
      toast(msg)
    } catch (e: IllegalArgumentException) {
      toast(msg)
    }

  }

  @JvmOverloads
  open fun snack(msg: String, button: String, onNext: (() -> Unit)? = null) {
    try {
      Snackbar.make(fragmentView!!, msg, Snackbar.LENGTH_LONG)
          .setAction(button) { onNext?.invoke() }
          .show()
    } catch (e: IllegalStateException) {
      //java.lang.IllegalStateException: Fatal Exception thrown on Scheduler.
      //Caused by: java.lang.IllegalArgumentException: No suitable parent found from the given view. Please provide a valid view.
      toast(msg)
    } catch (e: IllegalArgumentException) {
      toast(msg)
    }

  }

  open fun snack(@StringRes resId: Int) {
    Snackbar.make(fragmentView!!, resId, Snackbar.LENGTH_SHORT).show()
  }

  @JvmOverloads
  open fun snack(@StringRes resId: Int, button: String, onNext: (() -> Unit)? = null) {
    Snackbar.make(fragmentView!!, resId, Snackbar.LENGTH_LONG)
        .setAction(button) { onNext?.invoke() }
        .show()
  }

  open fun longSnack(msg: String) {
    Snackbar.make(fragmentView!!, msg, Snackbar.LENGTH_LONG).show()
  }

  open fun longSnack(@StringRes resId: Int) {
    Snackbar.make(fragmentView!!, resId, Snackbar.LENGTH_LONG).show()
  }

  open fun toast(msg: String?) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
  }

  open fun toast(@StringRes resId: Int) {
    Toast.makeText(context, resId, Toast.LENGTH_SHORT).show()
  }

  @JvmOverloads
  open fun showLoading(content: String = "加载中...") {
    if (progressDialog == null) {
      progressDialog = MaterialDialog.Builder(context)
          .progress(true, 10000, true)
          .cancelable(false)
          .build()
    }
    progressDialog!!.setContent(content)
    progressDialog!!.show()
  }

  open fun hideLoading() {
    if (progressDialog != null && progressDialog!!.isShowing) {
      progressDialog!!.dismiss()
    }
  }

  fun confirm(confirm: String, ok: String, cancel: String, onOk: () -> Unit) {
    MaterialDialog.Builder(context)
        .content(confirm)
        .positiveText(ok)
        .neutralText(cancel)
        .onPositive { _, _ -> onOk() }
        .show()
  }

  fun showMessageAndFinish(message: String?) {
    MaterialDialog.Builder(context)
        .title("提示")
        .content(message ?: "")
        .neutralText("好")
        .onNeutral { dialog, _ ->
          dialog.dismiss()
          activity.finish()
        }
        .cancelable(false)
        .show()
  }

  open fun showMessage(msg: String?) = snack(msg ?: "")
  open fun showMessage(@StringRes resId: Int) = snack(resId)

}

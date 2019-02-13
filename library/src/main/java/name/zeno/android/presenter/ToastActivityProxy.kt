package name.zeno.android.presenter

import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleObserver
import com.google.android.material.snackbar.Snackbar

/**
 * @author Zeno [mail](mailto:zenochan@qq.com)
 * @since 2018/10/17
 */
class ToastActivityProxy(private val activity: AppCompatActivity) : ToastActivity, LifecycleObserver {
  private val rootView: View
    get() = (activity.findViewById(android.R.id.content) as ViewGroup).getChildAt(0)


  init {
    activity.lifecycle.addObserver(this)
  }

  override fun snack(text: String?, @StringRes textRes: Int?,
                     button: String?, @StringRes buttonRes: Int?,
                     action: (() -> Unit)?
  ) {
    var snack: Snackbar? = when {
      text != null -> Snackbar.make(rootView, text, Snackbar.LENGTH_SHORT)
      textRes != null -> Snackbar.make(rootView, textRes, Snackbar.LENGTH_SHORT)
      else -> null
    }

    snack = when {
      button != null -> snack?.setAction(button) { action?.invoke() }
      buttonRes != null -> snack?.setAction(buttonRes) { action?.invoke() }
      else -> snack
    }

    snack?.show()
  }

  override fun toast(msg: String?, @StringRes msgRes: String?) {
    when {
      msg != null -> Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
      msgRes != null -> Toast.makeText(activity, msgRes, Toast.LENGTH_SHORT).show()
    }
  }

}
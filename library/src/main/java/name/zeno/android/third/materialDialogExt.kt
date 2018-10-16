package name.zeno.android.third

import androidx.annotation.StringRes
import com.afollestad.materialdialogs.MaterialDialog

/**
 * @author [陈治谋](mailto:zenochan@qq.com)
 * @since 2018/6/14
 */
fun MaterialDialog.Builder.positive(@StringRes res: Int, action: ((MaterialDialog) -> Unit)? = null) = apply {
  this.positiveText(res)
  if (action != null) this.onPositive { dialog, _ -> action(dialog) }
}

fun MaterialDialog.Builder.positive(text: String, action: ((MaterialDialog) -> Unit)? = null) = apply {
  this.positiveText(text)
  if (action != null) this.onPositive { dialog, _ -> action(dialog) }
}

fun MaterialDialog.Builder.negative(@StringRes res: Int, action: ((MaterialDialog) -> Unit)? = null) = apply {
  this.negativeText(res)
  if (action != null) this.onNegative { dialog, _ -> action(dialog) }
}

fun MaterialDialog.Builder.negative(text: String, action: ((MaterialDialog) -> Unit)? = null) = apply {
  this.negativeText(text)
  if (action != null) this.onNegative { dialog, _ -> action(dialog) }
}

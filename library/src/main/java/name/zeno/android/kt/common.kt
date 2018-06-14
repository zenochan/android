package name.zeno.android.kt

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Paint
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import name.zeno.android.presenter.ZFragment
import name.zeno.android.third.positive
import name.zeno.android.util.ZAction
import name.zeno.android.util.ZDate
import name.zeno.android.util.min
import name.zeno.android.widget.ZTextWatcher
import name.zeno.ktrxpermission.ZPermission
import name.zeno.ktrxpermission.rxPermissions
import java.io.Closeable
import java.io.IOException

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/9/21
 */
/**
 * # 扩展 int 布尔判断
 * - false : null | 0
 * - true: others
 */
val Int?.boolean
  get() = this != null && this != 0


val Int?.notZero
  get() = this != null && this != 0

val Int?.zero
  get() = this == 0

/**
 * # 扩展 int 符号判断
 * - `-`: null | 0
 * - `+`: others
 */
val Int?.sign
  get() = if (this != null && this != 0) "+" else "-"

// 设置 tab layout 的分割线
fun TabLayout.setDivider(@DrawableRes res: Int) {
  val linearLayout = this.getChildAt(0) as LinearLayout
  linearLayout.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
  linearLayout.dividerDrawable = ContextCompat.getDrawable(this.context, res)
}

fun View.gone() {
  this.visibility = View.GONE
}

fun View.visible() {
  this.visibility = View.VISIBLE
}

fun View.invisible() {
  this.visibility = View.INVISIBLE
}

var View.show: Boolean
  get() = this.visibility == View.VISIBLE
  set(value) {
    this.visibility = if (value) View.VISIBLE else View.GONE
  }


/**
 * # 安全的关闭 Closeable
 * @return 错误数量
 */
fun close(vararg io: Closeable?): Int {
  var error = 0
  io.forEach {
    try {
      it?.close()
    } catch (io: IOException) {
      error++
    }
  }

  return error
}

@SuppressLint("MissingPermission")
fun <T : ZFragment> T.call(mobile: String, @StringRes call: Int, @StringRes cancel: Int) {
  MaterialDialog.Builder(activity as Context)
      .content(mobile)
      .negativeText(cancel)
      .positive(call) {
        rxPermissions(ZPermission.CALL_PHONE).subscribe { granted ->
          if (granted) {
            ZAction.call(ctx, mobile)
          } else {
            snack("呼叫客服需要【电话】权限", "去设置") { ZAction.appDetailSetting(ctx) };
          }
        }
      }
      .show()
}


fun String.encrypt(start: Int = 1, end: Int = length - 1): String {
  return String(this.mapIndexed { i, char ->
    if (i >= start && i <= min(this.length - 1, end)) '*' else char
  }.toCharArray())
}

var TextView.colorRes: Int
  get() = throw IllegalAccessException("not implement")
  set(@ColorRes value) {
    this.setTextColor(ContextCompat.getColor(context, value))
  }


fun TextView.setText(provider: (resources: Resources) -> CharSequence) {
  text = provider(resources)
}

fun TextView.onTextChanged(action: (String) -> Unit) {
  ZTextWatcher.watch(this) { _, text -> action(text) }
}

/**
 * 删除线
 */
fun TextView.strike() {
  this.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
}

/**
 * 获取正确的 index
 */
fun Int.remain(size: Int): Int {
  if (size < 1) throw IllegalArgumentException("size must > 0")
  var value = this
  while (value < 0) value += size
  return value % size
}


fun String.ms(format: String = "yyyy-MM-dd") = ZDate.date(this, format).time
fun Long.dateFormat(format: String = "yyyy-MM-dd"): String = ZDate.format(this, format)

fun View.getString(@StringRes res: Int): String = context.getString(res)
fun View.getString(@StringRes res: Int, vararg formatArgs: Any?): String = context.getString(res, formatArgs)

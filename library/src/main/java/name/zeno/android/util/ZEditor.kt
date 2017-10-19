package name.zeno.android.util

import android.content.Context
import android.os.Build
import android.text.method.DigitsKeyListener
import android.text.method.KeyListener
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import name.zeno.android.util.ZEditor.actionDone
import name.zeno.android.util.ZEditor.decimal
import name.zeno.android.util.ZEditor.number
import name.zeno.android.util.ZEditor.signDecimal
import java.util.*

/**
 * 输入控件常用方法工具类
 *
 *  * [actionDone] 完成Action
 *  * [number] 仅输入数字
 *  * [decimal] 输入无符号小数
 *  * [signDecimal] 输入有符号小数
 *
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/9/5
 */
object ZEditor {
  fun actionDone(view: TextView, action: () -> Unit) {
    view.setOnEditorActionListener { v, actionId, event ->
      if (actionId == EditorInfo.IME_ACTION_DONE) action()
      true
    }
  }

  /** [0-9]  */
  fun number(view: EditText) {
    view.keyListener = DigitsKeyListener.getInstance("0123456789")
  }

  /** 无符号小数  */
  fun decimal(view: EditText) {
    val listener: KeyListener
    if (Build.VERSION.SDK_INT < 26) {
      @Suppress("DEPRECATION")
      listener = DigitsKeyListener.getInstance(false, true)
    } else {
      listener = DigitsKeyListener.getInstance(Locale.CHINESE, false, true)
    }

    view.keyListener = listener
  }

  /** 有符号小数  */
  fun signDecimal(view: EditText) {
    val listener: KeyListener
    if (Build.VERSION.SDK_INT < 26) {
      @Suppress("DEPRECATION")
      listener = DigitsKeyListener.getInstance(true, true)
    } else {
      listener = DigitsKeyListener.getInstance(Locale.CHINESE, true, true)
    }

    view.keyListener = listener
  }


  /** 显示输入法  */
  fun showInputMethod(view: EditText) {
    val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
  }

  /** 隐藏输入法  */
  fun hideInputMethod(view: View) {
    val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
  }

  /** 切换输入法显示  */
  fun toggleInputMethod(view: View) {
    val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED)
  }

}

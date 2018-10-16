package name.zeno.android.widget

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.CallSuper
import java.util.regex.Pattern

/**
 * Create Date: 16/5/30
 *
 * @author 陈治谋 (513500085@qq.com)
 */
abstract class ZTextWatcher : TextWatcher {

  override fun afterTextChanged(s: Editable) {}

  override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

  @CallSuper
  override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
    onTextChanged(s.toString())
  }

  abstract fun onTextChanged(txt: String)

  companion object {

    fun watch(view: TextView, watcher: (view: TextView, text: String) -> Unit): TextWatcher {
      val textWatcher = object : TextWatcher {

        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
          watcher(view, charSequence.toString())
        }

        override fun afterTextChanged(editable: Editable) {}
      }

      view.addTextChangedListener(textWatcher)
      return textWatcher
    }

    /**
     * 通过 [TextWatcher] 纠正输入
     */
    fun pattern(view: EditText, regex: String): TextWatcher {
      val textWatcher = object : TextWatcher {
        internal var pattern = Pattern.compile(regex)
        internal var old: CharSequence = ""

        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
          if (old.isEmpty() || pattern.matcher(charSequence).matches()) {
            old = charSequence.toString()
          }
        }

        override fun onTextChanged(s: CharSequence, i: Int, i1: Int, i2: Int) {
          if (s.length > 0 && old != s.toString() && !pattern.matcher(s).matches()) {
            var p = view.selectionStart
            p += if (s.length > old.length) -1 else +1
            view.setText(old)
            view.setSelection(Math.min(p, view.length()))
          }
        }

        override fun afterTextChanged(editable: Editable) {}
      }

      view.addTextChangedListener(textWatcher)
      return textWatcher
    }
  }

}

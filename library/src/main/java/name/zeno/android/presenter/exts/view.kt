/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/10/18
 */
package name.zeno.android.presenter.exts

import android.view.View

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

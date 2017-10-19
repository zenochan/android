package name.zeno.android.presenter.activities

import android.content.Context
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText


/**
 * # [点击空白地方时关闭软键盘](http://gold.xitu.io/entry/5788b97d0a2b5800682b77f5)
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 16/8/22
 */
abstract class AutoHideIMActivity : ZLogActivity() {
  private var lastFocus: View? = null

  override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
    var eventConsumed = false

    try {
      if (ev.action == MotionEvent.ACTION_DOWN) {
        val v = currentFocus
        if (v != null && isShouldHideInput(v, ev)) {
          val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
          imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
        eventConsumed = super.dispatchTouchEvent(ev)
      } else {
        eventConsumed = window.superDispatchTouchEvent(ev) || onTouchEvent(ev)
      }
    } catch (ignore: IllegalArgumentException) {
      //pointerIndex out of range pointerIndex=-1 pointerCount=1
    }

    return eventConsumed
  }

  // 是否应该隐藏软键盘
  private fun isShouldHideInput(v: View?, event: MotionEvent): Boolean {
    enableRootViewFocus()
    if (v != null && v is EditText) {

      if (lastFocus == null) {
        lastFocus = v
      } else if (lastFocus !== v) {
        lastFocus = v
        return false
      }

      if (eventInFocusView(v, event)) {
        //点击的是输入框区域，保留点击EditText的事件
        return false
      } else {
        //使EditText触发一次失去焦点事件
        v.isFocusable = false
        v.isFocusableInTouchMode = true
        return true
      }
    }
    return false
  }

  private fun eventInFocusView(v: View, event: MotionEvent): Boolean {
    val leftTop = intArrayOf(0, 0)
    //获取输入框当前的location位置
    v.getLocationInWindow(leftTop)
    val rect = Rect()
    rect.left = leftTop[0]
    rect.top = leftTop[1]
    rect.bottom = rect.top + v.height
    rect.right = rect.left + v.width

    return rect.contains(event.x.toInt(), event.y.toInt())
  }

  private fun enableRootViewFocus() {
    val rootView = (findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)
    if (rootView != null && !rootView.isFocusable) {
      rootView.isFocusable = true
      rootView.isFocusableInTouchMode = true
    }
  }
}

package name.zeno.android.presenter.activities;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


/**
 * 点击空白处隐藏软键盘
 *
 * @author 陈治谋 (513500085@qq.com)
 * @see <a href='http://gold.xitu.io/entry/5788b97d0a2b5800682b77f5'>点击空白地方时关闭软键盘</a>
 * @since 16/8/22
 */
public abstract class AutoHideIMActivity extends ZLogActivity
{
  private View lastFocus;

  @Override public boolean dispatchTouchEvent(MotionEvent ev)
  {
    boolean eventConsumed = false;

    try {
      if (ev.getAction() == MotionEvent.ACTION_DOWN) {
        View v = getCurrentFocus();
        if (v != null && isShouldHideInput(v, ev)) {
          InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
          if (imm != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
          }
        }
        eventConsumed = super.dispatchTouchEvent(ev);
      } else {
        eventConsumed = getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
      }
    } catch (IllegalArgumentException ignore) {
      //pointerIndex out of range pointerIndex=-1 pointerCount=1
    }

    return eventConsumed;
  }

  // 是否应该隐藏软键盘
  private boolean isShouldHideInput(View v, MotionEvent event)
  {
    enableRootViewFocus();
    if (v != null && v instanceof EditText) {

      if (lastFocus != null && lastFocus != v) {
        lastFocus = v;
        return false;
      }

      int[] leftTop = {0, 0};
      //获取输入框当前的location位置
      v.getLocationInWindow(leftTop);
      int left   = leftTop[0];
      int top    = leftTop[1];
      int bottom = top + v.getHeight();
      int right  = left + v.getWidth();
      if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
        //点击的是输入框区域，保留点击EditText的事件
        return false;
      } else {
        //使EditText触发一次失去焦点事件
        v.setFocusable(false);
        v.setFocusableInTouchMode(true);
        return true;
      }
    }
    return false;
  }

  private void enableRootViewFocus()
  {
    View rootView = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
    if (rootView != null && !rootView.isFocusable()) {
      rootView.setFocusable(true);
      rootView.setFocusableInTouchMode(true);
    }
  }
}

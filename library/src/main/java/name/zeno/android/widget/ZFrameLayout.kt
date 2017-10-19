package name.zeno.android.widget

import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.support.annotation.AttrRes
import android.util.AttributeSet
import android.view.WindowInsets
import android.widget.FrameLayout

/**
 * 重写 [fitSystemWindows] ，解决 window resizing 失效的问题
 *
 * @author 陈治谋 (微信: puppet2436)
 * @since 2017/3/29
 */
class ZFrameLayout : FrameLayout {
  private val mInsets = IntArray(4)

  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
  constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr)

  override fun fitSystemWindows(insets: Rect): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      // Intentionally do not modify the bottom inset. For some reason,
      // if the bottom inset is modified, window resizing stops working.
      // TODO: Figure out why.
      mInsets[0] = insets.left
      mInsets[1] = insets.top
      mInsets[2] = insets.right

      insets.left = 0
      insets.top = 0
      insets.right = 0
    }

    return super.fitSystemWindows(insets)
  }

  override fun onApplyWindowInsets(insets: WindowInsets): WindowInsets {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
      mInsets[0] = insets.systemWindowInsetLeft
      mInsets[1] = insets.systemWindowInsetTop
      mInsets[2] = insets.systemWindowInsetRight
      return super.onApplyWindowInsets(insets.replaceSystemWindowInsets(0, 0, 0, insets.systemWindowInsetBottom))
    } else {
      return insets
    }
  }
}

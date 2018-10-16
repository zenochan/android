package name.zeno.android.widget.draglayout

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.widget.NestedScrollView
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration

/**
 * 只为顶部ScrollView使用
 * 如果使用了其它的可拖拽的控件，请仿照该类实现isAtBottom方法
 */
class DragScrollView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : NestedScrollView(context, attrs, defStyle) {

  internal var isAtBottom: Boolean = false // 按下的时候是否在底部
  private var mTouchSlop = 4 // 判定为滑动的阈值，单位是像素
  private var scrollMode: Int = 0
  private var downY: Float = 0.toFloat()

  init {
    val configuration = ViewConfiguration.get(context)
    mTouchSlop = configuration.scaledTouchSlop
  }

  override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
    when (ev.action) {
      MotionEvent.ACTION_DOWN -> {
        downY = ev.rawY
        isAtBottom = isAtBottom()
        scrollMode = TOUCH_IDLE
        parent.requestDisallowInterceptTouchEvent(true)
      }
      MotionEvent.ACTION_MOVE -> if (scrollMode == TOUCH_IDLE) {
        val yOffset = downY - ev.rawY
        val yDistance = Math.abs(yOffset)
        if (yDistance > mTouchSlop) {
          if (yOffset > 0 && isAtBottom) {
            scrollMode = TOUCH_DRAG_LAYOUT
            parent.requestDisallowInterceptTouchEvent(false)
            return true
          } else {
            scrollMode = TOUCH_INNER_CONSUME
          }
        }
      }
    }
    return super.dispatchTouchEvent(ev)
  }

  override fun onTouchEvent(ev: MotionEvent): Boolean = when (scrollMode) {
    TOUCH_DRAG_LAYOUT -> false
    else -> super.onTouchEvent(ev)
  }

  @SuppressLint("RestrictedApi")
  private fun isAtBottom(): Boolean = scrollY + measuredHeight >= computeVerticalScrollRange() - 2

  companion object {
    private val TOUCH_IDLE = 0
    private val TOUCH_INNER_CONSUME = 1 // touch事件由ScrollView内部消费
    private val TOUCH_DRAG_LAYOUT = 2 // touch事件由上层的DragLayout去消费
  }

}

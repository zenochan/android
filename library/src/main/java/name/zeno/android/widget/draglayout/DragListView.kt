package name.zeno.android.widget.draglayout

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.ListView


class DragListView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ListView(context, attrs, defStyle) {
  private var scrollMode: Int = 0
  private var downX: Float = 0F
  private var downY: Float = 0F

  private var isAtTop = true // 如果是true，则允许拖动至底部的下一页
  private var mTouchSlop = 4 // 判定为滑动的阈值，单位是像素

  init {
    mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
  }

  override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
    if (ev.action == MotionEvent.ACTION_DOWN) {
      downX = ev.rawX
      downY = ev.rawY
      isAtTop = isAtTop()
      scrollMode = MODE_IDLE
      parent.requestDisallowInterceptTouchEvent(true)
    } else if (ev.action == MotionEvent.ACTION_MOVE) {
      if (scrollMode == MODE_IDLE) {
        val xDistance = Math.abs(downX - ev.rawX)
        val yDistance = Math.abs(downY - ev.rawY)
        if (xDistance > yDistance && xDistance > mTouchSlop) {
          scrollMode = MODE_HORIZONTAL
        } else if (yDistance > xDistance && yDistance > mTouchSlop) {
          scrollMode = MODE_VERTICAL
          if (downY < ev.rawY && isAtTop) {
            parent.requestDisallowInterceptTouchEvent(false)
            return false
          }
        }
      }
    }

    return super.dispatchTouchEvent(ev)
  }

  /**
   * 判断listView是否在顶部
   *
   * @return 是否在顶部
   */
  fun isAtTop(): Boolean {
    var resultValue = false
    val childNum = childCount
    if (childNum == 0) {
      // 没有child，肯定在顶部
      resultValue = true
    } else {
      if (firstVisiblePosition == 0) {
        // 根据第一个childView来判定是否在顶部
        val firstView = getChildAt(0)
        if (Math.abs(firstView.top) < 2) {
          resultValue = true
        }
      }
    }

    return resultValue
  }

  companion object {
    private val MODE_IDLE = 0
    private val MODE_HORIZONTAL = 1
    private val MODE_VERTICAL = 2
  }
}
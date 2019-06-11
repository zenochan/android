package cn.izeno.android.widget.draglayout

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.webkit.WebView

class DragWebView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : WebView(context, attrs, defStyle) {

  private var scrollMode: Int = 0
  private var downX: Float = 0F
  private var downY: Float = 0F

  internal var isAtTop = true // 如果是true，则允许拖动至底部的下一页
  private var mTouchSlop = 4 // 判定为滑动的阈值，单位是像素

  init {
    //    disableZoomController();
    mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
  }

  // 使得控制按钮不可用
  private fun disableZoomController() {
    this.settings.builtInZoomControls = true
    this.settings.displayZoomControls = false
  }

  override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
    when (ev.action) {
      MotionEvent.ACTION_DOWN -> {
        downX = ev.rawX
        downY = ev.rawY
        isAtTop = isAtTop()
        scrollMode = MODE_IDLE
        parent.requestDisallowInterceptTouchEvent(true)
      }
      MotionEvent.ACTION_MOVE -> if (scrollMode == MODE_IDLE) {
        val xDistance = Math.abs(downX - ev.rawX)
        val yDistance = Math.abs(downY - ev.rawY)
        if (xDistance > yDistance && xDistance > mTouchSlop) {
          scrollMode = MODE_HORIZONTAL
          parent.requestDisallowInterceptTouchEvent(false)
        } else if (yDistance > xDistance && yDistance > mTouchSlop) {
          scrollMode = MODE_VERTICAL
          if (downY < ev.rawY && isAtTop) {
            parent.requestDisallowInterceptTouchEvent(false)
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
    return scrollY == 0
  }

  companion object {
    private val MODE_IDLE = 0
    private val MODE_HORIZONTAL = 1
    private val MODE_VERTICAL = 2
  }
}

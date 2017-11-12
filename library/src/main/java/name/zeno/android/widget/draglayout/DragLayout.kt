package name.zeno.android.widget.draglayout

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.view.GestureDetectorCompat
import android.support.v4.view.ViewCompat
import android.support.v4.widget.ViewDragHelper
import android.util.AttributeSet
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import name.zeno.android.data.CommonConnector

/**
 * # [xmuSistone/android-vertical-slide-view](https://github.com/xmuSistone/android-vertical-slide-view)
 * 需要重写 children 的 dispatchTouchEvent 方法
 * 这是一个viewGroup容器，实现上下两个 View 拖动切换
 *
 * @author sistone.Zhang
 */
@Suppress("unused")
@SuppressLint("NewApi")
class DragLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ViewGroup(context, attrs, defStyle) {

  /* 拖拽工具类 */
  private val dragHelper: ViewDragHelper
  /** 检测Y方向滚动 */
  private val yScrollDetector: GestureDetectorCompat

  /* 上下两个frameLayout，在Activity中注入fragment */
  private lateinit var topView: View
  private lateinit var bottomView: View
  private var topHeight: Int = 0
  private var downTop1: Int = 0 // 手指按下的时候，topView的getTop值

  var nextPageListener: (() -> Unit)? = null // 手指松开是否加载下一页的notifier
  var onDrag: ((Int) -> Unit)? = null // 拖动改变时回掉

  init {
    dragHelper = ViewDragHelper.create(this, 10f, DragHelperCallback())
    dragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_BOTTOM)
    yScrollDetector = GestureDetectorCompat(context, object : SimpleOnGestureListener() {
      // 垂直滑动时dy>dx，才被认定是上下拖动
      override fun onScroll(e1: MotionEvent, e2: MotionEvent, dx: Float, dy: Float): Boolean =
          Math.abs(dy) > Math.abs(dx)
    })
  }

  override fun onFinishInflate() {
    super.onFinishInflate()
    // 跟findviewbyId一样，初始化上下两个view
    topView = getChildAt(0)
    bottomView = getChildAt(1)
  }

  internal inner class YScrollDetector : SimpleOnGestureListener() {

    override fun onScroll(e1: MotionEvent, e2: MotionEvent, dx: Float, dy: Float): Boolean {
      // 垂直滑动时dy>dx，才被认定是上下拖动
      return Math.abs(dy) > Math.abs(dx)
    }
  }

  override fun computeScroll() {
    if (dragHelper.continueSettling(true)) {
      ViewCompat.postInvalidateOnAnimation(this)
    }
  }

  /**
   * 这是拖拽效果的主要逻辑
   */
  private inner class DragHelperCallback : ViewDragHelper.Callback() {

    override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
      // 一个view位置改变，另一个view的位置要跟进
      onViewPosChanged(changedView)
    }

    // 两个子View都需要跟踪，返回true
    override fun tryCaptureView(child: View, pointerId: Int): Boolean = true

    // 这个用来控制拖拽过程中松手后，自动滑行的速度，暂时给一个随意的数值
    override fun getViewVerticalDragRange(child: View): Int = 1

    override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
      // 滑动松开后，需要向上或者向下粘到特定的位置
      animTopOrBottom(releasedChild, yvel)
    }

    // view 停止滑动了
    override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
      var finalTop = top
      if (child === topView) {
        // 拖动的时第一个view
        if (top > 0) {
          // 不让第一个view往下拖，因为顶部会白板
          finalTop = 0
        }
      } else if (child === bottomView) {
        // 拖动的时第二个view
        if (top < 0) {
          // 不让第二个view网上拖，因为底部会白板
          finalTop = 0
        }
      }

      // finalTop代表的是理论上应该拖动到的位置。此处计算拖动的距离除以一个参数(3)，是让滑动的速度变慢。数值越大，滑动的越慢
      return child.top + (finalTop - child.top) / 3
    }
  }

  /** 滑动时view位置改变协调处理 */
  private fun onViewPosChanged(changedView: View) {
    val offsetTopBottom = topHeight + topView.top - bottomView.top
    if (changedView === topView) {
      bottomView.offsetTopAndBottom(offsetTopBottom)
    } else {
      topView.offsetTopAndBottom(-offsetTopBottom)
    }
    // TODO 有的时候会默认白板，这个很恶心。后面有时间再优化
    invalidate()
  }

  private fun animTopOrBottom(releasedChild: View, yvel: Float) {
    var finalTop = 0 // 默认是粘到最顶端
    if (releasedChild === topView) {
      // 拖动第一个view松手
      if (yvel < -VEL_THRESHOLD || downTop1 == 0 && topView.top < -DISTANCE_THRESHOLD) {
        // 向上的速度足够大，就滑动到顶端
        // 向上滑动的距离超过某个阈值，就滑动到顶端
        finalTop = -topHeight

        // 下一页可以初始化了
        nextPageListener?.invoke()
      }
    } else {
      // 拖动第二个view松手
      if (yvel > VEL_THRESHOLD || downTop1 == -topHeight && releasedChild.top > DISTANCE_THRESHOLD) {
        // 保持原地不动
        finalTop = topHeight
      }
    }

    if (dragHelper.smoothSlideViewTo(releasedChild, 0, finalTop)) {
      onDrag?.invoke(if (releasedChild === topView) 1 else 0)
      ViewCompat.postInvalidateOnAnimation(this)
    }
  }

  /* touch事件的拦截与处理都交给mDraghelper来处理 */
  override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {

    if (topView.bottom > 0 && topView.top < 0) {
      // view粘到顶部或底部，正在动画中的时候，不处理touch事件
      return false
    }

    var shouldIntercept = false
    try {
      shouldIntercept = dragHelper.shouldInterceptTouchEvent(ev)
    } catch (e: Exception) {
      CommonConnector.sendCrash(e)
    }

    val action = ev.actionMasked

    if (action == MotionEvent.ACTION_DOWN) {
      // action_down时就让mDragHelper开始工作，否则有时候导致异常 他大爷的
      dragHelper.processTouchEvent(ev)
      downTop1 = topView.top
    }

    val yScroll = yScrollDetector.onTouchEvent(ev)
    return shouldIntercept && yScroll
  }

  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(e: MotionEvent): Boolean {
    try {
      // 统一交给mDragHelper处理，由DragHelperCallback实现拖动效果
      dragHelper.processTouchEvent(e)
    } catch (e: Exception) {
      CommonConnector.sendCrash(e)
    }
    return true
  }

  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    // 只在初始化的时候调用
    // 一些参数作为全局变量保存起来

    if (topView.top == 0) {
      // 只在初始化的时候调用
      // 一些参数作为全局变量保存起来
      topView.layout(l, 0, r, b - t)
      bottomView.layout(l, 0, r, b - t)

      topHeight = topView.measuredHeight
      bottomView.offsetTopAndBottom(topHeight)
    } else {
      // 如果已被初始化，这次onLayout只需要将之前的状态存入即可
      topView.layout(l, topView.top, r, topView.bottom)
      bottomView.layout(l, bottomView.top, r, bottomView.bottom)
    }
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    measureChildren(widthMeasureSpec, heightMeasureSpec)

    val maxWidth = View.MeasureSpec.getSize(widthMeasureSpec)
    val maxHeight = View.MeasureSpec.getSize(heightMeasureSpec)

    val wms = resolveSizeAndState(maxWidth, widthMeasureSpec, 0)
    val hms = resolveSizeAndState(maxHeight, heightMeasureSpec, 0)
    setMeasuredDimension(wms, hms)
  }

  companion object {
    private val VEL_THRESHOLD = 100 // 滑动速度的阈值，超过这个绝对值认为是上下
    private val DISTANCE_THRESHOLD = 100 // 单位是像素，当上下滑动速度不够时，通过这个阈值来判定是应该粘到顶部还是底部
  }
}

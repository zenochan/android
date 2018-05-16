package name.zeno.android.behavior

import android.content.Context
import android.support.annotation.IntDef
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View

/**
 * <h1>垂直滚动</h1>
 *
 *
 * 主要的方法
 *
 *  * [.onDirectionNestedPreScroll]
 *  * [.onNestedVerticalOverScroll]
 *  * [.onNestedDirectionFling]
 *
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 16/8/22
 */
abstract class VerticalScrollingBehavior<V : View> : CoordinatorLayout.Behavior<V> {

  private var mTotalDyUnconsumed = 0
  private var mTotalDy = 0

  @ScrollDirection
  var mOverScrollDirection = SCROLL_NONE
    private set
  @ScrollDirection
  var mScrollDirection = SCROLL_NONE
    private set

  constructor() : super() {}

  constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

  @IntDef(SCROLL_DIRECTION_UP, SCROLL_DIRECTION_DOWN)
  @Retention(AnnotationRetention.SOURCE)
  annotation class ScrollDirection()

  override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: V, directTargetChild: View, target: View, nestedScrollAxes: Int): Boolean {
    return nestedScrollAxes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
  }

  override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: V, target: View, dx: Int, dy: Int, consumed: IntArray) {
    super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed)
    if (dy > 0 && mTotalDy < 0) {
      mTotalDy = 0
      mScrollDirection = SCROLL_DIRECTION_UP
    } else if (dy < 0 && mTotalDy > 0) {
      mTotalDy = 0
      mScrollDirection = SCROLL_DIRECTION_DOWN
    }
    mTotalDy += dy
    onDirectionNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, mScrollDirection)
  }

  override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: V, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
    super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
    if (dyUnconsumed > 0 && mTotalDyUnconsumed < 0) {
      mTotalDyUnconsumed = 0
      mOverScrollDirection = SCROLL_DIRECTION_UP
    } else if (dyUnconsumed < 0 && mTotalDyUnconsumed > 0) {
      mTotalDyUnconsumed = 0
      mOverScrollDirection = SCROLL_DIRECTION_DOWN
    }
    mTotalDyUnconsumed += dyUnconsumed
    onNestedVerticalOverScroll(coordinatorLayout, child, mOverScrollDirection, dyConsumed, mTotalDyUnconsumed)
  }

  override fun onNestedFling(coordinatorLayout: CoordinatorLayout, child: V, target: View, velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
    super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed)
    mScrollDirection = if (velocityY > 0) SCROLL_DIRECTION_UP else SCROLL_DIRECTION_DOWN
    return onNestedDirectionFling(coordinatorLayout, child, target, velocityX, velocityY, mScrollDirection)
  }

  /**
   * @param direction         [ScrollDirection]
   * @param currentOverScroll Unconsumed value, negative or positive based on the direction;
   * @param totalOverScroll   Cumulative value for current direction
   */
  fun onNestedVerticalOverScroll(coordinatorLayout: CoordinatorLayout, child: V, @ScrollDirection direction: Int, currentOverScroll: Int, totalOverScroll: Int) {}

  /**
   * @param scrollDirection [ScrollDirection]
   */
  open fun onDirectionNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: V, target: View, dx: Int, dy: Int, consumed: IntArray, @ScrollDirection scrollDirection: Int) {}

  /**
   * @return true if the Behavior consumed the fling
   */
  protected open fun onNestedDirectionFling(coordinatorLayout: CoordinatorLayout, child: V, target: View, velocityX: Float, velocityY: Float, @ScrollDirection scrollDirection: Int): Boolean {
    return false
  }

  companion object {
    const val SCROLL_DIRECTION_UP = 1
    const val SCROLL_DIRECTION_DOWN = -1
    const val SCROLL_NONE = 0
  }

}


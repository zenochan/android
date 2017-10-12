package name.zeno.android.behavior;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <h1>垂直滚动</h1>
 * <p>
 * 主要的方法
 * <ul>
 * <li>{@link #onDirectionNestedPreScroll(CoordinatorLayout, View, View, int, int, int[], int)}</li>
 * <li>{@link #onNestedVerticalOverScroll(CoordinatorLayout, View, int, int, int)}</li>
 * <li>{@link #onNestedDirectionFling(CoordinatorLayout, View, View, float, float, int)}</li>
 * </ul>
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 16/8/22
 */
@SuppressWarnings("unused")
public abstract class VerticalScrollingBehavior<V extends View> extends CoordinatorLayout.Behavior<V>
{

  private static final String TAG = "VerticalScrollingBehavior";

  private int mTotalDyUnconsumed = 0;
  private int mTotalDy           = 0;

  @ScrollDirection
  private int mOverScrollDirection = ScrollDirection.SCROLL_NONE;
  @ScrollDirection
  private int mScrollDirection     = ScrollDirection.SCROLL_NONE;

  public VerticalScrollingBehavior()
  {
    super();
  }

  public VerticalScrollingBehavior(Context context, AttributeSet attrs)
  {
    super(context, attrs);
  }

  public int getMOverScrollDirection()
  {return this.mOverScrollDirection;}

  public int getMScrollDirection()
  {return this.mScrollDirection;}

  @Retention(RetentionPolicy.SOURCE)
  @IntDef({ScrollDirection.SCROLL_DIRECTION_UP, ScrollDirection.SCROLL_DIRECTION_DOWN})
  public @interface ScrollDirection
  {
    int SCROLL_DIRECTION_UP   = 1;
    int SCROLL_DIRECTION_DOWN = -1;
    int SCROLL_NONE           = 0;
  }

  @Override
  public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, V child, View directTargetChild, View target, int nestedScrollAxes)
  {
    return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
  }

  @Override
  public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, V child, View target, int dx, int dy, int[] consumed)
  {
    super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
    if (dy > 0 && mTotalDy < 0) {
      mTotalDy = 0;
      mScrollDirection = ScrollDirection.SCROLL_DIRECTION_UP;
    } else if (dy < 0 && mTotalDy > 0) {
      mTotalDy = 0;
      mScrollDirection = ScrollDirection.SCROLL_DIRECTION_DOWN;
    }
    mTotalDy += dy;
    onDirectionNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, mScrollDirection);
  }

  @Override
  public void onNestedScroll(CoordinatorLayout coordinatorLayout, V child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed)
  {
    super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    if (dyUnconsumed > 0 && mTotalDyUnconsumed < 0) {
      mTotalDyUnconsumed = 0;
      mOverScrollDirection = ScrollDirection.SCROLL_DIRECTION_UP;
    } else if (dyUnconsumed < 0 && mTotalDyUnconsumed > 0) {
      mTotalDyUnconsumed = 0;
      mOverScrollDirection = ScrollDirection.SCROLL_DIRECTION_DOWN;
    }
    mTotalDyUnconsumed += dyUnconsumed;
    onNestedVerticalOverScroll(coordinatorLayout, child, mOverScrollDirection, dyConsumed, mTotalDyUnconsumed);
  }

  @Override
  public boolean onNestedFling(CoordinatorLayout coordinatorLayout, V child, View target, float velocityX, float velocityY, boolean consumed)
  {
    super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    mScrollDirection = velocityY > 0 ? ScrollDirection.SCROLL_DIRECTION_UP : ScrollDirection.SCROLL_DIRECTION_DOWN;
    return onNestedDirectionFling(coordinatorLayout, child, target, velocityX, velocityY, mScrollDirection);
  }

  /**
   * @param direction         {@link ScrollDirection}
   * @param currentOverScroll Unconsumed value, negative or positive based on the direction;
   * @param totalOverScroll   Cumulative value for current direction
   */
  public void onNestedVerticalOverScroll(CoordinatorLayout coordinatorLayout, V child, @ScrollDirection int direction, int currentOverScroll, int totalOverScroll) {}

  /**
   * @param scrollDirection {@link ScrollDirection}
   */
  public void onDirectionNestedPreScroll(CoordinatorLayout coordinatorLayout, V child, View target, int dx, int dy, int[] consumed, @ScrollDirection int scrollDirection) {}

  /**
   * @return true if the Behavior consumed the fling
   */
  protected boolean onNestedDirectionFling(CoordinatorLayout coordinatorLayout, V child, View target, float velocityX, float velocityY, @ScrollDirection int scrollDirection)
  {
    return false;
  }

}


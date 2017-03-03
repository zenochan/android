package name.zeno.android.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 16/8/17
 */
@SuppressWarnings("unused")
public class HideBehavior extends VerticalScrollingBehavior
{
  private ViewPropertyAnimatorCompat anim;
  private boolean hide = false;

  public HideBehavior(Context context, AttributeSet attrs)
  {
    super(context, attrs);
  }

  @Override
  public void onDirectionNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed, @ScrollDirection int scrollDirection)
  {
    showOrHideView(child, target, scrollDirection);
  }

  @Override
  protected boolean onNestedDirectionFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY, @ScrollDirection int scrollDirection)
  {
    showOrHideView(child, target, scrollDirection);
    return true;
  }

  private void showOrHideView(View child, View target, @ScrollDirection int direction)
  {
    boolean willHide = direction == ScrollDirection.SCROLL_DIRECTION_UP;
    if (this.hide == willHide) return;

    this.hide = willHide;

    if (anim == null) {
      anim = ViewCompat.animate(child);
      anim.setDuration(300);
      anim.setInterpolator(new LinearOutSlowInInterpolator());
    } else {
      anim.cancel();
    }
    anim.scaleX(direction == ScrollDirection.SCROLL_DIRECTION_UP ? 0 : 1)
        .scaleY(direction == ScrollDirection.SCROLL_DIRECTION_UP ? 0 : 1)
        .start();
  }

}

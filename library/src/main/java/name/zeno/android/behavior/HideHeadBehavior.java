package name.zeno.android.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 16/8/17
 */
public class HideHeadBehavior extends VerticalScrollingBehavior
{
  private static final String TAG = "name.zeno.android.behavior.HideHeadBehavior";

  private ViewPropertyAnimatorCompat anim;
  private boolean hide = false;

  public HideHeadBehavior()
  {
  }

  @Override public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection)
  {
//    int count = parent.getChildCount();
//    for (int i = 0; i < count; i++) {
//      View c = parent.getChildAt(i);
//      if (c instanceof NestedScrollingChild) {
//        c.setPadding(c.getPaddingLeft(), child.getMeasuredHeight(), c.getPaddingRight(), c.getPaddingBottom());
//        ((ViewGroup) c).setClipToPadding(false);
//        break;
//      }
//    }
    return super.onLayoutChild(parent, child, layoutDirection);
  }

  public HideHeadBehavior(Context context, AttributeSet attrs)
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
    return false;
  }

  private void showOrHideView(View view, View target, @ScrollDirection int direction)
  {
    boolean willHide = direction == ScrollDirection.SCROLL_DIRECTION_UP;
    if (this.hide == willHide) return;

    this.hide = willHide;

    if (anim == null) {
      anim = ViewCompat.animate(view);
      anim.setDuration(300);
      anim.setInterpolator(new LinearOutSlowInInterpolator());
    } else {
      anim.cancel();
    }
    anim.translationY(this.hide ? -view.getMeasuredHeight() : 0).start();

//    int pt = this.hide ? 0 : view.getMeasuredHeight();
//    target.setPadding(target.getPaddingLeft(), pt, target.getPaddingRight(), target.getPaddingBottom());
  }

}

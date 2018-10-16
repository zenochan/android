package name.zeno.android.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorCompat
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 16/8/17
 */
@Suppress("UNUSED_PARAMETER")
class HideHeadBehavior : VerticalScrollingBehavior<View> {

  private var anim: ViewPropertyAnimatorCompat? = null
  private var hide = false

  constructor() {}
  constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

  override fun onDirectionNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: View, target: View, dx: Int, dy: Int, consumed: IntArray, @ScrollDirection scrollDirection: Int) {
    showOrHideView(child, target, scrollDirection)
  }

  protected override fun onNestedDirectionFling(coordinatorLayout: CoordinatorLayout, child: View, target: View, velocityX: Float, velocityY: Float, @ScrollDirection scrollDirection: Int): Boolean {
    showOrHideView(child, target, scrollDirection)
    return false
  }

  private fun showOrHideView(view: View, target: View, @ScrollDirection direction: Int) {
    val willHide = direction == VerticalScrollingBehavior.SCROLL_DIRECTION_UP
    if (this.hide == willHide) return

    this.hide = willHide

    if (anim == null) {
      anim = ViewCompat.animate(view)
      anim?.duration = 300
      anim?.interpolator = LinearOutSlowInInterpolator()
    } else {
      anim?.cancel()
    }
    anim?.translationY((if (this.hide) -view.measuredHeight else 0).toFloat())?.start()

    //    int pt = this.hide ? 0 : view.getMeasuredHeight();
    //    target.setPadding(target.getPaddingLeft(), pt, target.getPaddingRight(), target.getPaddingBottom());
  }

}

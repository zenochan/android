package name.zeno.android.behavior

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPropertyAnimatorCompat
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.util.AttributeSet
import android.view.View

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 16/8/17
 */
class HideBehavior(context: Context, attrs: AttributeSet) : VerticalScrollingBehavior<View>(context, attrs) {
  private var anim: ViewPropertyAnimatorCompat? = null
  private var hide = false

  override fun onDirectionNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: View, target: View, dx: Int, dy: Int, consumed: IntArray, @ScrollDirection scrollDirection: Int) {
    showOrHideView(child, target, scrollDirection)
  }

  protected override fun onNestedDirectionFling(coordinatorLayout: CoordinatorLayout, child: View, target: View, velocityX: Float, velocityY: Float, @ScrollDirection scrollDirection: Int): Boolean {
    showOrHideView(child, target, scrollDirection)
    return true
  }

  private fun showOrHideView(child: View, target: View, @ScrollDirection direction: Int) {
    val willHide = direction == VerticalScrollingBehavior.SCROLL_DIRECTION_UP
    if (this.hide == willHide) return

    this.hide = willHide

    if (anim == null) {
      anim = ViewCompat.animate(child)
      anim!!.duration = 300
      anim!!.interpolator = LinearOutSlowInInterpolator()
    } else {
      anim!!.cancel()
    }
    anim!!.scaleX((if (direction == VerticalScrollingBehavior.SCROLL_DIRECTION_UP) 0 else 1).toFloat())
        .scaleY((if (direction == VerticalScrollingBehavior.SCROLL_DIRECTION_UP) 0 else 1).toFloat())
        .start()
  }

}

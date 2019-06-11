package cn.izeno.android.behavior

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
class HideFooterBehavior(context: Context, attrs: AttributeSet) : VerticalScrollingBehavior<View>(context, attrs) {
  private var anim: ViewPropertyAnimatorCompat? = null
  private var hide = false

  override fun onDirectionNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: View, target: View, dx: Int, dy: Int, consumed: IntArray, @ScrollDirection scrollDirection: Int) {
    showOrHideView(child, target, scrollDirection)
  }

  override fun onNestedDirectionFling(coordinatorLayout: CoordinatorLayout, child: View, target: View, velocityX: Float, velocityY: Float, @ScrollDirection scrollDirection: Int): Boolean {
    showOrHideView(child, target, scrollDirection)
    return true
  }

  private fun showOrHideView(child: View, target: View, @ScrollDirection direction: Int) {
    val hide = direction == VerticalScrollingBehavior.SCROLL_DIRECTION_UP
    if (this.hide == hide) return

    this.hide = hide

    if (anim == null) {
      anim = ViewCompat.animate(child)
      anim!!.duration = 300
      anim!!.interpolator = LinearOutSlowInInterpolator()
    } else {
      anim!!.cancel()
    }
    anim!!.translationY((if (hide) child.measuredHeight else 0).toFloat()).start()
  }

}

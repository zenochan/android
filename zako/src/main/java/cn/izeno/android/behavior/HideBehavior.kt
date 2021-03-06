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
class HideBehavior(context: Context, attrs: AttributeSet) : VerticalScrollingBehavior<View>(context, attrs) {
  private var anim: ViewPropertyAnimatorCompat? = null
  private var hide = false

  override fun onDirectionNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: View, target: View, dx: Int, dy: Int, consumed: IntArray, @ScrollDirection scrollDirection: Int) {
    showOrHideView(child, scrollDirection)
  }

  override fun onNestedDirectionFling(coordinatorLayout: CoordinatorLayout, child: View, target: View, velocityX: Float, velocityY: Float, @ScrollDirection scrollDirection: Int): Boolean {
    showOrHideView(child, scrollDirection)
    return true
  }

  private fun showOrHideView(child: View, @ScrollDirection direction: Int) {
    val hide = direction == VerticalScrollingBehavior.SCROLL_DIRECTION_UP
    if (this.hide == hide) return

    this.hide = hide

    if (anim == null) {
      anim = ViewCompat.animate(child).apply {
        duration = 300
        interpolator = LinearOutSlowInInterpolator()
      }
    } else {
      anim?.cancel()
    }
    val scale = (if (hide) 0 else 1).toFloat()
    anim!!.scaleX(scale).scaleY(scale).start()
  }
}

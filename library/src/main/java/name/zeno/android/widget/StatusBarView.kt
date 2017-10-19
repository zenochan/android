package name.zeno.android.widget

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

import name.zeno.android.system.ZStatusBar

/**
 * @author 陈治谋 (微信: puppet2436)
 * @since 2017/4/20
 */
class StatusBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = -1
) : View(context, attrs, defStyleAttr) {

  private val statusBarSize = ZStatusBar.getStatusBarHeight(context)


  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    setMeasuredDimension(measuredWidth, statusBarSize)
  }
}

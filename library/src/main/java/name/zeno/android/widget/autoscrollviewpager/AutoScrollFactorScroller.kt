package name.zeno.android.widget.autoscrollviewpager

import android.content.Context
import android.view.animation.Interpolator
import android.widget.Scroller

class AutoScrollFactorScroller : Scroller {

  /** 滚动时间因子 */
  var factor = 1.0

  constructor(context: Context) : super(context)
  constructor(context: Context, interpolator: Interpolator) : super(context, interpolator)

  override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
    super.startScroll(startX, startY, dx, dy, (duration * factor).toInt())
  }
}

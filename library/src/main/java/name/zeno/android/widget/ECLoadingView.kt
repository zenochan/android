package name.zeno.android.widget

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import name.zeno.android.util.max
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.dip

/**
 * # 能量守恒撞击球(energy	conservation loading view)
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/11/15
 */
class ECLoadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : View(context, attrs, defStyle) {
  var debug = true

  private val anim: ValueAnimator = ValueAnimator.ofFloat(-1F, 1F)
  private var animatedValue = 0F

  private val paint = Paint()

  private var rCenter = dip(12).toFloat()
  private var rSide = dip(6).toFloat()
  private var distance = dip(24)

  init {
    anim.duration = 800
    anim.repeatMode = ValueAnimator.REVERSE
    anim.repeatCount = -1
    anim.interpolator = TimeInterpolator {
      when {
        it < 0.5 -> it * it * 2
        else -> 1 - (1 - it) * (1 - it) * 2
      }
    }
    anim.addUpdateListener {
      animatedValue = it.animatedValue as Float
      invalidate()
    }
    anim.start()

    paint.isAntiAlias = true
    paint.color = Color.RED
    paint.style = Paint.Style.FILL

    if (debug) {
      backgroundColor = Color.LTGRAY
    }
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    val cy = (height / 2).toFloat()
    var l = width / 2 - rCenter - rSide
    var r = width / 2 + rCenter + rSide
    if (animatedValue < 0) {
      l += distance * animatedValue
    } else {
      r += distance * animatedValue
    }

    paint.style = Paint.Style.STROKE
    canvas.drawCircle(width / 2.toFloat(), cy, rCenter, paint)
    paint.style = Paint.Style.FILL
    canvas.drawCircle(l, cy, rSide, paint)
    canvas.drawCircle(r, cy, rSide, paint)
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    val w = max(measuredWidth, (rCenter * 2 + rSide * 4 * 3).toInt())
    val h = max(measuredHeight, (rCenter * 2).toInt())
    setMeasuredDimension(w, h)
  }
}
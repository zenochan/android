package name.zeno.android.widget

import android.animation.Animator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import name.zeno.android.core.bezier
import org.jetbrains.anko.dip

/**
 * # 底线
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/11/16
 */
class EndLineView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : View(context, attrs, defStyle) {
  private val paint = Paint(Paint.ANTI_ALIAS_FLAG and Paint.DITHER_FLAG)
  private val path = Path()

  private val anim = ValueAnimator.ofFloat(0F, 1F)
  private var animatedValue = 0f

  private var start = PointF()
  private var controlA = PointF()
  private var controlB = PointF()
  private var end = PointF()

  init {
    anim.repeatCount = -1
    anim.duration = 1000
    anim.repeatMode = ValueAnimator.REVERSE
    anim.interpolator = TimeInterpolator {
      when {
        it < 0.5 -> it * it * 2
        else -> 1 - (1 - it) * (1 - it) * 2
      }
    }
    anim.addUpdateListener {
      this.animatedValue = it.animatedFraction
      invalidate()
    }
    anim.start()


    paint.textSize = dip(12).toFloat()
    paint.color = Color.LTGRAY
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)

    paint.color = Color.LTGRAY
    paint.style = Paint.Style.STROKE
    // 绘制底线
    val oneThirdH = height / 3F
    val oneThirdW = width / 3F
    path.reset()
    path.moveTo(0F, 0F)
    path.cubicTo(oneThirdW, oneThirdH * 4, oneThirdW * 2, oneThirdH * 4, width.toFloat(), 0F)
    paint.style = Paint.Style.STROKE
    canvas.drawPath(path, paint)

    start.set(0F, 0F)
    end.set(width.toFloat(), 0F)
    controlA.set(oneThirdW, oneThirdH * 4)
    controlB.set(oneThirdW * 2, oneThirdH * 4)

    paint.color = Color.GRAY
    paint.style = Paint.Style.FILL
    val a = animatedValue * 0.2F + 0.4F
    val target = bezier(a, start, controlA, controlB, end)
    canvas.drawCircle(target.x, target.y - dip(6), dip(6).toFloat(), paint)

    paint.color = Color.LTGRAY
    val dp4 = dip(4)
    canvas.drawText("底", 0F + dp4, height.toFloat() - dp4, paint)
    canvas.drawText("线", width - paint.textSize - dp4, height.toFloat() - dp4, paint)
  }
}
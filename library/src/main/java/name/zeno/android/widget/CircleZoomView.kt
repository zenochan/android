package name.zeno.android.widget

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import name.zeno.android.anim.InterpolationObservableAnimation


/**
 * Create Date: 15/10/22
 *
 * @author 陈治谋 (513500085@qq.com)
 */
class CircleZoomView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

  private var stroke = 1f
  private var smallR = 4f
  private var minR = 4f
  private var maxR = 16f
  private var paint: Paint? = null
  @ColorInt
  private var color = DEFAULT_COLOR


  private var interpolationAnim: InterpolationObservableAnimation? = null
  private var status: Int = 0
  private var interpolation: Float = 0.toFloat()

  internal interface Status {
    companion object {
      val SMALL = 0 //小的
      val ZOOM_IN = 1 //放大中
      val ZOOM_OUT = 2 //缩小中
      val BIG = 3 //大的
    }
  }

  init {
    initializeView()
  }

  fun zoomIn() {
    if (status == Status.SMALL || status == Status.ZOOM_OUT) {
      status = Status.ZOOM_IN
      startAnimation(interpolationAnim)
    }
  }

  fun setSmall() {
    clearAnimation()
    status = Status.SMALL
    invalidate()
  }

  fun zoomOut() {
    if (status == Status.BIG || status == Status.ZOOM_IN) {
      status = Status.ZOOM_OUT
      startAnimation(interpolationAnim)
    }
  }

  fun setBig() {
    clearAnimation()
    status = Status.BIG
    invalidate()
  }

  fun toggle() {
    if (status == Status.SMALL) {
      zoomIn()
    } else if (status == Status.BIG) {
      zoomOut()
    }
  }

  fun setColor(@ColorInt color: Int) {
    this.color = color
    paint!!.color = color
    invalidate()
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val w = ((maxR + stroke) * 2).toInt()
    setMeasuredDimension(w, w)
  }

  override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
    super.onLayout(changed, left, top, right, bottom)
  }

  override fun onDraw(canvas: Canvas) {
    var r = 0f
    when (status) {
      Status.SMALL -> r = minR
      Status.ZOOM_IN -> r = minR + interpolation * (maxR - minR)
      Status.ZOOM_OUT -> r = maxR - interpolation * (maxR - minR)
      Status.BIG -> r = maxR
    }
    paint!!.style = Paint.Style.FILL
    canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), smallR, paint!!)
    paint!!.style = Paint.Style.STROKE
    canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), r, paint!!)
  }

  override fun onAnimationEnd() {
    super.onAnimationEnd()
    if (status == Status.ZOOM_IN) {
      status = Status.BIG
    } else if (status == Status.ZOOM_OUT) {
      status = Status.SMALL
    }
    interpolation = 0f
  }


  private fun initializeView() {
    density = Resources.getSystem().displayMetrics.density
    stroke *= density
    smallR *= density
    maxR *= density
    minR *= density

    paint = Paint()
    paint!!.color = color
    paint!!.style = Paint.Style.FILL
    paint!!.isAntiAlias = true
    paint!!.strokeWidth = 1 * density

    status = Status.BIG
    interpolation = 0f
    interpolationAnim = InterpolationObservableAnimation()
    interpolationAnim?.duration = 300
    interpolationAnim?.onInterpolatedListener = { interpolatedTime ->
      interpolation = interpolatedTime
      this@CircleZoomView.invalidate()
    }
  }

  companion object {

    private val TAG = "CircleView"

    private val DEFAULT_COLOR = -0xb24601

    private var density: Float = 0.toFloat()
  }

}


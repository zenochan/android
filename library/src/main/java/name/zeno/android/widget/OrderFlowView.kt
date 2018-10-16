package name.zeno.android.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import name.zeno.android.util.R
import name.zeno.android.util.ZDimen

/**
 * Create Date: 16/6/22
 *
 * @author 陈治谋 (513500085@qq.com)
 */
class OrderFlowView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
  @ColorInt
  private var colorCircle = Color.parseColor("#437af7")
  @ColorInt private var colorLine = Color.parseColor("#9e9e9e")
  private var isStart = false
  private var isEnd = false
  private var radius = ZDimen.dp2px(4f)
  private val lineWidth = ZDimen.dp2px(1f)
  private var cY = ZDimen.dp2px(16f)

  private var rightLineEnable = false
  private var rightLineWidth = ZDimen.dp2px(32f)

  private var paint: Paint? = null

  init {
    init(attrs)
  }

  fun setColorCircle(colorCircle: Int) {
    this.colorCircle = colorCircle
    invalidate()
  }

  fun setColorCircleRes(@ColorRes resId: Int) {
    this.colorCircle = ContextCompat.getColor(context, resId)
    invalidate()
  }

  fun setRadius(radiusPx: Int) {
    this.radius = radiusPx
  }

  fun setColorLine(@ColorInt colorLine: Int) {
    this.colorLine = colorLine
    invalidate()
  }

  fun setColorLineRes(@ColorRes colorRes: Int) {
    this.colorLine = ContextCompat.getColor(context, colorRes)
    invalidate()
  }


  fun setEnd(end: Boolean) {
    isEnd = end
    invalidate()
  }

  fun setStart(start: Boolean) {
    isStart = start
    invalidate()
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    var w = View.MeasureSpec.getSize(widthMeasureSpec)
    var h = View.MeasureSpec.getSize(heightMeasureSpec)
    val wm = View.MeasureSpec.getMode(widthMeasureSpec)
    val hm = View.MeasureSpec.getMode(heightMeasureSpec)

    when (wm) {
      View.MeasureSpec.AT_MOST -> if (w > dp32) {
        w = dp32
      }
      View.MeasureSpec.UNSPECIFIED -> if (w < ZDimen.dp2px(32f)) {
        w = dp32
      }
      View.MeasureSpec.EXACTLY -> if (w < ZDimen.dp2px(8f)) {
        w = ZDimen.dp2px(8f)
      }
    }

    if (rightLineEnable) {
      w += rightLineWidth - w / 2
    }

    when (hm) {
      View.MeasureSpec.AT_MOST, View.MeasureSpec.UNSPECIFIED -> if (h < ZDimen.dp2px(32f)) {
        h = ZDimen.dp2px(32f)
      }
      View.MeasureSpec.EXACTLY -> if (h < ZDimen.dp2px(8f)) {
        h = ZDimen.dp2px(8f)
      }
    }

    setMeasuredDimension(w, h)
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    val width = width
    val height = height

    paint!!.style = Paint.Style.FILL

    //int lineTop = isStart ? cY : 0;
    val lineTop = 0
    val lineBottom = if (isEnd) cY else height
    paint!!.color = colorLine
    paint!!.strokeWidth = lineWidth.toFloat()

    val x = if (rightLineEnable) width - rightLineWidth else width / 2

    canvas.drawLine(x.toFloat(), lineTop.toFloat(), x.toFloat(), lineBottom.toFloat(), paint!!)
    paint!!.color = colorCircle
    canvas.drawCircle(x.toFloat(), cY.toFloat(), radius.toFloat(), paint!!)


    if (rightLineEnable) {
      canvas.drawLine(x.toFloat(), cY.toFloat(), width.toFloat(), cY.toFloat(), paint!!)
    }

    if (isStart) {
      paint!!.style = Paint.Style.STROKE
      canvas.drawCircle(x.toFloat(), cY.toFloat(), (radius + ZDimen.dp2px(2f)).toFloat(), paint!!)
    }
  }

  private fun init(attrs: AttributeSet?) {
    paint = Paint()
    paint!!.isAntiAlias = true

    val ta = context.obtainStyledAttributes(attrs, R.styleable.OrderFlowView)
    cY = ta.getDimensionPixelSize(R.styleable.OrderFlowView_topHeight, cY)
    rightLineEnable = ta.getBoolean(R.styleable.OrderFlowView_rightLineEnable, rightLineEnable)
    rightLineWidth = ta.getDimensionPixelSize(R.styleable.OrderFlowView_rightLineWidth, rightLineWidth)
    ta.recycle()
  }

  companion object {

    private val dp32 = ZDimen.dp2px(32f)
  }

}

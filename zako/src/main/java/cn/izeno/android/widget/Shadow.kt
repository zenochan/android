package cn.izeno.android.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.annotation.IntDef
import cn.izeno.android.util.R
import cn.izeno.android.util.ZDimen

/**
 * @author 陈治谋 (微信: puppet2436)
 * @since 2017/4/15
 */
class Shadow @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

  private lateinit var paint: Paint
  private lateinit var path: Path

  var cornersRadius = ZDimen.dp2px(4f)
  var color = Color.WHITE

  private val rect = RectF()
  var mode = CORNERS
    set(value) {
      field = value
      invalidate()
    }

  @ColorInt
  var borderColor = 0
  @Dimension
  var borderWidth = 0

  companion object {
    const val TRIANGLE = 0         // 三角形
    const val BEZIER = 1           // 贝塞尔
    const val CORNERS = 2          // 圆角
    const val CIRCLE = 3           // 圆形
  }

  @IntDef(BEZIER, TRIANGLE)
  internal annotation class Mode

  init {
    init(context, attrs)
  }


  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    val h = measuredHeight.toFloat()
    val w = measuredWidth.toFloat()
    path.reset()
    paint.color = color
    paint.style = Paint.Style.FILL
    when (mode) {
      BEZIER -> {
        path.fillType = Path.FillType.WINDING
        path.moveTo(0f, h)
        path.quadTo((w / 2), -h, w, h)
        canvas.drawPath(path, paint)
      }
      CORNERS -> {
        // 编辑模式时 Path.FillType.INVERSE_WINDING 不生效
        if (isInEditMode) return

        rect.set(0f, 0f, w, h)
        path.fillType = Path.FillType.INVERSE_WINDING
        path.addRoundRect(rect, cornersRadius.toFloat(), cornersRadius.toFloat(), Path.Direction.CW)
        canvas.drawPath(path, paint)
        if (borderWidth > 0) {
          val halfW = (borderWidth - 1) / 2F
          rect.set(halfW, halfW, (w - halfW), (h - halfW))
          paint.strokeWidth = borderWidth.toFloat()
          paint.color = borderColor
          paint.style = Paint.Style.STROKE
          path.fillType = Path.FillType.WINDING
          path.reset()
          path.addRoundRect(rect, cornersRadius - halfW, cornersRadius - halfW, Path.Direction.CW)
          canvas.drawPath(path, paint)
        }
      }
      CIRCLE -> {
        val halfW = (borderWidth + 0.5F) / 2
        rect.set(0f + halfW, 0f + halfW, w - halfW, h - halfW)
        paint.style = Paint.Style.STROKE
        paint.color = borderColor
        paint.strokeWidth = borderWidth.toFloat()
        canvas.drawOval(rect, paint)
      }

      else -> {
        path.fillType = Path.FillType.WINDING
        path.moveTo(0f, h)
        path.lineTo((w / 2), 0f)
        path.lineTo(w, h)
        canvas.drawPath(path, paint)
      }
    }
  }

  internal fun init(context: Context, attrs: AttributeSet?) {
    paint = Paint()
    paint.isAntiAlias = true
    path = Path()

    if (attrs != null) {
      val ta = context.obtainStyledAttributes(attrs, R.styleable.Shadow)
      if (ta.hasValue(R.styleable.Shadow_colorShadow)) {
        color = ta.getColor(R.styleable.Shadow_colorShadow, color)
      }
      if (ta.hasValue(R.styleable.Shadow_mode)) {
        mode = ta.getInt(R.styleable.Shadow_mode, mode)
      }
      cornersRadius = ta.getDimensionPixelSize(R.styleable.Shadow_radius, cornersRadius)
      borderWidth = ta.getDimensionPixelSize(R.styleable.Shadow_borderWidth, borderWidth)
      borderColor = ta.getColor(R.styleable.Shadow_borderColor, borderColor)
      ta.recycle()
    }
  }
}

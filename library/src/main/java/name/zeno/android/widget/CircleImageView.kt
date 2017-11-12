package name.zeno.android.widget

import android.content.Context
import android.graphics.*
import android.support.annotation.IntDef
import android.util.AttributeSet
import name.zeno.android.util.R
import name.zeno.android.util.R.styleable.*

/**
 * ## ImageView 圆形遮罩
 *
 * - padding       圆形边框的宽度
 *
 * - [CircleImageView_mask_color]    圆形的背景色
 * - [CircleImageView_circle_color]  圆形边框颜色
 */
class CircleImageView @JvmOverloads constructor(
    private var mContext: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ZImageView(mContext, attrs, defStyle) {

  private lateinit var paint: Paint
  private var path: Path = Path()
  private var init = false
  private val rect = RectF()

  private var borderColor: Int = 0
  private var borderWidth: Float = 0.toFloat()
  private var maskColor: Int = 0

  @Mode
  private var mode = OVERDRAW

  fun setBorderColor(borderColor: Int) {
    this.borderColor = borderColor
  }

  fun setBorderWidth(borderWidth: Float) {
    this.borderWidth = borderWidth
  }

  fun setMaskColor(maskColor: Int) {
    this.maskColor = maskColor
  }

  fun setMode(mode: Int) {
    this.mode = mode
  }


  companion object {
    const val OVERDRAW = 0   // 覆盖绘制
    const val CLIP = 1   // 裁剪画布
  }

  @IntDef(OVERDRAW.toLong(), CLIP.toLong()) annotation class Mode()

  init {
    init(mContext, attrs)
  }


  private fun init(context: Context, attrs: AttributeSet?) {
    mContext = context
    borderWidth = 0f

    if (null != attrs) {
      val ta = mContext.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
      borderColor = ta.getColor(R.styleable.CircleImageView_circle_color, Color.GRAY)
      maskColor = ta.getColor(R.styleable.CircleImageView_mask_color, Color.WHITE)
      if (ta.hasValue(R.styleable.CircleImageView_clipMode)) {

        mode = ta.getInt(R.styleable.CircleImageView_clipMode, OVERDRAW)
      }
      ta.recycle()
    } else {
      borderColor = Color.GRAY
      maskColor = Color.WHITE
    }
  }


  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    if (!init) {
      initPaint()
    }
  }

  override fun onDraw(canvas: Canvas) {
    val w = width
    val h = height
    if (mode == CLIP) {
      if (layerType != LAYER_TYPE_SOFTWARE) {
        // View 级关闭硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null)
      }

      // 裁剪画布
      path.reset()
      rect.set(0f, 0f, w.toFloat(), h.toFloat())
//      path.addArc(rect, 0f, 360f)
      path.addOval(rect, Path.Direction.CW)
//      canvas.clipPath(path, Region.Op.REPLACE)
      canvas.save()
      canvas.clipPath(path)
    }

    super.onDraw(canvas)

    if (mode == CLIP) {
      canvas.restore()
    }
  }

  override fun dispatchDraw(canvas: Canvas) {
    val w = width
    val h = height

    if (mode == OVERDRAW) {
      //draw mask
      paint.color = maskColor
      paint.style = Paint.Style.FILL
      val radius = w / 2.toFloat()
      path.reset()
      path.moveTo(0f, radius)
      path.lineTo(0f, 0f)
      path.lineTo(w.toFloat(), 0f)
      path.lineTo(w.toFloat(), h.toFloat())
      path.lineTo(0f, h.toFloat())
      path.lineTo(0f, radius)
      rect.set(0f, 0f, w.toFloat(), h.toFloat())
      path.arcTo(rect, 180f, -359f, true)
      path.close()
      canvas.drawPath(path, paint)
    }


    if (borderWidth > 0) {
      // draw border ，画一个椭圆
      paint.color = borderColor
      paint.style = Paint.Style.STROKE
      paint.strokeWidth = borderWidth
      val halfW = (borderWidth / 2).toInt()
      rect.set(halfW.toFloat(), halfW.toFloat(), (w - halfW).toFloat(), (h - halfW).toFloat())
      canvas.drawOval(rect, paint)
    }

    super.dispatchDraw(canvas)
  }

  private fun initPaint() {
    if (borderWidth <= 0) {
      borderWidth = paddingLeft.toFloat()
    }
    setPadding(0, 0, 0, 0)
    paint = Paint()
    paint.style = Paint.Style.FILL
    paint.color = Color.WHITE
    paint.isAntiAlias = true
    init = true
  }
}


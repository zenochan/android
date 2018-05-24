package name.zeno.android.camera

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import name.zeno.android.util.MeasureUtils
import name.zeno.android.util.R
import name.zeno.android.util.ZDimen

/**
 * Create Date: 16/6/19
 *
 * @author 陈治谋 (513500085@qq.com)
 */
class PreviewBorderView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

  private var screenH: Int = 0
  private var screenW: Int = 0

  private val rectF = RectF()
  private lateinit var paint: Paint
  private lateinit var paintLine: Paint

  private var tipTextSize: Float = 0.toFloat()
  private var tipTextColor: Int = 0
  private var tipText: String? = null

  init {
    initAttrs(context, attrs)
    init()
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)

    //灰色底色遮罩
    canvas.drawARGB(100, 0, 0, 0)
    screenW = width
    screenH = height
    val unit = (screenH / 3).toFloat()
    val l: Float
    val t: Float
    val r: Float
    val b: Float
    l = screenW * 0.5f - unit * 1.5f
    t = unit * 0.5f
    r = screenW * 0.5f + unit * 1.5f
    b = screenH - unit * 0.5f


    //中间去除遮罩
    rectF.set(l, t, r, b)
    canvas.drawRect(rectF, paint)

    //四个角
    canvas.drawLine(l, t, l, t + 50, paintLine)
    canvas.drawLine(l, t, l + 50, t, paintLine)
    canvas.drawLine(r, t, r, t + 50, paintLine)
    canvas.drawLine(r, t, r - 50, t, paintLine)
    canvas.drawLine(l, b, l, b - 50, paintLine)
    canvas.drawLine(l, b, l + 50, b, paintLine)
    canvas.drawLine(r, b, r, b - 50, paintLine)
    canvas.drawLine(r, b, r - 50, b, paintLine)

    //提示文本
    paintLine.textSize = tipTextSize
    paintLine.isAntiAlias = true
    paintLine.isDither = true
    val length = paintLine.measureText(tipText)
    canvas.drawText(tipText!!, screenW / 2 - length / 2, unit * 0.5f - tipTextSize, paintLine)
  }


  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val size = MeasureUtils.as4Rate3(widthMeasureSpec, heightMeasureSpec)
    setMeasuredDimension(size.x, size.y)
  }

  private fun initAttrs(context: Context, attrs: AttributeSet?) {
    val ta = context.obtainStyledAttributes(attrs, R.styleable.PreviewBorderView)
    if (ta.hasValue(R.styleable.PreviewBorderView_tipText)) {
      tipText = ta.getString(R.styleable.PreviewBorderView_tipText)
    } else {
      tipText = DEFAULT_TIPS_TEXT
    }
    tipTextColor = ta.getColor(R.styleable.PreviewBorderView_tipTextColor, DEFAULT_TIPS_TEXT_COLOR)
    tipTextSize = ta.getDimensionPixelSize(R.styleable.PreviewBorderView_tipTextSize, DEFAULT_TIPS_TEXT_SIZE).toFloat()

    ta.recycle()

  }


  private fun init() {
    paint = Paint()
    paint.isAntiAlias = true
    paint.color = Color.WHITE
    paint.style = Paint.Style.FILL_AND_STROKE
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)

    paintLine = Paint()
    paintLine.color = tipTextColor
    paintLine.strokeWidth = 3.0f
    keepScreenOn = true
  }

  companion object {
    private val DEFAULT_TIPS_TEXT = "请将方框对准证件拍摄"
    private val DEFAULT_TIPS_TEXT_SIZE = ZDimen.dp2px(16f)
    private val DEFAULT_TIPS_TEXT_COLOR = Color.GREEN
  }
}

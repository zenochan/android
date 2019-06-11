package cn.izeno.android.widget


import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Style
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

class ClipBorderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {
  /**
   * 水平方向与View的边距
   */
  private var mHorizontalPadding: Int = 0
  /**
   * 边框的宽度 单位dp
   */
  private var mBorderWidth = 2

  private val clearPaint: Paint
  private val circlePaint: Paint

  init {
    mBorderWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mBorderWidth.toFloat(), resources.displayMetrics).toInt()
    setLayerType(View.LAYER_TYPE_SOFTWARE, null)

    clearPaint = Paint()
    clearPaint.isAntiAlias = true
    clearPaint.alpha = 0xFF
    clearPaint.color = Color.TRANSPARENT
    clearPaint.style = Paint.Style.FILL_AND_STROKE
    clearPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)

    circlePaint = Paint()
    circlePaint.isAntiAlias = true
    circlePaint.color = Color.parseColor("#FFFFFF")
    circlePaint.strokeWidth = mBorderWidth.toFloat()
    circlePaint.style = Style.STROKE
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)

    val w = width
    val h = height

    //灰色底色遮罩
    canvas.drawARGB(200, 0, 0, 0)

    //中间去除遮罩
    canvas.drawCircle((w / 2).toFloat(), (h / 2).toFloat(), (w / 2 - mHorizontalPadding).toFloat(), clearPaint)

    // 绘制边框
    //方形边框
    //		canvas.drawRect(mHorizontalPadding, mVerticalPadding, getWidth()- mHorizontalPadding, getHeight() - mVerticalPadding, circlePaint);
    //圆形边框
    canvas.drawCircle((w / 2).toFloat(), (h / 2).toFloat(), (w / 2 - mHorizontalPadding).toFloat(), circlePaint)

  }

  fun setHorizontalPadding(mHorizontalPadding: Int) {
    this.mHorizontalPadding = mHorizontalPadding
  }

}

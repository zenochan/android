package cn.izeno.android.widget.text

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.text.style.ImageSpan
import androidx.annotation.ColorInt

/**
 * <pre>
 * SpannableStringBuilder ssb = new SpannableStringBuilder();
 * SpannableString string    = new SpannableString("热销");
 * TagImageSpan    imageSpan = new TagImageSpan(ZDimen.sp2px(12), ZDimen.sp2px(4));
 * imageSpan.setBackgroundColor(Color.parseColor("#008d4c"));
 * imageSpan.setLineExtra(ZDimen.sp2px(4));
 * string.setSpan(imageSpan, 0, string.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
 * ssb.append(string);
</pre> *
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/12/20.
 */
class TagImageSpan @JvmOverloads constructor(
    private val expandWidth: Int = 0,
    private val expandHeight: Int = 0
) : ImageSpan(TagImageSpan.shape) {

  @ColorInt
  private var textColor = Color.WHITE

  private var backgroundColor = Color.BLUE

  private var lineExtra = 0

  companion object {
    /**
     * 生成shape 可以通过xml实现
     */
    private val shape: GradientDrawable
      get() {
        val drawable = GradientDrawable()
        drawable.cornerRadius = 10f
        drawable.setColor(Color.parseColor("#d8d8d8"))
        drawable.setStroke(1, Color.parseColor("#b2b2b2"))
        return drawable
      }
  }

  fun setBackgroundColor(@ColorInt backgroundColor: Int) {
    this.backgroundColor = backgroundColor
    val drawable = drawable as GradientDrawable
    drawable.setColor(backgroundColor)
  }

  override fun draw(canvas: Canvas, text: CharSequence, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, paint: Paint) {
    val width = getWidth(text, start, end, paint)
    val height = getHeight(paint)
    drawable.setBounds(0, 0, width, height)
    var bgBottom = (bottom + expandHeight.toDouble() / 2.0 / 1.3).toInt()//使得在垂直方向居中
    if (bottom > height) {
      bgBottom -= lineExtra
    }
    super.draw(canvas, text, start, end, x, top, y, bgBottom, paint)

    paint.color = textColor
    canvas.drawText(text.subSequence(start, end).toString(), x + expandWidth * 0.5f, y.toFloat(), paint)
  }


  override fun getSize(paint: Paint, text: CharSequence, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
    return getWidth(text, start, end, paint)
  }

  /**
   * 计算span的宽度
   */
  private fun getWidth(text: CharSequence, start: Int, end: Int, paint: Paint): Int {
    return Math.round(paint.measureText(text, start, end)) + expandWidth
  }

  /**
   * 计算span的高度
   */
  private fun getHeight(paint: Paint): Int {
    val fm = paint.fontMetrics
    return Math.ceil((fm.descent - fm.ascent).toDouble()).toInt() + expandHeight
  }

  fun setTextColor(@ColorInt textColor: Int) {
    this.textColor = textColor
  }

  fun setLineExtra(lineExtra: Int) {
    this.lineExtra = lineExtra
  }
}

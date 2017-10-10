package name.zeno.android.widget.text;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.text.style.ImageSpan;


/**
 * <pre>
 * SpannableStringBuilder ssb = new SpannableStringBuilder();
 * SpannableString string    = new SpannableString("热销");
 * TagImageSpan    imageSpan = new TagImageSpan(ZDimen.sp2px(12), ZDimen.sp2px(4));
 * imageSpan.setBackgroundColor(Color.parseColor("#008d4c"));
 * imageSpan.setLineExtra(ZDimen.sp2px(4));
 * string.setSpan(imageSpan, 0, string.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
 * ssb.append(string);
 * </pre>
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/12/20.
 */
public class TagImageSpan extends ImageSpan
{
  private int expandWidth  = 0; //设置之后可能会出现显示不全的问题，可通过TextView的 padding解决
  private int expandHeight = 0;//设置之后可能会出现显示不全的问题，可通过TextView的 padding解决

  @ColorInt
  private int textColor = Color.WHITE;

  private int backgroundColor = Color.BLUE;

  private int lineExtra = 0;


  public void setTextColor(int textColor)
  {
    this.textColor = textColor;
  }

  public void setLineExtra(int lineExtra)
  {
    this.lineExtra = lineExtra;
  }

  public TagImageSpan()
  {
    this(0, 0);
  }

  public TagImageSpan(int expandWidth, int expandHeight)
  {
    super(getShape());
    this.expandWidth = expandWidth;
    this.expandHeight = expandHeight;
  }

  public void setBackgroundColor(@ColorInt int backgroundColor)
  {
    this.backgroundColor = backgroundColor;
    GradientDrawable drawable = (GradientDrawable) getDrawable();
    drawable.setColor(backgroundColor);
  }

  @Override
  public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint)
  {
    int width  = getWidth(text, start, end, paint);
    int height = getHeight(paint);
    getDrawable().setBounds(0, 0, width, height);
    int bgBottom = (int) (bottom + expandHeight / 2 / 1.3);//使得在垂直方向居中
    if (bottom > height) {
      bgBottom -= lineExtra;
    }
    super.draw(canvas, text, start, end, x, top, y, bgBottom, paint);

    paint.setColor(textColor);
    canvas.drawText(text.subSequence(start, end).toString(), x + (expandWidth * 0.5F), y, paint);
  }


  @Override
  public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm)
  {
    return getWidth(text, start, end, paint);
  }

  /**
   * 计算span的宽度
   */
  private int getWidth(CharSequence text, int start, int end, Paint paint)
  {
    return Math.round(paint.measureText(text, start, end)) + expandWidth;
  }

  /**
   * 计算span的高度
   */
  private int getHeight(Paint paint)
  {
    Paint.FontMetrics fm = paint.getFontMetrics();
    return (int) Math.ceil(fm.descent - fm.ascent) + expandHeight;
  }

  /**
   * 生成shape 可以通过xml实现
   */
  private static GradientDrawable getShape()
  {
    GradientDrawable drawable = new GradientDrawable();
    drawable.setCornerRadius(10);
    drawable.setColor(Color.parseColor("#d8d8d8"));
    drawable.setStroke(1, Color.parseColor("#b2b2b2"));
    return drawable;
  }
}

package name.zeno.android.widget;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class ClipBorderView extends View
{
  /**
   * 水平方向与View的边距
   */
  private int mHorizontalPadding;
  /**
   * 边框的宽度 单位dp
   */
  private int mBorderWidth = 2;

  private Paint clearPaint;
  private Paint circlePaint;

  public ClipBorderView(Context context)
  {
    this(context, null);
  }

  public ClipBorderView(Context context, AttributeSet attrs)
  {
    this(context, attrs, 0);
  }

  public ClipBorderView(Context context, AttributeSet attrs, int defStyle)
  {
    super(context, attrs, defStyle);

    mBorderWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mBorderWidth, getResources().getDisplayMetrics());
    setLayerType(LAYER_TYPE_SOFTWARE, null);

    clearPaint = new Paint();
    clearPaint.setAntiAlias(true);
    clearPaint.setAlpha(0xFF);
    clearPaint.setColor(Color.TRANSPARENT);
    clearPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

    circlePaint = new Paint();
    circlePaint.setAntiAlias(true);
    circlePaint.setColor(Color.parseColor("#FFFFFF"));
    circlePaint.setStrokeWidth(mBorderWidth);
    circlePaint.setStyle(Style.STROKE);
  }


  @Override
  protected void onDraw(Canvas canvas)
  {
    super.onDraw(canvas);

    int w = getWidth();
    int h = getHeight();

    //灰色底色遮罩
    canvas.drawARGB(100, 0, 0, 0);

    //中间去除遮罩
    canvas.drawCircle(w / 2, h / 2, w / 2 - mHorizontalPadding, clearPaint);

    // 绘制边框
    //方形边框
    //		canvas.drawRect(mHorizontalPadding, mVerticalPadding, getWidth()- mHorizontalPadding, getHeight() - mVerticalPadding, circlePaint);
    //圆形边框
    canvas.drawCircle(w / 2, h / 2, w / 2 - mHorizontalPadding, circlePaint);

  }

  public void setHorizontalPadding(int mHorizontalPadding)
  {
    this.mHorizontalPadding = mHorizontalPadding;
  }

}

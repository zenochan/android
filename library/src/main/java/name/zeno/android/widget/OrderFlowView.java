package name.zeno.android.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import name.zeno.android.util.ZDimen;

/**
 * Create Date: 16/6/22
 *
 * @author 陈治谋 (513500085@qq.com)
 */
@SuppressWarnings("unused")
public class OrderFlowView extends View
{
  @ColorInt private int colorCircle = Color.parseColor("#437af7");
  @ColorInt private int colorLine   = Color.parseColor("#9e9e9e");

  private boolean isStart   = false;
  private boolean isEnd     = false;
  private int     radius    = ZDimen.dp2px(4);
  private int     lineWidth = ZDimen.dp2px(1);
  private int     cY        = ZDimen.dp2px(16);

  private Paint paint;

  public OrderFlowView(Context context)
  {
    this(context, null);
  }

  public OrderFlowView(Context context, AttributeSet attrs)
  {
    this(context, attrs, 0);
  }

  public OrderFlowView(Context context, AttributeSet attrs, int defStyleAttr)
  {
    super(context, attrs, defStyleAttr);
    init();
  }

  public void setColorCircle(int colorCircle)
  {
    this.colorCircle = colorCircle;
    invalidate();
  }

  public void setColorCircleRes(@ColorRes int resId)
  {
    this.colorCircle = ContextCompat.getColor(getContext(), resId);
    invalidate();
  }

  public void setRadius(int radiusPx)
  {
    this.radius = radiusPx;
  }

  public void setColorLine(@ColorInt int colorLine)
  {
    this.colorLine = colorLine;
    invalidate();
  }

  public void setColorLineRes(@ColorRes int colorRes)
  {
    this.colorLine = ContextCompat.getColor(getContext(), colorRes);
    invalidate();
  }


  public void setEnd(boolean end)
  {
    isEnd = end;
    invalidate();
  }

  public void setStart(boolean start)
  {
    isStart = start;
    invalidate();
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
  {
    int w = MeasureSpec.getSize(widthMeasureSpec);
    int h = MeasureSpec.getSize(heightMeasureSpec);
    int wm = MeasureSpec.getMode(widthMeasureSpec);
    int hm = MeasureSpec.getMode(heightMeasureSpec);

    switch (wm) {
      case MeasureSpec.AT_MOST:
        if (w > ZDimen.dp2px(32)) {
          w = ZDimen.dp2px(32);
        }
        break;
      case MeasureSpec.UNSPECIFIED:
        if (w < ZDimen.dp2px(32)) {
          w = ZDimen.dp2px(32);
        }
        break;
      case MeasureSpec.EXACTLY:
        if (w < ZDimen.dp2px(8)) {
          w = ZDimen.dp2px(8);
        }
        break;
    }

    switch (hm) {
      case MeasureSpec.AT_MOST:
      case MeasureSpec.UNSPECIFIED:
        if (h < ZDimen.dp2px(32)) {
          h = ZDimen.dp2px(32);
        }
        break;
      case MeasureSpec.EXACTLY:
        if (h < ZDimen.dp2px(8)) {
          h = ZDimen.dp2px(8);
        }
        break;
    }

    setMeasuredDimension(w, h);
  }

  @Override protected void onDraw(Canvas canvas)
  {
    super.onDraw(canvas);
    int width = getWidth();
    int height = getHeight();

    int lineTop = isStart ? cY : 0;
    int lineBottom = isEnd ? cY : height;
    paint.setColor(colorLine);
    paint.setStrokeWidth(lineWidth);

    canvas.drawLine(width / 2, lineTop, width / 2, lineBottom, paint);
    paint.setColor(colorCircle);
    canvas.drawCircle(width / 2, cY, radius, paint);

  }

  private void init()
  {
    paint = new Paint();
    paint.setAntiAlias(true);
  }

}

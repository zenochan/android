package name.zeno.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import name.zeno.android.util.R;
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

  private static int     dp32      = ZDimen.dp2px(32);
  private        boolean isStart   = false;
  private        boolean isEnd     = false;
  private        int     radius    = ZDimen.dp2px(4);
  private        int     lineWidth = ZDimen.dp2px(1);
  private        int     cY        = ZDimen.dp2px(16);

  private boolean rightLineEnable = false;
  private int     rightLineWidth  = ZDimen.dp2px(32);

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
    init(attrs);
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
    int w  = MeasureSpec.getSize(widthMeasureSpec);
    int h  = MeasureSpec.getSize(heightMeasureSpec);
    int wm = MeasureSpec.getMode(widthMeasureSpec);
    int hm = MeasureSpec.getMode(heightMeasureSpec);

    switch (wm) {
      case MeasureSpec.AT_MOST:
        if (w > dp32) {
          w = dp32;
        }
        break;
      case MeasureSpec.UNSPECIFIED:
        if (w < ZDimen.dp2px(32)) {
          w = dp32;
        }
        break;
      case MeasureSpec.EXACTLY:
        if (w < ZDimen.dp2px(8)) {
          w = ZDimen.dp2px(8);
        }
        break;
    }

    if (rightLineEnable) {
      w += rightLineWidth - w / 2;
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
    int width  = getWidth();
    int height = getHeight();

    paint.setStyle(Paint.Style.FILL);

    //int lineTop = isStart ? cY : 0;
    int lineTop    = 0;
    int lineBottom = isEnd ? cY : height;
    paint.setColor(colorLine);
    paint.setStrokeWidth(lineWidth);

    int x = rightLineEnable ? (width - rightLineWidth) : width / 2;

    canvas.drawLine(x, lineTop, x, lineBottom, paint);
    paint.setColor(colorCircle);
    canvas.drawCircle(x, cY, radius, paint);


    if (rightLineEnable) {
      canvas.drawLine(x, cY, width, cY, paint);
    }

    if (isStart) {
      paint.setStyle(Paint.Style.STROKE);
      canvas.drawCircle(x, cY, radius + ZDimen.dp2px(2), paint);
    }
  }

  private void init(AttributeSet attrs)
  {
    paint = new Paint();
    paint.setAntiAlias(true);

    TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.OrderFlowView);
    cY = ta.getDimensionPixelSize(R.styleable.OrderFlowView_topHeight, cY);
    rightLineEnable = ta.getBoolean(R.styleable.OrderFlowView_rightLineEnable, rightLineEnable);
    rightLineWidth = ta.getDimensionPixelSize(R.styleable.OrderFlowView_rightLineWidth, rightLineWidth);
    ta.recycle();
  }

}

package name.zeno.android.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;

import name.zeno.android.anim.InterpolationObservableAnimation;


/**
 * Create Date: 15/10/22
 *
 * @author 陈治谋 (513500085@qq.com)
 */
@SuppressWarnings("unused")
public class CircleZoomView extends View
{
  interface Status
  {
    int SMALL    = 0; //小的
    int ZOOM_IN  = 1; //放大中
    int ZOOM_OUT = 2; //缩小中
    int BIG      = 3; //大的
  }

  private static final String TAG = "CircleView";

  private static final int DEFAULT_COLOR = 0xFF4DB9FF;

  @SuppressWarnings("FieldCanBeLocal")
  private static float density;

  private float stroke = 1, smallR = 4, minR = 4, maxR = 16;
  private Paint paint;
  @ColorInt
  private int color = DEFAULT_COLOR;


  private InterpolationObservableAnimation interpolationAnim;
  private int                              status;
  private float                            interpolation;

  public CircleZoomView(Context context)
  {
    this(context, null);
  }

  public CircleZoomView(Context context, AttributeSet attrs)
  {
    this(context, attrs, 0);
  }

  public CircleZoomView(Context context, AttributeSet attrs, int defStyleAttr)
  {
    super(context, attrs, defStyleAttr);
    initializeView();
  }

  public void zoomIn()
  {
    if (status == Status.SMALL || status == Status.ZOOM_OUT) {
      status = Status.ZOOM_IN;
      startAnimation(interpolationAnim);
    }
  }

  public void setSmall()
  {
    clearAnimation();
    status = Status.SMALL;
    invalidate();
  }

  public void zoomOut()
  {
    if (status == Status.BIG || status == Status.ZOOM_IN) {
      status = Status.ZOOM_OUT;
      startAnimation(interpolationAnim);
    }
  }

  public void setBig()
  {
    clearAnimation();
    status = Status.BIG;
    invalidate();
  }

  public void toggle()
  {
    if (status == Status.SMALL) {
      zoomIn();
    } else if (status == Status.BIG) {
      zoomOut();
    }
  }

  public void setColor(@ColorInt int color)
  {
    this.color = color;
    paint.setColor(color);
    invalidate();
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
  {
    int w = (int) ((maxR + stroke) * 2);
    setMeasuredDimension(w, w);
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom)
  {
    super.onLayout(changed, left, top, right, bottom);
  }

  @Override
  protected final void onDraw(Canvas canvas)
  {
    float r = 0;
    switch (status) {
      case Status.SMALL:
        r = minR;
        break;
      case Status.ZOOM_IN:
        r = minR + interpolation * (maxR - minR);
        break;
      case Status.ZOOM_OUT:
        r = maxR - interpolation * (maxR - minR);
        break;
      case Status.BIG:
        r = maxR;
        break;

    }
    paint.setStyle(Paint.Style.FILL);
    canvas.drawCircle(getWidth() / 2, getHeight() / 2, smallR, paint);
    paint.setStyle(Paint.Style.STROKE);
    canvas.drawCircle(getWidth() / 2, getHeight() / 2, r, paint);
  }

  @Override protected void onAnimationEnd()
  {
    super.onAnimationEnd();
    if (status == Status.ZOOM_IN) {
      status = Status.BIG;
    } else if (status == Status.ZOOM_OUT) {
      status = Status.SMALL;
    }
    interpolation = 0;
  }


  private void initializeView()
  {
    density = Resources.getSystem().getDisplayMetrics().density;
    stroke *= density;
    smallR *= density;
    maxR *= density;
    minR *= density;

    paint = new Paint();
    paint.setColor(color);
    paint.setStyle(Paint.Style.FILL);
    paint.setAntiAlias(true);
    paint.setStrokeWidth(1 * density);

    status = Status.BIG;
    interpolation = 0;
    interpolationAnim = new InterpolationObservableAnimation();
    interpolationAnim.setDuration(300);
    interpolationAnim.setOnInterpolatedListener(new InterpolationObservableAnimation.OnInterpolatedListener()
    {
      @Override public void onInterpolated(float interpolatedTime)
      {
        interpolation = interpolatedTime;
        CircleZoomView.this.invalidate();
      }
    });
  }

}


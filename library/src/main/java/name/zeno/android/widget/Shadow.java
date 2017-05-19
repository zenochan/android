package name.zeno.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.Dimension;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import name.zeno.android.util.R;
import name.zeno.android.util.ZDimen;

/**
 * @author 陈治谋 (微信: puppet2436)
 * @since 2017/4/15
 */
public class Shadow extends View
{

  @IntDef({Mode.BEZIER, Mode.TRIANGLE}) @interface Mode
  {
    int TRIANGLE = 0;           // 三角形
    int BEZIER   = 1;           // 贝塞尔
    int CORNERS  = 2;           // 圆角
  }

  private Paint paint;
  private Path  path;

  private int cornersRadius = ZDimen.dp2px(4);
  private int color         = Color.WHITE;

  private            RectF rect        = new RectF();
  private            int   mode        = Mode.CORNERS;
  @ColorInt private  int   borderColor = 0;
  @Dimension private int   borderWidth = 0;

  public Shadow(Context context)
  {
    this(context, null);
  }

  public Shadow(Context context, @Nullable AttributeSet attrs)
  {
    this(context, attrs, 0);
  }

  public Shadow(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
  {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }


  @Override protected void onDraw(Canvas canvas)
  {
    super.onDraw(canvas);
    int h = getMeasuredHeight();
    int w = getMeasuredWidth();
    path.reset();
    paint.setColor(color);
    paint.setStyle(Paint.Style.FILL);
    switch (mode) {
      case Mode.BEZIER:
        path.setFillType(Path.FillType.WINDING);
        path.moveTo(0, h);
        path.quadTo(w / 2, -h, w, h);
        canvas.drawPath(path, paint);
        break;
      case Mode.CORNERS:
        rect.set(0, 0, w, h);
        path.setFillType(Path.FillType.INVERSE_WINDING);
        path.addRoundRect(rect, cornersRadius, cornersRadius, Path.Direction.CW);
        canvas.drawPath(path, paint);
        if (borderWidth > 0) {
          int halfW = (int) ((borderWidth + 0.5) / 2);
          rect.set(halfW, halfW, w - halfW, h - halfW);
          paint.setStrokeWidth(borderWidth);
          paint.setColor(borderColor);
          paint.setStyle(Paint.Style.STROKE);
          path.reset();
          path.setFillType(Path.FillType.WINDING);
          path.addRoundRect(rect, cornersRadius - halfW, cornersRadius - halfW, Path.Direction.CW);
          canvas.drawPath(path, paint);
        }
        break;
      default:
        path.setFillType(Path.FillType.WINDING);
        path.moveTo(0, h);
        path.lineTo(w / 2, 0);
        path.lineTo(w, h);
        canvas.drawPath(path, paint);
        break;
    }
  }

  void init(Context context, AttributeSet attrs)
  {
    paint = new Paint();
    paint.setAntiAlias(true);
    path = new Path();

    if (attrs != null) {
      TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Shadow);
      if (ta.hasValue(R.styleable.Shadow_colorShadow)) {
        color = ta.getColor(R.styleable.Shadow_colorShadow, color);
      }
      if (ta.hasValue(R.styleable.Shadow_mode)) {
        mode = ta.getInt(R.styleable.Shadow_mode, mode);
      }
      cornersRadius = ta.getDimensionPixelSize(R.styleable.Shadow_radius, cornersRadius);
      borderWidth = ta.getDimensionPixelSize(R.styleable.Shadow_borderWidth, borderWidth);
      borderColor = ta.getColor(R.styleable.Shadow_borderColor, borderColor);
      ta.recycle();
    }
  }
}

package name.zeno.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
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

  private RectF rect = new RectF();
  private int   mode = Mode.CORNERS;

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
    path.setFillType(Path.FillType.WINDING);

    switch (mode) {
      case Mode.BEZIER:
        path.moveTo(0, h);
        path.quadTo(w / 2, -h, w, h);
        canvas.drawPath(path, paint);
        break;
      case Mode.CORNERS:
        // 左上
        path.moveTo(0, cornersRadius);
        path.lineTo(0, 0);
        path.lineTo(cornersRadius, 0);
        rect.set(0, 0, cornersRadius * 2, cornersRadius * 2);
        path.arcTo(rect, -90, -90);

        // 右上
        path.moveTo(w - cornersRadius, 0);
        path.lineTo(w, 0);
        path.lineTo(w, cornersRadius);
        rect.set(w - cornersRadius * 2, 0, w, cornersRadius * 2);
        path.arcTo(rect, 0, -90);

        // 右下
        path.moveTo(w, h - cornersRadius);
        path.lineTo(w, h);
        path.lineTo(w - cornersRadius, h);
        rect.set(w - cornersRadius * 2, h - cornersRadius * 2, w, h);
        path.arcTo(rect, 90, -90);

        // 左下
        path.moveTo(cornersRadius, h);
        path.lineTo(0, h);
        path.lineTo(0, h - cornersRadius);
        rect.set(0, h - cornersRadius * 2, cornersRadius * 2, h);
        path.arcTo(rect, 180, -90);

        canvas.drawPath(path, paint);
        break;
      default:
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
      if (ta.hasValue(R.styleable.Shadow_radius)) {
        cornersRadius = ta.getDimensionPixelSize(R.styleable.Shadow_radius, cornersRadius);
      }
      ta.recycle();
    }
  }
}

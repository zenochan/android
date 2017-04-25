package name.zeno.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.IntDef;
import android.support.annotation.InterpolatorRes;
import android.util.AttributeSet;
import android.view.View;

import lombok.Setter;
import name.zeno.android.util.R;


/**
 * <h1>ImageView 圆形遮罩</h1>
 * <p>
 * padding       圆形边框的宽度
 *
 * @see name.zeno.android.util.R.styleable#CircleImageView_mask_color      圆形的背景色
 * @see name.zeno.android.util.R.styleable#CircleImageView_circle_color  圆形边框颜色
 */
public class CircleImageView extends ZImageView
{

  @IntDef({Mode.OVERDRAW, Mode.CLIP}) @interface Mode
  {
    int OVERDRAW = 0;   // 覆盖绘制
    int CLIP     = 1;   // 裁剪画布
  }

  private Paint paint;
  private Path  path;
  private boolean init = false;
  private RectF   rect = new RectF();
  private Context mContext;

  @Setter private int   borderColor;
  @Setter private float borderWidth;
  @Setter private int   maskColor;

  @Mode
  @Setter private int mode = Mode.OVERDRAW;

  public CircleImageView(Context context)
  {
    this(context, null);
  }

  public CircleImageView(Context context, AttributeSet attrs)
  {
    this(context, attrs, 0);
  }

  public CircleImageView(Context context, AttributeSet attrs, int defStyle)
  {
    super(context, attrs, defStyle);
    init(context, attrs);
    mContext = context;
  }


  private void init(Context context, AttributeSet attrs)
  {
    mContext = context;
    borderWidth = 0;

    if (null != attrs) {
      TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.CircleImageView);
      borderColor = ta.getColor(R.styleable.CircleImageView_circle_color, Color.GRAY);
      maskColor = ta.getColor(R.styleable.CircleImageView_mask_color, Color.WHITE);
      if (ta.hasValue(R.styleable.CircleImageView_clipMode)) {
        //noinspection WrongConstant
        mode = ta.getInt(R.styleable.CircleImageView_clipMode, Mode.OVERDRAW);
      }
      ta.recycle();
    } else {
      borderColor = Color.GRAY;
      maskColor = Color.WHITE;
    }
  }


  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
  {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    if (!init) {
      initPaint();
    }
  }

  @Override
  protected void onDraw(Canvas canvas)
  {
    int w = getWidth();
    int h = getHeight();
    if (mode == Mode.CLIP) {
      if (getLayerType() != LAYER_TYPE_SOFTWARE) {
        // View 级关闭硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);
      }

      // 裁剪画布
      path.reset();
      rect.set(0, 0, w, h);
      path.addArc(rect, 0, 360);
      canvas.clipPath(path, Region.Op.REPLACE);
    }
    super.onDraw(canvas);

    if (mode == Mode.OVERDRAW) {
      //draw mask
      paint.setColor(maskColor);
      paint.setStyle(Paint.Style.FILL);
      float radius = w / (float) 2;
      path.reset();
      path.moveTo(0, radius);
      path.lineTo(0, 0);
      path.lineTo(w, 0);
      path.lineTo(w, h);
      path.lineTo(0, h);
      path.lineTo(0, radius);
      rect.set(0, 0, w, h);
      path.arcTo(rect, 180, -359, true);
      path.close();
      canvas.drawPath(path, paint);
    }

    if (borderWidth > 0) {
      // draw border ，画一个椭圆
      paint.setColor(borderColor);
      paint.setStyle(Paint.Style.STROKE);
      paint.setStrokeWidth(borderWidth);
      int halfW = (int) (borderWidth / 2);
      rect.set(halfW, halfW, w - halfW, h - halfW);
      canvas.drawOval(rect, paint);
    }
  }

  private void initPaint()
  {
    if (borderWidth <= 0) {borderWidth = getPaddingLeft();}
    setPadding(0, 0, 0, 0);
    paint = new Paint();
    paint.setStyle(Paint.Style.FILL);
    paint.setColor(Color.WHITE);
    paint.setAntiAlias(true);
    path = new Path();
    init = true;
  }
}


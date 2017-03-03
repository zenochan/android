package name.zeno.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

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
  private Paint paint;
  private Path  path;
  private boolean init = false;
  private RectF   rect = new RectF();
  private Context mContext;
  private int     borderColor;
  private float   borderWidth;
  private int     maskColor;

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
    borderWidth = 6;

    if (null != attrs) {
      TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.CircleImageView);
      borderColor = ta.getColor(R.styleable.CircleImageView_circle_color, Color.GRAY);
      maskColor = ta.getColor(R.styleable.CircleImageView_mask_color, Color.WHITE);
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
    super.onDraw(canvas);
    paint.setColor(maskColor);
    paint.setStyle(Paint.Style.FILL);

    int width = getMeasuredWidth();
    int height = getMeasuredHeight();
    float radius = width / (float) 2;
    path.reset();
    path.moveTo(0, radius);
    path.lineTo(0, 0);
    path.lineTo(width, 0);
    path.lineTo(width, height);
    path.lineTo(0, height);
    path.lineTo(0, radius);
    rect.set(0, 0, width, height);
    path.arcTo(rect, 180, -359, true);
    path.close();
    canvas.drawPath(path, paint);

    paint.setColor(borderColor);
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeWidth(borderWidth);
    float dw = (float) (borderWidth / 2 - .4);
    rect.set(dw, dw, width - dw, height - dw);
    canvas.drawOval(rect, paint);
  }

  private void initPaint()
  {
    borderWidth = getPaddingLeft();
    setPadding(0, 0, 0, 0);
    paint = new Paint();
    paint.setStyle(Paint.Style.FILL);
    paint.setColor(Color.WHITE);
    paint.setAntiAlias(true);
    path = new Path();
    init = true;
  }
}


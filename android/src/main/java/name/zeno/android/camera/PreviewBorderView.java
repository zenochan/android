package name.zeno.android.camera;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import name.zeno.android.util.MeasureUtils;
import name.zeno.android.util.R;
import name.zeno.android.util.ZDimen;

/**
 * Create Date: 16/6/19
 *
 * @author 陈治谋 (513500085@qq.com)
 */
public class PreviewBorderView extends View
{
  private static final String DEFAULT_TIPS_TEXT       = "请将方框对准证件拍摄";
  private static final int    DEFAULT_TIPS_TEXT_SIZE  = ZDimen.dp2px(16);
  private static final int    DEFAULT_TIPS_TEXT_COLOR = Color.GREEN;

  @SuppressWarnings("FieldCanBeLocal") private int screenH;
  @SuppressWarnings("FieldCanBeLocal") private int screenW;

  private RectF rectF = new RectF();
  private Paint paint;
  private Paint paintLine;

  private float  tipTextSize;
  private int    tipTextColor;
  private String tipText;

  public PreviewBorderView(Context context)
  {
    this(context, null);
  }

  public PreviewBorderView(Context context, AttributeSet attrs)
  {
    this(context, attrs, 0);
  }

  public PreviewBorderView(Context context, AttributeSet attrs, int defStyleAttr)
  {
    super(context, attrs, defStyleAttr);
    initAttrs(context, attrs);
    init();
  }

  protected void onDraw(Canvas canvas)
  {
    super.onDraw(canvas);

    //灰色底色遮罩
    canvas.drawARGB(100, 0, 0, 0);
    screenW = getWidth();
    screenH = getHeight();
    float unit = screenH / 3;
    float l, t, r, b;
    l = screenW * 0.5F - unit * 1.5F;
    t = unit * 0.5F;
    r = screenW * 0.5F + unit * 1.5F;
    b = screenH - unit * 0.5F;


    //中间去除遮罩
    rectF.set(l, t, r, b);
    canvas.drawRect(rectF, paint);

    //四个角
    canvas.drawLine(l, t, l, t + 50, paintLine);
    canvas.drawLine(l, t, l + 50, t, paintLine);
    canvas.drawLine(r, t, r, t + 50, paintLine);
    canvas.drawLine(r, t, r - 50, t, paintLine);
    canvas.drawLine(l, b, l, b - 50, paintLine);
    canvas.drawLine(l, b, l + 50, b, paintLine);
    canvas.drawLine(r, b, r, b - 50, paintLine);
    canvas.drawLine(r, b, r - 50, b, paintLine);

    //提示文本
    paintLine.setTextSize(tipTextSize);
    paintLine.setAntiAlias(true);
    paintLine.setDither(true);
    float length = paintLine.measureText(tipText);
    canvas.drawText(tipText, screenW / 2 - length / 2, unit * 0.5F - tipTextSize, paintLine);
  }


  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
  {
    Point size = MeasureUtils.as4Rate3(widthMeasureSpec, heightMeasureSpec);
    setMeasuredDimension(size.x, size.y);
  }

  private void initAttrs(Context context, AttributeSet attrs)
  {
    TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PreviewBorderView);
    if (ta.hasValue(R.styleable.PreviewBorderView_tipText)) {
      tipText = ta.getString(R.styleable.PreviewBorderView_tipText);
    } else {
      tipText = DEFAULT_TIPS_TEXT;
    }
    tipTextColor = ta.getColor(R.styleable.PreviewBorderView_tipTextColor, DEFAULT_TIPS_TEXT_COLOR);
    tipTextSize = ta.getDimensionPixelSize(R.styleable.PreviewBorderView_tipTextSize, DEFAULT_TIPS_TEXT_SIZE);

    ta.recycle();

  }


  private void init()
  {
    paint = new Paint();
    paint.setAntiAlias(true);
    paint.setColor(Color.WHITE);
    paint.setStyle(Paint.Style.FILL_AND_STROKE);
    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

    paintLine = new Paint();
    paintLine.setColor(tipTextColor);
    paintLine.setStrokeWidth(3.0F);
    setKeepScreenOn(true);
  }
}

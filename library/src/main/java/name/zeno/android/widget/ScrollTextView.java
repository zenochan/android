package name.zeno.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import name.zeno.android.anim.InterpolationObservableAnimation;
import name.zeno.android.util.ZDimen;
import name.zeno.android.util.R;

/**
 * 单行文字信息纵向滚动
 * <p>
 * Create Date: 16/5/29
 *
 * @author 陈治谋 (513500085@qq.com)
 */
@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class ScrollTextView extends View
    implements Handler.Callback
{
  private static final String TAG = "ScrollTextView";

  public interface OnItemClickListener
  {
    void onItemClick(int position);
  }

  private Context context;
  private int     width;
  private int     height;
  private Paint   paint;

  private boolean scroll = true;

  //字体大小，默认 12sp
  private int textSize  = ZDimen.sp2px(12);
  //字体颜色，默认黑色
  private int textColor = Color.BLACK;

  private InterpolationObservableAnimation anim;
  private Animation.AnimationListener      animListener;

  private float interTime = 0;
  private long  during    = 1000;
  private long  interval  = 3000;

  private Handler handler = new Handler(this);
  private Timer     timer;
  private TimerTask timerTask;

  private List<String> stringList;
  private int position = 0;
  private OnItemClickListener onItemClickListener;

  public ScrollTextView(Context context)
  {
    this(context, null);
  }

  public ScrollTextView(Context context, AttributeSet attrs)
  {
    this(context, attrs, 0);
  }

  public ScrollTextView(Context context, AttributeSet attrs, int defStyleAttr)
  {
    super(context, attrs, defStyleAttr);
    initView(context, attrs);
  }

  @Override protected void onAnimationEnd()
  {
    super.onAnimationEnd();
    if (stringList != null && !stringList.isEmpty()) {
      position = (position + 1) % stringList.size();
    } else {
      position = 0;
    }
    interTime = 0;
    invalidate();
  }

  @Override protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    initTimer();
    startAnimation(anim);
  }


  @Override protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    clearTimer();
  }

  public List<String> getStringList()
  {
    return stringList;
  }

  public void setStringList(List<String> stringList)
  {
    clearAnimation();
    this.stringList = stringList;
  }

  public long getDuring()
  {
    return during;
  }

  public void setDuring(long during)
  {
    this.during = during;
    anim.setDuration(during);
  }

  public long getInterval() { return interval; }

  public void setInterval(long interval) { this.interval = interval; }

  public int getTextColor() { return textColor; }

  public void setTextColor(int textColor) { this.textColor = textColor; }

  public int getTextSize() { return textSize; }

  public void setTextSize(int textSize) { this.textSize = textSize; }

  @Override public boolean handleMessage(Message msg)
  {
//    timer.schedule(timerTask, 0, interval);
    startAnimation(anim);
    return true;
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
  {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int width       = MeasureSpec.getSize(widthMeasureSpec);
    int height      = MeasureSpec.getSize(heightMeasureSpec);
    int heightModel = MeasureSpec.getMode(heightMeasureSpec);
    if (heightModel == MeasureSpec.AT_MOST) {
      height = (int) (textSize * 1.2) + getPaddingBottom() + getPaddingTop();
    }
    setMeasuredDimension(width, height);
  }

  @Override protected void onDraw(Canvas canvas)
  {
    super.onDraw(canvas);
    paint.setColor(textColor);
    paint.setTextSize(textSize);
    String currStr, nextStr;

    if (stringList != null && !stringList.isEmpty()) {
      currStr = stringList.get(position);
      nextStr = stringList.get((position + 1) % stringList.size());
    } else {
      currStr = nextStr = "nothing to show!";
    }
    canvas.drawText(currStr, getPaddingLeft(), (float) ((height + textSize * 0.8) / 2 - interTime * height * 1.2), paint);
    canvas.drawText(nextStr, getPaddingLeft(), (float) ((height + textSize * 0.8) / 2 - (interTime - 1) * height), paint);
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh)
  {
    super.onSizeChanged(w, h, oldw, oldh);
    width = w;
    height = h;
  }

  private void initView(Context context, AttributeSet attrs)
  {
    this.context = context;
    paint = new Paint();
    paint.setAntiAlias(true);
    initAttr(attrs);
    initAnim();
  }

  private void initAttr(AttributeSet attrs)
  {
    TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ScrollTextView);

    for (int i = 0; i < ta.getIndexCount(); i++) {
      int index = ta.getIndex(i);
      if (index == R.styleable.ScrollTextView_textColor) {
        textColor = ta.getColor(index, Color.BLACK);
      } else if (index == R.styleable.ScrollTextView_textSize) {
        textSize = ta.getDimensionPixelSize(index, 48);
      }

    }
    ta.recycle();
  }

  private void initTimer()
  {
    timer = new Timer();
    timerTask = new TimerTask()
    {
      @Override public void run()
      {
        handler.sendEmptyMessage(0);
      }
    };

    timer.schedule(timerTask, 0, interval);
  }

  /**
   * 清除计时器
   */
  private void clearTimer()
  {
    if (timerTask != null) {
      timerTask.cancel();
      timerTask = null;
      timer = null;
    }
  }

  /**
   * 初试化动画，通过插值动画，重绘 View
   */
  private void initAnim()
  {
    anim = new InterpolationObservableAnimation();
    anim.setDuration(during);
    anim.setInterpolator(new FastOutSlowInInterpolator());
    anim.setOnInterpolatedListener(interpolatedTime -> {
      interTime = interpolatedTime;
      invalidate();
    });
  }
}


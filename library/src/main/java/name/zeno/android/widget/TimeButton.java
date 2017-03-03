package name.zeno.android.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.StringRes;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.Timer;
import java.util.TimerTask;

/**
 * PS: 由于发现 timer 每次 cancel() 之后不能重新schedule方法,所以计时完毕置空timer.
 * 每次开始计时的时候重新设置timer, 没想到好办法初次下策
 * 注意把该类的onCreate()onDestroy()和activity的onCreate()onDestroy()同步处理
 */
public class TimeButton extends AppCompatButton implements OnClickListener, Handler.Callback
{
  public static final String TAG = TimeButton.class.getSimpleName();

  private SharedPreferences preferences;

  private       long   length     = 60 * 1000;// 倒计时长度,默认60s
  private       String textAfter  = "秒后重新获取~";
  private       String textBefore = "点击获取验证码~";
  private final String TIME       = "time";
  private final String C_TIME     = "cTime";
  private OnClickListener mOnclickListener;
  private Timer           timer;
  private TimerTask       timerTask;
  private long            time;

  Handler han = new Handler(this);


  public TimeButton(Context context)
  {
    this(context, null);
  }

  public TimeButton(Context context, AttributeSet attrs)
  {
    this(context, attrs, 0);
  }

  public TimeButton(Context context, AttributeSet attrs, int defStyleAttr)
  {
    super(context, attrs, defStyleAttr);
    preferences = context.getApplicationContext().getSharedPreferences(TAG, Context.MODE_PRIVATE);
    setOnClickListener(this);
  }

  @Override
  public boolean handleMessage(Message msg)
  {
    setText(time / 1000 + textAfter);
    time -= 1000;
    if (time < 0) {
      super.setEnabled(true);
      setText(textBefore);
      clearTimer();
    }
    return true;
  }

  @Override
  public void onClick(View v)
  {
    if (mOnclickListener != null)
      mOnclickListener.onClick(v);
    startTime();
  }

  public void startTime()
  {
    initTimer();
    setText(time / 1000 + textAfter);
    super.setEnabled(false);
    timer.schedule(timerTask, 0, 1000);
  }

  @Override public void setEnabled(boolean enabled)
  {
    if (enabled && time > 0) {
      return;
    }
    super.setEnabled(enabled);
  }

  @Override protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    long t = preferences.getLong(TIME, -1);
    long cT = preferences.getLong(C_TIME, -1);
    if (t == -1 || cT == -1)// 这里表示没有上次未完成的计时
      return;
    long time = System.currentTimeMillis() - cT - t;
    if (time <= 0) {
      initTimer();
      this.time = Math.abs(time);
      timer.schedule(timerTask, 0, 1000);
      setText(time + textAfter);
      super.setEnabled(false);
    }
  }

  @Override protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();

    //记录剩余时间
    SharedPreferences.Editor editor = preferences.edit();
    editor.putLong(TIME, time);
    editor.putLong(C_TIME, System.currentTimeMillis());
    editor.apply();

    clearTimer();
  }

  /**
   * 清除倒计时
   */
  public void reset()
  {
    post(() -> time = -1);
  }

  private void initTimer()
  {
    time = length;
    timer = new Timer();
    timerTask = new TimerTask()
    {
      @Override
      public void run()
      {
        han.sendEmptyMessage(0x01);
      }
    };
  }

  private void clearTimer()
  {
    if (timerTask != null) {
      timerTask.cancel();
      timerTask = null;
    }
    if (timer != null)
      timer.cancel();
    timer = null;
  }

  @Override
  public void setOnClickListener(OnClickListener l)
  {
    if (l instanceof TimeButton) {
      super.setOnClickListener(l);
    } else {
      mOnclickListener = l;
    }
  }

//  /**
//   * 和activity的onCreate()方法同步
//   */
//  public void onCreate()
//  {
//    Log.w(TAG, "onCreate()");
//
//  }

  /**
   * 设置计时时候显示的文本
   */
  public TimeButton setTextAfter(String afterText)
  {
    textAfter = afterText;
    return this;
  }

  /**
   * 设置点击之前的文本
   */
  public TimeButton setTextBefore(String beforeText)
  {
    textBefore = beforeText;
    setText(textBefore);
    return this;
  }

  /**
   * 设置点击之前的文本
   */
  public TimeButton setTextBefore(@StringRes int resID)
  {
    return setTextBefore(getResources().getString(resID));
  }

  /**
   * 设置到计时长度
   *
   * @param length 时间 默认毫秒
   */
  @SuppressWarnings("unused")
  public TimeButton setLength(long length)
  {
    this.length = length;
    return this;
  }
}

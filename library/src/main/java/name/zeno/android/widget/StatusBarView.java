package name.zeno.android.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import name.zeno.android.system.ZStatusBar;

/**
 * @author 陈治谋 (微信: puppet2436)
 * @since 2017/4/20
 */
public class StatusBarView extends View
{

  private int statusBarSize = 0;

  public StatusBarView(Context context)
  {
    this(context, null);
  }

  public StatusBarView(Context context, @Nullable AttributeSet attrs)
  {
    this(context, attrs, -1);
  }

  public StatusBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
  {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  private void init(Context context)
  {
    statusBarSize = ZStatusBar.getStatusBarHeight(context);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
  {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    setMeasuredDimension(getMeasuredWidth(), statusBarSize);
  }
}

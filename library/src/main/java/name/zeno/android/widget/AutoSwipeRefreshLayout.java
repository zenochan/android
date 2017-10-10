package name.zeno.android.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import name.zeno.android.util.ZLog;

/**
 * SwipeRefreshLayout 支持自动刷新
 * Create Date: 16/6/28
 *
 * @author 陈治谋 (513500085@qq.com)
 */
public class AutoSwipeRefreshLayout extends SwipeRefreshLayout
{
  private static final String TAG = "AutoSwipeRefreshLayout";

  private boolean firstAutoRefreshed = false;

  public AutoSwipeRefreshLayout(Context context)
  {
    super(context);
  }

  public AutoSwipeRefreshLayout(Context context, AttributeSet attrs)
  {
    super(context, attrs);
  }

  public void autoRefresh()
  {
    try {
      Field mCircleView = SwipeRefreshLayout.class.getDeclaredField("mCircleView");
      mCircleView.setAccessible(true);
      View progress = (View) mCircleView.get(this);
      progress.setVisibility(VISIBLE);

      if (firstAutoRefreshed) {
        Method reset = SwipeRefreshLayout.class.getDeclaredMethod("reset");
        reset.setAccessible(true);
        reset.invoke(this);
      } else {
        firstAutoRefreshed = true;
      }

      Method setRefreshing = SwipeRefreshLayout.class.getDeclaredMethod("setRefreshing", boolean.class, boolean.class);
      setRefreshing.setAccessible(true);
      setRefreshing.invoke(this, true, true);
    } catch (Exception e) {
      ZLog.e(TAG, "autoRefresh failed", e);
    }
  }

  public boolean isFirstAutoRefreshed()
  {return this.firstAutoRefreshed;}
}

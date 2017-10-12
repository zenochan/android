package name.zeno.android.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.WindowInsets;
import android.widget.FrameLayout;

/**
 * @author 陈治谋 (微信: puppet2436)
 * @since 2017/3/29
 */
public class ZFrameLayout extends FrameLayout
{
  private int[] mInsets = new int[4];

  public ZFrameLayout(@NonNull Context context)
  {
    super(context);
  }

  public ZFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs)
  {
    super(context, attrs);
  }

  public ZFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr)
  {
    super(context, attrs, defStyleAttr);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public ZFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes)
  {
    super(context, attrs, defStyleAttr, defStyleRes);
  }


  @Override
  protected final boolean fitSystemWindows(Rect insets)
  {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      // Intentionally do not modify the bottom inset. For some reason,
      // if the bottom inset is modified, window resizing stops working.
      // TODO: Figure out why.

      mInsets[0] = insets.left;
      mInsets[1] = insets.top;
      mInsets[2] = insets.right;

      insets.left = 0;
      insets.top = 0;
      insets.right = 0;
    }

    return super.fitSystemWindows(insets);
  }

  @Override
  public final WindowInsets onApplyWindowInsets(WindowInsets insets)
  {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
      mInsets[0] = insets.getSystemWindowInsetLeft();
      mInsets[1] = insets.getSystemWindowInsetTop();
      mInsets[2] = insets.getSystemWindowInsetRight();
      return super.onApplyWindowInsets(insets.replaceSystemWindowInsets(0, 0, 0, insets.getSystemWindowInsetBottom()));
    } else {
      return insets;
    }
  }
}

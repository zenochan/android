package name.zeno.android.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.design.widget.AppBarLayout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import lombok.Getter;
import name.zeno.android.system.ZStatusBar;
import name.zeno.android.util.R;

/**
 * Create Date: 16/6/15
 *
 * @author 陈治谋 (513500085@qq.com)
 */
@SuppressWarnings("unused")
public class ZAppBarLayout extends AppBarLayout
{
  @Getter private View statusBarView;

  public ZAppBarLayout(Context context)
  {
    this(context, null);
  }

  public ZAppBarLayout(Context context, AttributeSet attrs)
  {
    super(context, attrs);


    // 编辑模式添加view 会导致渲染不可见
    if (isInEditMode()) return;

//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//      TypedValue typedValue = new TypedValue();
//      context.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
//      int color = Color.parseColor(typedValue.coerceToString().toString());
//      View v = ZStatusBar.createStatusBarView((Activity) getContext(), color);
//      addView(v);
//    } else

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      TypedValue typedValue = new TypedValue();
      context.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
      int color = Color.parseColor(typedValue.coerceToString().toString());
      statusBarView = ZStatusBar.createStatusBarView((Activity) getContext(), color);
      addView(statusBarView);
    }

    TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ZAppBarLayout);
    if (ta.hasValue(R.styleable.ZAppBarLayout_backgroundStatusBar)) {
      int c = ta.getColor(R.styleable.ZAppBarLayout_backgroundStatusBar, Color.BLACK);
      if (c != Color.BLACK) {
        statusBarView.setBackgroundColor(c);
        this.setBackgroundColor(c);
      }
    }
    ta.recycle();
  }

  public void setStatusBarViewBackgroundColor(@ColorInt int color)
  {
    if (statusBarView != null) {
      statusBarView.setBackgroundColor(color);
    }
  }


}

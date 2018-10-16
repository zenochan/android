package name.zeno.android.widget

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.annotation.ColorInt
import com.google.android.material.appbar.AppBarLayout
import name.zeno.android.system.ZStatusBar
import name.zeno.android.util.R

/**
 * Create Date: 16/6/15
 *
 * @author 陈治谋 (513500085@qq.com)
 */
open class ZAppBarLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : AppBarLayout(context, attrs) {
  var statusBarView: View? = null
    private set

  init {
    init(context, attrs)
  }

  fun init(context: Context, attrs: AttributeSet?) {


    // 编辑模式添加view 会导致渲染不可见
    if (isInEditMode) return

    //    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
    //      TypedValue typedValue = new TypedValue();
    //      context.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
    //      int color = Color.parseColor(typedValue.coerceToString().toString());
    //      View v = ZStatusBar.createStatusBarView((Activity) getContext(), color);
    //      addView(v);
    //    } else

    //4.4+
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      val typedValue = TypedValue()
      context.theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)
      val color = Color.parseColor(typedValue.coerceToString().toString())
      statusBarView = ZStatusBar.createStatusBarView(getContext() as Activity, color)
      addView(statusBarView)

      val ta = context.obtainStyledAttributes(attrs, R.styleable.ZAppBarLayout)
      if (ta.hasValue(R.styleable.ZAppBarLayout_backgroundStatusBar)) {
        val c = ta.getColor(R.styleable.ZAppBarLayout_backgroundStatusBar, Color.BLACK)
        if (c != Color.BLACK) {
          statusBarView?.setBackgroundColor(c)
          //          this.setBackgroundColor(c);
        }
      }
      ta.recycle()
    }
  }

  fun setStatusBarViewBackgroundColor(@ColorInt color: Int) {
    statusBarView?.setBackgroundColor(color)
  }
}

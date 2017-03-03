package name.zeno.android.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import name.zeno.android.tint.TintHelper;
import name.zeno.android.util.R;

/**
 * Create Date: 16/7/8
 *
 * @author 陈治谋 (513500085@qq.com)
 */
public class ZButton extends AppCompatButton
{
  public ZButton(Context context)
  {
    this(context, null);
  }

  public ZButton(Context context, AttributeSet attrs)
  {
    this(context, attrs, 0);
  }

  public ZButton(Context context, AttributeSet attrs, int defStyleAttr)
  {
    super(context, attrs, defStyleAttr);
    TintHelper.loadFromAttributes(this, attrs, defStyleAttr, R.styleable.ZButton,
        R.styleable.ZButton_backgroundTint,
        R.styleable.ZButton_backgroundTintMode
    );
  }
}

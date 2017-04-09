package name.zeno.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import lombok.Setter;
import name.zeno.android.util.R;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/03/30
 */
public class RateLayout extends FrameLayout
{
  @Setter private float rate = 0;

  public RateLayout(Context context)
  {
    this(context, null);
  }

  public RateLayout(Context context, AttributeSet attrs)
  {
    this(context, attrs, 0);
  }

  public RateLayout(Context context, AttributeSet attrs, int defStyleAttr)
  {
    super(context, attrs, defStyleAttr);


    TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RateLayout, defStyleAttr, 0);
    if (ta.hasValue(R.styleable.RateLayout_rate)) {
      rate = ta.getFloat(R.styleable.RateLayout_rate, 0);
      rate = rate > 0 ? rate : 0;
    }
    ta.recycle();
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
  {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    //按比例显示 ImageView 高度
    if (rate != 0) {
      int height = (int) (getMeasuredWidth() * rate);
      height = height < 0 ? -1 : height;
      getLayoutParams().height = height;
    }
  }
}

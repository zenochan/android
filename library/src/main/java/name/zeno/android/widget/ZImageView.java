package name.zeno.android.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import name.zeno.android.tint.TintHelper;
import name.zeno.android.tint.TintInfo;
import name.zeno.android.tint.TintableDrawableView;
import name.zeno.android.util.R;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 16/6/19
 */
public class ZImageView extends AppCompatImageView implements TintableDrawableView
{
  private TintInfo drawableTintInfo;

  private float rate = 0;

  public void setRate(float rate)
  {
    this.rate = rate;
  }

  public ZImageView(Context context)
  {
    this(context, null);
  }

  public ZImageView(Context context, AttributeSet attrs)
  {
    this(context, attrs, 0);
  }

  public ZImageView(Context context, AttributeSet attrs, int defStyleAttr)
  {
    super(context, attrs, defStyleAttr);

    TintHelper.loadFromAttributes(this, attrs, defStyleAttr,
        R.styleable.ZImageView,
        R.styleable.ZImageView_backgroundTint,
        R.styleable.ZImageView_backgroundTintMode
    );

    drawableTintInfo = new TintInfo();
    drawableTintInfo.hasTintList = true;

    TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ZImageView, defStyleAttr, 0);
    if (ta.hasValue(R.styleable.ZImageView_drawableTint)) {
      ColorStateList c = ta.getColorStateList(R.styleable.ZImageView_drawableTint);
      drawableTintInfo.tintList = c;
      TintHelper.setSupportDrawableTintList(this, drawableTintInfo, c);
    }
    if (ta.hasValue(R.styleable.ZImageView_rate)) {
      rate = ta.getFloat(R.styleable.ZImageView_rate, 0);
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

  @Override public void setImageDrawable(Drawable drawable)
  {
    super.setImageDrawable(drawable);
    if (drawableTintInfo != null) {
      TintHelper.setSupportDrawableTintList(this, drawableTintInfo, drawableTintInfo.tintList);
    }
  }

  @Nullable @Override public ColorStateList getSupportDrawableTintList()
  {
    return null;
  }

  @Override public void setSupportDrawableTintList(@Nullable ColorStateList tint)
  {
    TintHelper.setSupportDrawableTintList(this, drawableTintInfo, tint);
  }

  @Override public void setSupportDrawableTintMode(@Nullable PorterDuff.Mode tintMode)
  {
    //do nothing
  }

  @Nullable @Override public PorterDuff.Mode getSupportDrawableTintMode()
  {
    return TintHelper.getSupportTintMode(drawableTintInfo);
  }
}

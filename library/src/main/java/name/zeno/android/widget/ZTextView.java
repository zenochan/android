package name.zeno.android.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import name.zeno.android.tint.TintHelper;
import name.zeno.android.tint.TintInfo;
import name.zeno.android.tint.TintableCompoundDrawableView;
import name.zeno.android.util.R;

/**
 * Create Date: 16/6/16
 *
 * @author 陈治谋 (513500085@qq.com)
 */
public class ZTextView extends AppCompatTextView implements TintableCompoundDrawableView
{
  private TintInfo compoundDrawableTint;

  int drawableWidth        = -1;
  int drawableHeight       = -1;
  int LeftDrawableWidth    = -1;
  int LeftDrawableHeight   = -1;
  int TopDrawableWidth     = -1;
  int TopDrawableHeight    = -1;
  int RightDrawableWidth   = -1;
  int RightDrawableHeight  = -1;
  int BottomDrawableWidth  = -1;
  int BottomDrawableHeight = -1;

  public ZTextView(Context context)
  {
    this(context, null);
  }

  public ZTextView(Context context, AttributeSet attrs)
  {
    this(context, attrs, 0);
  }

  public ZTextView(Context context, AttributeSet attrs, int defStyleAttr)
  {
    super(context, attrs, defStyleAttr);
    compoundDrawableTint = new TintInfo();
    compoundDrawableTint.hasTintList = true;

    TintHelper.loadFromAttributes(this, attrs, defStyleAttr,
        R.styleable.ZTextView,
        R.styleable.ZTextView_backgroundTint,
        R.styleable.ZTextView_backgroundTintMode
    );

    TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ZTextView, defStyleAttr, 0);
    if (ta.hasValue(R.styleable.ZTextView_compoundDrawableTint)) {
      ColorStateList c = ta.getColorStateList(R.styleable.ZTextView_compoundDrawableTint);
      compoundDrawableTint.tintList = c;
      TintHelper.setSupportCompoundDrawableTintList(this, compoundDrawableTint, c);
    }
    initCompoundDrawableSize(ta);
    ta.recycle();
  }

  @Nullable @Override public ColorStateList getSupportCompoundDrawableTintList()
  {
    return TintHelper.getSupportTintList(compoundDrawableTint);
  }

  @Override public void setSupportCompoundDrawableTintList(@Nullable ColorStateList tint)
  {
    TintHelper.setSupportCompoundDrawableTintList(this, compoundDrawableTint, tint);
  }

  @Override public void setSupportCompoundDrawableTintMode(@Nullable PorterDuff.Mode tintMode)
  {
    TintHelper.setSupportTintMode(this, compoundDrawableTint, tintMode);
  }

  @Nullable @Override public PorterDuff.Mode getSupportCompoundDrawableTintMode()
  {
    return TintHelper.getSupportTintMode(compoundDrawableTint);
  }

  @Override public void setSelected(boolean selected)
  {
    super.setSelected(selected);
    setSupportCompoundDrawableTintList(getSupportCompoundDrawableTintList());
  }

  private void initCompoundDrawableSize(TypedArray tArray)
  {
    //遍历参数:
    //A.将index从TypedArray中读出来
    //B.得到的这个index对应于attrs.xml中设置的参数名称在R中编译得到的数
    //C.获取宽和高
    int index;
    for (int i = 0; i < tArray.getIndexCount(); i++) {
      index = tArray.getIndex(i);
      if (index == R.styleable.ZTextView_drawableWidth) {
        drawableWidth = tArray.getDimensionPixelSize(index, -1);
      } else if (index == R.styleable.ZTextView_drawableHeight) {
        drawableHeight = tArray.getDimensionPixelSize(index, -1);
      } else if (index == R.styleable.ZTextView_LeftDrawableWidth) {
        LeftDrawableWidth = tArray.getDimensionPixelSize(index, -1);
      } else if (index == R.styleable.ZTextView_LeftDrawableHeight) {
        LeftDrawableHeight = tArray.getDimensionPixelSize(index, -1);
      } else if (index == R.styleable.ZTextView_TopDrawableWidth) {
        TopDrawableWidth = tArray.getDimensionPixelSize(index, -1);
      } else if (index == R.styleable.ZTextView_TopDrawableHeight) {
        TopDrawableHeight = tArray.getDimensionPixelSize(index, -1);
      } else if (index == R.styleable.ZTextView_RightDrawableWidth) {
        RightDrawableWidth = tArray.getDimensionPixelSize(index, -1);
      } else if (index == R.styleable.ZTextView_RightDrawableHeight) {
        RightDrawableHeight = tArray.getDimensionPixelSize(index, -1);
      } else if (index == R.styleable.ZTextView_BottomDrawableWidth) {
        BottomDrawableWidth = tArray.getDimensionPixelSize(index, -1);
      } else if (index == R.styleable.ZTextView_BottomDrawableHeight) {
        BottomDrawableHeight = tArray.getDimensionPixelSize(index, -1);
      }
    }

    // 获取各个方向的图片，按照：l-t-r-b(左-上-右-下) 的顺序存于数组中
    Drawable[] drawables = getCompoundDrawables();
    for (int i = 0; i < drawables.length; i++) {
      setImageSize(drawables[i], i);
    }
    // 将图片放回到TextView中
    setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
  }

  private void setImageSize(Drawable drawable, int position)
  {
    if (null != drawable) {
      int width = -1;
      int height = -1;
      // 0-left; 1-top; 2-right; 3-bottom;
      switch (position) {
        case 0:
          width = LeftDrawableWidth;
          height = LeftDrawableHeight;
          break;
        case 1:
          width = RightDrawableWidth;
          height = RightDrawableHeight;
          break;
        case 2:
          width = TopDrawableWidth;
          height = TopDrawableHeight;
          break;
        case 3:
          width = BottomDrawableWidth;
          height = BottomDrawableHeight;
          break;
      }
      if (width == -1) {
        width = drawableWidth;
      }
      if (height == -1) {
        height = drawableHeight;
      }
      if (width != -1 && height != -1) {
        drawable.setBounds(0, 0, width, height);
      }
    }
  }
}

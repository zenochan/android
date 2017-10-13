package name.zeno.android.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import name.zeno.android.tint.TintHelper;
import name.zeno.android.tint.TintInfo;
import name.zeno.android.tint.TintableCompoundDrawableView;
import name.zeno.android.util.R;

import static android.R.attr.cacheColorHint;
import static android.R.attr.drawable;
import static android.R.attr.drawableLeft;

/**
 * Create Date: 16/6/16
 *
 * @author 陈治谋 (513500085@qq.com)
 */
public class ZTextView extends AppCompatTextView implements TintableCompoundDrawableView
{
  private TintInfo compoundDrawableTint;

  int drawableGravity      = -1;
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

  private static Bitmap getBitmap(Drawable drawable)
  {
    BitmapDrawable bd = (BitmapDrawable) drawable;
    return bd.getBitmap();
  }

  private void initCompoundDrawableSize(TypedArray tArray)
  {
    //遍历参数
    drawableGravity = tArray.getInt(R.styleable.ZTextView_drawableGravity, -1);
    drawableWidth = tArray.getDimensionPixelSize(R.styleable.ZTextView_drawableWidth, -1);
    drawableHeight = tArray.getDimensionPixelSize(R.styleable.ZTextView_drawableHeight, -1);
    LeftDrawableWidth = tArray.getDimensionPixelSize(R.styleable.ZTextView_LeftDrawableWidth, -1);
    LeftDrawableHeight = tArray.getDimensionPixelSize(R.styleable.ZTextView_LeftDrawableHeight, -1);
    TopDrawableWidth = tArray.getDimensionPixelSize(R.styleable.ZTextView_TopDrawableWidth, -1);
    TopDrawableHeight = tArray.getDimensionPixelSize(R.styleable.ZTextView_TopDrawableHeight, -1);
    RightDrawableWidth = tArray.getDimensionPixelSize(R.styleable.ZTextView_RightDrawableWidth, -1);
    RightDrawableHeight = tArray.getDimensionPixelSize(R.styleable.ZTextView_RightDrawableHeight, -1);
    BottomDrawableWidth = tArray.getDimensionPixelSize(R.styleable.ZTextView_BottomDrawableWidth, -1);
    BottomDrawableHeight = tArray.getDimensionPixelSize(R.styleable.ZTextView_BottomDrawableHeight, -1);


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
      int width  = -1;
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
        int offset = 20;
        drawable.setBounds(0, 0, width, height);
      }
    }
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
  {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  @Override protected void onDraw(Canvas canvas)
  {
    Drawable[] drawables = getCompoundDrawables();
    switch (drawableGravity) {
      case 0://l
        drawableAtLeft(drawables[1]);
        drawableAtLeft(drawables[3]);
        break;
      case 1://t
        drawableAtTop(drawables[0]);
        drawableAtTop(drawables[2]);
        break;
      case 2://r
        drawableAtRight(drawables[1]);
        drawableAtRight(drawables[3]);
        break;
      case 3://b
        drawableAtBottom(drawables[0]);
        drawableAtBottom(drawables[2]);
        break;
    }
    super.onDraw(canvas);
  }

  private void drawableAtTop(@Nullable Drawable drawable)
  {
    if (drawable != null) {
      Rect bounds = drawable.getBounds();
      int  height = bounds.bottom - bounds.top;
      bounds.top = (height - getHeight()) / 2;
      bounds.bottom = bounds.top + height;
    }
  }

  private void drawableAtBottom(@Nullable Drawable drawable)
  {
    if (drawable != null) {
      Rect bounds = drawable.getBounds();
      int  height = bounds.bottom - bounds.top;
      bounds.top = (-height + getHeight()) / 2;
      bounds.bottom = bounds.top + height;
    }
  }


  private void drawableAtLeft(@Nullable Drawable drawable)
  {
    if (drawable != null) {
      Rect bounds = drawable.getBounds();
      int  width  = bounds.right - bounds.left;
      bounds.left = (width - getWidth()) / 2;
      bounds.right = bounds.left + width;
    }
  }


  private void drawableAtRight(@Nullable Drawable drawable)
  {
    if (drawable != null) {
      Rect bounds = drawable.getBounds();
      int  width  = bounds.right - bounds.left;
      bounds.left = (-width + getWidth()) / 2;
      bounds.right = bounds.left + width;
    }
  }
}

package name.zeno.android.widget;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class ClipImageLayout extends RelativeLayout
{

  private ClipImageView  clipView;
  private ClipBorderView borderView;

  private int paddingHorizontal = 60;

  public ClipImageLayout(Context context, AttributeSet attrs)
  {
    super(context, attrs);

    clipView = new ClipImageView(context);
    borderView = new ClipBorderView(context);

    ViewGroup.LayoutParams lp = new LayoutParams(-1, -1);
    this.addView(clipView, lp);
    this.addView(borderView, lp);

    // 计算padding的px
    paddingHorizontal = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, paddingHorizontal, getResources().getDisplayMetrics());
    clipView.setHorizontalPadding(paddingHorizontal);
    borderView.setHorizontalPadding(paddingHorizontal);
  }

  /**
   * 对外公布设置边距的方法,单位为dp
   */
  public void setHorizontalPadding(int mHorizontalPadding)
  {
    this.paddingHorizontal = mHorizontalPadding;
  }

  /**
   * 裁切图片
   */
  public Bitmap clip()
  {
    return clipView.clip();
  }

  public void setBitmap(Bitmap bitmap)
  {
    clipView.setImageBitmap(bitmap);
  }

}

package name.zeno.android.widget.indicator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import name.zeno.android.util.R;
import name.zeno.android.widget.PageIndicator;
import name.zeno.android.widget.autoscrollviewpager.AutoScrollViewPager;

/**
 * Create Date: 16/7/4
 *
 * @author 陈治谋 (513500085@qq.com)
 */
public class NumberPageIndicator extends AppCompatTextView implements PageIndicator
{
  private ViewPager pager;
  private Paint     paint;

  private int mode = 0;

  @interface Mode
  {
    int NORMAL   = 0; // 常规
    int OVERTURN = 1; // 翻转
  }

  public NumberPageIndicator(Context context)
  {
    this(context, null);
  }

  public NumberPageIndicator(Context context, AttributeSet attrs)
  {
    this(context, attrs, 0);
  }

  public NumberPageIndicator(Context context, AttributeSet attrs, int defStyleAttr)
  {
    super(context, attrs, defStyleAttr);
    setText("1/1");
    paint = new Paint();
    paint.setAntiAlias(true);
    TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.NumberPageIndicator);
    mode = ta.getInt(R.styleable.NumberPageIndicator_indicateMode, mode);
    ta.recycle();
  }

  @Override protected void onDraw(Canvas canvas)
  {
    if (mode == Mode.OVERTURN) {
      int w = getWidth();
      int h = getHeight();

      paint.setColor(Color.parseColor("#bdbdbd"));
      canvas.drawCircle(w / 2, h / 2, Math.min(w, h) / 2, paint);
    }
    super.onDraw(canvas);
  }

  @Override public void notifyDataSetChanged()
  {
    invalidate();
  }

  @Override public void setViewPager(ViewPager view)
  {
    setViewPager(view, 0);
  }

  @Override public void setViewPager(ViewPager view, int initialPosition)
  {
    pager = view;
    view.addOnPageChangeListener(this);
  }

  @Deprecated
  @Override public void setCurrentItem(int item)
  {
  }

  @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
  {
    if (mode != Mode.OVERTURN) return;

    float scale = (float) (Math.abs(positionOffset - 0.5) / 0.5);
    setScaleX(scale);

    int currP;
    int count = pager.getAdapter().getCount();

    if ((pager instanceof AutoScrollViewPager) && ((AutoScrollViewPager) pager).isInfinite()) {
      currP = pager.getCurrentItem();
      if (currP == position) {
        currP = currP + count - 1 % count;
      }
    } else {
      position++;
      currP = pager.getCurrentItem();
      if (currP == position && positionOffset <= 0.5) {
        currP--;
      }
    }

    onPageSelected(positionOffset <= 0.5 ? currP : position);
  }

  @SuppressLint("DefaultLocale")
  @Override public void onPageSelected(int position)
  {
    int count = pager.getAdapter().getCount();
    position = position % count;
    position += 1;
    setText(String.format("%d/%d", position, count));
  }

  @Override public void onPageScrollStateChanged(int state)
  {

  }
}

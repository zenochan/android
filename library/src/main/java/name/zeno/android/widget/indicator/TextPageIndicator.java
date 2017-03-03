package name.zeno.android.widget.indicator;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import name.zeno.android.widget.PageIndicator;

/**
 * Create Date: 16/7/4
 *
 * @author 陈治谋 (513500085@qq.com)
 */
public class TextPageIndicator extends AppCompatTextView implements PageIndicator
{
  private ViewPager pager;

  public TextPageIndicator(Context context)
  {
    this(context, null);
  }

  public TextPageIndicator(Context context, AttributeSet attrs)
  {
    this(context, attrs, 0);
  }

  public TextPageIndicator(Context context, AttributeSet attrs, int defStyleAttr)
  {
    super(context, attrs, defStyleAttr);
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
    pager.setCurrentItem(item);
  }

  @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
  {
    float scale = 0.5F + (float) Math.abs(positionOffset - 0.5);
    setAlpha(scale);

    int currP = pager.getCurrentItem();
    int count = pager.getAdapter().getCount();
    if (currP == position) {
      currP = currP + count - 1 % count;
    }
    onPageSelected(positionOffset <= 0.5 ? currP : position);
  }

  @Override public void onPageSelected(int position)
  {
    int count = pager.getAdapter().getCount();
    position = position % count;
    setText(pager.getAdapter().getPageTitle(position));
  }

  @Override public void onPageScrollStateChanged(int state)
  {

  }
}

package name.zeno.android.widget.autoscrollviewpager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.animation.Interpolator;

import java.lang.reflect.Field;

import name.zeno.android.listener.Action2;

public class AutoScrollViewPager extends ViewPager implements Handler.Callback
{

  private boolean infinite = true;

  public boolean isInfinite()
  {
    return infinite;
  }

  private static final int MSG_AUTO_SCROLL            = 0;
  private static final int DEFAULT_INTERNAL_IM_MILLIS = 2000;

  private PagerAdapter              adapter;
  private AutoScrollPagerAdapter    adapterWrapper;
  private InnerOnPageChangeListener listener;
  private AutoScrollFactorScroller  scroller;
  private Handler handler = new Handler(this);

  private boolean autoScroll = false;
  private int intervalInMillis;

  private float mInitialMotionX;
  private float mInitialMotionY;
  private float mLastMotionX;
  private float mLastMotionY;
  private int   touchSlop;

  private Action2<AutoScrollViewPager, Integer> onClickPage;

  public Action2<AutoScrollViewPager, Integer> getOnClickPage()
  {
    return onClickPage;
  }

  public void setOnClickPage(Action2<AutoScrollViewPager, Integer> onClickPage)
  {
    this.onClickPage = onClickPage;
  }

  @Override public boolean handleMessage(Message message)
  {
    switch (message.what) {
      case MSG_AUTO_SCROLL:
        setCurrentItem(getCurrentItem() + 1);
        handler.sendEmptyMessageDelayed(MSG_AUTO_SCROLL, intervalInMillis);
        break;
    }
    return true;
  }

  public AutoScrollViewPager(Context context)
  {
    this(context, null);
  }

  public AutoScrollViewPager(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    init();
  }

  public void startAutoScroll()
  {
    startAutoScroll(intervalInMillis != 0 ? intervalInMillis : DEFAULT_INTERNAL_IM_MILLIS);
  }

  public void startAutoScroll(int intervalInMillis)
  {
    // Only post scroll message when necessary.
    if (getCount() > 1) {
      this.intervalInMillis = intervalInMillis;
      autoScroll = true;
      handler.removeMessages(MSG_AUTO_SCROLL);
      handler.sendEmptyMessageDelayed(MSG_AUTO_SCROLL, intervalInMillis);
    }
    setCurrentItem(getCurrentItem());
  }

  public void stopAutoScroll()
  {
    autoScroll = false;
    handler.removeMessages(MSG_AUTO_SCROLL);
    setCurrentItem(getCurrentItem());
  }

  public void setInfinite(boolean infinite)
  {
    this.infinite = infinite;
    if (adapterWrapper != null) {
      adapterWrapper.setInfinite(this.infinite);
    }
  }

  public void setInterval(int intervalInMillis)
  {
    this.intervalInMillis = intervalInMillis;
  }

  public void setScrollFactgor(double factor)
  {
    setScrollerIfNeeded();
    scroller.setFactor(factor);
  }

  @Override
  public void setOnPageChangeListener(OnPageChangeListener listener)
  {
    this.listener.setOnPageChangeListener(listener);
  }

  @Override
  public void setAdapter(PagerAdapter adapter)
  {
    this.adapter = adapter;
    if (this.adapter == null) {
      adapterWrapper = null;
    } else {
      adapterWrapper = new AutoScrollPagerAdapter(adapter);
      adapterWrapper.setInfinite(this.infinite);
    }
    super.setAdapter(adapterWrapper);

    if (adapter != null && adapter.getCount() > 0) {
      post(() -> setCurrentItem(0, false));
    }
  }

  @Override
  public PagerAdapter getAdapter()
  {
    // In order to be compatible with ViewPagerIndicator
    return adapter;
  }

  @Override
  public void setCurrentItem(int item)
  {
    item += positionOffset();
    if (adapterWrapper != null && adapterWrapper.getCount() > 0) {
      item %= adapterWrapper.getCount();
    }
//    super.setCurrentItem(item + positionOffset());
    super.setCurrentItem(item);
  }

  @Override
  public void setCurrentItem(int item, boolean smoothScroll)
  {
    super.setCurrentItem(item + positionOffset(), smoothScroll);
  }

  @Override
  public int getCurrentItem()
  {
    int curr = super.getCurrentItem();

    if (infinite && adapter != null && adapter.getCount() > 1) {
      if (curr == 0) {
        curr = adapter.getCount() - 1;
      } else if (curr == adapterWrapper.getCount() - 1) {
        curr = 0;
      } else {
        curr = curr - 1;
      }
    }
    return curr;
  }

  @Override
  public boolean onTouchEvent(MotionEvent ev)
  {
    switch (MotionEventCompat.getActionMasked(ev)) {
      case MotionEvent.ACTION_DOWN:
        if (!infinite) {
        } else if (getCurrentItemOfWrapper() + 1 == getCountOfWrapper()) {
          setCurrentItem(0, false);
        } else if (getCurrentItemOfWrapper() == 0) {
          setCurrentItem(getCount() - 1, false);
        }
        handler.removeMessages(MSG_AUTO_SCROLL);
        mInitialMotionX = ev.getX();
        mInitialMotionY = ev.getY();
        break;
      case MotionEvent.ACTION_MOVE:
        mLastMotionX = ev.getX();
        mLastMotionY = ev.getY();
        if ((int) Math.abs(mLastMotionX - mInitialMotionX) > touchSlop || (int) Math.abs(mLastMotionY - mInitialMotionY) > touchSlop) {
          mInitialMotionX = 0.0f;
          mInitialMotionY = 0.0f;
        }
        break;
      case MotionEvent.ACTION_UP:
        // 点击调转会歪掉
//        if (autoScroll) {
//          startAutoScroll();
//        }

        // Manually swipe not affected by scroll factor.
        if (scroller != null) {
          final double lastFactor = scroller.getFactor();
          scroller.setFactor(1);
          post(() -> scroller.setFactor(lastFactor));
        }

        mLastMotionX = ev.getX();
        mLastMotionY = ev.getY();
        if ((int) mInitialMotionX != 0 && (int) mInitialMotionY != 0) {
          if ((int) Math.abs(mLastMotionX - mInitialMotionX) < touchSlop
              && (int) Math.abs(mLastMotionY - mInitialMotionY) < touchSlop)
          {
            mInitialMotionX = 0.0f;
            mInitialMotionY = 0.0f;
            mLastMotionX = 0.0f;
            mLastMotionY = 0.0f;
            if (onClickPage != null) {
              onClickPage.call(this, getCurrentItem());
            }
          }
        }
        break;
    }
    return super.onTouchEvent(ev);
  }

  @Override protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    startAutoScroll();
  }

  @Override
  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    stopAutoScroll();
    handler.removeMessages(MSG_AUTO_SCROLL);
  }

  private void init()
  {
    listener = new InnerOnPageChangeListener();
    super.addOnPageChangeListener(listener);

    touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop() * 2;
  }

  private int positionOffset()
  {
    return infinite ? 1 : 0;
  }

  // wrapper item 位置
  private int getCurrentItemOfWrapper()
  {
    return super.getCurrentItem();
  }

  // wrapper 的数量
  private int getCountOfWrapper()
  {
    return adapterWrapper == null ? 0 : adapterWrapper.getCount();
  }

  // 实际的数量
  private int getCount()
  {
    return adapter == null ? 0 : adapter.getCount();
  }

  private void setScrollerIfNeeded()
  {
    if (scroller == null) {
      try {
        Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
        scrollerField.setAccessible(true);
        Field interpolatorField = ViewPager.class.getDeclaredField("sInterpolator");
        interpolatorField.setAccessible(true);
        scroller = new AutoScrollFactorScroller(getContext(), (Interpolator) interpolatorField.get(null));
        scrollerField.set(this, scroller);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private class InnerOnPageChangeListener implements OnPageChangeListener
  {
    private OnPageChangeListener listener;
    private int lastSelectedPage = -1;

    InnerOnPageChangeListener()
    {
    }

    void setOnPageChangeListener(OnPageChangeListener listener)
    {
      this.listener = listener;
    }

    @Override
    public void onPageScrollStateChanged(int state)
    {
      if (infinite && state == SCROLL_STATE_IDLE && getCount() > 1) {
        if (getCurrentItemOfWrapper() == 0) {
          // 滚动到最后一页
          setCurrentItem(getCount() - 1, false);
        } else if (getCurrentItemOfWrapper() == getCountOfWrapper() - 1) {
          // 滚动到第一页
          setCurrentItem(0, false);
        }
      }
      if (listener != null) {
        listener.onPageScrollStateChanged(state);
      }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {
      if (listener != null && position > 0 && position < getCount()) {
        listener.onPageScrolled(position - positionOffset(), positionOffset, positionOffsetPixels);
      }
    }

    @Override
    public void onPageSelected(final int position)
    {
//            if (listener != null && position != 0 && position < wrappedPagerAdapter.getCount() + 1) {
      if (listener != null) {
        final int pos;
        // Fix position
        if (!infinite) {
          //不做 position 修正
          pos = position;
        } else if (position == 0) {
          pos = getCount() - 1;
        } else if (position == getCountOfWrapper() - 1) {
          pos = 0;
        } else {
          pos = position - 1;
        }

        // Comment this, onPageSelected will be triggered twice for position 0 and getCount -1.
        // Uncomment this, PageIndicator will have trouble.
//                if (lastSelectedPage != pos) {
        lastSelectedPage = pos;
        // Post a Runnable in order to be compatible with ViewPagerIndicator because
        // onPageSelected is invoked before onPageScrollStateChanged.
        AutoScrollViewPager.this.post(() -> listener.onPageSelected(pos));
//                }
      }
    }
  }

}

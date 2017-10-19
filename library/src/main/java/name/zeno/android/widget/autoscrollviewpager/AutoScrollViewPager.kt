package name.zeno.android.widget.autoscrollviewpager

import android.content.Context
import android.os.Handler
import android.os.Message
import android.support.v4.view.MotionEventCompat
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.view.animation.Interpolator

import name.zeno.android.listener.Action2

class AutoScrollViewPager @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ViewPager(context, attrs), Handler.Callback {

  var isInfinite = true
    set(infinite) {
      field = infinite
      if (adapterWrapper != null) {
        adapterWrapper!!.setInfinite(this.isInfinite)
      }
    }

  private var adapter: PagerAdapter? = null
  private var adapterWrapper: AutoScrollPagerAdapter? = null
  private var listener: InnerOnPageChangeListener? = null
  private var scroller: AutoScrollFactorScroller? = null
  private val _handler = Handler(this)

  private var autoScroll = false
  private var intervalInMillis: Int = 0

  private var mInitialMotionX: Float = 0.toFloat()
  private var mInitialMotionY: Float = 0.toFloat()
  private var mLastMotionX: Float = 0.toFloat()
  private var mLastMotionY: Float = 0.toFloat()
  private var touchSlop: Int = 0

  var onClickPage: Action2<AutoScrollViewPager, Int>? = null

  // wrapper item 位置
  private val currentItemOfWrapper: Int
    get() = super.getCurrentItem()

  // wrapper 的数量
  private val countOfWrapper: Int
    get() = if (adapterWrapper == null) 0 else adapterWrapper!!.count

  // 实际的数量
  private val count: Int
    get() = if (adapter == null) 0 else adapter!!.count

  override fun handleMessage(message: Message): Boolean {
    when (message.what) {
      MSG_AUTO_SCROLL -> {
        currentItem = currentItem + 1
        _handler.sendEmptyMessageDelayed(MSG_AUTO_SCROLL, intervalInMillis.toLong())
      }
    }
    return true
  }

  init {
    init()
  }

  @JvmOverloads
  fun startAutoScroll(intervalInMillis: Int = DEFAULT_INTERNAL_IM_MILLIS) {
    if (intervalInMillis <= 0) throw IllegalArgumentException("intervalInMillis must > 0 but $intervalInMillis")

    // Only post scroll message when necessary.
    if (count > 1) {
      this.intervalInMillis = intervalInMillis
      autoScroll = true
      _handler.removeMessages(MSG_AUTO_SCROLL)
      _handler.sendEmptyMessageDelayed(MSG_AUTO_SCROLL, intervalInMillis.toLong())
    }
    currentItem = currentItem
  }

  fun stopAutoScroll() {
    autoScroll = false
    _handler.removeMessages(MSG_AUTO_SCROLL)
    currentItem = currentItem
  }

  fun setInterval(intervalInMillis: Int) {
    this.intervalInMillis = intervalInMillis
  }

  fun setScrollFactgor(factor: Double) {
    setScrollerIfNeeded()
    scroller!!.factor = factor
  }

  override fun setOnPageChangeListener(listener: ViewPager.OnPageChangeListener) {
    this.listener!!.setOnPageChangeListener(listener)
  }

  override fun setAdapter(adapter: PagerAdapter?) {
    this.adapter = adapter
    if (this.adapter == null) {
      adapterWrapper = null
    } else {
      adapterWrapper = AutoScrollPagerAdapter(adapter)
      adapterWrapper!!.setInfinite(this.isInfinite)
    }
    super.setAdapter(adapterWrapper)

    if (adapter != null && adapter.count > 0) {
      post { setCurrentItem(0, false) }
    }
  }

  override fun getAdapter(): PagerAdapter? {
    // In order to be compatible with ViewPagerIndicator
    return adapter
  }

  override fun setCurrentItem(item: Int) {
    var item = item
    item += positionOffset()
    if (adapterWrapper != null && adapterWrapper!!.count > 0) {
      item %= adapterWrapper!!.count
    }
    //    super.setCurrentItem(item + positionOffset());
    super.setCurrentItem(item)
  }

  override fun setCurrentItem(item: Int, smoothScroll: Boolean) {
    super.setCurrentItem(item + positionOffset(), smoothScroll)
  }

  override fun getCurrentItem(): Int {
    var curr = super.getCurrentItem()

    if (isInfinite && adapter != null && adapter!!.count > 1) {
      if (curr == 0) {
        curr = adapter!!.count - 1
      } else if (curr == adapterWrapper!!.count - 1) {
        curr = 0
      } else {
        curr = curr - 1
      }
    }
    return curr
  }

  override fun onTouchEvent(ev: MotionEvent): Boolean {
    when (MotionEventCompat.getActionMasked(ev)) {
      MotionEvent.ACTION_DOWN -> {
        if (!isInfinite) {
        } else if (currentItemOfWrapper + 1 == countOfWrapper) {
          setCurrentItem(0, false)
        } else if (currentItemOfWrapper == 0) {
          setCurrentItem(count - 1, false)
        }
        _handler.removeMessages(MSG_AUTO_SCROLL)
        mInitialMotionX = ev.x
        mInitialMotionY = ev.y
      }
      MotionEvent.ACTION_MOVE -> {
        mLastMotionX = ev.x
        mLastMotionY = ev.y
        if (Math.abs(mLastMotionX - mInitialMotionX).toInt() > touchSlop || Math.abs(mLastMotionY - mInitialMotionY).toInt() > touchSlop) {
          mInitialMotionX = 0.0f
          mInitialMotionY = 0.0f
        }
      }
      MotionEvent.ACTION_UP -> {
        // 点击调转会歪掉
        //        if (autoScroll) {
        //          startAutoScroll();
        //        }

        // Manually swipe not affected by scroll factor.
        if (scroller != null) {
          val lastFactor = scroller!!.factor
          scroller!!.factor = 1.0
          post { scroller!!.factor = lastFactor }
        }

        mLastMotionX = ev.x
        mLastMotionY = ev.y
        if (mInitialMotionX.toInt() != 0 && mInitialMotionY.toInt() != 0) {
          if (Math.abs(mLastMotionX - mInitialMotionX).toInt() < touchSlop && Math.abs(mLastMotionY - mInitialMotionY).toInt() < touchSlop) {
            mInitialMotionX = 0.0f
            mInitialMotionY = 0.0f
            mLastMotionX = 0.0f
            mLastMotionY = 0.0f
            if (onClickPage != null) {
              onClickPage!!.call(this, currentItem)
            }
          }
        }
      }
    }
    return super.onTouchEvent(ev)
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    startAutoScroll()
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    stopAutoScroll()
    _handler.removeMessages(MSG_AUTO_SCROLL)
  }

  private fun init() {
    listener = InnerOnPageChangeListener()
    super.addOnPageChangeListener(listener)

    touchSlop = ViewConfiguration.get(context).scaledTouchSlop * 2
  }

  private fun positionOffset(): Int {
    return if (isInfinite) 1 else 0
  }

  private fun setScrollerIfNeeded() {
    if (scroller == null) {
      try {
        val scrollerField = ViewPager::class.java.getDeclaredField("mScroller")
        scrollerField.isAccessible = true
        val interpolatorField = ViewPager::class.java.getDeclaredField("sInterpolator")
        interpolatorField.isAccessible = true
        scroller = AutoScrollFactorScroller(context, interpolatorField.get(null) as Interpolator)
        scrollerField.set(this, scroller)
      } catch (e: Exception) {
        e.printStackTrace()
      }

    }
  }

  private inner class InnerOnPageChangeListener internal constructor() : ViewPager.OnPageChangeListener {
    private var listener: ViewPager.OnPageChangeListener? = null
    private var lastSelectedPage = -1

    internal fun setOnPageChangeListener(listener: ViewPager.OnPageChangeListener) {
      this.listener = listener
    }

    override fun onPageScrollStateChanged(state: Int) {
      if (isInfinite && state == ViewPager.SCROLL_STATE_IDLE && count > 1) {
        if (currentItemOfWrapper == 0) {
          // 滚动到最后一页
          setCurrentItem(count - 1, false)
        } else if (currentItemOfWrapper == countOfWrapper - 1) {
          // 滚动到第一页
          setCurrentItem(0, false)
        }
      }
      if (listener != null) {
        listener!!.onPageScrollStateChanged(state)
      }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
      if (listener != null && position > 0 && position < count) {
        listener!!.onPageScrolled(position - positionOffset(), positionOffset, positionOffsetPixels)
      }
    }

    override fun onPageSelected(position: Int) {
      //            if (listener != null && position != 0 && position < wrappedPagerAdapter.getCount() + 1) {
      if (listener != null) {
        val pos: Int
        // Fix position
        if (!isInfinite) {
          //不做 position 修正
          pos = position
        } else if (position == 0) {
          pos = count - 1
        } else if (position == countOfWrapper - 1) {
          pos = 0
        } else {
          pos = position - 1
        }

        // Comment this, onPageSelected will be triggered twice for position 0 and getCount -1.
        // Uncomment this, PageIndicator will have trouble.
        //                if (lastSelectedPage != pos) {
        lastSelectedPage = pos
        // Post a Runnable in order to be compatible with ViewPagerIndicator because
        // onPageSelected is invoked before onPageScrollStateChanged.
        this@AutoScrollViewPager.post { listener!!.onPageSelected(pos) }
        //                }
      }
    }
  }

  companion object {

    private val MSG_AUTO_SCROLL = 0
    private val DEFAULT_INTERNAL_IM_MILLIS = 2000
  }

}

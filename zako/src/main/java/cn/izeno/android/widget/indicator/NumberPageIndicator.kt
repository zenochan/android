package cn.izeno.android.widget.indicator

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.viewpager.widget.ViewPager
import cn.izeno.android.util.R
import cn.izeno.android.widget.PageIndicator
import cn.izeno.android.widget.autoscrollviewpager.AutoScrollViewPager

/**
 * Create Date: 16/7/4
 *
 * @author 陈治谋 (513500085@qq.com)
 */
class NumberPageIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr), PageIndicator<ViewPager> {
  private lateinit var pager: ViewPager
  private val paint: Paint

  private var mode = 0
  private val MODE_NORMAL = 0    // 常规
  private val MODE_OVERTURN = 1  // 翻转

  init {
    text = "1/1"
    paint = Paint()
    paint.isAntiAlias = true
    val ta = context.obtainStyledAttributes(attrs, R.styleable.NumberPageIndicator)
    mode = ta.getInt(R.styleable.NumberPageIndicator_indicateMode, mode)
    ta.recycle()
  }

  override fun onDraw(canvas: Canvas) {
    if (mode == MODE_OVERTURN) {
      val w = width
      val h = height

      paint.color = Color.parseColor("#bdbdbd")
      canvas.drawCircle((w / 2).toFloat(), (h / 2).toFloat(), (Math.min(w, h) / 2).toFloat(), paint)
    }
    super.onDraw(canvas)
  }

  override fun notifyDataSetChanged() {
    invalidate()
  }

  override fun setViewPager(view: ViewPager) {
    setViewPager(view, 0)
  }

  override fun setViewPager(view: ViewPager, initialPosition: Int) {
    pager = view
    view.addOnPageChangeListener(this)
  }

  @Deprecated("")
  override fun setCurrentItem(item: Int) {
  }

  override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    var p = position
    if (mode != MODE_OVERTURN) return

    val scale = (Math.abs(positionOffset - 0.5) / 0.5).toFloat()
    scaleX = scale

    var currP: Int
    val count = pager.adapter!!.count

    if (pager is AutoScrollViewPager && (pager as AutoScrollViewPager).isInfinite) {
      currP = pager.currentItem
      if (currP == p) {
        currP = currP + count - 1 % count
      }
    } else {
      p++
      currP = pager.currentItem
      if (currP == p && positionOffset <= 0.5) {
        currP--
      }
    }

    onPageSelected(if (positionOffset <= 0.5) currP else p)
  }

  @SuppressLint("DefaultLocale")
  override fun onPageSelected(position: Int) {
    var newP = position
    val count = pager.adapter!!.count
    newP %= count
    newP += 1
    text = String.format("%d/%d", newP, count)
  }

  override fun onPageScrollStateChanged(state: Int) {

  }
}

package cn.izeno.android.widget.indicator

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.viewpager.widget.ViewPager
import cn.izeno.android.widget.PageIndicator

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since  16/7/4
 */
class TextPageIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr), PageIndicator<ViewPager> {
  private var pager: ViewPager? = null

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
    pager!!.currentItem = item
  }

  override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    val scale = 0.5f + Math.abs(positionOffset - 0.5).toFloat()
    alpha = scale

    var currP = pager!!.currentItem
    val count = pager!!.adapter!!.count
    if (currP == position) {
      currP = currP + count - 1 % count
    }
    onPageSelected(if (positionOffset <= 0.5) currP else position)
  }

  override fun onPageSelected(position: Int) {
    val count = pager?.adapter?.count ?: return
    val p = position % count
    text = pager?.adapter?.getPageTitle(p)
  }

  override fun onPageScrollStateChanged(state: Int) {

  }
}

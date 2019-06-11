package cn.izeno.android.widget.autoscrollviewpager

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter

/**
 * Create Date: 16/7/13
 *
 * @author 陈治谋 (513500085@qq.com)
 */
abstract class ZPagerAdapter<T>(var data: List<T>? = null) : PagerAdapter() {

  fun getItem(position: Int): T? = data?.getOrNull(position)
  override fun getCount(): Int = data?.size ?: 0

  override fun isViewFromObject(view: View, `object`: Any): Boolean {
    return view === `object`
  }

  override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
    container.removeView(`object` as View)
  }

  override fun instantiateItem(container: ViewGroup, position: Int): Any {
    val view = ImageView(container.context)
    view.setBackgroundColor(Color.WHITE)
    loadImage(view, data!![position])
    container.addView(view)
    return view
  }

  open fun loadImage(view: ImageView, item: T) {}
}

package name.zeno.android.widget.autoscrollviewpager

import android.graphics.Color
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

/**
 * Create Date: 16/7/13
 *
 * @author 陈治谋 (513500085@qq.com)
 */
abstract class ZPagerAdapter<T> : PagerAdapter {
  var data: List<T>? = null


  @JvmOverloads constructor(data: List<T>? = null) {
    this.data = data
  }

  fun getItem(position: Int): T? {
    return if (data == null || position > data!!.size - 1) null else data!![position]
  }

  override fun getCount(): Int {
    return if (data == null) 0 else data!!.size
  }

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

  fun loadImage(view: ImageView, t: T) {
    // 加载图片
  }
}

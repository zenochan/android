package name.zeno.android.widget.autoscrollviewpager

import android.database.DataSetObserver
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup

class AutoScrollPagerAdapter(private val wrappedAdapter: PagerAdapter) : PagerAdapter() {

  var infinite = true

  init {
    wrappedAdapter.registerDataSetObserver(object : DataSetObserver() {
      override fun onChanged() {
        notifyDataSetChanged()
      }
    })
  }

  override fun getCount(): Int = when {
    infinite && wrappedAdapter.count > 1 -> wrappedAdapter.count + 2
    else -> wrappedAdapter.count
  }

  override fun instantiateItem(container: ViewGroup, position: Int): Any {
    //TODO 注释掉后可能会抛异常的，待测试
//    val context = container.context
//
//    val destroyed = context != null && context is ZActivity && context.isDestroyed || Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && context is Activity && context.isDestroyed
//    if (destroyed) {
//      //卧槽，Activity 都 destroy 了你还实例化，滚粗
//      return null
//    }

    return when {
      infinite && position == 0 -> wrappedAdapter.instantiateItem(container, wrappedAdapter.count - 1)
      infinite && position == wrappedAdapter.count + 1 -> wrappedAdapter.instantiateItem(container, 0)
      else -> wrappedAdapter.instantiateItem(container, position - if (infinite) 1 else 0)
    }
  }

  override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
    wrappedAdapter.destroyItem(container, position, `object`)
  }

  override fun isViewFromObject(view: View, o: Any): Boolean {
    return wrappedAdapter.isViewFromObject(view, o)
  }
}

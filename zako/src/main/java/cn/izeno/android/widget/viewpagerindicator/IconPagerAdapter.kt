package cn.izeno.android.widget.viewpagerindicator

interface IconPagerAdapter {

  // From PagerAdapter
  val count: Int

  /**
   * Get icon representing the page at `index` in the adapter.
   */
  fun getIconResId(index: Int): Int
}

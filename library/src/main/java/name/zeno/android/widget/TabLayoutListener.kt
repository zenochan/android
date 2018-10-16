package name.zeno.android.widget

import com.google.android.material.tabs.TabLayout

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/12/23.
 */
class TabLayoutListener private constructor(
    private val tabLayout: TabLayout,
    private val onTabSelected: ((position: Int) -> Unit)? = null
) : TabLayout.OnTabSelectedListener {

  fun unListen() {
    tabLayout.removeOnTabSelectedListener(this)
  }

  override fun onTabSelected(tab: TabLayout.Tab) {
    onTabSelected?.invoke(tabLayout.selectedTabPosition)
  }

  override fun onTabUnselected(tab: TabLayout.Tab) {}
  override fun onTabReselected(tab: TabLayout.Tab) {}

  companion object {
    @JvmStatic
    fun listen(tabLayout: TabLayout, onTabSelected: ((position: Int) -> Unit)): TabLayoutListener {
      val listener = TabLayoutListener(tabLayout, onTabSelected)
      listener.tabLayout.addOnTabSelectedListener(listener)
      return listener
    }
  }
}

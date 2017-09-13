package demo.android.zeno.name.zenokit

import android.os.Bundle
import demo.android.zeno.name.zenokit.item.StringItem
import kale.adapter.CommonPagerAdapter
import kale.adapter.item.AdapterItem
import kotlinx.android.synthetic.main.activity_circle_indicator.*
import name.zeno.android.presenter.ZActivity

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/9/13
 */
class CircleIndicatorActivity : ZActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_circle_indicator)

    pager.adapter = object : CommonPagerAdapter<String>(listOf("1", "2", "3")) {
      override fun createItem(type: Any?): AdapterItem<*> = StringItem()
    }
    indicator.setViewPager(pager)
  }
}
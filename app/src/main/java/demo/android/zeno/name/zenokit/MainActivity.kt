package demo.android.zeno.name.zenokit

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import demo.android.zeno.name.zenokit.item.MenuItem
import kale.adapter.CommonRcvAdapter
import kale.adapter.item.AdapterItem
import kotlinx.android.synthetic.main.activity_main.*
import name.zeno.android.core.ZType
import name.zeno.android.core.type
import name.zeno.android.data.models.BaseData
import name.zeno.android.listener.Action1
import name.zeno.android.presenter.ZActivity
import name.zeno.android.presenter.ZNav

class MainActivity : ZActivity() {

  private var classes: List<Class<out Activity>>? = null
  private var adapter: CommonRcvAdapter<Class<out Activity>>? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    init(savedInstanceState)
  }

  private fun init(savedInstanceState: Bundle?) {
    val onClick = Action1<Class<out Activity>> { ZNav.nav(this, it) }

    classes = listOf(
        TimeButtonActivity::class.java,
        ShapeActivity::class.java,
        CircleIndicatorActivity::class.java
    )

    adapter = object : CommonRcvAdapter<Class<out Activity>>(classes) {
      override fun createItem(type: Any): AdapterItem<*> = MenuItem(onClick)
    }

    rcv_class.layoutManager = LinearLayoutManager(context)
    rcv_class.adapter = adapter

    println(ZType<List<BaseData>> {}.type)
  }
}

package demo.android.zeno.name.zenokit

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.hwangjr.rxbus.annotation.Subscribe
import demo.android.zeno.name.zenokit.item.MenuItem
import io.reactivex.Observable
import kale.adapter.CommonRcvAdapter
import kale.adapter.item.AdapterItem
import kotlinx.android.synthetic.main.activity_main.*
import name.zeno.android.core.nav
import name.zeno.android.data.models.TextData
import name.zeno.android.presenter.ZActivity
import name.zeno.android.third.rxbus.registerRxBus
import name.zeno.android.third.rxbus.rxBus
import name.zeno.android.third.rxjava.RxUtils
import name.zeno.android.util.ZLog
import java.util.concurrent.TimeUnit

class MainActivity : ZActivity() {

  private var classes: List<Class<out Activity>>? = null
  private var adapter: CommonRcvAdapter<Class<out Activity>>? = null

  @SuppressLint("MissingSuperCall")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    init()
  }

  private fun init() {
    classes = listOf(
        TimeButtonActivity::class,
        ShapeActivity::class,
        CircleIndicatorActivity::class,
        DragLayoutActivity::class
    ).map { it.java }

    adapter = object : CommonRcvAdapter<Class<out Activity>>(classes) {
      val onClick = { clazz: Class<out Activity> -> nav(clazz) }
      override fun createItem(type: Any): AdapterItem<*> = MenuItem(onClick)
    }
    rcv_class.layoutManager = LinearLayoutManager(this)
    rcv_class.adapter = adapter
  }
}

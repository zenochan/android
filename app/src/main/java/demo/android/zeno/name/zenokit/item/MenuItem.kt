package demo.android.zeno.name.zenokit.item

import android.app.Activity
import android.view.View
import android.widget.TextView

import kale.adapter.item.AdapterItem
import name.zeno.android.listener.Action1

/**
 * @author 陈治谋 (微信: puppet2436)
 * @since 2017/4/15
 */
class MenuItem(private val onClick: (Class<out Activity>) -> Unit) : AdapterItem<Class<out Activity>> {
  private var view: TextView? = null

  private lateinit var clazz: Class<out Activity>

  override fun getLayoutResId(): Int {
    return android.R.layout.simple_expandable_list_item_1
  }

  override fun bindViews(root: View) {
    this.view = root as TextView
  }

  override fun setViews() {
    this.view!!.setOnClickListener { v -> onClick.invoke(clazz) }
  }

  override fun handleData(aClass: Class<out Activity>, position: Int) {
    this.clazz = aClass
    this.view!!.text = aClass.simpleName
  }
}

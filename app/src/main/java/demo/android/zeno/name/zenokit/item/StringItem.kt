package demo.android.zeno.name.zenokit.item

import android.app.Activity
import android.view.View
import android.widget.TextView

import kale.adapter.item.AdapterItem

/**
 * @author 陈治谋 (微信: puppet2436)
 * @since 2017/4/15
 */
class StringItem() : AdapterItem<String> {
  private lateinit var view: TextView


  override fun getLayoutResId(): Int {
    return android.R.layout.simple_expandable_list_item_1
  }

  override fun bindViews(root: View) {
    this.view = root as TextView
  }

  override fun setViews() {}

  override fun handleData(aClass: String, position: Int) {
    this.view.text = aClass
  }
}

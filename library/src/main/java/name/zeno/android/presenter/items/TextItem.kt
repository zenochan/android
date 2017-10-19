package name.zeno.android.presenter.items

import android.view.View
import android.widget.TextView

import kale.adapter.item.AdapterItem

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/12/5.
 */
@Suppress("unused")
class TextItem : AdapterItem<String> {
  private lateinit var root: TextView
  override fun getLayoutResId(): Int = android.R.layout.simple_list_item_1

  override fun bindViews(root: View) {
    this.root = root as TextView
  }

  override fun setViews() {}

  override fun handleData(text: String, position: Int) {
    root.text = text
  }
}

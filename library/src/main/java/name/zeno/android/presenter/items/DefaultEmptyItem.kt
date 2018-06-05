package name.zeno.android.presenter.items

import android.view.View

import kale.adapter.item.AdapterItem
import name.zeno.android.util.R

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/12/21.
 */
class DefaultEmptyItem : AdapterItem<Any> {
  override fun bindViews(root: View) {}
  override fun setViews() {}
  override fun handleData(t: Any, position: Int) {}
  override val layoutResId: Int = R.layout.item_empty_default
}

package cn.izeno.android.presenter.items

import android.view.View
import kale.adapter.item.AdapterItem

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/11/1
 */
abstract class ZItem<T : Any> : AdapterItem<T> {
  protected lateinit var root: View
  protected lateinit var data: T
  protected var position: Int = 0

  override final fun bindViews(root: View) {
    this.root = root
  }

  override final fun handleData(t: T, position: Int) {
    data = t
    this.position = position
    render()
  }

  override fun setViews() {}

  abstract fun render()
}

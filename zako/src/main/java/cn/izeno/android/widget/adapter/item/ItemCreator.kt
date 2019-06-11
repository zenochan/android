package cn.izeno.android.widget.adapter.item

import kale.adapter.item.AdapterItem

/**
 * Create Date: 16/7/4
 *
 * @author 陈治谋 (513500085@qq.com)
 */
interface ItemCreator<T : Any> {
  fun create(t: T): AdapterItem<T>
}

package name.zeno.android.widget.adapter.item

import kale.adapter.item.AdapterItem

/**
 * Create Date: 16/7/20
 *
 * @author 陈治谋 (513500085@qq.com)
 */
interface SectionItem<S, T> : AdapterItem<T> {
  fun handleSection(section: S, position: Int)
}

package name.zeno.android.presenter.items

import android.view.View
import com.baidu.mapapi.search.core.PoiInfo
import kale.adapter.item.AdapterItem
import kotlinx.android.synthetic.main.item_poi_info.view.*
import name.zeno.android.util.R

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/12/14.
 */
class PoiItem(
    internal var onClick: ((PoiInfo) -> Unit)
) : AdapterItem<PoiInfo> {


  private lateinit var root: View
  private lateinit var data: PoiInfo

  override val layoutResId: Int = R.layout.item_poi_info

  override fun bindViews(root: View) {
    this.root = root
    root.setOnClickListener { onClick.invoke(data) }
  }

  override fun setViews() {

  }

  override fun handleData(poiInfo: PoiInfo, position: Int) = with(root) {
    data = poiInfo
    tv_poi_title.text = data.name
    tv_poi_address.text = data.address
  }
}

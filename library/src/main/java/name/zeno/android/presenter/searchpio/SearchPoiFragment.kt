package name.zeno.android.presenter.searchpio

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.baidu.mapapi.search.core.PoiInfo
import com.tbruyelle.rxpermissions2.RxPermissions
import kale.adapter.CommonRcvAdapter
import kale.adapter.LoadAdapterWrapper
import kale.adapter.item.AdapterItem
import kotlinx.android.synthetic.main.fragment_search_poi.view.*
import name.zeno.android.core.data
import name.zeno.android.core.okAndFinish
import name.zeno.android.presenter.ZFragment
import name.zeno.android.presenter.items.PoiItem
import name.zeno.android.system.ZPermission
import name.zeno.android.third.baidu.PoiModel
import name.zeno.android.util.R
import name.zeno.android.util.ZString
import name.zeno.android.widget.ZTextWatcher

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/12/14.
 */
class SearchPoiFragment : ZFragment(), SearchPoiView {
  private val presenter = SearchPoiPresenter(this)
  private lateinit var fragmentView: View

  private lateinit var request: SearchPoiRequest
  private var adapter: CommonRcvAdapter<PoiInfo>? = null
  private var wrapper: LoadAdapterWrapper? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    request = data()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    fragmentView = inflater.inflate(R.layout.fragment_search_poi, container, false)
    init()
    return fragmentView
  }


  override fun requestLocationPermission(next: () -> Unit) {
    RxPermissions(activity).request(
        ZPermission.WRITE_EXTERNAL_STORAGE,
        ZPermission.ACCESS_COARSE_LOCATION,
        ZPermission.ACCESS_FINE_LOCATION
    ).subscribe { granted ->
      if (granted!!) {
        next.invoke()
      } else {
        toast("地址搜索需要定位权限和文件存储权限")
        empty()
      }
    }
  }

  override fun empty() {
    wrapper?.empty()
  }


  private fun init() = with(fragmentView) {
    val onClickPoi: (PoiInfo) -> Unit = { onClickPoi(it) }
    btn_input.setOnClickListener { customInput(tv_keyword.text.toString()) }

    adapter = object : CommonRcvAdapter<PoiInfo>(presenter.getInfoList()) {
      override fun createItem(type: Any): AdapterItem<*> {
        val item = PoiItem(onClickPoi)
        return item
      }
    }

    wrapper = LoadAdapterWrapper.Builder(context)
        .adapter(adapter)
        .recycler(rcv_search_poi)
        .layoutManager(LinearLayoutManager(context))
        .build()

    tv_keyword.setText(request.fill)
    ZTextWatcher.watch(tv_keyword) { _, txt ->
      if (!request.isEnableOriginInput || txt.isEmpty()) {
        btn_input.visibility = View.GONE
      } else {
        btn_input.text = txt
        btn_input.visibility = View.VISIBLE
        presenter.search(txt)
      }
    }
    if (request.isEnableOriginInput && ZString.notEmpty(request.fill)) {
      btn_input.text = request.fill
      btn_input.visibility = View.VISIBLE
    }
  }

  // 使用用户输入的值
  private fun customInput(text: String) {
    val info = PoiInfo()
    info.address = text
    onClickPoi(info)
  }

  // 选择热点
  private fun onClickPoi(poiInfo: PoiInfo) {
    okAndFinish(PoiModel(poiInfo))
  }
}

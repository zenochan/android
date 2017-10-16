package name.zeno.android.presenter.searchpio

import android.annotation.SuppressLint
import android.databinding.ObservableArrayList
import com.baidu.location.BDLocation
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.core.PoiInfo
import name.zeno.android.data.CommonConnector
import name.zeno.android.presenter.BasePresenter
import name.zeno.android.third.baidu.GeoCoderHelper
import name.zeno.android.third.baidu.LocationHelper
import name.zeno.android.third.baidu.PoiSearchHelper

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/12/14.
 */
class SearchPoiPresenter(view: SearchPoiView) : BasePresenter<SearchPoiView>(view) {
  private var pageNo: Int = 0

  private var bdLocation: BDLocation? = null
  private val poiSearch = PoiSearchHelper()
  private val geoCoder = GeoCoderHelper()

  private val infoList = ObservableArrayList<PoiInfo>()

  override fun onCreate() {
    super.onCreate()
    poiSearch.subscriber = sub { value ->
      if (value != null) {
        if (pageNo == 1) {
          infoList.clear()
        }
        infoList.addAll(value)
      }
    }
  }

  override fun onViewCreated() {
    super.onViewCreated()
    view.requestLocationPermission {
      LocationHelper.getInstance(view.context).requestLocation(
          sub({
            this.bdLocation = it
            reverseGeoCode(this.bdLocation)
          }
          ) { e ->
            view.showMessage(e.message)
            view.empty()
          }
      )
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    geoCoder.onDestroy()
  }

  fun search(keyword: String) {
    pageNo = 1
    poiSearch.searchInCity(if (bdLocation == null) "上海" else bdLocation!!.city, keyword, pageNo)
  }

  @SuppressLint("MissingPermission")
  private fun reverseGeoCode(bdLocation: BDLocation?) {
    val latLng = LatLng(bdLocation!!.latitude, bdLocation.longitude)
    geoCoder.reverseGeoCode(view.context, latLng) { poiInfos ->
      infoList.clear()
      infoList.addAll(poiInfos)
    }
  }

  fun getInfoList(): List<PoiInfo> {
    return this.infoList
  }
}
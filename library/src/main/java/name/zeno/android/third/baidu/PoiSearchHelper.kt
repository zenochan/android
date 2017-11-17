package name.zeno.android.third.baidu

import android.util.Log
import com.alibaba.fastjson.JSON
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.core.PoiInfo
import com.baidu.mapapi.search.core.SearchResult
import com.baidu.mapapi.search.poi.*
import com.baidu.mapapi.search.sug.SuggestionSearch
import com.baidu.mapapi.search.sug.SuggestionSearchOption
import com.orhanobut.logger.Logger
import io.reactivex.Observer

/**
 *  - [searchInCity]
 *  - [searchNearby]
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since  16/6/19
 */
class PoiSearchHelper : OnGetPoiSearchResultListener {

  private val poiSearch: PoiSearch
  private val suggestionSearch: SuggestionSearch

  var subscriber: Observer<List<PoiInfo>>? = null

  init {
    poiSearch = PoiSearch.newInstance()
    poiSearch.setOnGetPoiSearchResultListener(this)
    suggestionSearch = SuggestionSearch.newInstance()
    suggestionSearch.setOnGetSuggestionResultListener { suggestionResult -> Logger.t(TAG).d(JSON.toJSONString(suggestionResult, true)) }
  }

  @JvmOverloads
  fun searchInCity(city: String, keyword: String, pageNum: Int, pageCapacity: Int = 20) {
    Log.v(TAG, "search in city[$city],keyword:$keyword")
    suggestionSearch.requestSuggestion(SuggestionSearchOption()
        .keyword(keyword)
        .city(city))

    poiSearch.searchInCity(PoiCitySearchOption()
        .city(city)
        .keyword(keyword)
        .pageNum(pageNum)
        .pageCapacity(pageCapacity)
    )
  }

  fun searchNearby(latLng: LatLng, keyword: String, pageNum: Int) {
    Log.v(TAG, "latLng = [$latLng], keyword = [$keyword], pageNum = [$pageNum]")
    searchNearby(latLng, keyword, pageNum, 5)
  }

  fun searchNearby(latLng: LatLng, keyword: String, pageNum: Int, radius: Int) {
    Logger.t(TAG).v("search nearby[" + latLng.toString() + "],keyword:" + keyword)
    poiSearch.searchNearby(PoiNearbySearchOption()
        .location(latLng)
        .keyword(keyword)
        .sortType(PoiSortType.comprehensive)
        .pageNum(pageNum)
        .radius(radius)
    )
  }

  fun searchDetail(poiInfo: PoiInfo) {
    poiSearch.searchPoiDetail(PoiDetailSearchOption().poiUid(poiInfo.uid))
  }

  fun destroy() {
    // 不需要注销监听，设置为 null 会抛异常
    //poiSearch.setOnGetPoiSearchResultListener(null);
    poiSearch.destroy()
  }

  override fun onGetPoiResult(result: PoiResult?) {
    if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
      onSuccess(emptyList())
    } else if (result.error == SearchResult.ERRORNO.NO_ERROR) {
      // mmp, 命名没有错误，但是竟然有时候会返回空的结果
      var pois: List<PoiInfo>? = result.allPoi
      if (pois == null) pois = emptyList()
      onSuccess(pois)
    } else if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
      // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
      onSuccess(emptyList())
      var strInfo = "在"
      for (cityInfo in result.suggestCityList) {
        strInfo += cityInfo.city
        strInfo += ","
      }
      strInfo += "找到结果"
      Log.v(TAG, strInfo)
    }
  }

  override fun onGetPoiDetailResult(poiDetailResult: PoiDetailResult) {}

  override fun onGetPoiIndoorResult(poiIndoorResult: PoiIndoorResult) {}

  private fun onSuccess(infoList: List<PoiInfo>) {
    subscriber?.onNext(infoList)
    subscriber?.onComplete()
  }

  companion object {
    private val TAG = "PoiSearchHelper"
  }
}

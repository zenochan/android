package name.zeno.android.third.baidu;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.orhanobut.logger.Logger;


import java.util.Collections;
import java.util.List;

import io.reactivex.Observer;
import lombok.Getter;
import lombok.Setter;
import retrofit2.http.GET;

/**
 * Create Date: 16/6/19
 * <p>
 * <ul>
 * <li>{@link #searchInCity(String, String, int)}</li>
 * <li>{@link #searchNearby(LatLng, String, int)}</li>
 * </ul>
 *
 * @author 陈治谋 (513500085@qq.com)
 */
public class PoiSearchHelper implements OnGetPoiSearchResultListener
{
  private static final String TAG = "PoiSearchHelper";

  private final PoiSearch        poiSearch;
  private final SuggestionSearch suggestionSearch;

  @Getter @Setter
  private Observer<List<PoiInfo>> subscriber;

  public PoiSearchHelper()
  {
    poiSearch = PoiSearch.newInstance();
    poiSearch.setOnGetPoiSearchResultListener(this);
    suggestionSearch = SuggestionSearch.newInstance();
    suggestionSearch.setOnGetSuggestionResultListener(
        suggestionResult -> Logger.t(TAG).d(JSON.toJSONString(suggestionResult, true))
    );
  }

  public void searchInCity(String city, String keyword, int pageNum)
  {
    searchInCity(city, keyword, pageNum, 20);
  }

  public void searchInCity(String city, String keyword, int pageNum, int pageCapacity)
  {
    Log.v(TAG, "search in city[" + city + "],keyword:" + keyword);
    suggestionSearch.requestSuggestion((new SuggestionSearchOption())
        .keyword(keyword)
        .city(city));

    poiSearch.searchInCity(new PoiCitySearchOption()
        .city(city)
        .keyword(keyword)
        .pageNum(pageNum)
        .pageCapacity(pageCapacity)
    );
  }

  @SuppressWarnings("unused")
  public void searchNearby(LatLng latLng, String keyword, int pageNum)
  {
    Log.v(TAG, "latLng = [" + latLng + "], keyword = [" + keyword + "], pageNum = [" + pageNum + "]");
    searchNearby(latLng, keyword, pageNum, 5);
  }

  public void searchNearby(LatLng latLng, String keyword, int pageNum, int radius)
  {
    Logger.t(TAG).v("search nearby[" + latLng.toString() + "],keyword:" + keyword);
    poiSearch.searchNearby(new PoiNearbySearchOption()
        .location(latLng)
        .keyword(keyword)
        .sortType(PoiSortType.comprehensive)
        .pageNum(pageNum)
        .radius(radius)
    );
  }

  @SuppressWarnings("unused")
  public void searchDetail(PoiInfo poiInfo)
  {
    poiSearch.searchPoiDetail(new PoiDetailSearchOption().poiUid(poiInfo.uid));
  }

  public void destroy()
  {
    poiSearch.setOnGetPoiSearchResultListener(null);
    poiSearch.destroy();
  }

  @Override
  public void onGetPoiResult(PoiResult result)
  {
    if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
      Log.v(TAG, "未找到结果");
      onSuccess(Collections.emptyList());
    } else if (result.error == SearchResult.ERRORNO.NO_ERROR) {
      onSuccess(result.getAllPoi());
    } else if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
      // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
      onSuccess(Collections.emptyList());
      String strInfo = "在";
      for (CityInfo cityInfo : result.getSuggestCityList()) {
        strInfo += cityInfo.city;
        strInfo += ",";
      }
      strInfo += "找到结果";
      Log.v(TAG, strInfo);
    }
  }

  @Override
  public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) { }

  @Override
  public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) { }

  private void onSuccess(List<PoiInfo> infoList)
  {
    subscriber.onNext(infoList);
    subscriber.onComplete();
  }

}

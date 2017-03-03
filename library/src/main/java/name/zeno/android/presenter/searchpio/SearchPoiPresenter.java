package name.zeno.android.presenter.searchpio;

import android.databinding.ObservableArrayList;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.orhanobut.logger.Logger;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import lombok.Getter;
import name.zeno.android.presenter.BasePresenter;
import name.zeno.android.third.baidu.GeoCoderHelper;
import name.zeno.android.third.baidu.LocationHelper;
import name.zeno.android.third.baidu.PoiSearchHelper;
import name.zeno.android.third.rxjava.ZObserver;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/12/14.
 */
public class SearchPoiPresenter extends BasePresenter<SearchPoiView>
{
  private int pageNo;

  public SearchPoiPresenter(SearchPoiView view)
  {
    super(view);
  }

  private BDLocation bdLocation;
  private PoiSearchHelper poiSearch = new PoiSearchHelper();
  private GeoCoderHelper  geoCoder  = new GeoCoderHelper();

  @Getter
  private List<PoiInfo> infoList = new ObservableArrayList<>();

  @Override public void onCreate()
  {
    super.onCreate();
    poiSearch.setSubscriber(new ZObserver<List<PoiInfo>>()
    {
      @Override public void onNext(List<PoiInfo> value)
      {
        if (pageNo == 1) {
          infoList.clear();
        }
        infoList.addAll(value);
      }
    });
  }

  @Override public void onViewCreated()
  {
    super.onViewCreated();
    view.requestLocationPermission(granted -> {
      if (granted) {
        LocationHelper.getInstance(view.getContext()).requestLocation(new ZObserver<BDLocation>()
        {
          @Override public void onNext(BDLocation value)
          {
            bdLocation = value;
            reverseGeoCode(bdLocation);
          }
        });
      }
    });
  }

  @Override public void onDestroy()
  {
    super.onDestroy();
    geoCoder.onDestroy();
  }

  void search(String keyword)
  {
    pageNo = 1;
    poiSearch.searchInCity(bdLocation == null ? "上海" : bdLocation.getCity(), keyword, pageNo);
  }

  private void reverseGeoCode(BDLocation bdLocation)
  {
    LatLng latLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
    geoCoder.reverseGeoCode(view.getContext(), latLng, poiInfos -> {
      infoList.clear();
      infoList.addAll(poiInfos);
    });
  }
}

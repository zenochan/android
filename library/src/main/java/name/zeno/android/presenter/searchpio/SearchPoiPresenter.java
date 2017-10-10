package name.zeno.android.presenter.searchpio;

import android.databinding.ObservableArrayList;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;

import java.util.List;

import name.zeno.android.presenter.BasePresenter;
import name.zeno.android.third.baidu.GeoCoderHelper;
import name.zeno.android.third.baidu.LocationHelper;
import name.zeno.android.third.baidu.PoiSearchHelper;

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

  private List<PoiInfo> infoList = new ObservableArrayList<>();

  @Override public void onCreate()
  {
    super.onCreate();
    poiSearch.setSubscriber(sub(value -> {
      if (pageNo == 1) {
        infoList.clear();
      }
      infoList.addAll(value);

    }));
  }

  @Override public void onViewCreated()
  {
    super.onViewCreated();
    view.requestLocationPermission(() -> {
      LocationHelper.getInstance(view.getContext()).requestLocation(
          sub(bdLocation -> {
                this.bdLocation = bdLocation;
                reverseGeoCode(this.bdLocation);
              },
              e -> {
                view.showMessage(e.getMessage());
                view.empty();
              }
          )
      );
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

  @SuppressWarnings("MissingPermission")
  private void reverseGeoCode(BDLocation bdLocation)
  {
    LatLng latLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
    geoCoder.reverseGeoCode(view.getContext(), latLng, poiInfos -> {
      infoList.clear();
      infoList.addAll(poiInfos);
    });
  }

  public List<PoiInfo> getInfoList()
  {return this.infoList;}
}

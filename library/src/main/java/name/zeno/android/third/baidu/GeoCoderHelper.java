package name.zeno.android.third.baidu;

/**
 * Create Date: 16/6/19
 *
 * @author 陈治谋 (513500085@qq.com)
 */

import android.Manifest;
import android.content.Context;
import android.support.annotation.RequiresPermission;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import java.util.Collections;
import java.util.List;

import name.zeno.android.util.SystemUtils;
import name.zeno.android.util.ZLog;
import name.zeno.android.listener.Action1;

public class GeoCoderHelper
{
  private static final String TAG = "GeoCoderHelper";

  private Action1<List<PoiInfo>> next;

  private GeoCoder geoCoder;

  public GeoCoderHelper()
  {
    geoCoder = GeoCoder.newInstance();
    geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener()
    {
      @Override public void onGetGeoCodeResult(GeoCodeResult geoCodeResult)
      {
        ZLog.v(TAG, "GeoCoderHelper.onGetGeoCodeResult");
      }

      @Override public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult)
      {
        if (next != null) {
          List<PoiInfo> result = reverseGeoCodeResult.getPoiList();
          if (result == null) result = Collections.EMPTY_LIST;
          next.call(result);
          next = null;
        }
      }
    });
  }

  // 反Geo搜索
  @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
  public void reverseGeoCode(Context context, LatLng latLng, Action1<List<PoiInfo>> next)
  {
    this.next = next;
    if (SystemUtils.isNetworkConnected(context)) {
      geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
    } else {
      ZLog.e(TAG, "网络未连接");
    }
  }

  // Geo搜索
  public void geocode(String city, String geoCodeKey)
  {
    geoCoder.geocode(new GeoCodeOption().city(city).address(geoCodeKey));
  }

  public void onDestroy()
  {
    next = null;
    geoCoder.destroy();
  }
}


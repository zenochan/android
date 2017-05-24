package name.zeno.android.third.baidu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;
import name.zeno.android.third.rxjava.RxUtils;
import name.zeno.android.util.ZLog;

@SuppressWarnings({"FieldCanBeLocal", "unused", "Convert2streamapi"})
public class LocationHelper implements BDLocationListener
{

  private static final String TAG = "LocationHelper";

  private static LocationHelper instance = null;

  private Context context;

  private LocationClient locationClient;


  private SDKReceiver receiver;

  private ArrayList<Observer<BDLocation>> observers = new ArrayList<>();

  public static LocationHelper getInstance(Context context)
  {
    if (instance == null) {
      synchronized (LocationHelper.class) {
        if (instance == null) {
          instance = new LocationHelper(context);
        }
      }
    }
    return instance;
  }

  private LocationHelper(Context context)
  {
    this.context = context.getApplicationContext();
    locationClient = new LocationClient(this.context);
    locationClient.setLocOption(getLocationOption());
    locationClient.registerLocationListener(this);
    registerSdkReceiver();
  }

  @Override public void onReceiveLocation(BDLocation bdLocation)
  {
    try {
      switch (bdLocation.getLocType()) {
        case BDLocation.TypeGpsLocation: // GPS定位结果
        case BDLocation.TypeNetWorkLocation: // 网络定位结果
        case BDLocation.TypeOffLineLocation: // 离线定位结果
        case BDLocation.TypeOffLineLocationNetworkFail:
          onLocationSuccess(bdLocation);
          break;
        default://定位失败
          onLocationFailed();
          break;
      }
    } finally {
      stop();
    }
  }

  public synchronized void requestLocation(Observer<BDLocation> observer)
  {
    if (!observers.contains(observer)) {
      observers.add(observer);
    }

    if (!locationClient.isStarted()) {
      locationClient.start();
    }

    if (locationClient.isStarted()) {
      locationClient.requestLocation();
    } else {
      Observable.create(subscriber1 -> {
        int t = 0;
        while (!locationClient.isStarted() && t < 20) {
          try {
            ZLog.v(TAG, "sleep...");
            Thread.sleep(500);
            t++;
          } catch (InterruptedException e) {
            ZLog.e(TAG, e.getMessage());
          }
        }
        if (locationClient.isStarted()) {
          subscriber1.onNext(1);
          subscriber1.onComplete();
        } else {
          subscriber1.onError(new Exception("开启百度定位失败"));
        }
      }).compose(RxUtils.applySchedulers()).subscribe(
          o -> {
            ZLog.w(TAG, "start request");
            locationClient.requestLocation();
          },
          throwable -> ZLog.e(TAG, throwable.getMessage()));
    }
  }

  public void stop()
  {
    if (locationClient != null && locationClient.isStarted()) {
      locationClient.stop();
    }
//    mContext.unregisterReceiver(receiver);
  }

  private LocationClientOption getLocationOption()
  {
    LocationClientOption option = new LocationClientOption();
    //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
    option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
    //可选，默认gcj02，设置返回的定位结果坐标系
    option.setCoorType("bd09ll");
    //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
    option.setScanSpan(1000);
    //可选，设置是否需要地址信息，默认不需要
    option.setIsNeedAddress(true);
    //可选，默认false,设置是否使用gps
    //option.setOpenGps(true);
    //可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
    //设置为 false 时, 地理位置更新会失效(comment by: Zeno)
    option.setLocationNotify(true);
    //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
    //option.setIsNeedLocationDescribe(true);
    //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
    //option.setIsNeedLocationPoiList(true);
    //可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
    option.setIgnoreKillProcess(false);
    //可选，默认false，设置是否收集CRASH信息，默认收集
    option.SetIgnoreCacheException(false);
    //可选，默认false，设置是否需要过滤gps仿真结果，默认需要
    //option.setEnableSimulateGps(false);

    return option;
  }

  private void onLocationFailed()
  {
    for (Observer subscriber : observers) {
      subscriber.onError(new Exception("定位失败"));
    }
    observers.clear();
  }

  private void onLocationSuccess(BDLocation bdLocation)
  {
    for (Observer<BDLocation> subscriber : observers) {
      subscriber.onNext(bdLocation);
      subscriber.onComplete();
    }
    observers.clear();
  }

  private void registerSdkReceiver()
  {
    IntentFilter iFilter = new IntentFilter();
    iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
    iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
    iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK);
    receiver = new SDKReceiver();
    context.registerReceiver(receiver, iFilter);
  }

  /**
   * 构造广播监听类，监听 SDK key 验证以及网络异常广播
   */
  public class SDKReceiver extends BroadcastReceiver
  {
    public void onReceive(Context context, Intent intent)
    {
      String s = intent.getAction();
      //验证key出错
      if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
        Log.e(TAG, "百度 SDK 验证 key 出错");
        BDLocation location = new BDLocation();
        location.setLocType(BDLocation.TypeNetWorkException);
        onReceiveLocation(location);
      }
      //网络出错
      else if (s.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
        Log.e(TAG, "百度 SDK 网络出错");
        BDLocation location = new BDLocation();
        location.setLocType(BDLocation.TypeNetWorkException);
        onReceiveLocation(location);
      } else if (s.endsWith(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK)) {
        Log.e(TAG, "key 验证成功! 功能可以正常使用");
      }

    }
  }
}


package name.zeno.android.third.baidu

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.baidu.location.BDLocation
import com.baidu.location.BDLocationListener
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.SDKInitializer
import io.reactivex.Observable
import io.reactivex.Observer
import name.zeno.android.third.rxjava.RxUtils
import name.zeno.android.util.ZLog
import java.util.*

class LocationHelper private constructor(context: Context) : BDLocationListener {

  private val context: Context

  private val locationClient: LocationClient


  private var receiver: SDKReceiver? = null

  private val observers = ArrayList<Observer<BDLocation>>()

  private
  val locationOption: LocationClientOption = LocationClientOption().apply {
    //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
    locationMode = LocationClientOption.LocationMode.Hight_Accuracy
    //可选，默认gcj02，设置返回的定位结果坐标系
    coorType = "bd09ll"

    //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
    scanSpan = 1000

    //可选，设置是否需要地址信息，默认不需要
    setIsNeedAddress(true)

    //可选，默认false,设置是否使用gps
    //openGps = true

    //可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
    //isLocationNotify = true

    //设置为 false 时, 地理位置更新会失效(comment by: Zeno)
    setIgnoreKillProcess(false)


    //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
    //setIsNeedLocationDescribe(true);

    //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
    //setIsNeedLocationPoiList(true);

    //可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死

    //可选，默认false，设置是否需要过滤gps仿真结果，默认需要
    //setEnableSimulateGps(false);

    //可选，默认false，设置是否收集CRASH信息，默认收集
    SetIgnoreCacheException(false)
  }


  init {
    this.context = context.applicationContext
    locationClient = LocationClient(this.context)
    locationClient.locOption = locationOption
    locationClient.registerLocationListener(this)
    registerSdkReceiver()
  }

  override fun onReceiveLocation(bdLocation: BDLocation) {
    try {
      when (bdLocation.locType) {
        BDLocation.TypeGpsLocation // GPS定位结果
          , BDLocation.TypeNetWorkLocation // 网络定位结果
          , BDLocation.TypeOffLineLocation // 离线定位结果
          , BDLocation.TypeOffLineLocationNetworkFail -> onLocationSuccess(bdLocation)
        else//定位失败
        -> onLocationFailed()
      }
    } finally {
      stop()
    }
  }

  //  @Override public void onConnectHotSpotMessage(String s, int i) { /*热点消息*/ }

  @Synchronized
  fun requestLocation(observer: Observer<BDLocation>) {
    if (!observers.contains(observer)) {
      observers.add(observer)
    }

    if (!locationClient.isStarted) {
      locationClient.start()
    }

    if (locationClient.isStarted) {
      locationClient.requestLocation()
    } else {
      Observable.create<Any> { sub ->
        var t = 0
        while (!locationClient.isStarted && t < 20) {
          try {
            ZLog.v(TAG, "sleep...")
            Thread.sleep(500)
            t++
          } catch (e: InterruptedException) {
            ZLog.e(TAG, e.message)
          }

        }
        if (locationClient.isStarted) {
          sub.onNext(1)
          sub.onComplete()
        } else {
          sub.onError(Exception("开启百度定位失败"))
        }
      }.compose(RxUtils.applySchedulers()).subscribe(
          { o ->
            ZLog.w(TAG, "start request")
            locationClient.requestLocation()
          }
      ) { throwable ->
        ZLog.e(TAG, throwable.message)
        onLocationFailed()
      }
    }
  }

  fun stop() {
    if (locationClient.isStarted) {
      locationClient.stop()
    }
//        mContext.unregisterReceiver(receiver);
  }

  private fun onLocationFailed() {
    for (subscriber in observers) {
      subscriber.onError(Exception("定位失败"))
    }
    observers.clear()
  }

  private fun onLocationSuccess(bdLocation: BDLocation) {
    for (subscriber in observers) {
      subscriber.onNext(bdLocation)
      subscriber.onComplete()
    }
    observers.clear()
  }

  private fun registerSdkReceiver() {
    val iFilter = IntentFilter()
    iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)
    iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)
    iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK)
    receiver = SDKReceiver()
    context.registerReceiver(receiver, iFilter)
  }

  /**
   * 构造广播监听类，监听 SDK key 验证以及网络异常广播
   */
  inner class SDKReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
      val s = intent.action
      //验证key出错
      if (s == SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR) {
        Log.e(TAG, "百度 SDK 验证 key 出错")
        val location = BDLocation()
        location.locType = BDLocation.TypeNetWorkException
        onReceiveLocation(location)
      } else if (s == SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR) {
        Log.e(TAG, "百度 SDK 网络出错")
        val location = BDLocation()
        location.locType = BDLocation.TypeNetWorkException
        onReceiveLocation(location)
      } else if (s!!.endsWith(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK)) {
        Log.e(TAG, "key 验证成功! 功能可以正常使用")
      }//网络出错

    }
  }

  companion object {

    private val TAG = "LocationHelper"

    private var instance: LocationHelper? = null

    fun getInstance(context: Context): LocationHelper {
      if (instance == null) {
        synchronized(LocationHelper::class.java) {
          if (instance == null) {
            instance = LocationHelper(context)
          }
        }
      }
      return instance!!
    }
  }
}


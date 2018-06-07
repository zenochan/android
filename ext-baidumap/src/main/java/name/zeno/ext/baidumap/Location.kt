package name.zeno.ext.baidumap

//import com.baidu.mapapi.SDKInitializer
import android.content.Context
import android.util.Log
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

/**
 * @author [陈治谋](mailto:zenochan@qq.com)
 * @since 2018/6/6
 */

internal class Location(context: Context) : BDAbstractLocationListener(), ILocation {

  private val context: Context

  private val locationClient: LocationClient

//  private var receiver: SDKReceiver? = null

  private var subject: PublishSubject<BDLocation>? = null


  init {
    this.context = context.applicationContext
    locationClient = LocationClient(this.context)
    locationClient.locOption = LocationClientOption().apply {
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
      isIgnoreKillProcess = true
    }

    locationClient.registerLocationListener(this)
  }


  override fun onReceiveLocation(bdLocation: BDLocation) {
    try {
      when (bdLocation.locType) {
        BDLocation.TypeGpsLocation // GPS定位结果
          , BDLocation.TypeNetWorkLocation // 网络定位结果
          , BDLocation.TypeOffLineLocation // 离线定位结果
          , BDLocation.TypeOffLineLocationNetworkFail -> onLocationSuccess(bdLocation)
        BDLocation.TypeServerCheckKeyError -> {
          Log.e(TAG, bdLocation.locTypeDescription)
          context.printSign()
          onLocationFailed("签名配置错误")
        }
        else -> {//定位失败
          Log.e(TAG, bdLocation.locTypeDescription)
          onLocationFailed("定位失败【${bdLocation.locType}】")
        }
      }
    } catch (e: Exception) {
      onLocationFailed("定位失败【${e.message}】")
    } finally {
      stop()
    }
  }

  //  @Override public void onConnectHotSpotMessage(String s, int i) { /*热点消息*/ }

  @Synchronized
  override fun requestLocation(): Subject<BDLocation> {
    var subject = this.subject
    if (subject != null) {
      return subject
    } else {
      subject = PublishSubject.create()
      this.subject = subject
    }

    Single.create<Any> { sub ->
      if (!locationClient.isStarted) {
        locationClient.start()
      } else {
        locationClient.restart()
      }

      if (locationClient.isStarted) {
        sub.onSuccess(1)
        return@create
      }

      // 等待百度 sdk 初始化
      var t = 0
      while (!locationClient.isStarted && t < 20) {
        Log.v(TAG, "sleep...")
        Thread.sleep(200)
        t++
      }

      if (locationClient.isStarted) {
        sub.onSuccess(1)
      } else {
        sub.onError(Exception("开启百度定位失败"))
      }

    }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        .subscribe({ o ->
          Log.w(TAG, "start request")
          locationClient.requestLocation()
        }, {
          onLocationFailed(it.message.orEmpty())
        })

    return subject
  }

  private fun stop() {
    if (locationClient.isStarted) {
      locationClient.stop()
    }
    subject = null
  }

  private fun onLocationSuccess(bdLocation: BDLocation) {
    subject?.onNext(bdLocation)
    stop()
  }

  private fun onLocationFailed(msg: String = "定位失败") {
    subject?.onError(IllegalStateException(msg))
    stop()
  }

  companion object {

    private val TAG = "Location"

    private var instance: Location? = null

    fun getInstance(context: Context): Location {
      if (instance == null) {
        synchronized(Location::class.java) {
          if (instance == null) {
            instance = Location(context)
          }
        }
      }
      return instance!!
    }
  }
}

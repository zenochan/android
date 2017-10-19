package name.zeno.android.third.baidu


import android.Manifest
import android.content.Context
import android.support.annotation.RequiresPermission
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.core.PoiInfo
import com.baidu.mapapi.search.geocode.*
import name.zeno.android.util.SystemUtils
import name.zeno.android.util.ZLog

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 16/6/19
 */
class GeoCoderHelper {

  private var next: ((List<PoiInfo>) -> Unit)? = null
  private val geoCoder: GeoCoder = GeoCoder.newInstance()

  init {
    geoCoder.setOnGetGeoCodeResultListener(object : OnGetGeoCoderResultListener {
      override fun onGetGeoCodeResult(geoCodeResult: GeoCodeResult) {
        ZLog.v(TAG, "GeoCoderHelper.onGetGeoCodeResult")
      }

      override fun onGetReverseGeoCodeResult(reverseGeoCodeResult: ReverseGeoCodeResult) {
        // 结果可能为空，需要处理
        val result = reverseGeoCodeResult.poiList ?: emptyList()
        next?.invoke(result)
        next = null
      }
    })
  }

  // 反Geo搜索
  @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
  fun reverseGeoCode(context: Context, latLng: LatLng, next: (List<PoiInfo>) -> Unit) {
    this.next = next
    if (SystemUtils.isNetworkConnected(context)) {
      geoCoder.reverseGeoCode(ReverseGeoCodeOption().location(latLng))
    } else {
      ZLog.e(TAG, "网络未连接")
    }
  }

  // Geo搜索
  fun geocode(city: String, geoCodeKey: String) {
    geoCoder.geocode(GeoCodeOption().city(city).address(geoCodeKey))
  }

  fun onDestroy() {
    next = null
    geoCoder.destroy()
  }

  companion object {
    private val TAG = "GeoCoderHelper"
  }
}


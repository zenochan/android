/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/9/7
 */
@file:Suppress("unused")

package name.zeno.ext.baidumap

//import android.os.Bundle
//import android.os.Parcelable
//import android.support.annotation.DrawableRes
//import android.view.View
//import com.baidu.location.BDLocation
//import com.baidu.mapapi.map.*
//import com.baidu.mapapi.model.LatLng
//import com.baidu.mapapi.model.LatLngBounds
//
//const val EXTRA_BD = "intent_data"
//
//
///**
// * # 百度地图Android SDK为您提供了3种类型的地图资源（普通矢量地图、卫星图和空白地图）
// *
// * - 普通地图 BaiduMap.MAP_TYPE_NORMAL
// * - 卫星地图 BaiduMap.MAP_TYPE_SATELLITE
// * - 空白地图 BaiduMap.MAP_TYPE_NONE
// *     > 基础地图瓦片将不会被渲染。在地图类型中设置为NONE，将不会使用流量下载基础地图瓦片图层。使用场景：与瓦片图层一起使用，节省流量，提升自定义瓦片图下载速度。
// */
//var MapView.mapType: Int
//  get() = this.map.mapType
//  set(value) {
//    this.map.mapType = value
//  }
//
///**
// * # 当前，全国范围内已支持多个城市实时路况查询，且会陆续开通其他城市。
// */
//var MapView.trafficEnabled: Boolean
//  get() = this.map.isTrafficEnabled
//  set(value) {
//    this.map.isTrafficEnabled = value
//  }
//
///**
// * > 百度地图SDK继为广大开发者开放热力图本地绘制能力之后，
// * 再次进一步开放百度自有数据的城市热力图层，
// * 帮助开发者构建形式更加多样的移动端应用。
// */
//var MapView.heatEnabled: Boolean
//  get() = map.isBaiduHeatMapEnabled
//  set(value) {
//    map.isBaiduHeatMapEnabled = value
//  }
//
//fun MapView.overlay(lat: Double, lng: Double): IconOverlay = this.iconOverlay(LatLng(lat, lng))
//
//fun MapView.iconOverlay(position: LatLng): IconOverlay = IconOverlay(this, position)
//
//fun MapView.showInfoWindow(view: View, lat: Double, lng: Double, offsetY: Int) = showInfoWindow(view, LatLng(lat, lng), offsetY)
//
//fun MapView.showInfoWindow(view: View, position: LatLng, offsetY: Int) {
//  val window = InfoWindow(view, position, offsetY)
//  map.showInfoWindow(window)
//}
//
//fun MapView.hideInfoWindow() = map.hideInfoWindow()
//
//fun MapView.showThere(vararg positions: LatLng) = showThere(positions.toList())
//fun MapView.showThere(positions: List<LatLng>) {
//  val builder = LatLngBounds.Builder()
//  positions.forEach { builder.include(it) }
//  map.setMapStatusLimits(builder.build())
//}
//
//fun MapView.center(position: LatLng, zoom: Float = 18F) {
//  val status = MapStatus.Builder().target(position).zoom(zoom).build()
//  map.setMapStatus(MapStatusUpdateFactory.newMapStatus(status))
//}
//
//fun MapView.whereI(location: BDLocation, @DrawableRes res: Int) {
//  val locData = MyLocationData.Builder()
//      .accuracy(location.radius)
//      .direction(location.direction)
//      .latitude(location.latitude)
//      .longitude(location.longitude)
//      .build()
//
//  map.isMyLocationEnabled = true
//  map.setMyLocationData(locData)
//  val mCurrentMarker = BitmapDescriptorFactory.fromResource(res)
//  val config = MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, mCurrentMarker);
//  map.setMyLocationConfiguration(config)
//}
//
//class IconOverlay(val mapView: MapView, position: LatLng) {
//  private val options = MarkerOptions()
//  private var onClick: ((overlay: Overlay) -> Unit)? = null
//
//  init {
//    options.position(position)
//  }
//
//  fun icon(@DrawableRes res: Int) = apply { options.icon(BitmapDescriptorFactory.fromResource(res)) }
//  fun title(title: String) = apply { options.title(title) }
//  fun zIndex(zIndex: Int) = apply { options.zIndex(zIndex) }
//  fun draggable(draggable: Boolean) = apply { options.draggable(draggable) }
//  fun data(data: Parcelable) = apply {
//    val extra = Bundle()
//    extra.putParcelable(EXTRA_BD, data)
//    options.extraInfo(extra)
//  }
//
//  fun onClick(onClick: (overlay: Overlay) -> Unit) = apply { this.onClick = onClick }
//
//  fun add() {
//    val overlay = mapView.map.addOverlay(options)
//    val onClick = onClick
//    if (onClick != null) {
//      mapView.map.setOnMarkerClickListener {
//        if (overlay == it) onClick(it)
//        overlay == it
//      }
////      overlay.remove()
//    }
//  }
//}

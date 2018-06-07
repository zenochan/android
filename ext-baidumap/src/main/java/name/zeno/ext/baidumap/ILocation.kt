package name.zeno.ext.baidumap

import android.content.Context
import com.baidu.location.BDLocation
import io.reactivex.subjects.Subject

/**
 * @author [陈治谋](mailto:zenochan@qq.com)
 * @since 2018/6/6
 */
interface ILocation {
  /**
   * 获取定位
   */
  fun requestLocation(): Subject<BDLocation>

  companion object {
    fun getInstance(context: Context): ILocation = Location.getInstance(context)
  }
}
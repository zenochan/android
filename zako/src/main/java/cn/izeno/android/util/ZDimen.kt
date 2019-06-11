package cn.izeno.android.util

import android.content.res.Resources
import android.graphics.Point
import android.util.TypedValue
import android.view.ViewGroup

/**
 * 尺寸工具类
 *
 * @author 陈治谋 (chenzhimou@tele-sing.com)
 * @version 2015-10-19 13:59:56
 */
object ZDimen {
  const val MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT
  const val WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT

  /**
   * 获取屏幕像素大小
   * @return Point(widthPixels, heightPixels)
   */
  val windowPixelsSize: Point
    get() {
      val dm = displayMetrics
      return Point(dm.widthPixels, dm.heightPixels)
    }

  fun dp2px(dp: Float): Int = dp.dp

  fun sp2px(sp: Float): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, displayMetrics).toInt()
  }

  fun px2dp(px: Int): Int {
    return (px / displayMetrics.density + 0.5f).toInt()
  }

  fun px2sp(px: Int): Int {
    return (px / displayMetrics.scaledDensity + 0.5f).toInt()
  }

}

val displayMetrics
  get() = Resources.getSystem().displayMetrics

val Float.dp: Int
  get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, displayMetrics).toInt()
val Int.dp: Int
  get() = this.toFloat().dp

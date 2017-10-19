package name.zeno.android.util

import android.graphics.Point
import android.view.View

/**
 * Create Date: 16/6/19
 *
 * @author 陈治谋 (513500085@qq.com)
 */
object MeasureUtils {
  fun as4Rate3(widthMeasureSpec: Int, heightMeasureSpec: Int): Point {
    val wm = View.MeasureSpec.getMode(widthMeasureSpec)
    val hm = View.MeasureSpec.getMode(heightMeasureSpec)
    var w: Int
    var h: Int
    w = when (wm) {
      View.MeasureSpec.AT_MOST, View.MeasureSpec.EXACTLY -> View.MeasureSpec.getSize(widthMeasureSpec)
      View.MeasureSpec.UNSPECIFIED -> ZDimen.windowPixelsSize.x
      else -> ZDimen.windowPixelsSize.x
    }

    h = when (hm) {
      View.MeasureSpec.AT_MOST, View.MeasureSpec.EXACTLY -> View.MeasureSpec.getSize(heightMeasureSpec)
      View.MeasureSpec.UNSPECIFIED -> ZDimen.windowPixelsSize.y
      else -> ZDimen.windowPixelsSize.y
    }
    if (w * 3 / 4 > h) {
      w = h * 4 / 3
    }
    if (h > w * 3 / 4) {
      h = w * 3 / 4
    }

    return Point(w, h)
  }

  fun full(widthMeasureSpec: Int, heightMeasureSpec: Int): Point {
    val wm = View.MeasureSpec.getMode(widthMeasureSpec)
    val hm = View.MeasureSpec.getMode(heightMeasureSpec)
    val w = when (wm) {
      View.MeasureSpec.AT_MOST, View.MeasureSpec.EXACTLY -> View.MeasureSpec.getSize(widthMeasureSpec)
      View.MeasureSpec.UNSPECIFIED -> ZDimen.windowPixelsSize.x
      else -> ZDimen.windowPixelsSize.x
    }

    val h = when (hm) {
      View.MeasureSpec.AT_MOST, View.MeasureSpec.EXACTLY -> View.MeasureSpec.getSize(heightMeasureSpec)
      View.MeasureSpec.UNSPECIFIED -> ZDimen.windowPixelsSize.y
      else -> ZDimen.windowPixelsSize.y
    }

    return Point(w, h)

  }
}

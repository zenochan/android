package name.zeno.android.core

import android.graphics.PointF
import android.support.annotation.FloatRange

/**
 * 二次贝塞尔曲线插值
 */
fun bezier(@FloatRange(from = 0.0, to = 1.0) t: Float, start: PointF, control: PointF, end: PointF): PointF {
  val oneMinusT = 1.0f - t
  val point = PointF()
  point.x = (oneMinusT * oneMinusT * start.x
      + 2f * t * oneMinusT * control.x
      + t * t * end.x)
  point.y = (oneMinusT * oneMinusT * start.y
      + 2f * t * oneMinusT * control.y
      + t * t * end.y)
  return point
}

/**
 * 三次贝塞尔曲线插值
 */
fun bezier(@FloatRange(from = 0.0, to = 1.0) t: Float, start: PointF, controlA: PointF, controlB: PointF, end: PointF): PointF {
  val oneMinusT = 1.0f - t
  val point = PointF()
  point.x = (oneMinusT * oneMinusT * oneMinusT * start.x
      + 3f * oneMinusT * oneMinusT * t * controlA.x
      + 3f * oneMinusT * t * t * controlB.x
      + t * t * t * end.x)

  point.y = (oneMinusT * oneMinusT * oneMinusT * start.y
      + 3f * oneMinusT * oneMinusT * t * controlA.y
      + 3f * oneMinusT * t * t * controlB.y
      + t * t * t * end.y)
  return point
}

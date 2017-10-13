package name.zeno.android.anim

import android.view.animation.Animation
import android.view.animation.Transformation

/**
 * 允许监听动画时间插值
 *
 *
 * Create Date: 15/10/22
 *
 * @author 陈治谋 (513500085@qq.com)
 */
class InterpolationObservableAnimation : Animation() {

  var onInterpolatedListener: OnInterpolatedListener? = null

  interface OnInterpolatedListener {
    fun onInterpolated(interpolatedTime: Float)
  }

  override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
    super.applyTransformation(interpolatedTime, t)
    if (onInterpolatedListener != null) {
      onInterpolatedListener!!.onInterpolated(interpolatedTime)
    }
  }
}

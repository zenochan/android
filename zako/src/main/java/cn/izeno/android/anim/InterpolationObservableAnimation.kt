package cn.izeno.android.anim

import android.view.animation.Animation
import android.view.animation.Transformation

/**
 * # 允许监听动画时间插值
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since  15/10/22
 */
class InterpolationObservableAnimation : Animation() {

  var onInterpolatedListener: ((interpolatedTime: Float) -> Unit)? = null

  override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
    super.applyTransformation(interpolatedTime, t)
    onInterpolatedListener?.invoke(interpolatedTime)
  }
}

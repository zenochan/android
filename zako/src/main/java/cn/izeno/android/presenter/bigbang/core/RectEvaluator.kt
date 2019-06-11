/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package cn.izeno.android.presenter.bigbang.core

import android.animation.TypeEvaluator
import android.graphics.Rect

/**
 * @author baoyongzhang
 * @since 2016/10/20
 */
class RectEvaluator : TypeEvaluator<Rect> {

  private val mRect: Rect?

  constructor() {
    mRect = null
  }

  constructor(reuseRect: Rect) {
    mRect = reuseRect
  }

  override fun evaluate(fraction: Float, startValue: Rect, endValue: Rect): Rect {
    val left = startValue.left + ((endValue.left - startValue.left) * fraction).toInt()
    val top = startValue.top + ((endValue.top - startValue.top) * fraction).toInt()
    val right = startValue.right + ((endValue.right - startValue.right) * fraction).toInt()
    val bottom = startValue.bottom + ((endValue.bottom - startValue.bottom) * fraction).toInt()
    if (mRect == null) {
      return Rect(left, top, right, bottom)
    } else {
      mRect.set(left, top, right, bottom)
      return mRect
    }
  }
}

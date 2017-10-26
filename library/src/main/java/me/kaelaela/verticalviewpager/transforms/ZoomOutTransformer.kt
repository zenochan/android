package me.kaelaela.verticalviewpager.transforms

/**
 * Copyright (C) 2015 Kaelaela
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

import android.support.v4.view.ViewPager
import android.view.View

class ZoomOutTransformer : ViewPager.PageTransformer {

  override fun transformPage(view: View, position: Float) {
    val pageWidth = view.width
    val pageHeight = view.height
    var alpha = 0f
    if (0 <= position && position <= 1) {
      alpha = 1 - position
    } else if (-1 < position && position < 0) {
      val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
      val verticalMargin = pageHeight * (1 - scaleFactor) / 2
      val horizontalMargin = pageWidth * (1 - scaleFactor) / 2
      if (position < 0) {
        view.translationX = horizontalMargin - verticalMargin / 2
      } else {
        view.translationX = -horizontalMargin + verticalMargin / 2
      }

      view.scaleX = scaleFactor
      view.scaleY = scaleFactor

      alpha = position + 1
    }

    view.alpha = alpha
    view.translationX = view.width * -position
    val yPosition = position * view.height
    view.translationY = yPosition
  }

  companion object {
    private val MIN_SCALE = 0.90f
  }

}

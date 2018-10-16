package me.kaelaela.verticalviewpager.transforms

import android.view.View
import androidx.viewpager.widget.ViewPager

/**
 * Copyright (C) 2015 Kaelaela
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
class DefaultTransformer : ViewPager.PageTransformer {
  override fun transformPage(view: View, position: Float) {
    var alpha = 0f
    if (position in 0.0..1.0) {
      alpha = 1 - position
    } else if (-1 < position && position < 0) {
      alpha = position + 1
    }
    view.alpha = alpha
    view.translationX = view.width * -position
    val yPosition = position * view.height
    view.translationY = yPosition
  }
}

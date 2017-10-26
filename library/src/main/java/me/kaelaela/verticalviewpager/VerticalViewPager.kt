package me.kaelaela.verticalviewpager

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

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

import me.kaelaela.verticalviewpager.transforms.DefaultTransformer

class VerticalViewPager @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ViewPager(context, attrs) {

  init {
    setPageTransformer(false, DefaultTransformer())
  }

  private fun swapTouchEvent(event: MotionEvent): MotionEvent {
    val width = width.toFloat()
    val height = height.toFloat()

    val swappedX = event.y / height * width
    val swappedY = event.x / width * height

    event.setLocation(swappedX, swappedY)

    return event
  }

  override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
    val intercept = super.onInterceptTouchEvent(swapTouchEvent(event))
    //If not intercept, touch event should not be swapped.
    swapTouchEvent(event)
    return intercept
  }

  override fun onTouchEvent(ev: MotionEvent): Boolean {
    return super.onTouchEvent(swapTouchEvent(ev))
  }

}

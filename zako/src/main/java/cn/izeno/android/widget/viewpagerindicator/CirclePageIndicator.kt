/*
 * Copyright (C) 2011 Patrik Akerfeldt
 * Copyright (C) 2011 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.izeno.android.widget.viewpagerindicator

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Paint.Style
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.LinearLayout.HORIZONTAL
import android.widget.LinearLayout.VERTICAL
import androidx.core.view.ViewCompat
import androidx.viewpager.widget.ViewPager
import cn.izeno.android.util.R

class CirclePageIndicator @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = R.attr.vpiCirclePageIndicatorStyle) : View(context, attrs, defStyle), PageIndicator {

  // 默认 3dp
  private var radiusNormal: Float = 0F
  // 选中的 radius， 默认为 radiusNormal
  private var radiusSelected = -1f
  // 间距，默认为 radiusSelected  * 3
  private var spacing = -1f

  private val mPaintPageFill = Paint(ANTI_ALIAS_FLAG)
  private val mPaintStroke = Paint(ANTI_ALIAS_FLAG)
  private val mPaintFill = Paint(ANTI_ALIAS_FLAG)
  private var mViewPager: ViewPager? = null
  private var mListener: ViewPager.OnPageChangeListener? = null
  private var mCurrentPage: Int = 0
  private var mSnapPage: Int = 0
  private var mPageOffset: Float = 0.toFloat()
  private var mScrollState: Int = 0
  private var mOrientation: Int = 0
  private var mCentered: Boolean = false
  private var mSnap: Boolean = false

  private val mTouchSlop: Int
  private var mLastMotionX = -1f
  private var mActivePointerId = INVALID_POINTER
  private var mIsDragging: Boolean = false

  var isCentered: Boolean
    get() = mCentered
    set(centered) {
      mCentered = centered
      invalidate()
    }

  var pageColor: Int
    get() = mPaintPageFill.color
    set(pageColor) {
      mPaintPageFill.color = pageColor
      invalidate()
    }

  var fillColor: Int
    get() = mPaintFill.color
    set(fillColor) {
      mPaintFill.color = fillColor
      invalidate()
    }

  var orientation: Int
    get() = mOrientation
    set(orientation) = when (orientation) {
      HORIZONTAL, VERTICAL -> {
        mOrientation = orientation
        requestLayout()
      }

      else -> throw IllegalArgumentException("Orientation must be either HORIZONTAL or VERTICAL.")
    }

  var strokeColor: Int
    get() = mPaintStroke.color
    set(strokeColor) {
      mPaintStroke.color = strokeColor
      invalidate()
    }

  var strokeWidth: Float
    get() = mPaintStroke.strokeWidth
    set(strokeWidth) {
      mPaintStroke.strokeWidth = strokeWidth
      invalidate()
    }

  var radius: Float
    get() = radiusNormal
    set(radius) {
      radiusNormal = radius
      invalidate()
    }

  var isSnap: Boolean
    get() = mSnap
    set(snap) {
      mSnap = snap
      invalidate()
    }

  init {
    //    if (isInEditMode()) return;

    //Load defaults from resources
    val res = resources
    val defaultPageColor = res.getColor(R.color.default_circle_indicator_page_color)
    val defaultFillColor = res.getColor(R.color.default_circle_indicator_fill_color)
    val defaultOrientation = res.getInteger(R.integer.default_circle_indicator_orientation)
    val defaultStrokeColor = res.getColor(R.color.default_circle_indicator_stroke_color)
    val defaultStrokeWidth = res.getDimension(R.dimen.default_circle_indicator_stroke_width)
    val defaultRadius = res.getDimension(R.dimen.default_circle_indicator_radius)
    val defaultCentered = res.getBoolean(R.bool.default_circle_indicator_centered)
    val defaultSnap = res.getBoolean(R.bool.default_circle_indicator_snap)

    //Retrieve styles attributes
    val a = context.obtainStyledAttributes(attrs, R.styleable.CirclePageIndicator, defStyle, 0)

    mCentered = a.getBoolean(R.styleable.CirclePageIndicator_centered, defaultCentered)
    mOrientation = a.getInt(R.styleable.CirclePageIndicator_android_orientation, defaultOrientation)
    mPaintPageFill.style = Style.FILL
    mPaintPageFill.color = a.getColor(R.styleable.CirclePageIndicator_pageColor, defaultPageColor)
    mPaintStroke.style = Style.STROKE
    mPaintStroke.color = a.getColor(R.styleable.CirclePageIndicator_strokeColor, defaultStrokeColor)
    mPaintStroke.strokeWidth = a.getDimension(R.styleable.CirclePageIndicator_strokeWidth, defaultStrokeWidth)
    mPaintFill.style = Style.FILL
    mPaintFill.color = a.getColor(R.styleable.CirclePageIndicator_fillColor, defaultFillColor)

    radiusNormal = a.getDimension(R.styleable.CirclePageIndicator_radius, defaultRadius)
    radiusSelected = a.getDimension(R.styleable.CirclePageIndicator_radiusSelected, defaultRadius)
    spacing = a.getDimension(R.styleable.CirclePageIndicator_spacing, radiusSelected * 3)

    mSnap = a.getBoolean(R.styleable.CirclePageIndicator_snap, defaultSnap)

    val background = a.getDrawable(R.styleable.CirclePageIndicator_android_background)
    if (background != null) {
//      setBackgroundDrawable(background)
      ViewCompat.setBackground(this, background)
    }

    a.recycle()

    val configuration = ViewConfiguration.get(context)
    mTouchSlop = configuration.scaledPagingTouchSlop
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    val count: Int

    when {
      isInEditMode -> {
        count = 5
        mCurrentPage = 2
      }
      mViewPager?.adapter != null -> {
        count = mViewPager!!.adapter!!.count
        if (count == 0) return
      }
      else -> return
    }

    if (mCurrentPage >= count) {
      setCurrentItem(count - 1)
      return
    }

    // 长边，长边前后padding， 短边前 padding
    val longSize: Int
    val longPaddingBefore: Int
    val longPaddingAfter: Int
    val shortPaddingBefore: Int
    if (mOrientation == HORIZONTAL) {
      longSize = width
      longPaddingBefore = paddingLeft
      longPaddingAfter = paddingRight
      shortPaddingBefore = paddingTop
    } else {
      longSize = height
      longPaddingBefore = paddingTop
      longPaddingAfter = paddingBottom
      shortPaddingBefore = paddingLeft
    }

    //    final float threeRadius = radiusNormal * 3;
    val threeRadius = spacing
    val shortOffset = shortPaddingBefore + Math.max(radiusNormal, radiusSelected)
    var longOffset = longPaddingBefore + Math.max(radiusNormal, radiusSelected)

    // 居中偏移量
    if (mCentered) {
      val centerOffset = (longSize - longPaddingBefore - longPaddingAfter) / 2.0f - count * threeRadius / 2.0f
      longOffset += centerOffset
    }

    var dX: Float
    var dY: Float

    var pageFillRadius = radiusNormal
    if (mPaintStroke.strokeWidth > 0) {
      pageFillRadius -= mPaintStroke.strokeWidth / 2.0f
    }

    // 圆边框
    for (i in 0 until count) {
      val drawLong = longOffset + i * threeRadius
      if (mOrientation == HORIZONTAL) {
        dX = drawLong
        dY = shortOffset
      } else {
        dX = shortOffset
        dY = drawLong
      }
      // Only paint fill if not completely transparent
      if (mPaintPageFill.alpha > 0) {
        canvas.drawCircle(dX, dY, pageFillRadius, mPaintPageFill)
      }

      // Only paint stroke if a stroke width was non-zero
      if (pageFillRadius != radiusNormal) {
        canvas.drawCircle(dX, dY, radiusNormal - mPaintStroke.strokeWidth / 2, mPaintStroke)
      }
    }

    // 圆填充
    //Draw the filled circle according to the current scroll
    var cx = (if (mSnap) mSnapPage else mCurrentPage) * threeRadius
    if (!mSnap) {
      cx += mPageOffset * threeRadius
    }
    if (mOrientation == HORIZONTAL) {
      dX = longOffset + cx
      dY = shortOffset
    } else {
      dX = shortOffset
      dY = longOffset + cx
    }
    canvas.drawCircle(dX, dY, radiusSelected, mPaintFill)
  }

  override fun onTouchEvent(ev: MotionEvent): Boolean {
    if (super.onTouchEvent(ev)) {
      return true
    }

    var count = mViewPager?.adapter?.count ?: 0
    if (count <= 0) return false


    val action = ev.action and MotionEvent.ACTION_MASK
    when (action) {
      MotionEvent.ACTION_DOWN -> {
        mActivePointerId = ev.getPointerId(0)
        mLastMotionX = ev.x
      }

      MotionEvent.ACTION_MOVE -> {
        val activePointerIndex = ev.findPointerIndex(mActivePointerId)
        val x = ev.getX(activePointerIndex)
        val deltaX = x - mLastMotionX

        if (!mIsDragging) {
          if (Math.abs(deltaX) > mTouchSlop) {
            mIsDragging = true
          }
        }

        if (mIsDragging) {
          mLastMotionX = x
          mViewPager?.let { if (it.isFakeDragging || it.beginFakeDrag()) it.fakeDragBy(deltaX) }
        }
      }

      MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
        if (!mIsDragging) {
          count = mViewPager?.adapter?.count ?: 0
          val width = width
          val halfWidth = width / 2f
          val sixthWidth = width / 6f

          if (mCurrentPage > 0 && ev.x < halfWidth - sixthWidth) {
            if (action != MotionEvent.ACTION_CANCEL) {
              mViewPager!!.currentItem = mCurrentPage - 1
            }
            return true
          } else if (mCurrentPage < count - 1 && ev.x > halfWidth + sixthWidth) {
            if (action != MotionEvent.ACTION_CANCEL) {
              mViewPager!!.currentItem = mCurrentPage + 1
            }
            return true
          }
        }

        mIsDragging = false
        mActivePointerId = INVALID_POINTER
        mViewPager?.let { if (it.isFakeDragging) it.endFakeDrag() }
      }

      MotionEvent.ACTION_POINTER_DOWN -> {
        val index = ev.actionIndex
        mLastMotionX = ev.getX(index)
        mActivePointerId = ev.getPointerId(index)
      }

      MotionEvent.ACTION_POINTER_UP -> {
        val pointerIndex = ev.actionIndex
        val pointerId = ev.getPointerId(pointerIndex)
        if (pointerId == mActivePointerId) {
          val newPointerIndex = if (pointerIndex == 0) 1 else 0
          mActivePointerId = ev.getPointerId(newPointerIndex)
        }
        mLastMotionX = ev.getX(ev.findPointerIndex(mActivePointerId))
      }
    }

    return true
  }

  override fun setViewPager(view: ViewPager) {
    if (mViewPager === view) {
      return
    }
    mViewPager?.removeOnPageChangeListener(this)

    if (view.adapter == null) {
      throw IllegalStateException("ViewPager does not have adapter instance.")
    }
    mViewPager = view
    mViewPager?.addOnPageChangeListener(this)
    invalidate()
  }

  override fun setViewPager(view: ViewPager, initialPosition: Int) {
    setViewPager(view)
    setCurrentItem(initialPosition)
  }

  override fun setCurrentItem(item: Int) {
    if (mViewPager == null) {
      throw IllegalStateException("ViewPager has not been bound.")
    }
    mViewPager?.currentItem = item
    mCurrentPage = item
    invalidate()
  }

  override fun notifyDataSetChanged() {
    invalidate()
  }

  override fun onPageScrollStateChanged(state: Int) {
    mScrollState = state
    mListener?.onPageScrollStateChanged(state)
  }

  override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    mCurrentPage = position
    mPageOffset = positionOffset
    invalidate()

    mListener?.onPageScrolled(position, positionOffset, positionOffsetPixels)
  }

  override fun onPageSelected(position: Int) {
    if (mSnap || mScrollState == ViewPager.SCROLL_STATE_IDLE) {
      mCurrentPage = position
      mSnapPage = position
      invalidate()
    }

    mListener?.onPageSelected(position)
  }

  override fun setOnPageChangeListener(listener: ViewPager.OnPageChangeListener) {
    mListener = listener
  }

  /*
   * (non-Javadoc)
   *
   * @see android.view.View#onMeasure(int, int)
   */
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    if (mOrientation == HORIZONTAL) {
      setMeasuredDimension(measureLong(widthMeasureSpec), measureShort(heightMeasureSpec))
    } else {
      setMeasuredDimension(measureShort(widthMeasureSpec), measureLong(heightMeasureSpec))
    }
  }

  /**
   * Determines the width of this view
   *
   * @param measureSpec A measureSpec packed into an int
   * @return The width of the view, honoring constraints from measureSpec
   */
  private fun measureLong(measureSpec: Int): Int {
    var result: Int
    val specMode = View.MeasureSpec.getMode(measureSpec)
    val specSize = View.MeasureSpec.getSize(measureSpec)

    if (specMode == View.MeasureSpec.EXACTLY || mViewPager == null) {
      //We were told how big to be
      result = specSize
    } else {
      //Calculate the width according the views count
      val count = mViewPager!!.adapter!!.count
      result = (paddingLeft.toFloat() + paddingRight.toFloat()
          + count.toFloat() * 2f * radiusNormal + (count - 1) * radiusNormal + 1f).toInt()
      //Respect AT_MOST value if that was what is called for by measureSpec
      if (specMode == View.MeasureSpec.AT_MOST) {
        result = Math.min(result, specSize)
      }
    }
    return result
  }

  /**
   * Determines the height of this view
   *
   * @param measureSpec A measureSpec packed into an int
   * @return The height of the view, honoring constraints from measureSpec
   */
  private fun measureShort(measureSpec: Int): Int {
    var result: Int
    val specMode = View.MeasureSpec.getMode(measureSpec)
    val specSize = View.MeasureSpec.getSize(measureSpec)

    if (specMode == View.MeasureSpec.EXACTLY) {
      //We were told how big to be
      result = specSize
    } else {
      //Measure the height
      result = (2 * radiusNormal + paddingTop.toFloat() + paddingBottom.toFloat() + 1f).toInt()
      //Respect AT_MOST value if that was what is called for by measureSpec
      if (specMode == View.MeasureSpec.AT_MOST) {
        result = Math.min(result, specSize)
      }
    }
    return result
  }

  public override fun onRestoreInstanceState(state: Parcelable) {
    val savedState = state as SavedState
    super.onRestoreInstanceState(savedState.superState)
    mCurrentPage = savedState.currentPage
    mSnapPage = savedState.currentPage
    requestLayout()
  }

  public override fun onSaveInstanceState(): Parcelable? {
    val superState = super.onSaveInstanceState()
    val savedState = SavedState(superState)
    savedState.currentPage = mCurrentPage
    return savedState
  }

  internal class SavedState : View.BaseSavedState {
    var currentPage: Int = 0

    constructor(superState: Parcelable) : super(superState) {}

    private constructor(`in`: Parcel) : super(`in`) {
      currentPage = `in`.readInt()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
      super.writeToParcel(dest, flags)
      dest.writeInt(currentPage)
    }

    companion object {

      val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
        override fun createFromParcel(`in`: Parcel): SavedState {
          return SavedState(`in`)
        }

        override fun newArray(size: Int): Array<SavedState?> {
          return arrayOfNulls(size)
        }
      }
    }
  }

  companion object {
    private val INVALID_POINTER = -1
  }
}

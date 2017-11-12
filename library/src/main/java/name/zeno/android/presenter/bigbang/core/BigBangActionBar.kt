/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package name.zeno.android.presenter.bigbang.core

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import name.zeno.android.util.R

internal class BigBangActionBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr), View.OnClickListener {
  var mSearch: ImageView
  var mShare: ImageView
  var mCopy: ImageView
  var mBorder: Drawable

  private var mActionGap: Int = 0
  var contentPadding: Int = 0
    private set
  private var mActionListener: ActionListener? = null

  init {
    mBorder = ContextCompat.getDrawable(context, R.drawable.bigbang_action_bar_bg)!!
    mBorder.callback = this

    mSearch = ImageView(context)
    mSearch.setImageResource(R.mipmap.bigbang_action_search)
    mSearch.setOnClickListener(this)
    mShare = ImageView(context)
    mShare.setImageResource(R.mipmap.bigbang_action_share)
    mShare.setOnClickListener(this)
    mCopy = ImageView(context)
    mCopy.setImageResource(R.mipmap.bigbang_action_copy)
    mCopy.setOnClickListener(this)

    addView(mSearch, createLayoutParams())
    addView(mShare, createLayoutParams())
    addView(mCopy, createLayoutParams())

    setWillNotDraw(false)

    mActionGap = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15f, resources.displayMetrics).toInt()
    contentPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, resources.displayMetrics).toInt()
  }

  private fun createLayoutParams(): ViewGroup.LayoutParams {
    return LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
  }

  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    super.onSizeChanged(w, h, oldw, oldh)
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val childCount = childCount
    val measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    for (i in 0 until childCount) {
      val child = getChildAt(i)
      child.measure(measureSpec, measureSpec)
    }

    val width = View.MeasureSpec.getSize(widthMeasureSpec)
    val height = View.MeasureSpec.getSize(heightMeasureSpec)

    setMeasuredDimension(width, height + contentPadding + mSearch.measuredHeight)
  }

  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    val width = measuredWidth
    val height = measuredHeight

    layoutSubView(mSearch, mActionGap, 0)
    layoutSubView(mShare, width - mActionGap * 2 - mShare.measuredWidth - mCopy.measuredWidth, 0)
    layoutSubView(mCopy, width - mActionGap - mCopy.measuredWidth, 0)

    val oldBounds = mBorder.bounds
    val newBounds = Rect(0, mSearch.measuredHeight / 2, width, height)

    if (oldBounds != newBounds) {
      ObjectAnimator.ofObject(mBorder, "bounds", RectEvaluator(), oldBounds, newBounds).setDuration(200).start()
    }
  }

  private fun layoutSubView(view: View, l: Int, t: Int) {
    view.layout(l, t, view.measuredWidth + l, view.measuredHeight + t)
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    mBorder.draw(canvas)
  }

  override fun verifyDrawable(who: Drawable): Boolean {
    return super.verifyDrawable(who) || who === mBorder
  }

  fun setActionListener(actionListener: ActionListener) {
    mActionListener = actionListener
  }

  override fun onClick(v: View) {
    if (mActionListener == null) {
      return
    }
    if (v === mSearch) {
      mActionListener!!.onSearch()
    } else if (v === mShare) {
      mActionListener!!.onShare()
    } else if (v === mCopy) {
      mActionListener!!.onCopy()
    }
  }

  internal interface ActionListener {
    fun onSearch()
    fun onShare()
    fun onCopy()
  }
}

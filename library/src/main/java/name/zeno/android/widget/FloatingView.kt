/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package name.zeno.android.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import android.util.TypedValue
import android.view.Gravity
import android.view.WindowManager
import androidx.appcompat.widget.AppCompatImageView
import name.zeno.android.util.R
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

class FloatingView(context: Context) : AppCompatImageView(context) {
  private val mWindowManager: WindowManager
  private var mMargin: Int = 0
  private val mDismissTask = Runnable { this.dismiss() }
  private var show: Boolean = false
  private var mText: String? = null

  init {
    setImageResource(R.mipmap.bigbang_action_copy)
    mMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, resources.displayMetrics).toInt()

    mWindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    setOnClickListener { v ->
      try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("bigBang://?extra_text=" + URLEncoder.encode(mText, "utf-8")))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        getContext().startActivity(intent)
      } catch (e: UnsupportedEncodingException) {
        e.printStackTrace()
      }

      dismiss()
    }
  }

  fun show() {
    if (!show) {
      val w = WindowManager.LayoutParams.WRAP_CONTENT
      val h = WindowManager.LayoutParams.WRAP_CONTENT
      val flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
      val type: Int
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        type = WindowManager.LayoutParams.TYPE_TOAST
      } else {
        type = WindowManager.LayoutParams.TYPE_PHONE
      }

      val layoutParams = WindowManager.LayoutParams(w, h, type, flags, PixelFormat.TRANSLUCENT)
      layoutParams.gravity = Gravity.RIGHT or Gravity.BOTTOM
      layoutParams.x = mMargin
      layoutParams.y = mMargin

      mWindowManager.addView(this, layoutParams)

      show = true

      scaleX = 0f
      scaleY = 0f
      animate().cancel()
      animate().scaleY(1f).scaleX(1f).setDuration(ANIMATION_DURATION.toLong()).setListener(null).start()
    }

    removeCallbacks(mDismissTask)
    postDelayed(mDismissTask, 3000)
  }

  fun dismiss() {
    if (show) {
      animate().cancel()
      animate().scaleX(0f).scaleY(0f).setDuration(ANIMATION_DURATION.toLong()).setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
          super.onAnimationEnd(animation)
          mWindowManager.removeView(this@FloatingView)
          show = false
        }
      }).start()
    }
    removeCallbacks(mDismissTask)
  }

  fun setText(text: String) {
    mText = text
  }

  fun setMMargin(mMargin: Int) {
    this.mMargin = mMargin
  }

  companion object {
    private val ANIMATION_DURATION = 500
  }
}

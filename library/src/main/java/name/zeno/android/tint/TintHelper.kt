package name.zeno.android.tint

import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.StyleableRes
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView


object TintHelper {

  fun loadFromAttributes(view: View, attrs: AttributeSet, defStyleAttr: Int, @StyleableRes styleable: IntArray, tintIndex: Int, tintModeIndex: Int) {
    val a = view.context.obtainStyledAttributes(attrs, styleable, defStyleAttr, 0)
    try {
      if (a.hasValue(tintIndex)) {
        ViewCompat.setBackgroundTintList(view, a.getColorStateList(tintIndex))
      }
      if (a.hasValue(tintModeIndex)) {
        ViewCompat.setBackgroundTintMode(view, parseTintMode(a.getInt(tintModeIndex, -1), null))
      }
    } finally {
      a.recycle()
    }
  }

  fun onSetBackgroundResource(view: View, info: TintInfo, resId: Int) {
    applySupportBackgroundTint(view, info)
  }

  fun onSetBackgroundDrawable(view: View, info: TintInfo, background: Drawable) {
    applySupportBackgroundTint(view, info)
  }

  fun setSupportBackgroundTintList(view: View, info: TintInfo, tint: ColorStateList) {
    info.tintList = tint
    applySupportBackgroundTint(view, info)
  }

  fun setSupportDrawableTintList(view: ImageView, info: TintInfo, tint: ColorStateList) {
    info.tintList = tint
    applySupportTint(view, info)
  }

  fun setSupportCompoundDrawableTintList(view: TextView, info: TintInfo, tint: ColorStateList) {
    info.tintList = tint
    val drawables = view.compoundDrawables
    for (drawable in drawables) {
      if (drawable != null) {
        TintManager.tintDrawable(drawable, info, view.drawableState)
      }
    }
  }

  fun getSupportTintList(info: TintInfo?): ColorStateList? {
    return info?.tintList
  }

  fun setSupportTintMode(view: View, info: TintInfo?, tintMode: PorterDuff.Mode) {
    val tintInfo = info ?: TintInfo()
    tintInfo.tintMode = tintMode
    tintInfo.hasTintMode = true

    applySupportBackgroundTint(view, tintInfo)
  }

  fun getSupportTintMode(info: TintInfo?): PorterDuff.Mode? {
    return info?.tintMode
  }

  internal fun applySupportBackgroundTint(view: View, info: TintInfo?) {
    val background = view.background
    if (background != null && info != null) {
      TintManager.tintDrawable(background, info, view.drawableState)
    }
  }

  internal fun applySupportTint(view: ImageView, info: TintInfo?) {
    val drawable = view.drawable
    if (drawable != null && info != null) {
      TintManager.tintDrawable(drawable, info, view.drawableState)
    }
  }

  internal fun parseTintMode(value: Int, defaultMode: PorterDuff.Mode?): PorterDuff.Mode? {
    when (value) {
      3 -> return PorterDuff.Mode.SRC_OVER
      5 -> return PorterDuff.Mode.SRC_IN
      9 -> return PorterDuff.Mode.SRC_ATOP
      14 -> return PorterDuff.Mode.MULTIPLY
      15 -> return PorterDuff.Mode.SCREEN
      16 -> return if (Build.VERSION.SDK_INT >= 11)
        PorterDuff.Mode.valueOf("ADD")
      else
        defaultMode
      else -> return defaultMode
    }
  }
}

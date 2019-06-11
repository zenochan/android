package cn.izeno.android.util

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.widget.TextView
import androidx.annotation.ColorInt
import cn.izeno.android.tint.TintInfo
import cn.izeno.android.tint.TintManager

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/5/19
 */
object ZDrawable {
  fun tintComponentDrawable(view: TextView, @ColorInt color: Int) {
    val drawables = view.compoundDrawables

    val info = TintInfo()
    info.hasTintList = true
    info.tintList = ColorStateList.valueOf(color)
    for (drawable in drawables) {
      if (drawable != null) {
        TintManager.tintDrawable(drawable, info, view.drawableState)
      }
    }
  }

  fun resizeComponentDrawable(view: TextView, widthDp: Int, heightDp: Int) {

    val drawables = view.compoundDrawables
    for (drawable in drawables) {
      setImageSize(drawable, widthDp, heightDp)
    }
    // 将图片放回到TextView中
    view.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3])
  }

  private fun setImageSize(drawable: Drawable?, widthDp: Int, heightDp: Int) {
    if (drawable != null && widthDp != -1 && heightDp != -1) {
      drawable.setBounds(0, 0, ZDimen.dp2px(widthDp.toFloat()), ZDimen.dp2px(heightDp.toFloat()))
    }
  }

}

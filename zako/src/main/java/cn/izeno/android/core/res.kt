/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/11/3
 */

package cn.izeno.android.core

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat


/**
 * 获取drawable， 没有则返回 ColorDrawable
 * @param res drawableRes
 * @param colorInt 没有找到 drawable 时，返回 ColorDrawable 的颜色
 */
fun Context?.drawable(
    @DrawableRes res: Int,
    @ColorInt colorInt: Int = Color.LTGRAY
) = when {
  this == null -> ColorDrawable(colorInt)
  else -> ContextCompat.getDrawable(this, res) ?: ColorDrawable(colorInt)
}


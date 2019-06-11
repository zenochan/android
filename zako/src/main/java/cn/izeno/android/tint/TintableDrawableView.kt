package cn.izeno.android.tint

import android.content.res.ColorStateList
import android.graphics.PorterDuff

/**
 * Create Date: 16/6/23
 *
 * @author 陈治谋 (513500085@qq.com)
 */
interface TintableDrawableView {
  var supportDrawableTintList: ColorStateList?

  var supportDrawableTintMode: PorterDuff.Mode?
}

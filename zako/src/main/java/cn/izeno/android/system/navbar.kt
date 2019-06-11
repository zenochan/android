package cn.izeno.android.system

import android.app.Activity
import android.util.TypedValue


/**
 * @author Zeno [mail](mailto:zenochan@qq.com)
 * @since 2018/7/5
 */


/**
 * 获取虚拟按钮ActionBar的高度
 *
 * @param activity activity
 * @return ActionBar高度
 */
val Activity.navbarHeight: Int
  get() {
    val tv = TypedValue()
    return if (theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
      TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
    } else 0
  }

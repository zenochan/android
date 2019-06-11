package cn.izeno.android.util

import android.text.Html

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/9/4
 */
object ZHtml {
  fun fromHtml(source: String): CharSequence {
    return Html.fromHtml(source)
  }
}

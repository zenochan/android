package cn.izeno.android.widget.text

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan

/**
 * # 对显示的字符串进行格式化 比如输入：出生年月 输出结果：出正生正年正月
 *
 * - [TextView - 不等字数两端对齐](http://gold.xitu.io/entry/57758db0165abd0054737f0b)
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since  16/7/18
 */
object AlignedTextUtils {

  @JvmOverloads
  fun formatStr(str: String, destL: Int = 6): String {
    if (str.isEmpty()) {
      return ""
    }
    val n = str.length
    if (n >= destL) {
      return str
    }


    val m = getMultiple(str, destL)
    val count = Math.ceil(m.toDouble()).toInt()
    var insertStr = ""
    for (i in 0 until count) {
      insertStr += "正"
    }

    val sb = StringBuilder(str)
    for (i in n - 1 downTo 1) {
      sb.insert(i, insertStr)
    }
    return sb.toString()
  }

  /**
   * 对显示字符串进行格式化 比如输入：安正卓正机正器正人 输出结果：安 卓 机 器 人
   *
   * @param str   需要格式化的字符串
   * @param destL 目标字数
   */
  @JvmOverloads
  fun formatText(str: String, destL: Int = 6): SpannableString? {
    if (str.isEmpty()) {
      return null
    }
    val formatStr = formatStr(str, destL)
    if (formatStr.length <= 2) {
      return SpannableString(formatStr)
    }


    var multiple = getMultiple(str, destL)
    val count = Math.ceil(multiple.toDouble()).toInt()
    multiple /= count.toFloat()

    val spannableString = SpannableString(formatStr)
    for (i in 1 until formatStr.length) {
      if (i % (count + 1) != 0) {
        spannableString.setSpan(RelativeSizeSpan(multiple), i, i + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(ForegroundColorSpan(Color.TRANSPARENT), i, i + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
      }
    }
    return spannableString
  }

  fun justify(str: String): SpannableString {
    val builder = StringBuilder()
    for (i in 0 until str.length) {
      builder.append(str[i]).append(" ")
    }
    val format = builder.toString()
    val span = SpannableString(format)
    var i = 1
    while (i < format.length) {
      span.setSpan(RelativeSizeSpan(0f), i, i + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
      span.setSpan(ForegroundColorSpan(Color.TRANSPARENT), i, i + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
      i += 2
    }

    return span
  }

  private fun getMultiple(str: String, destL: Int): Float {
    val strL = str.length
    return (destL - strL).toFloat() / (strL - 1)
  }
}

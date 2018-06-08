package name.zeno.android.util

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.widget.TextView

import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Create Date: 16/6/13
 *
 * @author 陈治谋 (513500085@qq.com)
 */
object ZString {
  @JvmStatic
  fun trimLength(s: String?): Int {
    return s?.trim { it <= ' ' }?.length ?: 0
  }

  @JvmStatic
  fun encryptPhone(phone: String?): String {
    if (phone == null)
      return ""

    return if (phone.length < 11) phone else String.format("%s****%s", phone.substring(0, 3), phone.substring(7))
  }

  /**
   * 解码 Unicode \\uXXXX
   */
  @JvmStatic
  fun decodeUnicode(str: String): String {
    val set = Charset.forName("UTF-16")
    val p = Pattern.compile("\\\\u([0-9a-fA-F]{4})")
    val m = p.matcher(str)
    var start = 0
    var start2 = 0
    val sb = StringBuilder()
    while (m.find(start)) {
      start2 = m.start()
      if (start2 > start) {
        val seg = str.substring(start, start2)
        sb.append(seg)
      }
      val code = m.group(1)
      val i = Integer.valueOf(code, 16)!!
      val bb = ByteArray(4)
      bb[0] = (i shr 8 and 0xFF).toByte()
      bb[1] = (i and 0xFF).toByte()
      val b = ByteBuffer.wrap(bb)
      sb.append(set.decode(b).toString().trim { it <= ' ' })
      start = m.end()
    }
    start2 = str.length
    if (start2 > start) {
      val seg = str.substring(start, start2)
      sb.append(seg)
    }
    return sb.toString()
  }

  /** 半角转换为全角  */
  @JvmStatic
  fun toDBC(input: String): String {
    val c = input.toCharArray()
    for (i in c.indices) {
      if (c[i].toInt() == 12288) {
        c[i] = 32.toChar()
        continue
      }
      if (c[i].toInt() in 65281..65374)
        c[i] = (c[i].toInt() - 65248).toChar()
    }
    return String(c)
  }

  /** 全角转半角  */
  @JvmStatic
  fun toSBC(input: String): String {
    val chars = input.toCharArray()
    for (i in chars.indices) {
      if (chars[i] == ' ') {
        chars[i] = '\u3000'
      } else if (chars[i] < '\u007f') {
        chars[i] = (chars[i].toInt() + 65248).toChar()
      }
    }
    return String(chars)
  }

  @JvmStatic
  fun addBadge(textView: TextView) {
    val t = textView.text.toString()
    val badge = " ●"
    if (t.endsWith(badge)) return

    val spannable = SpannableString(t + badge)
    val p = spannable.length - 1
    spannable.setSpan(RelativeSizeSpan(0.7f), p, p + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
    spannable.setSpan(ForegroundColorSpan(Color.RED), p, p + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
    textView.text = spannable
  }

  @JvmStatic
  fun removeBadge(textView: TextView) {
    var t = textView.text.toString()
    if (t.endsWith(" ●")) {
      t = t.substring(0, t.length - 2)
      textView.text = t
    }
  }

  //设置开头文字大小
  @JvmStatic
  fun resizePreText(textView: TextView, length: Int, multi: Float) {
    val ss = SpannableString(textView.text)
    ss.setSpan(RelativeSizeSpan(multi), 0, length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
    textView.text = ss
  }

  @JvmStatic
  fun sub(str: String, start: Int, end: Int): String {
    var start = start
    var end = end
    if (start > end) {
      throw IllegalArgumentException("start > end")
    }

    if (str.isEmpty()) {
      return ""
    }

    var length = str.length
    length = if (length == 0) 1 else length

    start = Math.min(start, length - 1)
    end = Math.min(end, length - 1)

    return str.substring(start, end)
  }
}

fun String?.length(): Int = this.orEmpty().length

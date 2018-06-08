package name.zeno.android.util

import java.util.*
import java.util.regex.Pattern

object ZRex {
  private val TAG = "ZRex"
  // 2018-06-08
  private val PHONE_PATTERN = Pattern.compile("^1[3456789]\\d{9}$")

  fun validPhone(phone: String?): Boolean {
    return !(phone == null || phone.trim { it <= ' ' }.isEmpty()) && PHONE_PATTERN.matcher(phone).matches()
  }

  /**
   * @param regex 正则 不带前后斜杠
   * @param res   需要验证的值
   */
  fun pattern(regex: String, res: String): Boolean {
    return Pattern.compile(regex).matcher(res).matches()
  }

  fun find(regex: String, res: String): List<String> {
    val stringList = ArrayList<String>()
    try {
      val matcher = Pattern.compile(regex).matcher(res)
      while (matcher.find()) {
        stringList.add(matcher.group(0))
      }
    } catch (e: Exception) {
      ZLog.e(TAG, "[ZRex.find]卧槽，出错了！什么吊系统--->" + e.message, e)
    }

    return stringList
  }

  fun findFirst(rex: String, res: String): String? {
    try {
      val matcher = Pattern.compile(rex).matcher(res)
      if (matcher.find()) {
        return matcher.group(0)
      }
    } catch (e: Exception) {
      ZLog.e(TAG, "[ZRex.findFirst]卧槽，出错了！什么吊系统--->" + e.message, e)
    }

    return null
  }
}

package name.zeno.android.util

import android.annotation.SuppressLint

import java.util.Locale

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/11/18.
 */
object Yuan {
  fun y(value: Double): String {
    return y("", value)
  }

  fun y(prefix: String, value: Double): String {
    return prefix + String.format(Locale.CHINA, "%.02f", value)
  }

  /**
   * @return 元整数部分
   */
  fun ys(value: Double): String {
    return String.format(Locale.CHINA, "%.0f", value)
  }

  fun prefix(value: String?): String? {
    var result = value
    if (result != null && !result.startsWith("￥")) {
      result = "￥$result"
    }
    return result
  }

  @SuppressLint("DefaultLocale")
  fun big(value: Double): String {
    var result = value
    var fmt = "%.02f"
    if (result > 10000) {
      result /= 10000.0
      fmt += " 万"
    }
    return String.format(fmt, result)
  }
}

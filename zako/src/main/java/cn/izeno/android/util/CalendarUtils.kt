package cn.izeno.android.util

import java.util.Calendar

/**
 * Create Date: 16/6/16
 *
 * @author 陈治谋 (513500085@qq.com)
 */
object CalendarUtils {
  fun y(): Int {
    return Calendar.getInstance().get(Calendar.YEAR)
  }

  fun y(calendar: Calendar): Int {
    return calendar.get(Calendar.YEAR)
  }

  fun m(calendar: Calendar): Int {
    return calendar.get(Calendar.MONTH)
  }

  fun dayOfYear(): Int {
    return Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
  }

  fun d(calendar: Calendar): Int {
    return calendar.get(Calendar.DAY_OF_MONTH)
  }

  /** 24-hour clock  */
  fun h(): Int {
    return Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
  }

  /** @return 0~6: 周日~周六
   */
  fun w(timestamp: Long): Int {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timestamp
    return calendar.get(Calendar.DAY_OF_WEEK) - 1
  }

  fun today(timestamp: Long): Boolean {
    return ZDate.format(System.currentTimeMillis(), "yyyy-MM-dd") == ZDate.format(timestamp, "yyyy-MM-dd")
  }

  fun addDay(calendar: Calendar, deltaDay: Int) {
    calendar.add(Calendar.DAY_OF_MONTH, deltaDay)
  }

  fun isToday(calendar: Calendar): Boolean {
    return dayOfYear() == calendar.get(Calendar.DAY_OF_YEAR)
  }

  fun isTomorrow(calendar: Calendar): Boolean {
    return calendar.get(Calendar.DAY_OF_YEAR) - dayOfYear() == 1
  }
}

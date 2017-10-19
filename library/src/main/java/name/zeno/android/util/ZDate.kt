package name.zeno.android.util

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * 时间转换工具类
 *
 * @author 陈治谋 (chenzhimou@tele-sing.com)
 * @version 2015-10-19 14:00:14
 */
object ZDate {
  private val TAG = "ZDate"
  /** one day in ms  */
  val ONE_DAY = 1000 * 60 * 60 * 24

  fun now(): Date {
    return Date()
  }

  fun nowString(dateFmt: String): String {
    return SimpleDateFormat(dateFmt, Locale.CHINESE).format(Date())
  }

  fun nowLong(): Long {
    return System.currentTimeMillis()
  }

  /**
   * @param date    [java.util.Date]
   * @param dateFmt eg. "yyyy-MM-dd HH:mm:ss" , "yyyy年MM月dd日 HH时mm分ss秒"
   * @return strDate
   */
  fun dateToString(date: Date?, dateFmt: String?): String? {
    var dateStr: String? = null
    if (date != null || dateFmt != null) {
      dateStr = SimpleDateFormat(dateFmt!!, Locale.CHINESE).format(date)
    }
    return dateStr
  }


  /**
   * @param dateFmt eg. "yyyy-MM-dd HH:mm:ss" , "yyyy年MM月dd日 HH时mm分ss秒"
   * @return strDate
   */
  fun longToString(longDate: Long, dateFmt: String?): String? {
    var dateStr: String? = null
    if (dateFmt != null) {
      dateStr = SimpleDateFormat(dateFmt, Locale.CHINESE).format(longDate)
    }
    return dateStr
  }

  /**
   * strDate的时间格式必须要与dateFmt的时间格式相同
   *
   * @param strDate eg. "2014-10-21"
   * @param dateFmt eg. "yyyy-MM-dd HH:mm:ss" , "yyyy年MM月dd日 HH时mm分ss秒"
   */
  fun stringToDate(strDate: String, dateFmt: String): Date? {
    var date: Date? = null

    try {
      val formatter = SimpleDateFormat(dateFmt, Locale.CHINESE)
      date = formatter.parse(strDate)
    } catch (ignore: ParseException) {
      Log.e(TAG, "时间格式错误", ignore)
      //ignore
    }

    return date
  }

  fun longToDate(longDate: Long): Date {
    return Date(longDate)
  }

  /**
   * strDate的时间格式必须要与dateFmt的时间格式相同
   *
   * @param strDate eg. "2014-10-21"
   * @param dateFmt eg. "yyyy-MM-dd HH:mm:ss" , "yyyy年MM月dd日 HH时mm分ss秒"
   */
  fun longDate(strDate: String, dateFmt: String): Long {
    var longDate: Long = 0
    val date = stringToDate(strDate, dateFmt)
    if (date != null) {
      longDate = longDate(date)
    }
    return longDate
  }

  fun longDate(date: Date): Long {
    return date.time
  }

  fun friendly(strData: String, dataFmt: String): Array<String> {
    return friendly(longDate(strData, dataFmt))
  }

  fun friendly(date: Long): Array<String> {
    val strings = arrayOf<String>("", "")

    val delta = System.currentTimeMillis() - date


    val deltaDay = delta / 1000 / 60 / 60 / 24

    if (deltaDay == 0L) {
      strings[0] = "今天"
      strings[1] = longToString(date, "HH:mm") ?: "00:00"
    } else if (deltaDay == 1L) {
      strings[0] = "昨天"
      strings[1] = longToString(date, "HH:mm") ?: "00:00"
    } else {
      strings[0] = longToString(date, "EEEE") ?: "星期一"
      strings[1] = longToString(date, "MM-dd") ?: "01-01"
    }
    return strings
  }
}


package name.zeno.android.util

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
   * @param format eg. "yyyy-MM-dd HH:mm:ss" , "yyyy年MM月dd日 HH时mm分ss秒"
   * @return strDate
   */
  fun format(date: Long, format: String = "yyyy-MM-dd HH:mm:ss"): String = SimpleDateFormat(format, Locale.CHINESE).format(date)

  fun date(longDate: Long): Date = Date(longDate)

  /**
   * strDate的时间格式必须要与dateFmt的时间格式相同
   *
   * @param date eg. "2014-10-21"
   * @param format eg. "yyyy-MM-dd HH:mm:ss" , "yyyy年MM月dd日 HH时mm分ss秒"
   */
  fun date(date: String, format: String = "yyyy-MM-dd HH:mm:ss"): Date = SimpleDateFormat(format, Locale.CHINESE).parse(date)

  /**
   * strDate的时间格式必须要与dateFmt的时间格式相同
   *
   * @param strDate eg. "2014-10-21"
   * @param dateFmt eg. "yyyy-MM-dd HH:mm:ss" , "yyyy年MM月dd日 HH时mm分ss秒"
   */
  fun longDate(strDate: String, dateFmt: String): Long {
    val date = date(strDate, dateFmt)
    return longDate(date)
  }

  fun longDate(date: Date): Long {
    return date.time
  }

  fun friendly(strData: String, dataFmt: String): Array<String> {
    return friendly(longDate(strData, dataFmt))
  }

  fun friendly(date: Long): Array<String> {
    val strings = arrayOf("", "")

    val delta = System.currentTimeMillis() - date


    val deltaDay = delta / 1000 / 60 / 60 / 24

    if (deltaDay == 0L) {
      strings[0] = "今天"
      strings[1] = format(date, "HH:mm")
    } else if (deltaDay == 1L) {
      strings[0] = "昨天"
      strings[1] = format(date, "HH:mm")
    } else {
      strings[0] = format(date, "EEEE")
      strings[1] = format(date, "MM-dd")
    }
    return strings
  }
}


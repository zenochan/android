package name.zeno.android.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 时间转换工具类
 *
 * @author 陈治谋 (chenzhimou@tele-sing.com)
 * @version 2015-10-19 14:00:14
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class ZDate
{
  private static final String TAG     = "ZDate";
  /** one day in ms */
  public static final int ONE_DAY = 1000 * 60 * 60 * 24;

  private ZDate() {}

  public static Date now()
  {
    return new Date();
  }

  public static String nowString(String dateFmt)
  {
    return new SimpleDateFormat(dateFmt, Locale.CHINESE).format(new Date());
  }

  public static long nowLong()
  {
    return System.currentTimeMillis();
  }

  /**
   * @param date    {@link java.util.Date}
   * @param dateFmt eg. "yyyy-MM-dd HH:mm:ss" , "yyyy年MM月dd日 HH时mm分ss秒"
   * @return strDate
   */
  public static String dateToString(Date date, String dateFmt)
  {
    String dateStr = null;
    if (date != null || dateFmt != null) {
      dateStr = new SimpleDateFormat(dateFmt, Locale.CHINESE).format(date);
    }
    return dateStr;
  }


  /**
   * @param dateFmt eg. "yyyy-MM-dd HH:mm:ss" , "yyyy年MM月dd日 HH时mm分ss秒"
   * @return strDate
   */
  public static String longToString(long longDate, String dateFmt)
  {
    String dateStr = null;
    if (dateFmt != null) {
      dateStr = new SimpleDateFormat(dateFmt, Locale.CHINESE).format(longDate);
    }
    return dateStr;
  }

  /**
   * strDate的时间格式必须要与dateFmt的时间格式相同
   *
   * @param strDate eg. "2014-10-21"
   * @param dateFmt eg. "yyyy-MM-dd HH:mm:ss" , "yyyy年MM月dd日 HH时mm分ss秒"
   */
  public static Date stringToDate(String strDate, String dateFmt)
  {
    Date date = null;

    try {
      SimpleDateFormat formatter = new SimpleDateFormat(dateFmt, Locale.CHINESE);
      date = formatter.parse(strDate);
    } catch (ParseException ignore) {
      Log.e(TAG, "时间格式错误", ignore);
      //ignore
    }

    return date;
  }

  public static Date longToDate(long longDate)
  {
    return new Date(longDate);
  }

  /**
   * strDate的时间格式必须要与dateFmt的时间格式相同
   *
   * @param strDate eg. "2014-10-21"
   * @param dateFmt eg. "yyyy-MM-dd HH:mm:ss" , "yyyy年MM月dd日 HH时mm分ss秒"
   */
  public static long longDate(String strDate, String dateFmt)
  {
    long longDate = 0;
    Date date     = stringToDate(strDate, dateFmt);
    if (date != null) {
      longDate = longDate(date);
    }
    return longDate;
  }

  public static long longDate(Date date)
  {
    return date.getTime();
  }

  public static String[] friendly(String strData, String dataFmt)
  {
    return friendly(longDate(strData, dataFmt));
  }

  public static String[] friendly(long date)
  {
    String[] strings = new String[2];

    long delta = System.currentTimeMillis() - date;


    long deltaDay = delta / 1000 / 60 / 60 / 24;

    if (deltaDay == 0) {
      strings[0] = "今天";
      strings[1] = longToString(date, "HH:mm");
    } else if (deltaDay == 1) {
      strings[0] = "昨天";
      strings[1] = longToString(date, "HH:mm");
    } else {
      strings[0] = longToString(date, "EEEE");
      strings[1] = longToString(date, "MM-dd");
    }
    return strings;
  }
}


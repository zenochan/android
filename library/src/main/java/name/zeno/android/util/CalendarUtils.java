package name.zeno.android.util;

import java.util.Calendar;

/**
 * Create Date: 16/6/16
 *
 * @author 陈治谋 (513500085@qq.com)
 */
public class CalendarUtils
{
  public static int y()
  {
    return Calendar.getInstance().get(Calendar.YEAR);
  }

  public static int y(Calendar calendar)
  {
    return calendar.get(Calendar.YEAR);
  }

  public static int m(Calendar calendar)
  {
    return calendar.get(Calendar.MONTH);
  }

  public static int dayOfYear()
  {
    return Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
  }

  public static int d(Calendar calendar)
  {
    return calendar.get(Calendar.DAY_OF_MONTH);
  }

  /** 24-hour clock */
  public static int h()
  {
    return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
  }

  /** @return 0~6: 周日~周六 */
  public static int w(long timestamp)
  {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(timestamp);
    return calendar.get(Calendar.DAY_OF_WEEK) - 1;
  }

  public static boolean today(long timestamp)
  {
    return DateUtils.longToString(System.currentTimeMillis(), "yyyy-MM-dd").equals(DateUtils.longToString(timestamp, "yyyy-MM-dd"));
  }

  public static void addDay(Calendar calendar, int deltaDay)
  {
    calendar.add(Calendar.DAY_OF_MONTH, deltaDay);
  }

  public static boolean isToday(Calendar calendar)
  {
    return dayOfYear() == calendar.get(Calendar.DAY_OF_YEAR);
  }

  public static boolean isTomorrow(Calendar calendar)
  {
    return calendar.get(Calendar.DAY_OF_YEAR) - dayOfYear() == 1;
  }
}

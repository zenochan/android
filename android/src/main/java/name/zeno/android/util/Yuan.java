package name.zeno.android.util;

import android.annotation.SuppressLint;

import java.util.Locale;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/11/18.
 */
public class Yuan
{
  public static String y(double value)
  {
    return y("", value);
  }

  public static String y(String prefix, double value)
  {
    return prefix + String.format(Locale.CHINA,"%.02f", value);
  }

  /**
   * @return 元整数部分
   */
  public static String ys(double value)
  {
    return String.format(Locale.CHINA,"%.0f", value);
  }

  public static String prefix(String value)
  {
    if (value != null && !value.startsWith("￥")) {
      value = "￥" + value;
    }
    return value;
  }

  @SuppressLint("DefaultLocale") public static String big(double value)
  {
    String fmt = "%.02f";
    if (value > 10000) {
      value /= 10000D;
      fmt += " 万";
    }
    return String.format(fmt, value);
  }
}

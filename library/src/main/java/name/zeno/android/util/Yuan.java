package name.zeno.android.util;

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
    return prefix + String.format("%.02f", value);
  }

  /**
   * @return 元整数部分
   */
  public static String ys(double value)
  {
    return String.format("%.0f", value);
  }

  public static String prefix(String value)
  {
    if (value != null && !value.startsWith("￥")) {
      value = "￥" + value;
    }
    return value;
  }
}

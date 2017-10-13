package name.zeno.android.util;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public final class ZRex
{
  private static final String  TAG           = "ZRex";
  // 2015-10-19 14:20:07
  private static final Pattern PHONE_PATTERN = Pattern.compile("^1(3|4|5|7|8)[0-9]\\d{8}$");

  private ZRex() {}

  public static boolean validPhone(String phone)
  {
    return !(phone == null || phone.trim().isEmpty()) && PHONE_PATTERN.matcher(phone).matches();
  }

  /**
   * @param regex 正则 不带前后斜杠
   * @param val   需要验证的值
   */
  public static boolean pattern(@NonNull String regex, @NonNull String val)
  {
    return Pattern.compile(regex).matcher(val).matches();
  }

  public static List<String> find(@NonNull String rex, @NonNull String res)
  {
    List<String> stringList = new ArrayList<>();
    try {
      Matcher matcher = Pattern.compile(rex).matcher(res);
      while (matcher.find()) {
        stringList.add(matcher.group(0));
      }
    } catch (Exception e) {
      ZLog.e(TAG, "[ZRex.find]卧槽，出错了！什么吊系统--->" + e.getMessage(), e);
    }

    return stringList;
  }

  public static String findFirst(@NonNull String rex, @NonNull String res)
  {
    try {
      Matcher matcher = Pattern.compile(rex).matcher(res);
      if (matcher.find()) {
        return matcher.group(0);
      }
    } catch (Exception e) {
      ZLog.e(TAG, "[ZRex.findFirst]卧槽，出错了！什么吊系统--->" + e.getMessage(), e);
    }

    return null;
  }
}

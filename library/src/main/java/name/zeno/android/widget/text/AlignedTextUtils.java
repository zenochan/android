package name.zeno.android.widget.text;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

import name.zeno.android.util.ZString;

/**
 * Create Date: 16/7/18
 * <p>
 *
 * @author 陈治谋 (513500085@qq.com)
 * @see <a href='http://gold.xitu.io/entry/57758db0165abd0054737f0b'>TextView - 不等字数两端对齐</a>
 */
@SuppressWarnings("unused")
public class AlignedTextUtils
{
  /**
   * 对显示的字符串进行格式化 比如输入：出生年月 输出结果：出正生正年正月
   */
  public static String formatStr(String str)
  {
    return formatStr(str, 6);
  }

  public static String formatStr(String str, int destL)
  {
    if (ZString.isEmpty(str)) {
      return "";
    }
    int n = str.length();
    if (n >= destL) {
      return str;
    }


    float  m         = getMultiple(str, destL);
    int    count     = (int) Math.ceil(m);
    String insertStr = "";
    for (int i = 0; i < count; i++) {
      insertStr += "正";
    }

    StringBuilder sb = new StringBuilder(str);
    for (int i = n - 1; i > 0; i--) {
      sb.insert(i, insertStr);
    }
    return sb.toString();
  }

  public static SpannableString formatText(String str)
  {
    return formatText(str, 6);
  }

  /**
   * 对显示字符串进行格式化 比如输入：安正卓正机正器正人 输出结果：安 卓 机 器 人
   *
   * @param str   需要格式化的字符串
   * @param destL 目标字数
   */
  public static SpannableString formatText(final String str, int destL)
  {
    if (ZString.isEmpty(str)) {
      return null;
    }
    String formatStr = formatStr(str, destL);
    if (formatStr.length() <= 2) {
      return new SpannableString(formatStr);
    }


    float multiple = getMultiple(str, destL);
    int   count    = (int) Math.ceil(multiple);
    multiple /= count;

    SpannableString spannableString = new SpannableString(formatStr);
    for (int i = 1; i < formatStr.length(); i++) {
      if (i % (count + 1) != 0) {
        spannableString.setSpan(new RelativeSizeSpan(multiple), i, i + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), i, i + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
      }
    }
    return spannableString;
  }

  public static SpannableString justify(final String str)
  {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < str.length(); i++) {
      builder.append(str.charAt(i)).append(" ");
    }
    String          format = builder.toString();
    SpannableString span   = new SpannableString(format);
    for (int i = 1; i < format.length(); i += 2) {
      span.setSpan(new RelativeSizeSpan(0), i, i + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
      span.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), i, i + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    }

    return span;
  }

  private static float getMultiple(String str, int destL)
  {
    int strL = str.length();
    return (float) (destL - strL) / (strL - 1);
  }
}

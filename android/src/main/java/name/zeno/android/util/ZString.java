package name.zeno.android.util;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.widget.TextView;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Create Date: 16/6/13
 *
 * @author 陈治谋 (513500085@qq.com)
 */
public class ZString
{
  public static boolean isEmpty(@Nullable CharSequence str)
  {
    return str == null || str.length() == 0 || str.toString().trim().isEmpty();
  }

  public static boolean notEmpty(@Nullable CharSequence str)
  {
    return !isEmpty(str);
  }

  public static int length(String s)
  {
    return s == null ? 0 : s.length();
  }

  public static int trimLength(String s)
  {
    return s == null ? 0 : s.trim().length();
  }

  public static String encryptPhone(String phone)
  {
    if (phone == null)
      return "";

    if (phone.length() < 11)
      return phone;
    return String.format("%s****%s", phone.substring(0, 3), phone.substring(7));
  }

  /**
   * 解码 Unicode \\uXXXX
   */
  public static String decodeUnicode(String str)
  {
    Charset       set    = Charset.forName("UTF-16");
    Pattern       p      = Pattern.compile("\\\\u([0-9a-fA-F]{4})");
    Matcher       m      = p.matcher(str);
    int           start  = 0;
    int           start2 = 0;
    StringBuilder sb     = new StringBuilder();
    while (m.find(start)) {
      start2 = m.start();
      if (start2 > start) {
        String seg = str.substring(start, start2);
        sb.append(seg);
      }
      String code = m.group(1);
      int    i    = Integer.valueOf(code, 16);
      byte[] bb   = new byte[4];
      bb[0] = (byte) ((i >> 8) & 0xFF);
      bb[1] = (byte) (i & 0xFF);
      ByteBuffer b = ByteBuffer.wrap(bb);
      sb.append(String.valueOf(set.decode(b)).trim());
      start = m.end();
    }
    start2 = str.length();
    if (start2 > start) {
      String seg = str.substring(start, start2);
      sb.append(seg);
    }
    return sb.toString();
  }

  /** 半角转换为全角 */
  public static String toDBC(String input)
  {
    char[] c = input.toCharArray();
    for (int i = 0; i < c.length; i++) {
      if (c[i] == 12288) {
        c[i] = (char) 32;
        continue;
      }
      if (c[i] > 65280 && c[i] < 65375)
        c[i] = (char) (c[i] - 65248);
    }
    return new String(c);
  }

  /** 全角转半角 */
  public static String toSBC(String input)
  {
    char c[] = input.toCharArray();
    for (int i = 0; i < c.length; i++) {
      if (c[i] == ' ') {
        c[i] = '\u3000';
      } else if (c[i] < '\177') {
        c[i] = (char) (c[i] + 65248);
      }
    }
    return new String(c);
  }

  public static void addBadge(TextView textView)
  {
    String t     = textView.getText().toString();
    String badge = " ●";
    if (t.endsWith(badge)) return;

    Spannable spannable = new SpannableString(t + badge);
    int       p         = spannable.length() - 1;
    spannable.setSpan(new RelativeSizeSpan(0.7F), p, p + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    spannable.setSpan(new ForegroundColorSpan(Color.RED), p, p + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    textView.setText(spannable);
  }

  public static void removeBadge(TextView textView)
  {
    String t = textView.getText().toString();
    if (t.endsWith(" ●")) {
      t = t.substring(0, t.length() - 2);
      textView.setText(t);
    }
  }

  //设置开头文字大小
  public static void resizePreText(TextView textView, int length, float multi)
  {
    SpannableString ss = new SpannableString(textView.getText());
    ss.setSpan(new RelativeSizeSpan(multi), 0, length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    textView.setText(ss);
  }

  @NonNull
  public static String sub(String str, int start, int end)
  {
    if (start > end) {
      throw new IllegalArgumentException("start > end");
    }

    if (isEmpty(str)) {
      return "";
    }

    int length = str.length();
    length = length == 0 ? 1 : length;

    start = Math.min(start, length - 1);
    end = Math.min(end, length - 1);

    return str.substring(start, end);
  }
}

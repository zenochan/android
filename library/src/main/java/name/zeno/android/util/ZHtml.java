package name.zeno.android.util;

import android.text.Html;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/9/4
 */
public abstract class ZHtml
{
  public static CharSequence fromHtml(String source)
  {
    return Html.fromHtml(source);
  }
}

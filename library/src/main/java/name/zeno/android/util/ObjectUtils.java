package name.zeno.android.util;

/**
 * Create Date: 16/7/18
 *
 * @author 陈治谋 (513500085@qq.com)
 */
public class ObjectUtils
{
  public static <T> boolean equals(T a, T b)
  {
    return (a == null && b == null) || (a != null && b != null && a.equals(b));
  }
}

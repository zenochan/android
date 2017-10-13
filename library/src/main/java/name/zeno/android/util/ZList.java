package name.zeno.android.util;

import android.support.annotation.Nullable;

import java.util.List;

/**
 * 集合工具类
 * Create Date: 16/7/2
 *
 * @author 陈治谋 (513500085@qq.com)
 */
public class ZList
{
  //集合是否空的
  public static boolean isEmpty(List list)
  {
    return list == null || list.isEmpty();
  }

  //安全的获取数据
  @Nullable
  public static <T> T get(List<T> list, int index)
  {
    return list != null && index >= 0 && list.size() > index ? list.get(index) : null;
  }

  public static int size(List list)
  {
    return list != null ? list.size() : 0;
  }

  //第一条数据
  @Nullable
  public static <T> T first(List<T> list)
  {
    return isEmpty(list) ? null : list.get(0);
  }


  //最后一条数据
  @Nullable
  public static <T> T last(List<T> list)
  {
    return isEmpty(list) ? null : list.get(list.size() - 1);
  }

}

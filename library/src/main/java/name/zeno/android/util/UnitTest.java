package name.zeno.android.util;

import name.zeno.android.third.rxjava.RxUtils;

/**
 * 单元测试工具类
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 16/7/29
 */
public class UnitTest
{
  private static final String TAG = "UnitTest";

  public static boolean test = false;

  public static void test()
  {
    RxUtils.openUnitTest();
    test = true;
  }

  public static boolean isTest()
  {return UnitTest.test;}
}

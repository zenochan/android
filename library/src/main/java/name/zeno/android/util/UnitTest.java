package name.zeno.android.util;

import com.orhanobut.logger.Logger;

import lombok.Getter;
import name.zeno.android.third.rxjava.RxUtils;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 16/7/29
 */
public class UnitTest
{
  private static final String TAG = "UnitTest";

  @Getter public static boolean test = false;

  public static void test()
  {
    RxUtils.openUnitTest();
    Logger.init(TAG);

    test = true;
  }
}

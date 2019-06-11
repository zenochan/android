package cn.izeno.android.util

import cn.izeno.android.third.rxjava.RxUtils

/**
 * 单元测试工具类
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 16/7/29
 */
object UnitTest {
  var isTest = false

  fun test() {
    RxUtils.openUnitTest()
    isTest = true
  }
}

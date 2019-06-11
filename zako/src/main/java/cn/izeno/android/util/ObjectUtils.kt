package cn.izeno.android.util

/**
 * Create Date: 16/7/18
 *
 * @author 陈治谋 (513500085@qq.com)
 */
object ObjectUtils {
  fun <T> equals(a: T?, b: T?): Boolean {
    return a == null && b == null || a != null && b != null && a == b
  }
}

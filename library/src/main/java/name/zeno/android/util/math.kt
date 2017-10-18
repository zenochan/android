/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/10/12
 */

package name.zeno.android.util

fun min(vararg values: Int): Int {
  var min = values[0]
  values.forEach { min = Math.min(min, it) }
  return min
}

fun max(vararg values: Int): Int {
  var max = values[0]
  values.forEach { max = Math.max(max, it) }
  return max
}

fun max(vararg values: Float): Float {
  var max = values[0]
  values.forEach { max = Math.max(max, it) }
  return max
}

fun max(vararg values: Long): Long {
  var max = values[0]
  values.forEach { max = Math.max(max, it) }
  return max
}

/**
 * @param decimal 小数保留位数
 * @param pre 前缀， 如 __$__ ,__￥__
 */
fun Double.fixed(decimal: Int, pre: String = ""): String {
  return String.format("$pre%.${max(decimal, 0)}f", this)
}

/**
 * @param decimal 小数保留位数
 * @param pre 前缀， 如 __$__ ,__￥__
 */
fun Float.fixed(decimal: Int, pre: String = ""): String {
  return String.format("$pre%.${max(decimal, 0)}f", this)
}

fun Float.min(other: Float) = Math.min(this, other)
fun Float.max(other: Float) = Math.max(this, other)

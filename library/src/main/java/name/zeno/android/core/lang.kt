/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/10/18
 */

package name.zeno.android.core

/*
* # 扩展 int 布尔判断
* - false : null | 0
* - true: others
*/
val Int?.boolean
  get() = this != null && this != 0


val Int?.notZero
  get() = this != null && this != 0

val Int?.zero
  get() = this == 0

/**
 * # 扩展 int 符号判断
 * - `-`: null | 0
 * - `+`: others
 */
val Int?.sign
  get() = if (this != null && this != 0) "+" else "-"

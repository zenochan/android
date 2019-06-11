package cn.izeno.android.kt

/**
 * ColorInt 辅助
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/12/19
 */

val Int.red
  get() = this and 0xFF0000 shr 16

val Int.green
  get() = this and 0x00FF00 shr 8

val Int.blue
  get() = this and 0x0000FF

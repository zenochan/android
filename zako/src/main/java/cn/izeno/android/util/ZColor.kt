package cn.izeno.android.util

import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import java.util.*

/**
 * Create Date: 16/5/30
 *
 * @author 陈治谋 (513500085@qq.com)
 */
object ZColor {
  @ColorInt
  @JvmStatic
  fun randomColor(): Int {
    val color: Int
    val r: Int
    val g: Int
    val b: Int
    val random = Random()
    r = random.nextInt(255)
    g = random.nextInt(255)
    b = random.nextInt(255)

    color = -0x1000000 or (r shl 16 and 0xFF0000) or (g shl 8 and 0xFF00) or (b and 0xFF)
    return color
  }

  @ColorInt
  @JvmStatic
  fun randomAlphaColor(): Int {
    val color: Int
    val a: Int
    val r: Int
    val g: Int
    val b: Int
    val random = Random()
    a = random.nextInt(255)
    r = random.nextInt(255)
    g = random.nextInt(255)
    b = random.nextInt(255)

    color = a shl 24 and -0x1000000 or (r shl 16 and 0xFF0000) or (g shl 8 and 0xFF00) or (b and 0xFF)
    return color
  }


  @ColorInt
  @JvmStatic
  fun color(alpha: Int, red: Int, green: Int, blue: Int): Int {
    return alpha shl 24 and -0x1000000 or (red shl 16 and 0xFF0000) or (green shl 8 and 0xFF00) or (blue and 0xFF)
  }


  @ColorInt
  @JvmStatic
  fun colorAcceptor(@ColorInt start: Int, @ColorInt end: Int, @FloatRange(from = 0.0, to = 1.0) t: Float): Int {
    //修正范围为 [0,1]
    val t = t.max(0F).min(1F)

    val startA = start and -0x1000000 shr 24
    val startR = start and 0x00ff0000 shr 16
    val startG = start and 0x0000ff00 shr 8
    val startB = start and 0x000000ff

    val endA = end and -0x1000000 shr 24
    val endR = end and 0x00ff0000 shr 16
    val endG = end and 0x0000ff00 shr 8
    val endB = end and 0x000000ff

    val a = (startA + (endA - startA) * t).toInt()
    val r = (startR.toFloat() + (endR - startR) * t + 1f).min(255F).toInt()
    val g = (startG.toFloat() + (endG - startG) * t + 1f).min(255F).toInt()
    val b = (startB.toFloat() + (endB - startB) * t + 1f).min(255F).toInt()


    return a shl 24 and -0x1000000 or (r shl 16 and 0xFF0000) or (g shl 8 and 0xFF00) or (b and 0xFF)
  }
}

package name.zeno.android.uicore

import android.graphics.*
import android.support.annotation.ColorInt

/**
 * TODO 那么问题来了，怎样让 ColorFilter 作用于一个颜色呢
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/11/9
 */

object ColorFilterFactory {
  /**
   * @param mul 用来和目标颜色相乘
   * @param add 用来和目标颜色相加
   */
  fun light(@ColorInt mul: Int, @ColorInt add: Int) = LightingColorFilter(mul, add)

  fun porterDuff(@ColorInt color: Int, mode: PorterDuff.Mode) = PorterDuffColorFilter(color, mode)

  /**
   * # [ColorMatrix] 这个类，内部是一个 4x5 的矩阵
   *
   * ```
   * [ r1, r2, r3, r4, r5,
   *   g1, g2, g3, g4, g5,
   *   b1, b2, b3, b4, b5,
   *   a1, a2, a3, a4, a5 ]
   * ```
   *
   * > 通过计算， ColorMatrix 可以把要绘制的像素进行转换。对于颜色 [R, G, B, A] ，转换算法是这样的
   *
   * ```
   * [ r1, r2, r3, r4, r5,      [ r         [ r1*r + r2*g + r3*b + r4*a + r5*1        [ newR
   *   g1, g2, g3, g4, g5,        g           g1*r + g2*g + g3*b + g4*a + g5*1          newG
   *   b1, b2, b3, b4, b5,   X    b     =     b1*r + b2*g + b3*b + b4*a + b5*1     =    newB
   *   a1, a2, a3, a4, a5 ]       a           a1*r + a2*g + a3*b + a4*a + a5*1 ]        newA ]
   *                              1 ]
   * ```
   *
   * ColorMatrix 有一些自带的方法可以做简单的转换，例如可以使用 setSaturation(float sat) 来设置饱和度；另外你也可以自己去设置它的每一个元素来对转换效果做精细调整。
   */
  fun matrix(matrix: ColorMatrix) = ColorMatrixColorFilter(matrix)

  /**
   * @param src 4*5 矩阵
   *
   * ```
   * [ r1, r2, r3, r4, r5,
   *   g1, g2, g3, g4, g5,
   *   b1, b2, b3, b4, b5,
   *   a1, a2, a3, a4, a5 ]
   * ```
   * see [matrix]
   */
  fun matrix(vararg src: Float) = ColorMatrix(src)
}

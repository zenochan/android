package name.zeno.android.uicore

import android.graphics.*
import android.graphics.Shader.TileMode
import android.support.annotation.ColorInt

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/11/9
 */

@Suppress("unused")
object ShaderFactory {
  /**
   * # 线性渐变着色
   * - ([x0],[y0]) -> ([x1],[y1])
   * - [color0] -> [color1]
   *
   * @param tile 重复方式
   */
  fun linearGradient(
      x0: Int, y0: Int,
      x1: Int, y1: Int,
      color0: Int, color1: Int,
      tile: TileMode = TileMode.CLAMP
  ) = LinearGradient(x0.toFloat(), y0.toFloat(), x1.toFloat(), y1.toFloat(), color0, color1, tile)


  /**
   * # 线性渐变着色
   * - ([x0],[y0]) -> ([x1],[y1])
   * - [colors] (0) [positions] (0) -> [colors] (n) [positions] (n)
   *
   * @param colors 颜色序列
   * @param positions 颜色相对位置 [0,1]
   */
  fun linearGradient(
      x0: Int, y0: Int,
      x1: Int, y1: Int,
      colors: IntArray,
      positions: FloatArray,
      tile: TileMode = TileMode.CLAMP
  ) = LinearGradient(x0.toFloat(), y0.toFloat(), x1.toFloat(), y1.toFloat(), colors, positions, tile)


  /** [辐射渐变](https://ws3.sinaimg.cn/large/52eb2279ly1fig6f2jz23j20ck08yach.jpg) */
  fun radialGradient(
      cx: Int, cy: Int, radius: Int,
      colors: IntArray, stops: FloatArray,
      tileMode: TileMode = TileMode.CLAMP
  ) = RadialGradient(cx.toFloat(), cy.toFloat(), radius.toFloat(), colors, stops, tileMode)


  /** [辐射渐变](https://ws3.sinaimg.cn/large/52eb2279ly1fig6f2jz23j20ck08yach.jpg) */
  fun radialGradient(
      centerX: Int, centerY: Int, radius: Int,
      @ColorInt centerColor: Int, @ColorInt edgeColor: Int,
      tileMode: TileMode = TileMode.CLAMP
  ) = RadialGradient(centerX.toFloat(), centerY.toFloat(), radius.toFloat(), centerColor, edgeColor, tileMode)

  /** 扫描渐变 */
  fun sweepGradient(cx: Int, cy: Int, @ColorInt color0: Int, @ColorInt color1: Int)
      = SweepGradient(cx.toFloat(), cy.toFloat(), color0, color1)

  /** 扫描渐变 */
  fun sweepGradient(cx: Int, cy: Int, colors: IntArray, positions: FloatArray)
      = SweepGradient(cx.toFloat(), cy.toFloat(), colors, positions)

  /** 位图着色 */
  fun bitmapShader(bitmap: Bitmap, tileX: TileMode = TileMode.CLAMP, tileY: TileMode = TileMode.CLAMP)
      = BitmapShader(bitmap, tileX, tileY)


  /** shader 类型相同时需要关闭硬件加速, 否则木有效果 */
  fun compose(shaderA: Shader, shaderB: Shader, mode: Xfermode) = ComposeShader(shaderA, shaderB, mode)

  /** shader 类型相同时需要关闭硬件加速, 否则木有效果 */
  fun compose(shaderA: Shader, shaderB: Shader, mode: PorterDuff.Mode) = ComposeShader(shaderA, shaderB, mode)
}

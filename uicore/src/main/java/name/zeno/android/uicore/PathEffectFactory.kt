package name.zeno.android.uicore

import android.graphics.*

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/11/9
 */
object PathEffectFactory {
  /**
   *  # 圆角拐弯
   *
   *  @param radius 圆角半径
   */
  fun corner(radius: Float) = CornerPathEffect(radius)

  /**
   * # 把绘制改为使用定长的线段来拼接，并且在拼接的时候对路径进行随机偏离
   *
   * @param segmentLength 用来拼接的每个线段的长度
   * @param deviation 偏移量
   */
  fun discrete(segmentLength: Float, deviation: Float) = DiscretePathEffect(segmentLength, deviation)

  /**
   * # 使用虚线绘制线条
   *
   * @param intervals 指定了虚线的格式：数组中元素必须为偶数（最少是 2 个），按照「画线长度、空白长度、画线长度、空白长度」……的顺序排列
   * @param phase 偏移量
   */
  fun dash(intervals: FloatArray, phase: Float) = DashPathEffect(intervals, phase)

  /**
   * @param shape 形状
   * @param advance 间隔，按照两个形状起点算
   * @param phase 偏移量
   * @param style 形状拐弯处理
   *   - [PathDashPathEffect.Style.TRANSLATE] 位移
   *   - [PathDashPathEffect.Style.ROTATE] 旋转
   *   - [PathDashPathEffect.Style.MORPH] 变形，再选转的基础上对拐弯处优化
   */
  fun pathDash(shape: Path, advance: Float, phase: Float, style: PathDashPathEffect.Style)
      = PathDashPathEffect(shape, advance, phase, style)

  /**
   * # 两种效果简单叠加作用到 Path
   */
  fun sum(first: PathEffect, second: PathEffect) = SumPathEffect(first, second)

  /**
   * # 先对目标 Path 使用一个 PathEffect，然后再对这个改变后的 Path 使用另一个 PathEffect。
   */
  fun compose(first: PathEffect, second: PathEffect) = ComposePathEffect(first, second)
}
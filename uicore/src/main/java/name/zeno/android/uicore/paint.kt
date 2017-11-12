package name.zeno.android.uicore

import android.graphics.*
import android.graphics.Paint.Cap
import android.graphics.Paint.Join

/**
 * ## Android API
 *
 * - [Paint.setShadowLayer] 文字之外的绘制必须关闭硬件加速才能正常绘制阴影
 * - [Paint.setMaskFilter] 遮罩滤镜，SDK 中提供了两种样式 [BlurMaskFilter] 模糊, [EmbossMaskFilter] 浮雕
 *
 *
 * > 文字显示效果
 * - [Paint.setTextSize]
 * - [Paint.setTypeface] 字体
 * - [Paint.setFakeBoldText] 伪粗体
 * - [Paint.setStrikeThruText] 删除线
 * - [Paint.setUnderlineText] 下划线
 * - [Paint.setTextSkewX] 斜体
 * - [Paint.setTextScaleX] 横向缩放
 * - [Paint.setLetterSpacing] 字间距
 * - [Paint.setFontFeatureSettings] css font-feature-settings 属性
 *
 * > 文字尺寸测量
 *
 * - [Paint.getFontSpacing] 获取推荐的行距, 两 baseline 的距离
 * - [Paint.getFontMetrics] 提供 top,bottom, ascent, decent
 * - [Paint.getTextBounds] 文字显示局域, **不包括文字透明部分**
 * - [Paint.getTextWidths] 文字绘制的宽度
 * - [Paint.measureText] 测试文字绘制宽度
 * - [Paint.getTextWidths] 测试每个字符的绘制宽度
 * - [Paint.breakText] 给定最大宽度，测量文本能绘制的数量
 * - [Paint.getRunAdvance] 光标相关
 * - [Paint.getOffsetForAdvance] 光标相关
 *
 * ## Extensions
 *
 * - [shader] 着色器
 * - [colorFilter] 颜色滤镜
 * - [stroke] 线条形状
 * - [pathEffect] 线条样式
 * - [enableDitherAndFilter] 色彩优化
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/11/9
 */
fun Paint.help() {}

/** 颜色 >> 着色器 */
inline fun Paint.shader(factory: ShaderFactory.() -> Shader?) {
  this.shader = factory(ShaderFactory)
}

/** 颜色 >> 滤镜 */
fun Paint.colorFilter(factory: ColorFilterFactory.() -> ColorFilter?) {
  colorFilter = factory(ColorFilterFactory)
}

/**
 * # 效果 >> 线条形状
 * @param width 宽度,设为0时，绘制 1 像素，且宽度不受几何变化的影响
 * @param cap 线头样式 [Cap.BUTT] (默认)平头， [Cap.SQUARE] 方头, [Cap.ROUND] 圆头
 * @param join 拐弯样式 [Join.MITER] 尖， [Join.BEVEL] 平， [Join.ROUND] 圆
 * @param miter 对 [join] = [Join.MITER] 的补充， 超过这个比例箭头会转为平头
 */
fun Paint.stroke(
    width: Float,
    cap: Cap = Cap.BUTT,
    join: Join = Join.MITER,
    miter: Float = 4F
) {
  strokeWidth = width
  strokeCap = cap
  strokeJoin = join
  strokeMiter = miter
}


/**
 * # 效果 >> 线条效果
 */
inline fun Paint.pathEffect(factory: PathEffectFactory.() -> PathEffect?) {
  pathEffect = factory(PathEffectFactory)
}

/**
 * # 效果 >> 色彩优化
 * 开启抖动和双线性过滤
 */
fun Paint.enableDitherAndFilter() {
  isDither = true
  isFilterBitmap = true
}

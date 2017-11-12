package name.zeno.android.uicore

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build

/**
 * ## Android API
 *
 * ## Extensions
 * - [offscreenBuffer] 离屏缓存, [Paint.setXfermode] 辅助方法
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/11/9
 */
fun Canvas.help() {}


/**
 * # 离屏缓存
 * - 要想使用 setXfermode() 正常绘制，必须使用离屏缓存 (Off-screen Buffer) 把内容绘制在额外的层上，再把绘制好的内容贴回 View 中。
 * - 使用 Xfermode 来绘制的内容，除了注意使用离屏缓冲，还应该注意控制它的透明区域不要太小，要让它足够覆盖到要和它结合绘制的内容，否则得到的结果很可能不是你想要的。
 *
 * @param bounds 混存的范围
 * @param paint 画笔
 */
inline fun Canvas.offscreenBuffer(
    bounds: RectF? = null,
    paint: Paint? = null,
    drawer: () -> Unit) {
  val saved = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
    saveLayer(bounds, paint)
  } else {
    @Suppress("DEPRECATION")
    saveLayer(bounds, paint, Canvas.ALL_SAVE_FLAG)
  }
  drawer()
  restoreToCount(saved)
}

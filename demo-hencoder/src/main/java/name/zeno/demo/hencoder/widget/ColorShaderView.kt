package name.zeno.demo.hencoder.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/11/11
 */
class ColorShaderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : View(context, attrs, defStyle) {

  val paint: Paint = Paint()
  val rect = RectF()

  init {
    paint.color = Color.LTGRAY
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    rect.set(0F, 0F, width.toFloat(), height.toFloat())
    canvas.drawRect(rect, paint)
  }
}
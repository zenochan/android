package name.zeno.android.camera

import android.content.Context
import android.util.AttributeSet
import android.view.SurfaceView
import name.zeno.android.util.MeasureUtils

/**
 * 重写 onMeasure 方法，使高宽比为 4:3
 * Create Date: 16/6/19
 *
 * @author 陈治谋 (513500085@qq.com)
 */
class IdCardSurfaceView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : SurfaceView(context, attrs, defStyleAttr) {

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val size = MeasureUtils.as4Rate3(widthMeasureSpec, heightMeasureSpec)
    setMeasuredDimension(size.x, size.y)
  }
}

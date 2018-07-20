package name.zeno.android.widget


import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.RelativeLayout

class ClipImageLayout(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {

  private val clipView: ClipImageView = ClipImageView(context)
  private val borderView: ClipBorderView = ClipBorderView(context)

  private var paddingHorizontal = 60

  init {
    val lp = RelativeLayout.LayoutParams(-1, -1)
    this.addView(clipView, lp)
    this.addView(borderView, lp)

    // 计算padding的px
    paddingHorizontal = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, paddingHorizontal.toFloat(), resources.displayMetrics).toInt()
    clipView.setHorizontalPadding(paddingHorizontal)
    borderView.setHorizontalPadding(paddingHorizontal)
  }

  /**
   * 对外公布设置边距的方法,单位为dp
   */
  fun setHorizontalPadding(mHorizontalPadding: Int) {
    this.paddingHorizontal = mHorizontalPadding
  }

  /**
   * 裁切图片
   */
  fun clip(): Bitmap {
    return clipView.clip()
  }

  fun setBitmap(bitmap: Bitmap) {
    clipView.setImageBitmap(bitmap)
  }

}

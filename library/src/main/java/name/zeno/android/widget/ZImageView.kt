package name.zeno.android.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import name.zeno.android.tint.TintHelper
import name.zeno.android.tint.TintInfo
import name.zeno.android.tint.TintableDrawableView
import name.zeno.android.util.R

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 16/6/19
 */
open class ZImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatImageView(context, attrs, defStyleAttr), TintableDrawableView {
  private val drawableTintInfo: TintInfo?

  private var rate = 0f

  override var supportDrawableTintList: ColorStateList?
    get() = null
    set(tint) = TintHelper.setSupportDrawableTintList(this, drawableTintInfo!!, tint!!)

  override//do nothing
  var supportDrawableTintMode: PorterDuff.Mode?
    get() = TintHelper.getSupportTintMode(drawableTintInfo)
    set(tintMode) {}

  init {

    TintHelper.loadFromAttributes(this, attrs!!, defStyleAttr,
        R.styleable.ZImageView,
        R.styleable.ZImageView_backgroundTint,
        R.styleable.ZImageView_backgroundTintMode
    )

    drawableTintInfo = TintInfo()
    drawableTintInfo.hasTintList = true

    val ta = context.obtainStyledAttributes(attrs, R.styleable.ZImageView, defStyleAttr, 0)
    if (ta.hasValue(R.styleable.ZImageView_drawableTint)) {
      val c = ta.getColorStateList(R.styleable.ZImageView_drawableTint)
      drawableTintInfo.tintList = c
      TintHelper.setSupportDrawableTintList(this, drawableTintInfo, c!!)
    }
    if (ta.hasValue(R.styleable.ZImageView_rate)) {
      rate = ta.getFloat(R.styleable.ZImageView_rate, 0f)
      rate = if (rate > 0) rate else 0F
    }
    ta.recycle()
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    //按比例显示 ImageView 高度
    if (rate != 0f) {
      var height = (measuredWidth * rate).toInt()
      height = if (height < 0) -1 else height
      layoutParams.height = height
    }
  }

  override fun setImageDrawable(drawable: Drawable?) {
    super.setImageDrawable(drawable)
    if (drawableTintInfo != null) {
      TintHelper.setSupportDrawableTintList(this, drawableTintInfo, drawableTintInfo.tintList)
    }
  }

  fun setRate(rate: Float) {
    this.rate = rate
  }
}

package cn.izeno.android.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

import cn.izeno.android.util.R

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/03/30
 */
class RateLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
  private var rate = 0f

  init {
    val ta = context.obtainStyledAttributes(attrs, R.styleable.RateLayout, defStyleAttr, 0)
    if (ta.hasValue(R.styleable.RateLayout_rate)) {
      rate = ta.getFloat(R.styleable.RateLayout_rate, 0f)
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

  fun setRate(rate: Float) {
    this.rate = rate
  }
}

package name.zeno.android.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.IntRange
import androidx.appcompat.widget.AppCompatTextView
import name.zeno.android.util.R

/**
 * Create Date: 16/7/1
 *
 * @author 陈治谋 (513500085@qq.com)
 */
class BadgeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {
  internal var v: View
  internal var labelTv: ZTextView
  internal var badgeTv: AppCompatTextView

  init {
    v = View.inflate(context, R.layout.view_badge, this)
    labelTv = v.findViewById(R.id.tv_label)
    badgeTv = findViewById(R.id.tv_badge)

    initAttr(context, attrs)
  }

  fun setBadge(@IntRange(from = 0) count: Int) {
    if (count < 99) {
      badgeTv.text = count.toString()
    } else {
      badgeTv.text = "99+"
    }
    badgeTv.visibility = if (count > 0) View.VISIBLE else View.GONE
  }

  private fun initAttr(context: Context, attributeSet: AttributeSet?) {

    val ta = context.obtainStyledAttributes(attributeSet, R.styleable.BadgeView)
    if (ta.hasValue(R.styleable.BadgeView_drawableTop)) {
      val drawable = ta.getDrawable(R.styleable.BadgeView_drawableTop)!!
      drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
      labelTv.setCompoundDrawables(null, drawable, null, null)
    }

    if (ta.hasValue(R.styleable.BadgeView_compoundDrawableTint)) {
      val tint = ta.getColorStateList(R.styleable.BadgeView_compoundDrawableTint)
      labelTv.supportCompoundDrawableTintList = tint
    }

    if (ta.hasValue(R.styleable.BadgeView_textLabel)) {
      val s = ta.getString(R.styleable.BadgeView_textLabel)
      labelTv.text = s
    }

    ta.recycle()
  }
}

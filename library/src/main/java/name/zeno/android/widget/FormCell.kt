package name.zeno.android.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.support.annotation.ColorInt
import android.support.annotation.Dimension
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

import kotlinx.android.synthetic.main.view_form_cell.view.*
import name.zeno.android.presenter.exts.show
import name.zeno.android.util.R
import org.jetbrains.anko.sdk25.coroutines.onClick


/**
 * 封装 label ，content， next箭头
 * Create Date: 16/6/16
 *
 * @author 陈治谋 (513500085@qq.com)
 */
class FormCell @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0)
  : LinearLayout(context, attrs, defStyleAttr) {

  private val root: View
  val labelTv: TextView
  val contentTv: TextView

  init {
    root = LayoutInflater.from(context).inflate(R.layout.view_form_cell, this)
    initAttr(context, attrs)
    labelTv = tv_label
    contentTv = tv_content
  }

  @ColorInt private var colorLabelText = 0


  var text: String?
    get() = tv_content.text.toString()
    set(content) {
      root.tv_content.text = content
    }

  val value: String
    get() = text!!


  var textRes: Int
    @StringRes get() = throw IllegalAccessException("not implement")
    set(@StringRes value) {
      tv_content.setText(value)
    }


  var textLabel: String?
    get() = tv_label.text.toString()
    set(label) {
      tv_label.text = label
    }

  var textLabelRes: Int
    @StringRes get() = throw IllegalAccessException("not implement")
    set(@StringRes value) {
      tv_label.setText(value)
    }


  fun setTextSizeLabel(size: Float, unit: Int = Dimension.SP) {
    tv_label.setTextSize(unit, size)
  }

  fun setHint(hint: String) {
    tv_content.hint = hint
  }

  fun setNextIvRes(@DrawableRes resId: Int) {
    iv_next.setImageResource(resId)
  }

  var nextEnable: Boolean
    get() = iv_next.show
    set(value) {
      iv_next.show = value
    }

  fun setOnClickNextListener(onClick: (() -> Unit)?) {
    iv_next.onClick { onClick?.invoke() }
  }

  private fun initAttr(context: Context, attrs: AttributeSet?) {
    setBackgroundResource(R.drawable.z_bg_white_selector)
    val ta = context.obtainStyledAttributes(attrs, R.styleable.FormCell)
    if (ta.hasValue(R.styleable.FormCell_labelDrawableLeft)) {
      val drawable = ta.getDrawable(R.styleable.FormCell_labelDrawableLeft)!!
      drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
      tv_label.setCompoundDrawables(drawable, null, null, null)
    }

    if (ta.hasValue(R.styleable.FormCell_labelDrawablePadding)) {
      val padding = ta.getDimensionPixelSize(R.styleable.FormCell_labelDrawablePadding, 0)
      tv_label.compoundDrawablePadding = padding
    }

    if (ta.hasValue(R.styleable.FormCell_labelDrawableTint)) {
      val c = ta.getColorStateList(R.styleable.FormCell_labelDrawableTint)
      tv_label.supportCompoundDrawableTintList = c
    }

    if (ta.hasValue(R.styleable.FormCell_labelText)) {
      tv_label.text = ta.getString(R.styleable.FormCell_labelText)
    }
    if (ta.hasValue(R.styleable.FormCell_contentText)) {
      tv_content.text = ta.getString(R.styleable.FormCell_contentText)
    }
    if (ta.hasValue(R.styleable.FormCell_contentTextColor)) {
      tv_content.setTextColor(ta.getColorStateList(R.styleable.FormCell_contentTextColor))
    }
    if (ta.hasValue(R.styleable.FormCell_contentHint)) {
      tv_content.hint = ta.getString(R.styleable.FormCell_contentHint)
    }

    if (ta.hasValue(R.styleable.FormCell_srcNext)) {
      iv_next.setImageDrawable(ta.getDrawable(R.styleable.FormCell_srcNext))
    }
    if (ta.hasValue(R.styleable.FormCell_nextTint)) {
      iv_next.supportDrawableTintList = ta.getColorStateList(R.styleable.FormCell_nextTint)
    } else {
      iv_next.supportDrawableTintList = ColorStateList(arrayOf(intArrayOf()), intArrayOf(Color.parseColor("#9e9e9e")))
    }

    if (ta.hasValue(R.styleable.FormCell_textColorLabel)) {
      this.colorLabelText = ta.getColor(R.styleable.FormCell_textColorLabel, this.colorLabelText)
      tv_label.setTextColor(this.colorLabelText)
    }

    if (ta.hasValue(R.styleable.FormCell_textSizeLabel)) {
      val dimen = ta.getDimensionPixelSize(R.styleable.FormCell_textSizeLabel, 0)
      tv_label.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimen.toFloat())
    }

    if (ta.hasValue(R.styleable.FormCell_textSizeContent)) {
      val dimen = ta.getDimensionPixelSize(R.styleable.FormCell_textSizeContent, 0)
      tv_content.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimen.toFloat())
    }

    iv_next.visibility = if (ta.getBoolean(R.styleable.FormCell_nextEnable, true)) View.VISIBLE else View.INVISIBLE
    ta.recycle()
  }
}

package name.zeno.android.widget

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.support.annotation.ColorInt
import android.support.annotation.StringRes
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import name.zeno.android.listener.Action0
import name.zeno.android.util.R

@Suppress("unused")
/**
 * Create Date: 16/6/15
 *
 * @author 陈治谋 (513500085@qq.com)
 */
class SimpleActionbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null)
  : ZAppBarLayout(context, attrs), View.OnClickListener {
  private var onClickPre: (() -> Unit)? = null
  private var onClickAction: (() -> Unit)? = null

  private val preTv: ZTextView
  private val titleTv: TextView
  private val actionTv: ZTextView
  val appbar: View

  init {
    appbar = LayoutInflater.from(context).inflate(R.layout.view_simple_actionbar, this)
    preTv = appbar.findViewById(R.id.tv_pre)
    titleTv = appbar.findViewById(R.id.tv_title)
    actionTv = appbar.findViewById(R.id.tv_action)
    preTv.setOnClickListener(this)
    actionTv.setOnClickListener(this)

    initAttrs(context, attrs)
    if (context is Activity) {
      onClickPre = { context.onBackPressed() }
    }
  }

  fun setTitleText(text: String?): SimpleActionbar {
    titleTv.text = text
    return this
  }

  fun setActionText(text: String?): SimpleActionbar {
    actionTv.text = text
    return this
  }

  fun setActionText(@StringRes resId: Int): SimpleActionbar {
    actionTv.setText(resId)
    return this
  }

  fun setPreEnable(enable: Boolean) {
    preTv.visibility = if (enable) View.VISIBLE else View.GONE
  }

  fun setActionEnable(enable: Boolean) {
    actionTv.visibility = if (enable) View.VISIBLE else View.GONE
  }

  fun setActionTextColor(@ColorInt color: Int) = apply {
    actionTv.setTextColor(color)
  }

  fun setActionTextVisibility(visibility: Int) = apply {
    actionTv.visibility = visibility
  }

  override fun onClick(view: View) {
    val i = view.id
    if (i == R.id.tv_pre) {
      onClickPre?.invoke()
    } else if (i == R.id.tv_action) {
      onClickAction?.invoke()
    }
  }

  private fun initAttrs(context: Context?, attrs: AttributeSet?) {
    if (context == null || attrs == null) {
      return
    }

    val ta = context.obtainStyledAttributes(attrs, R.styleable.SimpleActionbar)


    val whiteBar = ta.getBoolean(R.styleable.SimpleActionbar_whiteBar, false)
    val a = ta.hasValue(R.styleable.ZAppBarLayout_backgroundStatusBar)
    if (whiteBar) {
      setBackgroundColor(Color.WHITE)
      val v = statusBarView
      if (!a && v != null) v.setBackgroundColor(Color.WHITE)
    }

    if (ta.hasValue(R.styleable.SimpleActionbar_preTextBackgroundTint)) {
      val tint = ta.getColorStateList(R.styleable.SimpleActionbar_preTextBackgroundTint)
      preTv.supportCompoundDrawableTintList = tint
      preTv.visibility = View.VISIBLE
    }

    if (ta.hasValue(R.styleable.SimpleActionbar_preText)) {
      preTv.text = ta.getString(R.styleable.SimpleActionbar_preText)
      preTv.visibility = View.VISIBLE
    }
    if (ta.hasValue(R.styleable.SimpleActionbar_preEnable)) {
      val enable = ta.getBoolean(R.styleable.SimpleActionbar_preEnable, false)
      preTv.visibility = if (enable) View.VISIBLE else View.GONE
    }

    if (ta.hasValue(R.styleable.SimpleActionbar_titleText)) {
      titleTv.text = ta.getString(R.styleable.SimpleActionbar_titleText)
    } else if (isInEditMode) {
      titleTv.text = "app:titleText"
    }

    if (ta.hasValue(R.styleable.SimpleActionbar_textColorTitle)) {
      titleTv.setTextColor(ta.getColorStateList(R.styleable.SimpleActionbar_textColorTitle))
    }

    if (ta.hasValue(R.styleable.SimpleActionbar_actionText)) {
      actionTv.text = ta.getString(R.styleable.SimpleActionbar_actionText)
      actionTv.visibility = View.VISIBLE
    }

    if (ta.hasValue(R.styleable.SimpleActionbar_textColorAction)) {
      actionTv.setTextColor(ta.getColorStateList(R.styleable.SimpleActionbar_textColorAction))
    }

    if (ta.hasValue(R.styleable.SimpleActionbar_actionDrawable)) {
      val drawable = ta.getDrawable(R.styleable.SimpleActionbar_actionDrawable)!!
      drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
      actionTv.setCompoundDrawables(null, null, drawable, null)
    }

    if (ta.hasValue(R.styleable.SimpleActionbar_actionDrawableTint)) {
      val tint = ta.getColorStateList(R.styleable.SimpleActionbar_actionDrawableTint)
      actionTv.supportCompoundDrawableTintList = tint
    }

    if (ta.hasValue(R.styleable.SimpleActionbar_actionEnable)) {
      val enable = ta.getBoolean(R.styleable.SimpleActionbar_actionEnable, false)
      actionTv.visibility = if (enable) View.VISIBLE else View.GONE
    }

    ta.recycle()
  }

  fun setTitleColor(@ColorInt color: Int) {
    titleTv.setTextColor(color)
  }

  fun setPreTint(@ColorInt color: Int) {
    val tint = ColorStateList(arrayOf(intArrayOf()), intArrayOf(color))
    preTv.supportCompoundDrawableTintList = tint
  }

  @Deprecated(message = "20171019", replaceWith = ReplaceWith("a"))
  fun setOnClickPre(onClickPre: Action0) = onPre { onClickPre.call() }

  @Deprecated(message = "20171019", replaceWith = ReplaceWith("onAction"))
  fun setOnClickAction(onClickAction: Action0) = onAction { onClickAction.call() }

  fun onAction(action: () -> Unit) {
    this.onClickAction = action
  }

  fun onPre(action: () -> Unit) {
    this.onClickPre = action
  }
}

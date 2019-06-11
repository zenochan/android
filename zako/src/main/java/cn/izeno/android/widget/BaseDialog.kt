package cn.izeno.android.widget

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import cn.izeno.android.common.annotations.DataClass

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/1/10.
 */
@DataClass
open class BaseDialog : Dialog {
  constructor(context: Context) : super(context)
  constructor(context: Context, themeResId: Int) : super(context, themeResId)
  constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener) : super(context, cancelable, cancelListener)

  fun transparentBackground() = window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
  fun setLayout(w: Int, h: Int) = window?.setLayout(w, h)
}

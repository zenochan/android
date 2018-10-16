package name.zeno.android.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import name.zeno.android.tint.TintHelper
import name.zeno.android.util.R

/**
 * Create Date: 16/7/8
 *
 * @author 陈治谋 (513500085@qq.com)
 */
class ZButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {

  init {
    TintHelper.loadFromAttributes(this, attrs!!, defStyleAttr, R.styleable.ZButton,
        R.styleable.ZButton_backgroundTint,
        R.styleable.ZButton_backgroundTintMode
    )
  }
}

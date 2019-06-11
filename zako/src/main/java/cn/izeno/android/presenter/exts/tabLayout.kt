/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/10/18
 */

package cn.izeno.android.presenter.exts

import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout


/** 设置 tab layout 的分割线 */
fun TabLayout.setDivider(@DrawableRes res: Int) {
  val linearLayout = this.getChildAt(0) as LinearLayout
  linearLayout.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
  linearLayout.dividerDrawable = ContextCompat.getDrawable(this.context, res)
}

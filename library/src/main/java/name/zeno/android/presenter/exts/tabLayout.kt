/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/10/18
 */

package name.zeno.android.presenter.exts

import android.support.annotation.DrawableRes
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.widget.LinearLayout


/** 设置 tab layout 的分割线 */
fun TabLayout.setDivider(@DrawableRes res: Int) {
  val linearLayout = this.getChildAt(0) as LinearLayout
  linearLayout.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
  linearLayout.dividerDrawable = ContextCompat.getDrawable(this.context, res)
}

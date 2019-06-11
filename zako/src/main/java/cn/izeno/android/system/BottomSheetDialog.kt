/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/10/12
 */

package cn.izeno.android.system

import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import cn.izeno.android.util.R

/**
 * # 收起高度 (px)
 * - 只能在调用 [BottomSheetDialog.setContentView] 之后调用
 */
var BottomSheetDialog.peekHeight: Int
  get() {
    val bottomSheet = findViewById<FrameLayout>(R.id.design_bottom_sheet)
    return BottomSheetBehavior.from(bottomSheet).peekHeight
  }
  set(px) {
    val bottomSheet = findViewById<FrameLayout>(R.id.design_bottom_sheet)
    BottomSheetBehavior.from(bottomSheet).peekHeight = px
  }
package cn.izeno.android.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cn.izeno.android.util.ZLog

/**
 * SwipeRefreshLayout 支持自动刷新
 * Create Date: 16/6/28
 *
 * @author 陈治谋 (513500085@qq.com)
 */
class AutoSwipeRefreshLayout : SwipeRefreshLayout {

  var isFirstAutoRefreshed = false
    private set

  constructor(context: Context) : super(context) {}

  constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

  fun autoRefresh() {
    try {
      val mCircleView = SwipeRefreshLayout::class.java.getDeclaredField("mCircleView")
      mCircleView.isAccessible = true
      val progress = mCircleView.get(this) as View
      progress.visibility = View.VISIBLE

      if (isFirstAutoRefreshed) {
        val reset = SwipeRefreshLayout::class.java.getDeclaredMethod("reset")
        reset.isAccessible = true
        reset.invoke(this)
      } else {
        isFirstAutoRefreshed = true
      }

      val setRefreshing = SwipeRefreshLayout::class.java.getDeclaredMethod("setRefreshing", Boolean::class.javaPrimitiveType, Boolean::class.javaPrimitiveType)
      setRefreshing.isAccessible = true
      setRefreshing.invoke(this, true, true)
    } catch (e: Exception) {
      ZLog.e(TAG, "autoRefresh failed", e)
    }

  }

  companion object {
    private val TAG = "AutoSwipeRefreshLayout"
  }
}

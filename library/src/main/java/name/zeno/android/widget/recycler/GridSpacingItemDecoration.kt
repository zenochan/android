package name.zeno.android.widget.recycler

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * @author 陈治谋 (513500085@qq.com)
 *
 * - [Android Recyclerview GridLayoutManager column spacing](http://stackoverflow.com/questions/28531996/android-recyclerview-gridlayoutmanager-column-spacing)
 */
class GridSpacingItemDecoration(
    private val spacing: Int,
    private val includeEdge: Boolean = true
) : RecyclerView.ItemDecoration() {
  internal var manager: GridLayoutManager? = null
  private var spanCount: Int = 0

  internal var rectHook: ((outRect: Rect, span: Int) -> Unit)? = null


  override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
    if (manager == null) {
      manager = parent.layoutManager as GridLayoutManager
      spanCount = manager!!.spanCount
    }

    val position = parent.getChildAdapterPosition(view)
    val span = manager!!.spanSizeLookup.getSpanSize(position)
    val spanIndex = (view.layoutParams as GridLayoutManager.LayoutParams).spanIndex

    if (span == spanCount) {
      outRect.set(0, 0, 0, 0)
      return
    }

    val leftEdge = spanIndex == 0
    val rightEdge = spanIndex + span == spanCount


    val half = spacing / 2
    val edge = if (includeEdge) spacing else 0

    outRect.left = if (leftEdge) edge else half
    outRect.right = if (rightEdge) edge else half
    outRect.top = half
    outRect.bottom = half
    rectHook?.invoke(outRect, span)
  }

  fun setRectHook(rectHook: (outRect: Rect, span: Int) -> Unit) {
    this.rectHook = rectHook
  }
}

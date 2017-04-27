package name.zeno.android.widget.recycler;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import lombok.Setter;
import name.zeno.android.listener.Action2;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @see <a href="http://stackoverflow.com/questions/28531996/android-recyclerview-gridlayoutmanager-column-spacing">
 * Android Recyclerview GridLayoutManager column spacing
 * </a>
 */
public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration
{
  GridLayoutManager manager;
  private int spanCount;
  private int spacing;
  private boolean includeEdge = true;

  @Setter
  Action2<Rect, Integer> rectHook;

  public GridSpacingItemDecoration(int spacing, boolean includeEdge)
  {
    this.spacing = spacing;
    this.includeEdge = includeEdge;
  }

  @Override
  public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
  {
    if (manager == null) {
      manager = (GridLayoutManager) parent.getLayoutManager();
      spanCount = manager.getSpanCount();
    }

    int position  = parent.getChildAdapterPosition(view);
    int span      = manager.getSpanSizeLookup().getSpanSize(position);
    int spanIndex = ((GridLayoutManager.LayoutParams) view.getLayoutParams()).getSpanIndex();

    if (span == spanCount) {
      outRect.set(0, 0, 0, 0);
      return;
    }

    boolean leftEdge  = spanIndex == 0;
    boolean rightEdge = spanIndex + span == spanCount;


    int half = spacing / 2;
    int edge = includeEdge ? spacing : 0;

    outRect.left = leftEdge ? edge : half;
    outRect.right = rightEdge ? edge : half;
    outRect.top = half;
    outRect.bottom = half;

    if (rectHook != null) {
      rectHook.call(outRect, span);
    }
  }
}

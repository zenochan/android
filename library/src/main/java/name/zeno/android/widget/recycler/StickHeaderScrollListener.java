package name.zeno.android.widget.recycler;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 16/8/15
 */
public class StickHeaderScrollListener extends RecyclerView.OnScrollListener
{
  private        TextView stickHeaderView;
  @IdRes private int      textViewId;


  public StickHeaderScrollListener(TextView stickHeaderView, @IdRes int textViewId)
  {
    this.stickHeaderView = stickHeaderView;
    this.textViewId = textViewId;
  }

  @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy)
  {
    View stickyInfoView = recyclerView.findChildViewUnder(stickHeaderView.getMeasuredWidth() / 2, 5);

    if (stickyInfoView != null) {
      TextView tv = (TextView) stickyInfoView.findViewById(textViewId);
      stickHeaderView.setText(tv.getText());
    }

    // Get the sticky view's translationY by the first view below the sticky's height.
    View transInfoView = recyclerView.findChildViewUnder(stickHeaderView.getMeasuredWidth() / 2, stickHeaderView.getMeasuredHeight() + 1);
    if (transInfoView != null && transInfoView.getTag() != null) {
      TextView tv = (TextView) transInfoView.findViewById(textViewId);
      if (tv.isShown()) {
        // If the first view below the sticky's height scroll off the screen,
        // then recovery the sticky view's translationY.
        if (transInfoView.getTop() > 0) {
          int dealtY = transInfoView.getTop() - stickHeaderView.getMeasuredHeight();
          stickHeaderView.setTranslationY(dealtY);
        } else {
          stickHeaderView.setTranslationY(0);
        }
      } else {
        stickHeaderView.setTranslationY(0);
      }
    }
  }
}

package name.zeno.android.widget.autoscrollviewpager;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import name.zeno.android.presenter.ZActivity;

public class AutoScrollPagerAdapter extends PagerAdapter
{
  private static final String TAG = "AutoScrollPagerAdapter";

  private boolean infinite = true;

  public boolean isInfinite()
  {
    return infinite;
  }

  public void setInfinite(boolean infinite)
  {
    this.infinite = infinite;
  }

  private PagerAdapter wrappedAdapter;

  public AutoScrollPagerAdapter(PagerAdapter wrapped)
  {
    wrappedAdapter = wrapped;
    wrappedAdapter.registerDataSetObserver(new DataSetObserver()
    {
      @Override public void onChanged()
      {
        notifyDataSetChanged();
      }
    });
  }

  @Override
  public int getCount()
  {
    if (wrappedAdapter == null) {
      return 0;
    } else if (infinite && wrappedAdapter.getCount() > 1) {
      return wrappedAdapter.getCount() + 2;
    } else {
      return wrappedAdapter.getCount();
    }
  }

  @Override
  public Object instantiateItem(ViewGroup container, int position)
  {
    Context context = container.getContext();

    boolean destroyed = context != null && context instanceof ZActivity && ((ZActivity) context).isDestroyed()
        || Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && context instanceof Activity && ((Activity) context).isDestroyed();
    if (destroyed) {
      //卧槽，Activity 都 destroy 了你还实例化，滚粗
      return null;
    }

    if (infinite && position == 0) {
      return wrappedAdapter.instantiateItem(container, wrappedAdapter.getCount() - 1);
    } else if (infinite && position == wrappedAdapter.getCount() + 1) {
      return wrappedAdapter.instantiateItem(container, 0);
    } else {
      return wrappedAdapter.instantiateItem(container, position - (infinite ? 1 : 0));
    }
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object)
  {
    wrappedAdapter.destroyItem(container, position, object);
  }

  @Override
  public boolean isViewFromObject(View view, Object o)
  {
    return wrappedAdapter.isViewFromObject(view, o);
  }

}

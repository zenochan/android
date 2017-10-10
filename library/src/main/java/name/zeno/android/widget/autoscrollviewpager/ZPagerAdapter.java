package name.zeno.android.widget.autoscrollviewpager;

import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Create Date: 16/7/13
 *
 * @author 陈治谋 (513500085@qq.com)
 */
public abstract class ZPagerAdapter<T> extends PagerAdapter
{
  List<T> data;

  public List<T> getData()
  {
    return data;
  }

  public void setData(List<T> data)
  {
    this.data = data;
  }

  public ZPagerAdapter()
  {
  }

  public ZPagerAdapter(List<T> data)
  {
    this.data = data;
  }

  public T getItem(int position)
  {
    return data == null || position > data.size() - 1 ? null : data.get(position);
  }

  @Override public int getCount()
  {
    return data == null ? 0 : data.size();
  }

  @Override public boolean isViewFromObject(View view, Object object)
  {
    return view == object;
  }

  @Override public void destroyItem(ViewGroup container, int position, Object object)
  {
    container.removeView((View) object);
  }

  @Override public Object instantiateItem(ViewGroup container, int position)
  {
    ImageView view = new ImageView(container.getContext());
    view.setBackgroundColor(Color.WHITE);
    loadImage(view, data.get(position));
    container.addView(view);
    return view;
  }

  public void loadImage(ImageView view, T t)
  {
    // 加载图片
  }
}

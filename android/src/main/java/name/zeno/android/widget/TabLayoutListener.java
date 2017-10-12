package name.zeno.android.widget;

import android.support.design.widget.TabLayout;

import name.zeno.android.listener.Action1;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/12/23.
 */
public class TabLayoutListener implements TabLayout.OnTabSelectedListener
{
  private TabLayout        tabLayout;
  private Action1<Integer> next;

  private TabLayoutListener() {}

  public static TabLayoutListener listen(TabLayout tabLayout, Action1<Integer> next)
  {
    if (tabLayout == null) {
      return null;
    }

    TabLayoutListener listener = new TabLayoutListener();
    listener.tabLayout = tabLayout;
    listener.tabLayout.addOnTabSelectedListener(listener);
    listener.next = next;
    return listener;
  }

  public void unListen()
  {
    if (tabLayout != null) {
      tabLayout.removeOnTabSelectedListener(this);
    }
  }

  @Override public void onTabSelected(TabLayout.Tab tab)
  {
    if (next != null && tabLayout != null) {
      next.call(tabLayout.getSelectedTabPosition());
    }

  }

  @Override public void onTabUnselected(TabLayout.Tab tab)
  {

  }

  @Override public void onTabReselected(TabLayout.Tab tab)
  {

  }

  public void setNext(Action1<Integer> next)
  {this.next = next; }
}

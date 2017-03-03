package name.zeno.android.listener;

import android.support.v4.widget.DrawerLayout;
import android.view.View;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 16/8/5
 */
public class DefaultDrawListener implements DrawerLayout.DrawerListener
{
  Action1<View> onClosed;
  Action1<View> onSlide;
  Action1<View> onOpened;
  Action1<View> onStateChanged;

  @Override public void onDrawerClosed(View drawerView)
  {

  }

  @Override public void onDrawerSlide(View drawerView, float slideOffset)
  {

  }

  @Override public void onDrawerOpened(View drawerView)
  {

  }

  @Override public void onDrawerStateChanged(int newState)
  {

  }

  public DefaultDrawListener onClosed(Action1<View> onClosed)
  {
    this.onClosed = onClosed;
    return this;
  }

  public DefaultDrawListener onSlide(Action1<View> onSlide)
  {
    this.onSlide = onSlide;
    return this;
  }

  public DefaultDrawListener onOpened(Action1<View> onOpened)
  {
    this.onOpened = onOpened;
    return this;
  }

  public DefaultDrawListener onStateChanged(Action1<View> onStateChanged)
  {
    this.onStateChanged = onStateChanged;
    return this;
  }
}

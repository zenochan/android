package name.zeno.android.presenter.searchpio;

import name.zeno.android.listener.Action1;
import name.zeno.android.presenter.LoadDataView;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/12/14.
 */
public interface SearchPoiView extends LoadDataView
{
  void requestLocationPermission(Action1<Boolean> next);
}

package name.zeno.android.presenter.searchpio;

import android.os.Bundle;
import android.support.annotation.Nullable;

import name.zeno.android.presenter.Extra;
import name.zeno.android.presenter.ZActivity;
import name.zeno.android.util.R;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/12/14.
 */
public class SearchPoiActivity extends ZActivity
{
  @Override protected void onCreate(@Nullable Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.z_activity_default);
    SearchPoiRequest request = Extra.getData(getIntent());
    addFragment(R.id.layout_container, SearchPoiFragment.newInstance(request));
  }
}

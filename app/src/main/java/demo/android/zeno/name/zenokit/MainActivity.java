package demo.android.zeno.name.zenokit;

import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;
import name.zeno.android.presenter.ZActivity;
import name.zeno.android.presenter.ZNav;

public class MainActivity extends ZActivity
{
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
  }

  @OnClick(R.id.btn_time_button) void timeButton()
  {
    ZNav.nav(this, TimeButtonActivity.class);
  }
}

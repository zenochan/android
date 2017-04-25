package demo.android.zeno.name.zenokit;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import demo.android.zeno.name.zenokit.item.MenuItem;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;
import name.zeno.android.listener.Action1;
import name.zeno.android.presenter.ZActivity;
import name.zeno.android.presenter.ZNav;

public class MainActivity extends ZActivity
{
  @BindView(R.id.rcv_class) RecyclerView rcvClass;

  private List<Class<? extends Activity>>             classes;
  private CommonRcvAdapter<Class<? extends Activity>> adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    init(savedInstanceState);
  }

  private void init(Bundle savedInstanceState)
  {
    Action1<Class<? extends Activity>> onClick = aClass -> ZNav.nav(this, aClass);

    classes = Arrays.asList(TimeButtonActivity.class, ShapeActivity.class);

    adapter = new CommonRcvAdapter<Class<? extends Activity>>(classes)
    {
      @NonNull @Override public AdapterItem createItem(Object type)
      {
        return new MenuItem(onClick);
      }
    };

    rcvClass.setLayoutManager(new LinearLayoutManager(getContext()));
    rcvClass.setAdapter(adapter);
  }

}

package demo.android.zeno.name.zenokit.item;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import kale.adapter.item.AdapterItem;
import name.zeno.android.listener.Action1;

/**
 * @author 陈治谋 (微信: puppet2436)
 * @since 2017/4/15
 */
public class MenuItem implements AdapterItem<Class<? extends Activity>>
{
  private TextView view;

  private Class<? extends Activity>          clazz;
  private Action1<Class<? extends Activity>> onClick;

  public MenuItem(Action1<Class<? extends Activity>> onClick)
  {
    this.onClick = onClick;
  }

  @Override public int getLayoutResId()
  {
    return android.R.layout.simple_expandable_list_item_1;
  }

  @Override public void bindViews(View root)
  {
    this.view = (TextView) root;
  }

  @Override public void setViews()
  {
    this.view.setOnClickListener(v -> onClick.call(clazz));
  }

  @Override public void handleData(Class<? extends Activity> aClass, int position)
  {
    this.clazz = aClass;
    this.view.setText(aClass.getSimpleName());
  }
}

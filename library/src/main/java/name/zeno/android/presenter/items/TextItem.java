package name.zeno.android.presenter.items;

import android.view.View;
import android.widget.TextView;

import kale.adapter.item.AdapterItem;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/12/5.
 */
public class TextItem implements AdapterItem<String>
{
  private TextView root;

  @Override public int getLayoutResId()
  {
    return android.R.layout.simple_list_item_1;
  }

  @Override public void bindViews(View root)
  {
    this.root = (TextView) root;

  }

  @Override public void setViews()
  {

  }

  @Override public void handleData(String s, int position)
  {
    root.setText(s);
  }
}

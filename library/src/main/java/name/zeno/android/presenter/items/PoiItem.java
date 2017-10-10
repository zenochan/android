package name.zeno.android.presenter.items;

import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import kale.adapter.item.AdapterItem;
import name.zeno.android.listener.Action1;
import name.zeno.android.util.R;
import name.zeno.android.util.R2;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/12/14.
 */
public class PoiItem implements AdapterItem<PoiInfo>
{
  @BindView(R2.id.tv_poi_title)   TextView tvPoiTitle;
  @BindView(R2.id.tv_poi_address) TextView tvPoiAddress;

  Action1<PoiInfo> onClick;

  private PoiInfo poiInfo;

  @Override public int getLayoutResId()
  {

    return R.layout.item_poi_info;
  }

  @Override public void bindViews(View root)
  {
    ButterKnife.bind(this, root);
    root.setOnClickListener(view -> {
      if (onClick != null) {
        onClick.call(poiInfo);
      }
    });
  }

  @Override public void setViews()
  {

  }

  @Override public void handleData(PoiInfo poiInfo, int position)
  {
    this.poiInfo = poiInfo;
    tvPoiTitle.setText(this.poiInfo.name);
    tvPoiAddress.setText(this.poiInfo.address);
  }

  public void setOnClick(Action1<PoiInfo> onClick)
  {this.onClick = onClick; }
}

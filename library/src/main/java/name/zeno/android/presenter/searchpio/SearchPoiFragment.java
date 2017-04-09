package name.zeno.android.presenter.searchpio;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.baidu.mapapi.search.core.PoiInfo;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;
import name.zeno.android.listener.Action1;
import name.zeno.android.presenter.Extra;
import name.zeno.android.presenter.ZFragment;
import name.zeno.android.presenter.items.PoiItem;
import name.zeno.android.system.ZPermission;
import name.zeno.android.third.baidu.PoiModel;
import name.zeno.android.util.R;
import name.zeno.android.util.R2;
import name.zeno.android.util.ZString;
import name.zeno.android.widget.ZTextWatcher;
import name.zeno.android.widget.recycler.LoadAdapterWrapper;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/12/14.
 */
public class SearchPoiFragment extends ZFragment implements SearchPoiView {
  @BindView(R2.id.rcv_search_poi) RecyclerView rcvSearchPio;
  @BindView(R2.id.tv_keyword) AppCompatEditText etKeyword;
  @BindView(R2.id.btn_input) Button btnInput;
  private SearchPoiPresenter presenter = new SearchPoiPresenter(this);

  private SearchPoiRequest request;
  private CommonRcvAdapter<PoiInfo> adapter;
  private LoadAdapterWrapper wrapper;
  private Action1<PoiInfo> onClickPoi;

  public static SearchPoiFragment newInstance(SearchPoiRequest request) {
    Bundle args = new Bundle();
    args.putParcelable(SearchPoiRequest.EXTRA_NAME, request);

    SearchPoiFragment fragment = new SearchPoiFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    request = getArguments().getParcelable(SearchPoiRequest.EXTRA_NAME);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_search_poi, container, false);
    ButterKnife.bind(this, view);
    init(savedInstanceState);
    return view;
  }


  @Override
  public void requestLocationPermission(Action1<Boolean> next) {
    new RxPermissions(getActivity()).request(
        ZPermission.WRITE_EXTERNAL_STORAGE,
        ZPermission.ACCESS_COARSE_LOCATION,
        ZPermission.ACCESS_FINE_LOCATION
    ).subscribe(next::call);
  }

  @OnClick(R2.id.btn_input)
  void onClickBtnInput() {
    customInput(etKeyword.getText().toString());
  }

  private void init(Bundle savedInstanceState) {
    onClickPoi = this::onClickPoi;

    adapter = new CommonRcvAdapter<PoiInfo>(presenter.getInfoList()) {
      @NonNull
      @Override
      public AdapterItem createItem(Object type) {
        PoiItem item = new PoiItem();
        item.setOnClick(onClickPoi);
        return item;
      }
    };

    wrapper = new LoadAdapterWrapper.Builder(getContext())
        .adapter(adapter)
        .recycler(rcvSearchPio)
        .layoutManager(new LinearLayoutManager(getContext()))
        .build();

    etKeyword.setText(request.getFill());
    ZTextWatcher.watch(etKeyword, (view, txt) -> {
      if (!request.isEnableOriginInput() || ZString.isEmpty(txt)) {
        btnInput.setVisibility(View.GONE);
      } else {
        btnInput.setText(txt);
        btnInput.setVisibility(View.VISIBLE);
        presenter.search(txt);
      }
    });
    if (request.isEnableOriginInput() && ZString.notEmpty(request.getFill())) {
      btnInput.setText(request.getFill());
      btnInput.setVisibility(View.VISIBLE);
    }
  }

  // 使用用户输入的值
  private void customInput(String text) {
    PoiInfo info = new PoiInfo();
    info.address = text;
    onClickPoi(info);
  }

  // 选择热点
  private void onClickPoi(PoiInfo poiInfo) {
    PoiModel poi = new PoiModel(poiInfo);
    setActivityResult(RESULT_OK, Extra.setData(poi));
    finish();
  }
}

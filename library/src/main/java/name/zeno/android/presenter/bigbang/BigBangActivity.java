package name.zeno.android.presenter.bigbang;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import name.zeno.android.presenter.Extra;
import name.zeno.android.presenter.ZActivity;
import name.zeno.android.presenter.bigbang.core.BigBangLayout;
import name.zeno.android.system.ZStatusBar;
import name.zeno.android.util.R;
import name.zeno.android.util.R2;
import name.zeno.android.util.ZString;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/1/3.
 */
public class BigBangActivity extends ZActivity implements BigBangView
{
  @BindView(R2.id.layout_bigbang) BigBangLayout layoutBigbang;
  private BigBangPresenter presenter = new BigBangPresenter(this);

  public static Intent callIntent(Context context, String text)
  {
    Intent intent = new Intent(context, BigBangActivity.class);
    intent.putExtra(Extra.KEY, text);
    return intent;
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    ZStatusBar.INSTANCE.setImage(this);
    setContentView(R.layout.activity_bigbang);
    ButterKnife.bind(this);
    segments(getIntent().getStringExtra(Extra.KEY));

  }

  private void segments(String text)
  {
    if (TextUtils.isEmpty(text)) {
      finish();
      return;
    }

    List<String> uglySegments = new ArrayList<>();
    for (int i = 0; i < (text.length() + 1) / 4; i++) {
      uglySegments.add(ZString.INSTANCE.sub(text, 4 * i, 4 * i + 3));
    }

    layoutBigbang.setTextItems(uglySegments);
    presenter.segments(text, layoutBigbang::setTextItems);

  }
}

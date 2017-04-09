package demo.android.zeno.name.zenokit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import name.zeno.android.presenter.ZActivity;
import name.zeno.android.widget.TimeButton;

/**
 * @author 陈治谋 (微信: puppet2436)
 * @since 2017/4/9
 */
public class TimeButtonActivity extends ZActivity
{
  @BindView(R.id.btn_sms)   TimeButton btnSms;
  @BindView(R.id.btn_reset) Button     btnReset;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_time_button);
    ButterKnife.bind(this);
    btnSms.setLength(10000);
  }

  @OnClick(R.id.btn_reset) void reset()
  {
    btnSms.reset();
  }
}

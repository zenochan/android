package name.zeno.android.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import name.zeno.android.data.CommonConnector;
import name.zeno.android.exception.ZException;
import name.zeno.android.presenter.ZActivity;
import name.zeno.android.util.R;
import name.zeno.android.util.ZAction;
import name.zeno.android.util.ZLog;
import name.zeno.android.widget.SimpleActionbar;

/**
 * <h1>程序奔溃</h1>
 * <p>
 * 点击发送奔溃日志邮件
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 1.3.0
 */
public class CrashLogActivity extends ZActivity
{

  TextView tvInfo;
  private String crashLog;

  private ExceptionInfo info;


  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_crash_log);

    //接收异常对象
    Intent intent = getIntent();
    info = intent.getParcelableExtra(ExceptionInfo.EXTRA_NAME);

    tvInfo = (TextView) findViewById(R.id.tv_crash_log);
    SimpleActionbar actionbar = (SimpleActionbar) findViewById(R.id.layout_actionbar);
    actionbar.setOnClickAction(this::sendLog);

    crashLog = ZException.info(info.throwable);
    CommonConnector.sendCrash(info.throwable, info.accountJson);
    tvInfo.setText(crashLog);
    ZLog.e(TAG, crashLog);
  }

  private void sendLog()
  {
    String subject = String.format("来自 %s-%s(%s)的客户端奔溃日志", AppInfo.appName, AppInfo.versionName, AppInfo.versionCode);
    ZAction.sendEmail(this, info.email, subject, crashLog);
  }

  @Override public void finish()
  {
    Intent intent = new Intent(this, info.getMainActivityClass());
    intent.putExtra("exit", true);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
    startActivity(intent);
    super.finish();
  }
}

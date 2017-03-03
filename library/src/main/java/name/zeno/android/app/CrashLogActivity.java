package name.zeno.android.app;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;

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

  private ExceptionInfo   info;
  private SimpleActionbar actionbar;


  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_crash_log);

    //接收异常对象
    Intent intent = getIntent();
    info = intent.getParcelableExtra(ExceptionInfo.EXTRA_NAME);

    tvInfo = (TextView) findViewById(R.id.tv_crash_log);
    actionbar = (SimpleActionbar) findViewById(R.id.layout_actionbar);
    actionbar.setOnClickAction(this::sendLog);

    crashLog = buildCrashLog(info.throwable);
    tvInfo.setText(crashLog);
    ZLog.e(TAG, crashLog);
  }

  private String buildCrashLog(@NonNull Throwable e)
  {
    //构建字符串
    Writer      writer      = new StringWriter();
    PrintWriter printWriter = new PrintWriter(writer);
    e.printStackTrace(printWriter);
    Throwable cause = e.getCause();
    while (cause != null) {
      cause.printStackTrace(printWriter);
      cause = cause.getCause();
    }
    printWriter.close();
    StringBuilder sb;
    sb = new StringBuilder()
        .append("生产厂商：\n").append(Build.MANUFACTURER).append("\n\n")
        .append("手机型号：\n").append(Build.MODEL).append("\n\n")
        .append("系统版本：\n").append(Build.VERSION.RELEASE).append("\n\n")
        .append("异常时间：\n").append(new Date()).append("\n\n")
        .append("异常类型：\n").append(e.getClass().getName()).append("\n\n")
        .append("异常信息：\n").append(e.getMessage()).append("\n\n")
        .append("异常堆栈：\n").append(writer.toString());
    return sb.toString();
  }

  private void sendLog()
  {
    ZAction.sendEmail(this, info.subscribeEmail,
        String.format("来自 %s-%s(%s)的客户端奔溃日志", info.appName, info.versionName, info.versionCode),
        crashLog);
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

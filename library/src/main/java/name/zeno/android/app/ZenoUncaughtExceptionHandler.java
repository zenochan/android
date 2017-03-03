package name.zeno.android.app;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

import lombok.Setter;
import name.zeno.android.listener.Action1;

/**
 * Create Date: 16/6/30
 *
 * @author 陈治谋 (513500085@qq.com)
 */
public class ZenoUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler
{
  private Application                     application;
  @SuppressWarnings("FieldCanBeLocal,unused")
  private Thread.UncaughtExceptionHandler defaultHandler;

  @SuppressWarnings("unused")
  @Setter private Action1<Throwable> onError;

  @Setter private String                    subscribeEmail;
  @Setter private String                    appName;
  @Setter private String                    versionName;
  @Setter private int                       versionCode;
  @Setter private Class<? extends Activity> mainActivityClass;


  public ZenoUncaughtExceptionHandler(Application application)
  {
    this.application = application;
    defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
  }

  @Override public void uncaughtException(Thread thread, Throwable throwable)
  {
    if (onError != null) {
      onError.call(throwable);
    }
    Intent intent = new Intent(application, CrashLogActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    ExceptionInfo info = new ExceptionInfo();
    info.appName = this.appName;
    info.versionName = this.versionName;
    info.versionCode = this.versionCode;
    info.subscribeEmail = this.subscribeEmail;
    info.throwable = throwable;
    info.mainActivityClass = this.mainActivityClass;
    intent.putExtra(ExceptionInfo.EXTRA_NAME, info);
    application.startActivity(intent);

    System.exit(1);
  }
}

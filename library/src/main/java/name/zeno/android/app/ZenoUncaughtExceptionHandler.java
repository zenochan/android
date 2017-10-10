package name.zeno.android.app;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

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

  private Action1<Throwable> onError;

  private String                    email;
  private String                    accountJson;
  private Class<? extends Activity> mainActivityClass;

  public void setOnError(Action1<Throwable> onError)
  {
    this.onError = onError;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  public void setAccountJson(String accountJson)
  {
    this.accountJson = accountJson;
  }

  public void setMainActivityClass(Class<? extends Activity> mainActivityClass)
  {
    this.mainActivityClass = mainActivityClass;
  }

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
    info.email = email;
    info.accountJson = accountJson;
    info.throwable = throwable;
    info.mainActivityClass = mainActivityClass;
    intent.putExtra(ExceptionInfo.EXTRA_NAME, info);
    application.startActivity(intent);

    System.exit(1);
  }
}

package name.zeno.android.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.StringRes;

/**
 * Create Date: 16/6/18
 *
 * @author 陈治谋 (513500085@qq.com)
 */
public interface View extends LifeCycleObservable
{
  Context getContext();

  String getString(@StringRes int res);

  void toast(String msg);

  void toast(@StringRes int resId);

  void showMessage(String msg);

  void showMessage(@StringRes int resId);

  void showMessageAndFinish(String message);

  void finish();
}

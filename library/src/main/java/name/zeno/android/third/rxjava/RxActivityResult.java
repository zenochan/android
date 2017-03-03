package name.zeno.android.third.rxjava;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import name.zeno.android.listener.Action2;
import name.zeno.android.presenter.LifeCycleObservable;
import name.zeno.android.presenter.LifecycleListener;

/**
 * <h1>Rx封装 onActivityResult回掉</h1>
 * <p>
 * 同步  {@link #onActivityResult(int, int, Intent)} 方法的调用,占用{@link #REQUEST_CODE}
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/11/9.
 */
public class RxActivityResult
{
  public static final int REQUEST_CODE = 0xF001;

  private Activity activity;
  private Fragment fragment;

  private Action2<Boolean, Intent> onResult;

  @SuppressWarnings("unused")
  public <T extends Activity & LifeCycleObservable> RxActivityResult(@NonNull T activity)
  {
    this.activity = activity;
    regListener((LifeCycleObservable) this.activity);
  }

  public <T extends Fragment & LifeCycleObservable> RxActivityResult(@NonNull T fragment)
  {
    this.fragment = fragment;
    regListener((LifeCycleObservable) this.fragment);
  }

  private void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    if (requestCode == REQUEST_CODE && onResult != null) {
      onResult.call(resultCode == Activity.RESULT_OK, data);
    }
    onResult = null;
  }

  public void startActivityForResult(Intent intent, Action2<Boolean, Intent> onResult)
  {
    this.onResult = onResult;
    if (fragment != null) {
      fragment.startActivityForResult(intent, REQUEST_CODE);
    } else {
      activity.startActivityForResult(intent, REQUEST_CODE);
    }
  }

  private void regListener(LifeCycleObservable observable)
  {
    if (observable == null) {
      return;
    }

    observable.registerLifecycleListener(new LifecycleListener()
    {
      @Override public void onCreate() { }

      @Override public void onResume() { }

      @Override public void onActivityResult(int requestCode, int resultCode, Intent data)
      {
        RxActivityResult.this.onActivityResult(requestCode, resultCode, data);
      }

      @Override public void onViewCreated() { }

      @Override public void onDestroyView() { }

      @Override public void onDestroy() { }
    });

  }
}

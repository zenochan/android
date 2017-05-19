package name.zeno.android.util;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lombok.Setter;
import name.zeno.android.exception.ZException;
import name.zeno.android.listener.Action0;
import name.zeno.android.listener.Action1;
import name.zeno.android.presenter.LifecycleListener;
import name.zeno.android.presenter.ZFragment;
import name.zeno.android.third.rxjava.ZObserver;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/5/9
 */
public class Rx2Timer implements LifecycleListener
{
  private static final String TAG = "Rx2Timer";
  private Disposable disposable;
  @Setter private long expiredIn = 0;
  // 周期 s
  @Setter
  private         int  period    = 1;
  @Setter private Action1<Long> onNext;
  @Setter private Action0       onComplete;


  private Rx2Timer() { }

  public void start()
  {
    if (period < 1) period = 1;
    Observable.interval(0, period, TimeUnit.SECONDS)
        .takeWhile(value -> System.currentTimeMillis() <= expiredIn + 1000)
        .subscribeOn(Schedulers.single())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new ZObserver<Long>()
        {
          @Override public void onNext(Long value)
          {
            if (onNext != null) {
              long remain = (expiredIn - System.currentTimeMillis()) / 1000;
              onNext.call(remain < 0 ? 0 : remain);
            }
          }

          @Override public void onComplete()
          {
            if (onComplete != null) {onComplete.call();}
          }

          @Override public void handleError(ZException e)
          {
            ZLog.e(e.getMessage());
          }

          @Override public void onSubscribe(Disposable d)
          {
            super.onSubscribe(d);
            disposable = d;
          }
        });
  }

  public void stop()
  {
    if (disposable != null && !disposable.isDisposed()) {
      disposable.dispose();
      disposable = null;
    }
    expiredIn = 0;
  }

  public static Rx2Timer with(FragmentActivity activity)
  {
    Rx2Timer rx2Timer = new Rx2Timer();
    rx2Timer.registLifecycle(activity.getSupportFragmentManager());
    return rx2Timer;
  }

  public static Rx2Timer with(Fragment fragment)
  {
    Rx2Timer rx2Timer = new Rx2Timer();
    rx2Timer.registLifecycle(fragment.getFragmentManager());
    return rx2Timer;
  }

  //<editor-fold desc="lifecycle listener">
  @Override public void onCreate() { }

  @Override public void onResume() { }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) { }

  @Override public void onViewCreated() { }

  @Override public void onDestroyView() { }

  @Override public void onDestroy()
  {
    stop();
  }
  //</editor-fold>

  private void registLifecycle(FragmentManager fragmentManager)
  {
    ZFragment fragment = (ZFragment) fragmentManager.findFragmentByTag(TAG);
    if (fragment == null) {
      fragment = new ZFragment();
      fragmentManager.beginTransaction().add(fragment, TAG).commit();
    }
    fragment.registerLifecycleListener(this);
  }
}

package name.zeno.android.presenter;

import android.content.Intent;
import android.support.annotation.CallSuper;

import com.orhanobut.logger.Logger;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import name.zeno.android.exception.ZException;
import name.zeno.android.listener.Action0;
import name.zeno.android.listener.Action1;
import name.zeno.android.third.rxjava.ZObserver;
import name.zeno.android.util.ZLog;

/**
 * Create Date: 16/6/7
 *
 * @author 陈治谋 (513500085@qq.com)
 */
@SuppressWarnings("WeakerAccess")
public abstract class BasePresenter<T extends View> implements Presenter {
  private final String TAG;

  private CompositeDisposable compositeDisposable = new CompositeDisposable();

  protected T view;

  public BasePresenter(T view) {
    TAG = getClass().getSimpleName();

    this.view = view;
    view.registerLifecycleListener(this);
  }

  public void setView(T view) {
    this.view = view;
  }

  @Override
  public void onCreate() { }

  @Override
  public void onResume() { }

  @Override
  public void onViewCreated() { }

  @Override
  public void onDestroyView() {
  }

  @CallSuper
  @Override
  public void onDestroy() {
    compositeDisposable.dispose();
    view = null;
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
  }

  protected void addDisposable(Disposable disposable) {
    compositeDisposable.add(disposable);
  }

  protected void removeDisposable(Disposable disposable) {
    compositeDisposable.remove(disposable);
  }

  protected <E> Observer<E> sub(Action1<E> onNext) {
    return sub(onNext, e -> ZLog.e(TAG, "default on error", e));
  }

  protected <E> Observer<E> sub(Action1<E> onNext, Action1<ZException> onError) {
    return sub(onNext, onError, () -> Logger.t(TAG).v("default on complete"));
  }

  protected <E> Observer<E> sub(Action1<E> onNext, Action1<ZException> onError, Action0 onComplete) {
    return subWithDisposable(onNext, onError, onComplete, null);
  }


  protected <E> Observer<E> subWithDisposable(Action1<E> onNext, Action1<Disposable> onSub) {
    return subWithDisposable(onNext, e -> ZLog.e(TAG, "default on error", e), onSub);
  }


  protected <E> Observer<E> subWithDisposable(Action1<E> onNext, Action1<ZException> onError, Action1<Disposable> onSub) {
    return subWithDisposable(onNext, onError, () -> Logger.t(TAG).v("default on complete"), onSub);
  }

  protected <E> Observer<E> subWithDisposable(Action1<E> onNext, Action1<ZException> onError, Action0 onComplete, Action1<Disposable> onSub) {
    return new ZObserver<E>() {
      private Disposable disposable;

      @Override public void onSubscribe(Disposable d) {
        addDisposable(d);
        this.disposable = d;
        if (onSub != null) {
          onSub.call(d);
        }
      }

      @Override
      public void onNext(E e) {
        onNext.call(e);
      }

      @Override
      public void handleError(ZException e) {
        onError.call(e);
        onComplete();
      }

      @Override
      public void onComplete() {
        removeDisposable(disposable);
        onComplete.call();
      }
    };
  }

}

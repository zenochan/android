package name.zeno.android.presenter

import android.content.Intent
import android.support.annotation.CallSuper

import com.orhanobut.logger.Logger

import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import name.zeno.android.TAG
import name.zeno.android.data.CommonConnector
import name.zeno.android.exception.ZException
import name.zeno.android.third.rxjava.ZObserver
import name.zeno.android.util.ZLog

/**
 * Create Date: 16/6/7
 *
 * @author 陈治谋 (513500085@qq.com)
 */
abstract class BasePresenter<T : View>(var view: T?) : Presenter {
  private val compositeDisposable = CompositeDisposable()

  init {
    view?.registerLifecycleListener(this)
  }

  override fun onCreate() {}
  override fun onResume() {}
  override fun onViewCreated() {}
  override fun onDestroyView() {}

  @CallSuper
  override fun onDestroy() {
    compositeDisposable.dispose()
    view = null
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

  fun addDisposable(disposable: Disposable) {
    compositeDisposable.add(disposable)
  }

  fun removeDisposable(disposable: Disposable) {
    try {
      compositeDisposable.remove(disposable)
    } catch (e: Exception) {
      CommonConnector.sendCrash(e)
    }
  }

  fun <E> sub(onNext: (E) -> Unit) =
      sub(onNext, { e -> ZLog.e(TAG, "default on error", e) })

  fun <E> sub(onNext: (E) -> Unit, onError: (ZException) -> Unit): Observer<E> =
      sub(onNext, onError, { Logger.t(TAG).v("default on complete") })

  fun <E> sub(onNext: (E) -> Unit, onError: (ZException) -> Unit, onComplete: () -> Unit): Observer<E> =
      subWithDisposable(onNext, onError, onComplete, null)

  fun <E> subWithDisposable(onNext: (E) -> Unit, onSub: (Disposable) -> Unit): Observer<E> =
      subWithDisposable(onNext, { e -> ZLog.e(TAG, "default on error", e) }, onSub)

  fun <E> subWithDisposable(onNext: (E) -> Unit, onError: (ZException) -> Unit, onSub: (Disposable) -> Unit): Observer<E> =
      subWithDisposable(onNext, onError, { Logger.t(TAG).v("default on complete") }, onSub)

  fun <E> subWithDisposable(
      onNext: (E) -> Unit,
      onError: (ZException) -> Unit,
      onComplete: () -> Unit,
      onSub: ((Disposable) -> Unit)?
  ): Observer<E> = object : ZObserver<E>() {
    private var disposable: Disposable? = null

    override fun onSubscribe(d: Disposable) {
      addDisposable(d)
      this.disposable = d
      onSub?.invoke(d)
    }

    override fun onNext(value: E) {
      onNext.invoke(value)
    }

    override fun handleError(e: ZException) {
      onError.invoke(e)
      onComplete()
    }

    override fun onComplete() {
      onComplete.invoke()
      val d = disposable ?: return
      removeDisposable(d)
    }
  }
}

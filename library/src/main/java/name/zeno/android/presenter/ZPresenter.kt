package name.zeno.android.presenter

import android.content.Intent
import androidx.annotation.CallSuper
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import name.zeno.android.TAG
import name.zeno.android.data.CommonConnector
import name.zeno.android.exception.ZException
import name.zeno.android.third.rxjava.ZObserver
import name.zeno.android.util.ZLog
import java.lang.ref.WeakReference

/**
 * Create Date: 16/6/7
 *
 * @author 陈治谋 (513500085@qq.com)
 */
abstract class ZPresenter<T : View>(view: T) : Presenter {
  private val compositeDisposable = CompositeDisposable()

  private val viewRef: WeakReference<T> = WeakReference(view)

  init {
    view.registerLifecycleListener(this)
  }

  val view: T
    get() = viewRef.get()!!

  override fun onCreate() {}
  override fun onResume() {}
  override fun onViewCreated() {}
  override fun onDestroyView() {}

  @CallSuper
  override fun onDestroy() {
    compositeDisposable.dispose()
    viewRef.clear()
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

  fun <E> sub(
      next: (E) -> Unit,
      error: (ZException) -> Unit = { e -> ZLog.e(TAG, "ERROR!", e) },
      complete: () -> Unit = { Logger.t(TAG).v("COMPLETE!") },
      sub: ((Disposable) -> Unit)? = null
  ): Observer<E> = object : ZObserver<E>() {
    private var disposable: Disposable? = null

    override fun onSubscribe(d: Disposable) {
      addDisposable(d)
      this.disposable = d
      sub?.invoke(d)
    }

    override fun onNext(value: E) = next.invoke(value)
    override fun handleError(e: ZException) = error.invoke(e)

    override fun onComplete() {
      complete.invoke()
      val d = disposable ?: return
      removeDisposable(d)
    }
  }

  protected fun <E> Observable<E>.sub(next: (E) -> Unit) = this.subscribe(this@ZPresenter.sub(next))

  protected fun <E> Observable<E>.sub(
      next: (E) -> Unit,
      error: (ZException) -> Unit = { e -> ZLog.e(TAG, "ERROR!", e) },
      complete: () -> Unit = { Logger.t(TAG).v("COMPLETE!") },
      sub: ((Disposable) -> Unit)? = null
  ) = this.subscribe(this@ZPresenter.sub(next, error, complete, sub))
}

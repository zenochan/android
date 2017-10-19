package name.zeno.android.util

import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager

import java.util.concurrent.TimeUnit

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import name.zeno.android.exception.ZException
import name.zeno.android.listener.Action0
import name.zeno.android.listener.Action1
import name.zeno.android.presenter.LifecycleListener
import name.zeno.android.presenter.ZFragment
import name.zeno.android.third.rxjava.ZObserver

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/5/9
 */
class Rx2Timer private constructor() : LifecycleListener {
  private var disposable: Disposable? = null
  private var expiredIn: Long = 0
  // 周期 s
  private var period = 1
  private var onNext: Action1<Long>? = null
  private var onComplete: Action0? = null

  fun start() {
    if (period < 1) period = 1
    Observable.interval(0, period.toLong(), TimeUnit.SECONDS)
        .takeWhile { value -> System.currentTimeMillis() <= expiredIn + 1000 }
        .subscribeOn(Schedulers.single())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object : ZObserver<Long>() {
          override fun onNext(value: Long) {
            if (onNext != null) {
              val remain = (expiredIn - System.currentTimeMillis()) / 1000
              onNext!!.call(if (remain < 0) 0 else remain)
            }
          }

          override fun onComplete() {
            if (onComplete != null) {
              onComplete!!.call()
            }
          }

          override fun handleError(e: ZException) {
            ZLog.e(e.message ?: "")
          }

          override fun onSubscribe(d: Disposable) {
            super.onSubscribe(d)
            disposable = d
          }
        })
  }

  fun stop() {
    if (disposable != null && !disposable!!.isDisposed) {
      disposable!!.dispose()
      disposable = null
    }
    expiredIn = 0
  }

  //<editor-fold desc="lifecycle listener">
  override fun onCreate() {}

  override fun onResume() {}

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

  override fun onViewCreated() {}

  override fun onDestroyView() {}

  override fun onDestroy() {
    stop()
  }
  //</editor-fold>

  private fun registLifecycle(fragmentManager: FragmentManager) {
    var fragment: ZFragment? = fragmentManager.findFragmentByTag(TAG) as ZFragment
    if (fragment == null) {
      fragment = ZFragment()
      fragmentManager.beginTransaction().add(fragment, TAG).commit()
    }
    fragment.registerLifecycleListener(this)
  }

  fun setExpiredIn(expiredIn: Long) {
    this.expiredIn = expiredIn
  }

  fun setPeriod(period: Int) {
    this.period = period
  }

  fun setOnNext(onNext: Action1<Long>) {
    this.onNext = onNext
  }

  fun setOnComplete(onComplete: Action0) {
    this.onComplete = onComplete
  }

  companion object {
    private val TAG = "Rx2Timer"

    fun with(activity: FragmentActivity): Rx2Timer {
      val rx2Timer = Rx2Timer()
      rx2Timer.registLifecycle(activity.supportFragmentManager)
      return rx2Timer
    }

    fun with(fragment: Fragment): Rx2Timer {
      val rx2Timer = Rx2Timer()
      rx2Timer.registLifecycle(fragment.fragmentManager)
      return rx2Timer
    }
  }
}

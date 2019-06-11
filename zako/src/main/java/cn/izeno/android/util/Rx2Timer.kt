package cn.izeno.android.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Fragment
import android.app.FragmentManager
import android.content.Intent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import cn.izeno.android.core.JELLY_BEAN_MR1
import cn.izeno.android.core.now
import cn.izeno.android.core.sdkInt
import cn.izeno.android.exception.ZException
import cn.izeno.android.presenter.LifecycleListener
import cn.izeno.android.presenter.ZFragment
import cn.izeno.android.third.rxjava.ZObserver
import java.util.concurrent.TimeUnit

@Suppress("unused")
/**
 * - [Rx2Timer.with] 获取实例，自动周期管理
 *
 * - [start] 开始倒计时
 * - [stop] 停止倒计时
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/5/9
 */
class Rx2Timer private constructor() : LifecycleListener {
  private var disposable: Disposable? = null
  private var expiredIn: Long = 0
  // 周期 s
  private var period = 1
  private var onNext: ((remain: Long) -> Unit)? = null
  private var onComplete: (() -> Unit)? = null

  fun start() {
    if (period < 1) period = 1
    Observable.interval(0, period.toLong(), TimeUnit.SECONDS)
        .takeWhile { now <= expiredIn + 1000 }
        .subscribeOn(Schedulers.single())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object : ZObserver<Long>() {

          override fun onNext(value: Long) {
            val remain = (expiredIn - now) / 1000
            onNext?.invoke(if (remain < 0) 0 else remain)
          }

          override fun onComplete() {
            onComplete?.invoke()
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
    if (disposable?.isDisposed == false) {
      disposable?.dispose()
      disposable = null
    }
    expiredIn = 0
  }

  override fun onCreate() {}
  override fun onResume() {}
  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}
  override fun onViewCreated() {}
  override fun onDestroyView() {}
  override fun onDestroy() = stop()

  private fun registLifecycle(fragmentManager: FragmentManager) {
    var fragment: ZFragment? = fragmentManager.findFragmentByTag(TAG) as? ZFragment
    if (fragment == null) {
      fragment = ZFragment()
      fragmentManager.beginTransaction().add(fragment, TAG).commit()
    }
    fragment.registerLifecycleListener(this)
  }

  fun expiredIn(expiredIn: Long) = apply {
    this.expiredIn = expiredIn
  }

  /**
   * 周期，s
   */
  fun period(period: Int) = apply {
    this.period = period
  }

  fun next(action: (remain: Long) -> Unit) = apply {
    this.onNext = action
  }


  fun complete(action: () -> Unit) = apply {
    this.onComplete = action
  }

  companion object {
    private val TAG = "Rx2Timer"

    @JvmStatic
    fun with(activity: Activity): Rx2Timer {
      val rx2Timer = Rx2Timer()
      rx2Timer.registLifecycle(activity.fragmentManager)
      return rx2Timer
    }

    @SuppressLint("NewApi")
    @JvmStatic
    fun with(fragment: Fragment): Rx2Timer {
      val rx2Timer = Rx2Timer()
      val fm = when {
        sdkInt >= JELLY_BEAN_MR1 -> fragment.childFragmentManager
        else -> fragment.fragmentManager
      }
      rx2Timer.registLifecycle(fm)
      return rx2Timer
    }
  }
}

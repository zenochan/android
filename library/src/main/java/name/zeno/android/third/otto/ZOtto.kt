package name.zeno.android.third.otto

import android.os.Looper

import com.squareup.otto.Bus

import io.reactivex.Observable
import io.reactivex.functions.Consumer
import name.zeno.android.third.rxjava.RxUtils

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 16/5/30
 */
object ZOtto {

  private val bus: Bus = Bus()

  fun post(event: Any) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
      bus.post(event)
    } else {
      // 切换到主线程发送事件
      // Event bus [Bus "default"] accessed from non-main thread Looper
      Observable.just(event).compose(RxUtils.applySchedulers()).subscribe({ bus.post(it) })
    }
  }

  fun register(`object`: Any) {
    bus.register(`object`)
  }

  fun unregister(`object`: Any) {
    bus.unregister(`object`)
  }
}

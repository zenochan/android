package name.zeno.android.third.otto

import android.os.Looper
import com.squareup.otto.Bus
import io.reactivex.Observable
import name.zeno.android.third.rxjava.RxUtils

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/10/21
 */
private val bus: Bus by lazy { Bus() }

fun Any.otto() {
  if (Looper.myLooper() == Looper.getMainLooper()) {
    bus.post(this)
  } else {
    // 切换到主线程发送事件
    // Event bus [Bus "default"] accessed from non-main thread Looper
    Observable.just(this).compose(RxUtils.applySchedulers()).subscribe({ bus.post(it) })
  }
}

fun registerOtto(any: Any) {
  bus.register(any)
}

fun unregisterOtto(any: Any) {
  bus.unregister(any)
}

package name.zeno.android.third.rxbus

import com.hwangjr.rxbus.Bus
import com.hwangjr.rxbus.RxBus

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2018/5/18
 */
/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/10/21
 */
private val rxBus: Bus by lazy { RxBus.get() }

fun Any.rxBus(tag: String? = null) {
  if (tag != null) {
    rxBus.post(tag, this)
  } else {
    rxBus.post(this)
  }
}

fun Any.registerRxBus() {
  rxBus.register(this)
}

fun Any.unregisterRxBus() {
  rxBus.unregister(this)
}

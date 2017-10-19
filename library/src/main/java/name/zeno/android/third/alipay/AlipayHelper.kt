package name.zeno.android.third.alipay

import android.Manifest
import android.app.Activity
import android.support.annotation.RequiresPermission
import com.alipay.sdk.app.PayTask
import io.reactivex.Emitter
import io.reactivex.Observable
import name.zeno.android.third.rxjava.RxUtils

/**
 * Create Date: 16/6/18
 *
 * @author 陈治谋 (513500085@qq.com)
 */
object AlipayHelper {
  @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
  fun pay(activity: Activity, payInfo: String, next: (AlipayResult) -> Unit) {
    Observable.create({ subscriber: Emitter<AlipayResult> ->
      val payTask = PayTask(activity)
      val result = payTask.payV2(payInfo, true)
      subscriber.onNext(AlipayResult(result))
    }).compose(RxUtils.applySchedulers()).subscribe(next)
  }
}

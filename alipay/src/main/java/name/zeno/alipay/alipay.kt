package name.zeno.alipay

import android.Manifest
import android.app.Activity
import android.support.annotation.RequiresPermission
import com.alipay.sdk.app.PayTask
import io.reactivex.Single
import name.zeno.android.third.rxjava.RxUtils

/**
 * @author [陈治谋](mailto:zenochan@qq.com)
 * @since 2018/5/29
 */
@RequiresPermission(Manifest.permission.READ_PHONE_STATE)
fun Activity.alipay(payInfo: String, next: (AlipayResult) -> Unit) {
  Single.create<AlipayResult> {
    val payTask = PayTask(this)
    val result = payTask.payV2(payInfo, true)
    it.onSuccess(AlipayResult(result))
  }.compose(RxUtils.applySchedulersSingle()).subscribe(next)
}

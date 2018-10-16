package name.zeno.alipay

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import androidx.annotation.RequiresPermission
import com.alipay.sdk.app.PayTask
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * 支付宝支付
 * @author [陈治谋](mailto:zenochan@qq.com)
 * @since 2018/5/29
 */
@SuppressLint("MissingPermission")
@RequiresPermission(Manifest.permission.READ_PHONE_STATE)
fun Activity.alipay(payInfo: String, next: (AlipayResult) -> Unit): Disposable {
  return Single
      .create<AlipayResult> {
        val payTask = PayTask(this)
        val result = payTask.payV2(payInfo, true)
        it.onSuccess(AlipayResult(result))
      }
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(next)
}

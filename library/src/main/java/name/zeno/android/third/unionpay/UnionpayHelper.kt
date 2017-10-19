package name.zeno.android.third.unionpay

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.annotation.StringDef
import com.orhanobut.logger.Logger
import com.unionpay.UPPayAssistEx

/**
 * <h1> 银联支付简单封装  </h1>
 * <pre>
 * `
 *
 * protected void onActivityResult(int requestCode, int resultCode, Intent data)
 * {
 * super.onActivityResult(requestCode, resultCode, data);
 * UnionpayHelper.onActivityResult(data);
 * }
 *
 *
 * public void callUnionpay()
 * {
 * String tn = "";
 * String serverMode = BuildConfig.DEBUG
 * ? UnionpayHelper.Mode.DEV
 * : UnionpayHelper.Mode.PROD;
 * UnionpayHelper.startPay(this, tn, serverMode, (ok, msg) -> {
 * if (ok) {
 * this.onPaySuccess();
 * } else {
 * this.showMessage(msg);
 * }
 * });
 * }
` *
</pre> *
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/7/11
 */
object UnionpayHelper {
  private val TAG = "UnionpayHelper"
  private var onResult: ((ok: Boolean, msg: String) -> Unit)? = null

  const val PROD = "00"
  const val DEV = "01"

  @StringDef(PROD, DEV) annotation class Mode


  fun onActivityResult(data: Intent?) {
    if (data == null) return
    val str = data.getStringExtra("pay_result")!!.toLowerCase()
    when (str) {
      "success" -> {
        onResult?.invoke(true, "支付成功")
        val resultData = data.getStringExtra("result_data")
        Logger.t(TAG).d("Unionpay Result Data:\r\n" + resultData)
        //TODO: 验签
      }
      "fail" -> onResult?.invoke(false, "支付失败！")
      "cancel" -> onResult?.invoke(false, "取消支付！")
    }
  }

  fun startPay(activity: Activity, orderInfo: String, @Mode mode: String = PROD, onResult: (ok: Boolean, msg: String) -> Unit): Int {
    return startPay(activity, null, null, orderInfo, mode, onResult)
  }

  /**
   * see [UPPayAssistEx.startPay]
   *
   * @param activity    用于启动支付控件的活动对象
   * @param spId        保留使用，这里输入null
   * @param sysProvider 保留使用，这里输入null
   * @param orderInfo   订单信息为交易流水号，即TN，为商户后台从银联后台获取。
   * @param mode        银联后台环境标识，“00”将在银联正式环境发起交易,“01”将在银联测试环境发起交易
   * @param onResult    支付结果 success:boolean,msg:String
   * @return [UPPayAssistEx.PLUGIN_VALID] or [UPPayAssistEx.PLUGIN_VALID]
   */
  private fun startPay(activity: Context, spId: String?, sysProvider: String?, orderInfo: String, @Mode mode: String, onResult: (ok: Boolean, msg: String) -> Unit): Int {
    UnionpayHelper.onResult = onResult
    return UPPayAssistEx.startPay(activity, spId, sysProvider, orderInfo, mode)
  }
}

package name.zeno.android.third.unionpay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.StringDef;

import com.orhanobut.logger.Logger;
import com.unionpay.UPPayAssistEx;

import name.zeno.android.listener.Action2;

/**
 * <h1> 银联支付简单封装  </h1>
 * <pre>
 * <code>
 *
 * protected void onActivityResult(int requestCode, int resultCode, Intent data)
 * {
 *    super.onActivityResult(requestCode, resultCode, data);
 *    UnionpayHelper.onActivityResult(data);
 * }
 *
 *
 * public void callUnionpay()
 * {
 *    String tn = "";
 *    String serverMode = BuildConfig.DEBUG
 *      ? UnionpayHelper.Mode.DEV
 *      : UnionpayHelper.Mode.PROD;
 *    UnionpayHelper.startPay(this, tn, serverMode, (ok, msg) -> {
 *      if (ok) {
 *        this.onPaySuccess();
 *      } else {
 *        this.showMessage(msg);
 *      }
 *    });
 * }
 * </code>
 * </pre>
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/7/11
 */
public class UnionpayHelper
{
  private static final String TAG = "UnionpayHelper";
  private static Action2<Boolean, String> onResult;

  @StringDef({Mode.PROD, Mode.DEV}) public @interface Mode
  {
    String PROD = "00";
    String DEV  = "01";
  }


  public static void onActivityResult(Intent data)
  {
    if (data == null) return;

    String str = data.getExtras().getString("pay_result");
    if ("success".equalsIgnoreCase(str)) {
      if (onResult != null) onResult.call(true, "支付成功");
      String resultData = data.getStringExtra("result_data");
      Logger.t(TAG).d("Unionpay Result Data:\r\n" + resultData);
      // 此处没有验签
    } else if ("fail".equalsIgnoreCase(str)) {
      if (onResult != null) onResult.call(false, "支付失败！");
    } else if ("cancel".equalsIgnoreCase(str)) {
      if (onResult != null) onResult.call(false, "取消支付！");
    }
  }

  /**
   * see {@link #startPay(Context, String, String, String, String, Action2)}
   */
  public static int startPay(Activity activity, String orderInfo, @Mode String mode, Action2<Boolean, String> onResult)
  {
    return startPay(activity, null, null, orderInfo, mode, onResult);
  }

  /**
   * see {@link UPPayAssistEx#startPay(Context, String, String, String, String)}
   *
   * @param activity    用于启动支付控件的活动对象
   * @param spId        保留使用，这里输入null
   * @param sysProvider 保留使用，这里输入null
   * @param orderInfo   订单信息为交易流水号，即TN，为商户后台从银联后台获取。
   * @param mode        银联后台环境标识，“00”将在银联正式环境发起交易,“01”将在银联测试环境发起交易
   * @param onResult    支付结果 success:boolean,msg:String
   * @return {@link UPPayAssistEx#PLUGIN_VALID} or {@link UPPayAssistEx#PLUGIN_VALID}
   */
  public static int startPay(Context activity, String spId, String sysProvider, String orderInfo, @Mode String mode, Action2<Boolean, String> onResult)
  {
    UnionpayHelper.onResult = onResult;
    return UPPayAssistEx.startPay(activity, spId, sysProvider, orderInfo, mode);
  }
}

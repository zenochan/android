package name.zeno.android.third.alipay;

import android.Manifest;
import android.app.Activity;
import android.support.annotation.RequiresPermission;

import com.alipay.sdk.app.PayTask;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import name.zeno.android.third.rxjava.RxUtils;

/**
 * Create Date: 16/6/18
 *
 * @author 陈治谋 (513500085@qq.com)
 */
public class AlipayHelper
{
  @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
  public static void pay(Activity activity, String payInfo, Consumer<AlipayResult> next)
  {
    Observable.create((ObservableOnSubscribe<AlipayResult>) subscriber -> {
      PayTask             payTask = new PayTask(activity);
      Map<String, String> r       = payTask.payV2(payInfo, true);
      subscriber.onNext(new AlipayResult(r));
    }).compose(RxUtils.applySchedulers()).subscribe(next);
  }
}

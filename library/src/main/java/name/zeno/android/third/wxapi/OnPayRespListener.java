package name.zeno.android.third.wxapi;

/**
 * 微信支付响应监听
 * <p>
 * Create Date: 16/6/8
 *
 * @author 陈治谋 (513500085@qq.com)
 */
public interface OnPayRespListener
{
  void onPayResult(WxRespWrapper respWrapper);
}

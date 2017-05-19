package name.zeno.android.third.alipay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import name.zeno.android.util.ZDate;
import name.zeno.android.util.ZLog;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/11/23.
 */
@Data
public class AlipaySignUtil
{
  private static final String TAG = "AlipaySignUtil";

  private static final String ALGORITHM       = "RSA";
  private static final String SIGN_ALGORITHMS = "SHA1WithRSA";
  private static final String DEFAULT_CHARSET = "UTF-8";

  private String     appId;
  private BizContent bizContent;
  private String     notifyUrl;

  private String charset   = "utf-8";
  private String method    = "alipay.trade.app.pay";
  private String sign_type = "RSA";

  private String version   = "1.0";
  private String timestamp = ZDate.nowString("yyyy-MM-dd HH:mm:ss");

  @Data
  public static class BizContent
  {
    private String timeout_express = "30m";
    private String product_code    = "QUICK_MSECURITY_PAY";
    @JSONField(name = "totol_amount")

    private String totolAmount;
    private String subject;
    private String body;
    @JSONField(name = "out_trade_no")
    private String outTradeNo;
  }

  public String sign(String rsaKey)
  {
    Map<String, String> map = new HashMap<>();
    map.put("app_id", appId);
    map.put("biz_content", JSON.toJSONString(bizContent));
    map.put("charset", charset);
    map.put("method", method);
    map.put("sign_type", sign_type);
    map.put("notify_url", notifyUrl);

    map.put("timestamp", timestamp);
    map.put("version", version);

    return buildOrderParam(map) + "&" + getSign(map, rsaKey);
  }

  /**
   * 构造支付订单参数信息
   *
   * @param map 支付订单参数
   */
  private static String buildOrderParam(Map<String, String> map)
  {
    List<String> keys = new ArrayList<>(map.keySet());

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < keys.size() - 1; i++) {
      String key   = keys.get(i);
      String value = map.get(key);
      sb.append(buildKeyValue(key, value, true));
      sb.append("&");
    }

    String tailKey   = keys.get(keys.size() - 1);
    String tailValue = map.get(tailKey);
    sb.append(buildKeyValue(tailKey, tailValue, true));

    return sb.toString();
  }

  /**
   * 对支付参数信息进行签名
   *
   * @param map 待签名授权信息
   */
  private static String getSign(Map<String, String> map, String rsaKey)
  {
    List<String> keys = new ArrayList<>(map.keySet());
    // key排序
    Collections.sort(keys);

    StringBuilder authInfo = new StringBuilder();
    for (int i = 0; i < keys.size() - 1; i++) {
      String key   = keys.get(i);
      String value = map.get(key);
      authInfo.append(buildKeyValue(key, value, false));
      authInfo.append("&");
    }

    String tailKey   = keys.get(keys.size() - 1);
    String tailValue = map.get(tailKey);
    authInfo.append(buildKeyValue(tailKey, tailValue, false));

    String oriSign     = sign(authInfo.toString(), rsaKey);
    String encodedSign = "";

    try {
      encodedSign = URLEncoder.encode(oriSign, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      ZLog.e(TAG, e.getMessage(), e);
    }
    return "sign=" + encodedSign;
  }


  public static String sign(String content, String privateKey)
  {
    try {
      PKCS8EncodedKeySpec priPKCS8  = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
      KeyFactory          keyf      = KeyFactory.getInstance(ALGORITHM);
      PrivateKey          priKey    = keyf.generatePrivate(priPKCS8);
      Signature           signature = Signature.getInstance(SIGN_ALGORITHMS);

      signature.initSign(priKey);
      signature.update(content.getBytes(DEFAULT_CHARSET));

      byte[] signed = signature.sign();

      return Base64.encode(signed);
    } catch (Exception e) {
      ZLog.e(TAG, e.getMessage(), e);
    }

    return null;
  }

  /**
   * 拼接键值对
   */
  private static String buildKeyValue(String key, String value, boolean isEncode)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(key);
    sb.append("=");
    if (isEncode) {
      try {
        sb.append(URLEncoder.encode(value, "UTF-8"));
      } catch (UnsupportedEncodingException e) {
        sb.append(value);
      }
    } else {
      sb.append(value);
    }
    return sb.toString();
  }

}

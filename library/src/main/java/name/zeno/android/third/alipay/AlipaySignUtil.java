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

import name.zeno.android.util.ZDate;
import name.zeno.android.util.ZLog;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/11/23.
 */
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

  public AlipaySignUtil() {}

  public String getAppId()
  {return this.appId;}

  public BizContent getBizContent()
  {return this.bizContent;}

  public String getNotifyUrl()
  {return this.notifyUrl;}

  public String getCharset()
  {return this.charset;}

  public String getMethod()
  {return this.method;}

  public String getSign_type()
  {return this.sign_type;}

  public String getVersion()
  {return this.version;}

  public String getTimestamp()
  {return this.timestamp;}

  public void setAppId(String appId)
  {this.appId = appId; }

  public void setBizContent(BizContent bizContent)
  {this.bizContent = bizContent; }

  public void setNotifyUrl(String notifyUrl)
  {this.notifyUrl = notifyUrl; }

  public void setCharset(String charset)
  {this.charset = charset; }

  public void setMethod(String method)
  {this.method = method; }

  public void setSign_type(String sign_type)
  {this.sign_type = sign_type; }

  public void setVersion(String version)
  {this.version = version; }

  public void setTimestamp(String timestamp)
  {this.timestamp = timestamp; }

  public boolean equals(Object o)
  {
    if (o == this) return true;
    if (!(o instanceof AlipaySignUtil)) return false;
    final AlipaySignUtil other = (AlipaySignUtil) o;
    if (!other.canEqual((Object) this)) return false;
    final Object this$appId  = this.getAppId();
    final Object other$appId = other.getAppId();
    if (this$appId == null ? other$appId != null : !this$appId.equals(other$appId)) return false;
    final Object this$bizContent  = this.getBizContent();
    final Object other$bizContent = other.getBizContent();
    if (this$bizContent == null ? other$bizContent != null : !this$bizContent.equals(other$bizContent))
      return false;
    final Object this$notifyUrl  = this.getNotifyUrl();
    final Object other$notifyUrl = other.getNotifyUrl();
    if (this$notifyUrl == null ? other$notifyUrl != null : !this$notifyUrl.equals(other$notifyUrl))
      return false;
    final Object this$charset  = this.getCharset();
    final Object other$charset = other.getCharset();
    if (this$charset == null ? other$charset != null : !this$charset.equals(other$charset))
      return false;
    final Object this$method  = this.getMethod();
    final Object other$method = other.getMethod();
    if (this$method == null ? other$method != null : !this$method.equals(other$method))
      return false;
    final Object this$sign_type  = this.getSign_type();
    final Object other$sign_type = other.getSign_type();
    if (this$sign_type == null ? other$sign_type != null : !this$sign_type.equals(other$sign_type))
      return false;
    final Object this$version  = this.getVersion();
    final Object other$version = other.getVersion();
    if (this$version == null ? other$version != null : !this$version.equals(other$version))
      return false;
    final Object this$timestamp  = this.getTimestamp();
    final Object other$timestamp = other.getTimestamp();
    if (this$timestamp == null ? other$timestamp != null : !this$timestamp.equals(other$timestamp))
      return false;
    return true;
  }

  public int hashCode()
  {
    final int    PRIME  = 59;
    int          result = 1;
    final Object $appId = this.getAppId();
    result = result * PRIME + ($appId == null ? 43 : $appId.hashCode());
    final Object $bizContent = this.getBizContent();
    result = result * PRIME + ($bizContent == null ? 43 : $bizContent.hashCode());
    final Object $notifyUrl = this.getNotifyUrl();
    result = result * PRIME + ($notifyUrl == null ? 43 : $notifyUrl.hashCode());
    final Object $charset = this.getCharset();
    result = result * PRIME + ($charset == null ? 43 : $charset.hashCode());
    final Object $method = this.getMethod();
    result = result * PRIME + ($method == null ? 43 : $method.hashCode());
    final Object $sign_type = this.getSign_type();
    result = result * PRIME + ($sign_type == null ? 43 : $sign_type.hashCode());
    final Object $version = this.getVersion();
    result = result * PRIME + ($version == null ? 43 : $version.hashCode());
    final Object $timestamp = this.getTimestamp();
    result = result * PRIME + ($timestamp == null ? 43 : $timestamp.hashCode());
    return result;
  }

  protected boolean canEqual(Object other)
  {return other instanceof AlipaySignUtil;}

  public String toString()
  {return "AlipaySignUtil(appId=" + this.getAppId() + ", bizContent=" + this.getBizContent() + ", notifyUrl=" + this.getNotifyUrl() + ", charset=" + this.getCharset() + ", method=" + this.getMethod() + ", sign_type=" + this.getSign_type() + ", version=" + this.getVersion() + ", timestamp=" + this.getTimestamp() + ")";}

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

    public BizContent() {}

    public String getTimeout_express()
    {return this.timeout_express;}

    public String getProduct_code()
    {return this.product_code;}

    public String getTotolAmount()
    {return this.totolAmount;}

    public String getSubject()
    {return this.subject;}

    public String getBody()
    {return this.body;}

    public String getOutTradeNo()
    {return this.outTradeNo;}

    public void setTimeout_express(String timeout_express)
    {this.timeout_express = timeout_express; }

    public void setProduct_code(String product_code)
    {this.product_code = product_code; }

    public void setTotolAmount(String totolAmount)
    {this.totolAmount = totolAmount; }

    public void setSubject(String subject)
    {this.subject = subject; }

    public void setBody(String body)
    {this.body = body; }

    public void setOutTradeNo(String outTradeNo)
    {this.outTradeNo = outTradeNo; }

    public boolean equals(Object o)
    {
      if (o == this) return true;
      if (!(o instanceof BizContent)) return false;
      final BizContent other = (BizContent) o;
      if (!other.canEqual((Object) this)) return false;
      final Object this$timeout_express  = this.getTimeout_express();
      final Object other$timeout_express = other.getTimeout_express();
      if (this$timeout_express == null ? other$timeout_express != null : !this$timeout_express.equals(other$timeout_express))
        return false;
      final Object this$product_code  = this.getProduct_code();
      final Object other$product_code = other.getProduct_code();
      if (this$product_code == null ? other$product_code != null : !this$product_code.equals(other$product_code))
        return false;
      final Object this$totolAmount  = this.getTotolAmount();
      final Object other$totolAmount = other.getTotolAmount();
      if (this$totolAmount == null ? other$totolAmount != null : !this$totolAmount.equals(other$totolAmount))
        return false;
      final Object this$subject  = this.getSubject();
      final Object other$subject = other.getSubject();
      if (this$subject == null ? other$subject != null : !this$subject.equals(other$subject))
        return false;
      final Object this$body  = this.getBody();
      final Object other$body = other.getBody();
      if (this$body == null ? other$body != null : !this$body.equals(other$body)) return false;
      final Object this$outTradeNo  = this.getOutTradeNo();
      final Object other$outTradeNo = other.getOutTradeNo();
      if (this$outTradeNo == null ? other$outTradeNo != null : !this$outTradeNo.equals(other$outTradeNo))
        return false;
      return true;
    }

    public int hashCode()
    {
      final int    PRIME            = 59;
      int          result           = 1;
      final Object $timeout_express = this.getTimeout_express();
      result = result * PRIME + ($timeout_express == null ? 43 : $timeout_express.hashCode());
      final Object $product_code = this.getProduct_code();
      result = result * PRIME + ($product_code == null ? 43 : $product_code.hashCode());
      final Object $totolAmount = this.getTotolAmount();
      result = result * PRIME + ($totolAmount == null ? 43 : $totolAmount.hashCode());
      final Object $subject = this.getSubject();
      result = result * PRIME + ($subject == null ? 43 : $subject.hashCode());
      final Object $body = this.getBody();
      result = result * PRIME + ($body == null ? 43 : $body.hashCode());
      final Object $outTradeNo = this.getOutTradeNo();
      result = result * PRIME + ($outTradeNo == null ? 43 : $outTradeNo.hashCode());
      return result;
    }

    protected boolean canEqual(Object other)
    {return other instanceof BizContent;}

    public String toString()
    {return "AlipaySignUtil.BizContent(timeout_express=" + this.getTimeout_express() + ", product_code=" + this.getProduct_code() + ", totolAmount=" + this.getTotolAmount() + ", subject=" + this.getSubject() + ", body=" + this.getBody() + ", outTradeNo=" + this.getOutTradeNo() + ")";}
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

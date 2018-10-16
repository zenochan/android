# [支付宝 App 支付](https://docs.open.alipay.com/204/105296/)

## permissions 

- android.permission.INTERNET
- android.permission.ACCESS_NETWORK_STATE
- android.permission.ACCESS_WIFI_STATE
- android.permission.READ_PHONE_STATE
- android.permission.WRITE_EXTERNAL_STORAGE


## USAGE
```groovy

maven { url 'http://maven.mjtown.cn/'}

implementation "name.zeno:zako:x.y.z"
implementation "name.zeno:alipay:0.0.1+"
```

```kotlin
class PaymentActivity : Activity{
  fun callAlipay(){
    alipay(payInfo) { alipayResult ->
      if (alipayResult.resultStatus == 9000) {
        onPaySuccess()
      } else {
        snack("[支付结果]" + alipayResult.memo)
      }
    }
  }
}
```



## proguard 
```proguard
#######################################################
#                        AliPay                       #
#######################################################
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
-keep class com.alipay.sdk.app.H5PayCallback {
    <fields>;
    <methods>;
}
-keep class com.alipay.android.phone.mrpc.core.** { *; }
-keep class com.alipay.apmobilesecuritysdk.** { *; }
-keep class com.alipay.mobile.framework.service.annotation.** { *; }
-keep class com.alipay.mobilesecuritysdk.face.** { *; }
-keep class com.alipay.tscenter.biz.rpc.** { *; }
-keep class org.json.alipay.** { *; }
-keep class com.alipay.tscenter.** { *; }
-keep class com.ta.utdid2.** { *;}
-keep class com.ut.device.** { *;}
```
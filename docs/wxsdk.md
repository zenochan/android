# 集成 Tenpay 模块


#### 添加依赖

```groovy
dependencies {
    //包含统计功能
    //compile 'com.tencent.mm.opensdk:wechat-sdk-android-with-mta:+'
    compile 'com.tencent.mm.opensdk:wechat-sdk-android-without-mta:+'
}
```


#### 添加权限
```xml

<manifest 
  xmlns:android="http://schemas.android.com/apk/res/android"
  package="com.churgo.market"
  >
  <uses-permission android:name="android.permission.INTERNET"/>
  <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
  <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
  <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
  <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
</manifest>
```

#### 添加 入口Activity

`{yourPackage}.wxapi.WXEntryActivity`
```java
public class WXEntryActivity extends AbsWxEntryActivity
{
  @Override protected String getAppId()
  {
    return "your wx app id";
  }
}
```

`{yourPackage}.wxapi.WXPayEntryActivity`
```java
public class WXEntryPayActivity extends AbsWxEntryActivity
{
  @Override protected String getAppId()
  {
    return "your wx app id";
  }
}
```

#### 定义透明主题

```xml
  <style name="AppTheme.Transparent">
    <item name="android:windowBackground">@android:color/transparent</item>
    <item name="android:windowIsTranslucent">true</item>
    <item name="android:windowAnimationStyle">@android:style/Animation.Translucent</item>
  </style>
```

#### 注册入口Activity
```xml

  <application>
    <activity
      android:name=".wxapi.WXPayEntryActivity"
      android:exported="true"
      android:launchMode="singleTask"
      android:screenOrientation="portrait"
      android:theme="@style/AppTheme.Transparent"
      />
    <activity
      android:name=".wxapi.WXEntryActivity"
      android:exported="true"
      android:launchMode="singleTask"
      android:screenOrientation="portrait"
      android:theme="@style/AppTheme.Transparent"
      />
  </application>
```

#### 支付
```java
public class PaymentActivity extends AppcompatActivity
{
  private void tenpay(TenpaySign sign)
  {
    ZPayReq req = new ZPayReq();
    req.setAppId(sign.getAppid());
    req.setNonceStr(sign.getNoncestr());
    req.setTimeStamp(sign.getTimestamp());
    req.setPackageValue(sign.get_package());
    req.setPrepayId(sign.getPrepayid());
    req.setPartnerId(sign.getPartnerid());
    req.setSign(sign.getSign());

    startActivityForResult(WXPayEntryActivity.class, req, (ok, data) -> {
      WxRespWrapper respWrapper = Extra.getData(data);
      if (respWrapper != null) {
        if (respWrapper.getErrCode() == 0) {
          onPaySuccess();
        } else {
          snack(respWrapper.getDesc());
        }
      }
    });
  }
}
```

#### 分享


```java
public class PaymentActivity extends AppcompatActivity
{
  private void tenpay(TenpaySign sign)
  {
    ZWebpageMessageReq req = new ZWebpageMessageReq();
    req.setWebpageUrl(shareUrl);
    req.setTitle(shareTitle);
    req.setDescription(title);
    req.setScene(WXScene.SESSION);
    req.setThumbImage(bmp);
    //or req.setThumbImageUrl(pic);

    startActivityForResult(WXEntryActivity.class, req, (ok, data) -> {
      WxRespWrapper respWrapper = Extra.getData(data);
      if (ok && respWrapper != null && respWrapper.success()) {
        // do something when callback
      }
    });
  }
}
```

#### proguard 配置
```
-keep class com.tencent.mm.opensdk.** { *; }
-keep class com.tencent.wxop.** { *; }
-keep class com.tencent.mm.sdk.** { *; }
```

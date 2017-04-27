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

<manifest xmlns:android="http://schemas.android.com/apk/res/android"
          package="com.churgo.market"
  >
  <uses-permission android:name="android.permission.INTERNET"/>
  <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
  <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
  <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
  <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
</manifest>
```
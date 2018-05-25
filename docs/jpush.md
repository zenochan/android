# 极光推送

> [Android SDK 集成指南](https://docs.jiguang.cn/jpush/client/Android/android_guide/)


```groovy
repositories {
  jcenter()
}

android {
  defaultConfig {
    applicationId "com.xxx.xxx" //JPush上注册的包名.

    ndk {
        //选择要添加的对应cpu类型的.so库。
        abiFilters 'armeabi', 'armeabi-v7a', 'arm64-v8a'
        // 还可以添加 'x86', 'x86_64', 'mips', 'mips64'
    }

    manifestPlaceholders = [
        JPUSH_PKGNAME : applicationId,
        JPUSH_APPKEY : "你的appkey", //JPush上注册的包名对应的appkey.
        JPUSH_CHANNEL : "developer-default", //暂时填写默认值即可.
    ]
  }
}

dependencies {
  compile 'cn.jiguang.sdk:jpush:3.1.1'  // 此处以JPush 3.1.1 版本为例。
  compile 'cn.jiguang.sdk:jcore:1.1.9'  // 此处以JCore 1.1.9 版本为例。
}
```



## Receiver
```kotlin
class JPushReceiver : AbsJPushReceiver() {
  override fun onReceivedRegistrationId(id: String) {
    Log.e("jpush_id", id)
  }

  override fun onReceivedMessage(msg: JPushMsg) {
    Log.e("jpush_msg", msg.toString())
  }
}
```

```xml
<receiver
    android:name="JPushReceiver"
    android:enabled="true">
    <intent-filter>
        <action android:name="cn.jpush.android.intent.REGISTRATION" />
        <action android:name="cn.jpush.android.intent.MESSAGE_RECEIVED" />
        <action android:name="cn.jpush.android.intent.NOTIFICATION_RECEIVED" />
        <action android:name="cn.jpush.android.intent.NOTIFICATION_OPENED" />
        <action android:name="cn.jpush.android.intent.NOTIFICATION_CLICK_ACTION" />
        <action android:name="cn.jpush.android.intent.CONNECTION" />
        <category android:name="${applicationId}"/>
    </intent-filter>
</receiver>
```



## Proguard

```properties
-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }
```

# 极光推送

> [Android SDK 集成指南](https://docs.jiguang.cn/jpush/client/Android/android_guide/)


## USAGE

- groovy 配置
```groovy
repositories {
  maven { url 'http://maven.mjtown.cn/'}
}

android {
  defaultConfig {
    applicationId "com.xxx.xxx" //JPush上注册的包名.

    ndk {
        //选择要添加的对应cpu类型的.so库。
        abiFilters 'armeabi', 'armeabi-v7a', 'arm64-v8a','x86', 'x86_64', 'mips', 'mips64'
    }

    manifestPlaceholders = [
        JPUSH_PKGNAME : applicationId,
        JPUSH_APPKEY : "你的appkey", //JPush上注册的包名对应的appkey.
        JPUSH_CHANNEL : "developer-default", //暂时填写默认值即可.
    ]
  }
}

dependencies {
  implementation 'name.zeno:jiguang:0.0.1.1805251'
}
```

- 定义 receiver
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

- 注册 receiver
```xml
<receiver
  android:name=".JPushReceiver"
  android:enabled="true"
  >
  <intent-filter>
    <action android:name="cn.jpush.android.intent.REGISTRATION"/>
    <action android:name="cn.jpush.android.intent.MESSAGE_RECEIVED"/>
    <action android:name="cn.jpush.android.intent.NOTIFICATION_RECEIVED" />
    <action android:name="cn.jpush.android.intent.NOTIFICATION_OPENED" />
    <action android:name="cn.jpush.android.intent.NOTIFICATION_CLICK_ACTION" />
    <action android:name="cn.jpush.android.intent.CONNECTION" />

    <category android:name="${applicationId}"/>
  </intent-filter>
</receiver>
```

## API

#### 基础
- fun Application.jPushInit(debug: Boolean? = null)
- var Context.jPushStop
- val Context.jPushId

#### 别名
- fun Context.jPushSetAlias(alias: String): Observable<String>
- fun Context.jPushGetAlias(): Observable<String>
- fun Context.jPushDeleteAlias(): Observable<String>

#### TAG
- fun Context.jPushSetTags(vararg tags: String): Observable<Set<String>>
- fun Context.jPushAddTags(vararg tags: String): Observable<Set<String>>
- fun Context.jPushDeleteTags(vararg tags: String): Observable<Set<String>>
- fun Context.jPushCleanTags(): Observable<Set<String>>
- fun Context.jPushGetAllTags(): Observable<Set<String>>
- fun Context.jPushCheckTagBindState(tag: String): Observable<Boolean>

#### 手机号，用于短信补充
- fun Context.jPushSetMobile(mobile: String?): Observable<String>


## Proguard

```proguard
-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }
```

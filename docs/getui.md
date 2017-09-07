# [个推](http://docs.getui.com/mobile/android/androidstudio_maven/)
 
#### 配置 `build.gradle`
```groovy
repositories{
  //...
  maven { url "http://mvn.gt.igexin.com/nexus/content/repositories/releases/" }
}

android{
  defaultConfig {
    manifestPlaceholders = [
        GETUI_APP_ID     : properties['getui.appid'],
        GETUI_APP_KEY    : properties['getui.appkey'],
        GETUI_APP_SECRET : properties['getui.appsecret'],
    ]
  }
}

dependencies{
  //...
  compile 'com.getui:sdk:2.11.1.0'
}
```

#### 配置 项目根目录下的`gradle.properties`
```properties
# 个推
android.useDeprecatedNdk=true
```

#### 定义 service
```java
public class GetuiService extends ZGetuiService {}
```

```java
public class GetuiMessageService extends ZGetuiMessageService{

  @Override
  public final void onReceiveMessageData(Context context, GetuiMessage message)
  {
    // 处理透传消息
  }
}
```


```java
public class ZenoApp extends ZApplication {
  @Override
  public void onCreate() {
    super.onCreate();
    initGetui(GetuiService.class,GetuiMessageService.class);
  }
}
```

#### 配置 `AndroidManifest.xml`
```xml
<manifest>
  <!-- 个推可选配置-->
  <!-- iBeancon功能所需权限 -->;
  <uses-permission android:name="android.permission.BLUETOOTH"/>
  <uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>
  <!-- 个推3.0电子围栏功能所需权限 -->
  <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
  <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />

  <application>
    <service
      android:name=".GetuiService"
      android:exported="true"
      android:label="PushService"
      android:process=":pushservice">
    </service>
    <service android:name=".GetuiMessageService"/>
  </application>
</manifest>
```

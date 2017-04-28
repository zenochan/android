# [个推](http://docs.getui.com/mobile/android/androidstudio_maven/)
 
#### 添加依赖
```groovy
  compile 'com.getui:sdk:2.10.2.1'
```

#### 定义 service
```java
public class ZenoGetuiService extends GetuiService {}
```

```xml
<service
  android:name=".ZenoGetuiService"
  android:exported="true"
  android:label="PushService"
  android:process=":pushservice">
</service>
```

```java
public class ZenoApp extends ZApplication {
  @Override
  public void onCreate() {
    super.onCreate();
    initGetui(ChurgoGetuiService.class);
  }
}
```

#### 定义 BroadCastReceiver
```java
public class ChurgoGetuiReceiver extends AbsGetuiReceiver
{
  @Override protected void onGetMsgData(Context context, String messageId, String msgData)
  {
    super.onGetMsgData(context, messageId, msgData);
    // TODO: 处理透传消息
  }
}
```

```xml
    <receiver
      android:name=".receiver.ChurgoGetuiReceiver"
      android:exported="false"
      >
      <intent-filter>
        <action android:name="com.igexin.sdk.action.${GETUI_APP_ID}"/>
      </intent-filter>
    </receiver>
```

### build.gradle

```groovy
    manifestPlaceholders = [
        GETUI_APP_ID     : properties['getui.appid'],
        GETUI_APP_KEY    : properties['getui.appkey'],
        GETUI_APP_SECRET : properties['getui.appsecret'],
    ]
```

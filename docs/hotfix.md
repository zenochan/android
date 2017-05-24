# [HOTFIX 2.0](https://baichuan.taobao.com/docs/doc.htm?spm=a3c0d.7629140.0.0.GeBPXk&treeId=234&articleId=106531&docType=1)

`version`: `2.0.9`

#### Install

```groovy
repositories {
   maven { url "http://repo.baichuan-android.taobao.com/content/groups/BaichuanRepositories" }
}

dependencies {
    compile 'com.taobao.android:alisdk-hotfix:2.0.9'
}
```

#### Permission

```xml
<manifest>
  <!-- 网络权限 -->
  <uses-permission android:name="android.permission.INTERNET" />
  <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
  <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
  <!-- 外部存储读权限 -->
  <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
</manifest>
```

#### 注册HotFix

```xml
<application>
  <!-- hotfix 热更新-->
  <meta-data
    android:name="com.taobao.android.hotfix.IDSECRET"
    android:value="${HOTFIX_APP_ID}"
    />
  <meta-data
    android:name="com.taobao.android.hotfix.APPSECRET"
    android:value="${HOTFIX_APP_SECRET}"
    />
  <meta-data
    android:name="com.taobao.android.hotfix.RSASECRET"
    android:value="${HOTFIX_RSA_SECRET}"
    />
</application>
```

```groovy
 manifestPlaceholders = [
     HOTFIX_APP_ID    : properties['hotfix.appid'],
     HOTFIX_APP_SECRET: properties['hotfix.appsecret'],
     HOTFIX_RSA_SECRET: properties['hotfix.rsasecret'],
 ]
```

#### 配置混淆

```
#基线包使用，生成mapping.txt
-printmapping mapping.txt
#生成的mapping.txt在app/build/outputs/mapping/release路径下，移动到/app路径下
 
#修复后的项目使用，保证混淆结果一致
#-applymapping mapping.txt
 
#hotfix
-keep class com.taobao.sophix.**{*;}
-keep class com.ta.utdid2.device.**{*;}
```

#### 使用

```java
public class MyApplication extends ZApplication
{
  @Override
  public void onCreate()
  {
    super.onCreate();
    initHotFix(BuildConfig.VERSION_NAME);
  }
}
```

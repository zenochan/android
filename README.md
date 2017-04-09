# Zeno Android Dep Kit
> 个人用的 Android 开发包

##  功能模块

#### 日志打印: [logger](https://github.com/orhanobut/logger)
#### 图片加载: [glide](https://github.com/bumptech/glide)

- [glide-transformations](https://github.com/wasabeef/glide-transformations)

#### Json 解析: [FastJson](https://github.com/alibaba/fastjson)
#### View 绑定: [butterknife](https://github.com/JakeWharton/butterknife)
#### 网络请求: [Retrofit](http://square.github.io/retrofit/) + RxAndroid + RxJava2Adapter + FastJsonConverter
#### [Lombok](https://projectlombok.org/)
#### 事件: [otto](https://github.com/square/otto)
#### 动态权限: RxPermission
#### 消息推送: [个推](http://docs.getui.com/mobile/android/androidstudio_maven/)

- 添加依赖
```groovy
  compile 'com.getui:sdk:2.10.2.1'
```

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


#### 分析统计: U 盟+
#### 热修复: [HotFix]

#### [BottomBar](https://github.com/roughike/BottomBar)
#### dialog: [material-dialogs](https://github.com/afollestad/material-dialogs)
#### [Material Datetime Picker](https://github.com/wdullaer/MaterialDateTimePicker)

## Update

#### 1.0.1

- 升级 `rxjava adapter` 不向下兼容,包名改了

#### [Glide 扩展](./docs/glide.md)


## TODO

- [数据库框架研究](https://www.zhihu.com/question/46449188?sort=created)

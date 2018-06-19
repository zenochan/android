# Zeno Android Dep Kit
> 个人用的 Android 开发包


## Usage

```groovy
repositories{
  maven {url "http://maven.mjtown.cn/"}
}
dependencies{
  // compile "name.zeno:android:1.0.6"
  implementation  "name.zeno:zako:2.0.1806190"
}
```

## [zkt](./docs/zkt.md)

##  Modules

- [微信 open sdk](./docs/wxsdk.md)
- [支付宝 App 支付](./docs/alipay.md)
- [百度地图](./docs/baidumap.md)

#### 其他

- [日志打印 - logger](https://github.com/orhanobut/logger)
- [图片加载 - Glide](./docs/glide.md)
    - [glide-transformations](https://github.com/wasabeef/glide-transformations)

- Json 解析: [FastJson](https://github.com/alibaba/fastjson)
- 网络请求: [Retrofit](http://square.github.io/retrofit/) + RxAndroid + RxJava2Adapter + FastJsonConverter
- [事件 - RxBus](https://github.com/AndroidKnife/RxBus/tree/2.x)
- [动态权限 - KtRxPermission](https://github.com/zenochan/KtRxPermission)
- [消息推送(个推)](./docs/getui.md)

- 分析统计: U 盟+

- [BottomBar](https://github.com/roughike/BottomBar)
- dialog: [material-dialogs](https://github.com/afollestad/material-dialogs)
- [Material Datetime Picker](https://github.com/wdullaer/MaterialDateTimePicker)
- [异常上报 - ZExceptionHandler](library/src/main/java/name/zeno/android/app/ZExceptionHandler.kt)

## data binding

```groovy
android {
 dataBinding { enabled true }
}

dependencies {
  kapt "com.android.databinding:compiler:3.1.2"

}


```


## TODO
- [ ] [数据库框架研究](https://www.zhihu.com/question/46449188?sort=created)

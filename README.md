# Zeno Android Dep Kit
> 个人用的 Android 开发包


## Usage

```groovy
repositories{
    maven {url "http://maven.mjtown.cn/"}
}

android {
    // 开启数据绑定
    dataBinding { enabled true }
}

dependencies{
    implementation  "name.zeno:zako:2.0.1806190"
}
```

gradle.properties
```properties
# 使用 androidx 支持库
android.useAndroidX=true
android.enableJetifier=true
```



## FEATURES
- [x] kotlin扩展 [zkt](./docs/zkt.md)
- [x] Json解析 [FastJson](https://github.com/alibaba/fastjson)
- [x] 网络请求 [Retrofit](http://square.github.io/retrofit/) + RxAndroid + RxJava2Adapter + FastJsonConverter
- [x] 日志打印 [logger](https://github.com/orhanobut/logger)
- [x] 事件驱动 [RxBus](https://github.com/AndroidKnife/RxBus/tree/2.x)
- [x] 动态权限 [KtRxPermission](https://github.com/zenochan/KtRxPermission)
- [x] 日期选择 [Material Datetime Picker](https://github.com/wdullaer/MaterialDateTimePicker)
- [x] 异常上报 [ZExceptionHandler](library/src/main/java/name/zeno/android/app/ZExceptionHandler.kt)
- [x] 导航菜单 [BottomBar](https://github.com/roughike/BottomBar)
- [x] 路由导航 [arouterx](./docs/arouterx.md)
- [x] dialog [material-dialogs](https://github.com/afollestad/material-dialogs)
- [ ] androidx.fragment.app.Fragment

##  Modules

- [微信 open sdk](./docs/wxsdk.md)
- [支付宝 App 支付](./docs/alipay.md)
- [百度地图](./docs/baidumap.md)


## 图片处理
- [图片加载 - Glide](./docs/glide.md)
    - [glide-transformations](https://github.com/wasabeef/glide-transformations)
- 图片裁剪
    - ZFragment.cropByNative 使用系统自带工具
    - ZActivity.cropByNative 使用系统自带工具


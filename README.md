# Zeno Android Dep Kit
> 个人用的 Android 开发包


## Usage

```groovy
repositories{
  maven {url "http://maven.mjtown.cn/"}
}
dependencies{
  compile "name.zeno:android:1.0.6"
}
```

## [zkt](./docs/zkt.md)

##  Modules

- [微信 open sdk](./docs/wxsdk.md)
- [HotFix](./docs/hotfix.md)
- [百度地图](./docs/baidumap.md)

#### 其他

- 日志打印: [logger](https://github.com/orhanobut/logger)
- [图片加载 - Glide](./docs/glide.md)
    - [glide-transformations](https://github.com/wasabeef/glide-transformations)

- Json 解析: [FastJson](https://github.com/alibaba/fastjson)
- View 绑定: [butterknife](https://github.com/JakeWharton/butterknife)
- 网络请求: [Retrofit](http://square.github.io/retrofit/) + RxAndroid + RxJava2Adapter + FastJsonConverter
- [Lombok](https://projectlombok.org/)
- 事件: [otto](https://github.com/square/otto)
- 动态权限: RxPermission
- [消息推送(个推)](./docs/getui.md)

- 分析统计: U 盟+

- [BottomBar](https://github.com/roughike/BottomBar)
- dialog: [material-dialogs](https://github.com/afollestad/material-dialogs)
- [Material Datetime Picker](https://github.com/wdullaer/MaterialDateTimePicker)


## TODO

- [ ] 添加 kotlin 后必须依赖 Provider 依赖才能使用 lib，待优化
- [ ] Otto 替换为 RxBus - Otto 官方推荐
- [ ] [数据库框架研究](https://www.zhihu.com/question/46449188?sort=created)

## CHANGE LOG

#### 1.0.6

- libs update

|lib        |new version    |old version|
|-----------|---------------|-----------|
|glide      |4.0.0          |3.7.0      |

#### 1.0.8

 - FIX BUG: SearchPoiActivity NPE on destroy

#### 1.0.7

 - 重新整理了支付宝支付、百度地图依赖
 - 简单分装了银联支付 `UnionpayHelper`
 - 优化升级提示

#### 1.0.6

- libs update

|lib        |new version    |old version|
|-----------|---------------|-----------|
|logger     |2.1.1          |1.15       |
|fastjson   |1.1.59.android |1.1.56.android|
|butterknife|8.6.0          |8.5.1      |
|retrofit   |2.3.0          |2.2.9      |
|rxjava     |2.1.0          |2.0.7      |

#### 1.0.5

- `U` `HotFix` 升级至 `2.0.9`

#### 1.0.4

- `F` BasePresenter#removeDisposable 异常 <font color='red'>"Disposable item is null"</font>
- `M` 调整 simpleActionBar 的返回按钮
- `F` Poi 搜索定位失败时无反馈

#### 1.0.3

- ZDrawable
    - resizeComponentDrawable: 调整 ComponentDrawable 的大小
    - tintComponentDrawable:   对 ComponentDrawable 着色

#### 1.0.2
- CommonAdapter 重构，提取为单独的 lib， 方便跟进原作者的更新
- 跟新微信 sdk
- [Glide 修复压缩过度白色变绿色现象](http://blog.mjtown.cn/blogs/104)

#### 1.0.1
- 升级 `rxjava adapter` 不向下兼容,包名改了

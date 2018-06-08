# [百度地图](http://lbsyun.baidu.com/index.php?title=androidsdk/guide/buildproject)

`compile 'com.baidu.mapapi:LocPoiLBS:4.3.1'`

```groovy
android {
  defaultConfig {
    manifestPlaceHolders = [
      BAIDU_AK : "你的百度 api key"
    ]
  }
}

dependencies {
  implementation "com.baidu:location-base:7.5.0"
  implementation "name.zeno.ext:baidu-map:7.5.0:0.0.2.+"
}
```

#### proguard
```proguard
-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}    
-dontwarn com.baidu.**
```

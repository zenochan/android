# [百度地图](http://lbsyun.baidu.com/index.php?title=androidsdk/guide/buildproject)

`compile 'com.baidu.mapapi:LocPoiLBS:4.3.1'`


#### 混淆配置
```proguard
-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}    
-dontwarn com.baidu.**
```

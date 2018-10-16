
# ArouterX
> 对 [ARouter](https://github.com/alibaba/ARouter) 进行适配封装

```groovy

// aRouter
kapt {
  arguments {
    arg("moduleName", project.getName())
  }
}

dependencies {
    //...
    implementation 'name.zeno:arouterx:1.3.1.1807261'
    kapt 'com.alibaba:arouter-compiler:1.2.0'
}

```


## proguard
```proguard
-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}

# 如果使用了 byType 的方式获取 Service，需添加下面规则，保护接口
-keep interface * implements com.alibaba.android.arouter.facade.template.IProvider

# 如果使用了 单类注入，即不定义接口实现 IProvider，需添加下面规则，保护实现
-keep class * implements com.alibaba.android.arouter.facade.template.IProvider
```
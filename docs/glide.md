# [Glide](https://github.com/bumptech/glide)


##  Glide 4.0

- proguard

```proguard
###############################################
#                Glide 4.0                    #
###############################################
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# for DexGuard only
-keepresourcexmlelements manifest/application/meta-data@value=GlideModule
```


- [Glide 修复压缩过度白色变绿色现象](http://blog.mjtown.cn/blogs/104)

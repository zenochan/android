package name.zeno.android.third.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.InputStream;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import name.zeno.android.third.rxjava.RxUtils;


/**
 * <h1>API LIST</h1>
 * <li>{@link #getDiskCacheSize(Context)} 获取磁盘缓存大小</li>
 * <li>{@link #clearCache(Context)} 清除内存&磁盘缓存</li>
 */
@GlideModule
public class ZGlide extends AppGlideModule
{
  @Override public void applyOptions(Context context, GlideBuilder builder)
  {
    // Glide使用bitmap的编码为RGB565，所以有时的时候由于过度压缩导致了图片变绿。
    // 所以要改变一下Glide的bitmap编码。
    RequestOptions options = new RequestOptions();
    options.format(DecodeFormat.PREFER_ARGB_8888);
    builder.setDefaultRequestOptions(options);
  }

  @Override public void registerComponents(Context context, Glide glide, Registry registry)
  {
    super.registerComponents(context, glide, registry);
    //配置glide网络加载框架
    registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
  }

  @Override public boolean isManifestParsingEnabled()
  {
    //不使用清单配置的方式,减少初始化时间
    return false;
  }


  /**
   * Glide 磁盘缓存大小
   */
  public static String getDiskCacheSize(Context context)
  {
    File file = GlideApp.getPhotoCacheDir(context);
    if (file != null) {
      return formatMemorySize(getFolderSize(file));
    } else {
      return "0 Byte";
    }
  }

  /**
   * 异步清除内存&磁盘缓存
   */
  public static Observable<Boolean> clearCache(Context context)
  {
    final Glide glide = GlideApp.get(context);
    // 清除内存缓存必须在UI线程
    glide.clearMemory();
    return Observable.create((ObservableOnSubscribe<Boolean>) subscriber -> {
      // 清除磁盘缓存必须在UI线程
      glide.clearDiskCache();
      subscriber.onNext(true);
      subscriber.onComplete();
    }).compose(RxUtils.applySchedulers());
  }


  private static long getFolderSize(File directory)
  {
    long size = 0L;
    try {
      File[] fileList = directory.listFiles();
      for (File file : fileList) {
        if (file.isDirectory()) {
          size += getFolderSize(file);
        } else {
          size += file.length();
        }
      }
    } catch (Exception ignore) {
    }

    return size;
  }

  /**
   * 格式化单位
   */
  private static String formatMemorySize(long bytes)

  {
    String[] unit  = {"Byte", "KB", "MB", "GB", "TB"};
    double   value = (double) bytes;
    byte     level = 0;
    while (level < unit.length && value > 1024) {
      level++;
      value /= 1024;
    }

    return String.format(Locale.CHINA, "%.02f %s", value, unit[level]);
  }
}

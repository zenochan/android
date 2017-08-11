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

import java.io.InputStream;

@GlideModule
public class ZenoGlideModule extends AppGlideModule
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
}

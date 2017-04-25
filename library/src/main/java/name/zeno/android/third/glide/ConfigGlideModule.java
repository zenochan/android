package name.zeno.android.third.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.GlideModule;

/**
 * 增加图片清晰度
 * <p>
 * Glide使用bitmap的编码为RGB565，所以有时的时候由于过度压缩导致了图片变绿。
 * 所以要改变一下Glide的bitmap编码。
 *
 * @author 陈治谋 (微信: puppet2436)
 * @since 2017/4/20
 */
public class ConfigGlideModule implements GlideModule
{
  @Override public void applyOptions(Context context, GlideBuilder builder)
  {
    builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
  }

  @Override public void registerComponents(Context context, Glide glide)
  {

  }
}

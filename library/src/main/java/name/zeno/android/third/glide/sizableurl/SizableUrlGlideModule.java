package name.zeno.android.third.glide.sizableurl;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.LibraryGlideModule;

import java.io.InputStream;

@GlideModule
public class SizableUrlGlideModule extends LibraryGlideModule
{
  @Override public void registerComponents(Context context, Glide glide, Registry registry)
  {
    registry.append(SizableUrlModel.class, InputStream.class, new SizableUrlModelFactory());
  }
}

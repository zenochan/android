package name.zeno.android.third.glide.sizableurl;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelCache;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;

import java.io.InputStream;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/3/10.
 */
public class SizableUrlModelFactory implements ModelLoaderFactory<SizableUrlModel, InputStream>
{
  @Override
  public ModelLoader<SizableUrlModel, InputStream> build(MultiModelLoaderFactory multiFactory)
  {
    ModelLoader<GlideUrl, InputStream> concreteLoader = multiFactory.build(GlideUrl.class, InputStream.class);

    return new SizableUrlLoader(concreteLoader, new ModelCache<>());
  }

  @Override public void teardown() { }
}

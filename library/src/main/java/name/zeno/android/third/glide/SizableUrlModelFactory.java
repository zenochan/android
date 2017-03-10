package name.zeno.android.third.glide;

import android.content.Context;

import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;

import java.io.InputStream;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/3/10.
 */
public class SizableUrlModelFactory implements ModelLoaderFactory<SizableUrlModel, InputStream>
{
  @Override
  public ModelLoader<SizableUrlModel, InputStream> build(Context context, GenericLoaderFactory factories)
  {
    return new SizableUrlLoader(context);
  }

  @Override
  public void teardown()
  {

  }
}

package name.zeno.android.third.glide.sizableurl;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelCache;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader;
import com.orhanobut.logger.Logger;

import java.io.InputStream;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/3/10.
 */
public class SizableUrlLoader extends BaseGlideUrlLoader<SizableUrlModel>
{
  public SizableUrlLoader(@NonNull ModelLoader<GlideUrl, InputStream> concreteLoader)
  {
    super(concreteLoader);
  }

  public SizableUrlLoader(@NonNull ModelLoader<GlideUrl, InputStream> concreteLoader, @Nullable ModelCache<SizableUrlModel, GlideUrl> modelCache)
  {
    super(concreteLoader, modelCache);
  }

  @Override
  protected String getUrl(SizableUrlModel sizableUrlModel, int width, int height, Options options)
  {
    Logger.i(width + " , " + height);
    return sizableUrlModel.requestCustomSizeUrl(width, height);
  }

  @Override public boolean handles(SizableUrlModel sizableUrlModel)
  {
    return true;
  }
}

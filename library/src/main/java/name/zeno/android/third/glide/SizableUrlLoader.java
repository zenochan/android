package name.zeno.android.third.glide;

import android.content.Context;

import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/3/10.
 */
public class SizableUrlLoader extends BaseGlideUrlLoader<SizableUrlModel>
{
  public SizableUrlLoader(Context context)
  {
    super(context);
  }

  @Override
  protected String getUrl(SizableUrlModel model, int width, int height)
  {
    return model.requestCustomSizeUrl(width, height);
  }
}

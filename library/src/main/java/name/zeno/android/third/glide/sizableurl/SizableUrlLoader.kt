package name.zeno.android.third.glide.sizableurl

import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelCache
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader

import java.io.InputStream

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/3/10.
 */
class SizableUrlLoader : BaseGlideUrlLoader<SizableUrlModel> {
  constructor(concreteLoader: ModelLoader<GlideUrl, InputStream>) : super(concreteLoader)
  constructor(concreteLoader: ModelLoader<GlideUrl, InputStream>, modelCache: ModelCache<SizableUrlModel, GlideUrl>?) : super(concreteLoader, modelCache)

  override fun getUrl(sizableUrlModel: SizableUrlModel, width: Int, height: Int, options: Options): String? =
      sizableUrlModel.requestCustomSizeUrl(width, height)

  override fun handles(sizableUrlModel: SizableUrlModel): Boolean = true
}

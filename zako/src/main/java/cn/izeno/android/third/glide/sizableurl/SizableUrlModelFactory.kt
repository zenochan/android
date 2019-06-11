package cn.izeno.android.third.glide.sizableurl

import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelCache
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory

import java.io.InputStream

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/3/10.
 */
class SizableUrlModelFactory : ModelLoaderFactory<SizableUrlModel, InputStream> {
  override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<SizableUrlModel, InputStream> {
    val concreteLoader = multiFactory.build(GlideUrl::class.java, InputStream::class.java)
    return SizableUrlLoader(concreteLoader, ModelCache())
  }

  override fun teardown() {}
}

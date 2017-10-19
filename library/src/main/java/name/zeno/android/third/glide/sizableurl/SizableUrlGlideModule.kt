package name.zeno.android.third.glide.sizableurl

import android.content.Context

import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.LibraryGlideModule

import java.io.InputStream

@GlideModule
class SizableUrlGlideModule : LibraryGlideModule() {
  override fun registerComponents(context: Context?, glide: Glide?, registry: Registry) {
    registry.append(SizableUrlModel::class.java, InputStream::class.java, SizableUrlModelFactory())
  }
}

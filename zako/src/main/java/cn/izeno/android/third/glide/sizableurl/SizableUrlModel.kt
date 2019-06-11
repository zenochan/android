package cn.izeno.android.third.glide.sizableurl

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/3/10.
 */
abstract class SizableUrlModel {
  protected var baseUrl: String? = null

  internal abstract fun requestCustomSizeUrl(width: Int, height: Int): String?

  override fun equals(other: Any?): Boolean {
    return other === this
        || other is SizableUrlModel && baseUrl == other.baseUrl
  }

  override fun hashCode(): Int {
    return "$baseUrl".hashCode()
  }
}

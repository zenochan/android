package name.zeno.android.third.glide.sizableurl

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/8/29
 */
class ZQiniuUrl : SizableUrlModel() {
  override fun requestCustomSizeUrl(width: Int, height: Int): String? = when (baseUrl) {
    null -> null
    else -> "$baseUrl?imageView2/2/w/$width/h/$height"
  }
}

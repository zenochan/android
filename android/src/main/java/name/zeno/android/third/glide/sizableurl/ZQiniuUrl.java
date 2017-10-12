package name.zeno.android.third.glide.sizableurl;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/8/29
 */
public class ZQiniuUrl extends SizableUrlModel
{
  @Override public String requestCustomSizeUrl(int width, int height)
  {
    if (baseUrl == null) {
      return null;
    }
    return baseUrl + "?imageView2/2/w/" + width + "/h/" + height;
  }
}

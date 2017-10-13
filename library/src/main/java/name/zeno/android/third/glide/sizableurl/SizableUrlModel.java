package name.zeno.android.third.glide.sizableurl;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/3/10.
 */
public abstract class SizableUrlModel
{
  protected String baseUrl;

  abstract String requestCustomSizeUrl(int width, int height);

  @Override public boolean equals(Object obj)
  {
    if (obj instanceof ZQiniuUrl) {
      ZQiniuUrl other = (ZQiniuUrl) obj;
      return (baseUrl + "").equals(other.baseUrl);
    }
    return false;
  }

  @Override public int hashCode()
  {
    return (baseUrl + "").hashCode();
  }
}

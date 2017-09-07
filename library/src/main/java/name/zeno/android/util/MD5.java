package name.zeno.android.util;

@SuppressWarnings("unused")
public class MD5
{
  /**
   * @deprecated use {@link Encode#md5(String)}
   */
  public static String md5(String string)
  {
    return Encode.md5(string);
  }

  /**
   * @deprecated use {@link Encode#md5(byte[])}
   */
  public static String md5(byte[] buffer)
  {
    return Encode.md5(buffer);
  }
}

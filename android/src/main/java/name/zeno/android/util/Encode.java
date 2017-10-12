package name.zeno.android.util;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <h1> 编码解码工具类 </h1>
 * <p>
 * <li>{@link #UTF8}</li>
 * <li>{@link #md5(String)}</li>
 * <li>{@link #md5(byte[])}</li>
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 16/7/11
 */
public class Encode
{
  public static final  String UTF8          = "UTF-8";
  private static final String TAG           = "Encode";
  private static final String ALGORITHM_MD5 = "MD5";

  /**
   * 将字符串转成MD5值
   */
  public static String md5(String string)
  {
    String result = null;

    if (string != null) {
      byte[] hash;
      try {
        hash = MessageDigest.getInstance(ALGORITHM_MD5).digest(string.getBytes("UTF-8"));
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
          if ((b & 0xFF) < 0x10)
            hex.append("0");
          hex.append(Integer.toHexString(b & 0xFF));
        }
        result = hex.toString();
      } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
        Log.e(TAG, "error", e);
      }
    }

    return result;
  }


  public static String md5(byte[] buffer)
  {
    String result = null;

    char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    try {
      MessageDigest mdTemp = MessageDigest.getInstance(ALGORITHM_MD5);
      mdTemp.update(buffer);
      byte[] md    = mdTemp.digest();
      int    j     = md.length;
      char   str[] = new char[j * 2];
      int    k     = 0;
      for (byte byte0 : md) {
        str[k++] = hexDigits[byte0 >>> 4 & 0xf];
        str[k++] = hexDigits[byte0 & 0xf];
      }

      result = new String(str);
    } catch (Exception e) {
      Log.e(TAG, "", e);
    }

    return result;
  }

  public static String base64AndUriEncode(String content)
  {
    String encodedString = new String(Base64.encode(content.getBytes(), Base64.DEFAULT));
    encodedString = URLEncoder.encode(encodedString);
    return encodedString;
  }

  public static String base64AndUriEncode(long content)
  {
    return base64AndUriEncode("" + content);
  }


  public static String debase64AndUriDecode(String content)
  {
    String encodedString = URLDecoder.decode(content);
    new String(Base64.decode(encodedString.getBytes(), Base64.DEFAULT));
    return encodedString;
  }

}

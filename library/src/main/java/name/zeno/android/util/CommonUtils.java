package name.zeno.android.util;

import android.support.annotation.ColorInt;

import java.util.Random;

/**
 * Create Date: 16/5/30
 *
 * @author 陈治谋 (513500085@qq.com)
 */
@SuppressWarnings("unused")
public class CommonUtils
{
  @ColorInt
  public static int randomColor()
  {
    int    color;
    int    r, g, b;
    Random random = new Random();
    r = random.nextInt(255);
    g = random.nextInt(255);
    b = random.nextInt(255);

    color = 0xff000000 | r << 16 & 0xFF0000 | g << 8 & 0xFF00 | b & 0xFF;
    return color;
  }

  @ColorInt
  public static int randomAlphaColor()
  {
    int    color;
    int    a, r, g, b;
    Random random = new Random();
    a = random.nextInt(255);
    r = random.nextInt(255);
    g = random.nextInt(255);
    b = random.nextInt(255);

    color = a << 24 & 0xff000000 | r << 16 & 0xFF0000 | g << 8 & 0xFF00 | b & 0xFF;
    return color;
  }
}

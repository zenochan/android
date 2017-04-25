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


  @ColorInt
  public static int color(int alpha, int red, int green, int blue)
  {
    return alpha << 24 & 0xff000000 | red << 16 & 0xFF0000 | green << 8 & 0xFF00 | blue & 0xFF;
  }


  @ColorInt
  public static int colorAcceptor(@ColorInt int start, @ColorInt int end, float t)
  {
    int startA = (start & 0xff000000) >> 24;
    int startR = (start & 0x00ff0000) >> 16;
    int startG = (start & 0x0000ff00) >> 8;
    int startB = (start & 0x00ff0000);

    int endA = (end & 0xff000000) >> 24;
    int endR = (end & 0x00ff0000) >> 16;
    int endG = (end & 0x0000ff00) >> 8;
    int endB = (end & 0x00ff0000);

    int a = (int) (startA + (endA - startA) * t);
    int r = (int) (startR + (endR - startR) * t + 1);
    int g = (int) (startG + (endA - startG) * t + 1);
    int b = (int) (startB + (endA - startB) * t + 1);


    return a << 24 & 0xff000000 | r << 16 & 0xFF0000 | g << 8 & 0xFF00 | b & 0xFF;
  }
}

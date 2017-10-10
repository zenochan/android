package name.zeno.android.util;

import android.content.res.Resources;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.ViewGroup;

/**
 * 尺寸工具类
 *
 * @author 陈治谋 (chenzhimou@tele-sing.com)
 * @version 2015-10-19 13:59:56
 */
@SuppressWarnings("unused")
public final class ZDimen
{
  public static final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
  public static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;

  private ZDimen() {}

  public static int dp2px(float dp)
  {
//    return (int) (dipValue * displayMetrics().density + 0.5f);
    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics());
  }

  public static int sp2px(float spValue)
  {
    //return (int) (spValue * displayMetrics().scaledDensity + 0.5f);
    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, displayMetrics());
  }

  public static int px2dp(float pxValue)
  {
    return (int) (pxValue / displayMetrics().density + 0.5f);
  }

  public static int px2sp(float pxValue)
  {
    return (int) (pxValue / displayMetrics().scaledDensity + 0.5f);
  }

  /**
   * 获取屏幕像素大小
   *
   * @return Point(widthPixels, heightPixels)
   */
  public static Point getWindowPixelsSize()
  {
    DisplayMetrics dm = displayMetrics();
    return new Point(dm.widthPixels, dm.heightPixels);
  }

  public static DisplayMetrics displayMetrics()
  {
    return Resources.getSystem().getDisplayMetrics();
  }
}

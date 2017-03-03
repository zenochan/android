package name.zeno.android.util;

import android.graphics.Point;
import android.view.View;

/**
 * Create Date: 16/6/19
 *
 * @author 陈治谋 (513500085@qq.com)
 */
public class MeasureUtils
{
  public static Point as4Rate3(int widthMeasureSpec, int heightMeasureSpec)
  {
    int wm = View.MeasureSpec.getMode(widthMeasureSpec);
    int hm = View.MeasureSpec.getMode(heightMeasureSpec);
    int w, h;
    switch (wm) {
      case View.MeasureSpec.AT_MOST:
      case View.MeasureSpec.EXACTLY:
        w = View.MeasureSpec.getSize(widthMeasureSpec);
        break;
      case View.MeasureSpec.UNSPECIFIED:
      default:
        w = ZDimen.getWindowPixelsSize().x;
        break;
    }

    switch (hm) {
      case View.MeasureSpec.AT_MOST:
      case View.MeasureSpec.EXACTLY:
        h = View.MeasureSpec.getSize(heightMeasureSpec);
        break;
      case View.MeasureSpec.UNSPECIFIED:
      default:
        h = ZDimen.getWindowPixelsSize().y;
        break;
    }
    if (w * 3 / 4 > h) {
      w = h * 4 / 3;
    }
    if (h > w * 3 / 4) {
      h = w * 3 / 4;
    }

    return new Point(w, h);
  }

  public static Point full(int widthMeasureSpec, int heightMeasureSpec)
  {
    int wm = View.MeasureSpec.getMode(widthMeasureSpec);
    int hm = View.MeasureSpec.getMode(heightMeasureSpec);
    int w, h;
    switch (wm) {
      case View.MeasureSpec.AT_MOST:
      case View.MeasureSpec.EXACTLY:
        w = View.MeasureSpec.getSize(widthMeasureSpec);
        break;
      case View.MeasureSpec.UNSPECIFIED:
      default:
        w = ZDimen.getWindowPixelsSize().x;
        break;
    }

    switch (hm) {
      case View.MeasureSpec.AT_MOST:
      case View.MeasureSpec.EXACTLY:
        h = View.MeasureSpec.getSize(heightMeasureSpec);
        break;
      case View.MeasureSpec.UNSPECIFIED:
      default:
        h = ZDimen.getWindowPixelsSize().y;
        break;
    }

    return new Point(w, h);

  }
}

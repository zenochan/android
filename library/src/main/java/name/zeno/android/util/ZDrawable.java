package name.zeno.android.util;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.widget.TextView;

import name.zeno.android.tint.TintInfo;
import name.zeno.android.tint.TintManager;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/5/19
 */
public class ZDrawable
{
  public static void tintComponentDrawable(TextView view, @ColorInt int color)
  {
    Drawable[] drawables = view.getCompoundDrawables();

    TintInfo info = new TintInfo();
    info.hasTintList = true;
    info.tintList = ColorStateList.valueOf(color);
    for (Drawable drawable : drawables) {
      if (drawable != null) {
        TintManager.tintDrawable(drawable, info, view.getDrawableState());
      }
    }
  }

  public static void resizeComponentDrawable(TextView view, int widthDp, int heightDp)
  {

    Drawable[] drawables = view.getCompoundDrawables();
    for (Drawable drawable : drawables) {
      setImageSize(drawable, widthDp, heightDp);
    }
    // 将图片放回到TextView中
    view.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
  }

  private static void setImageSize(Drawable drawable, int widthDp, int heightDp)
  {
    if (drawable != null && widthDp != -1 && heightDp != -1) {
      drawable.setBounds(0, 0, ZDimen.dp2px(widthDp), ZDimen.dp2px(heightDp));
    }
  }

}

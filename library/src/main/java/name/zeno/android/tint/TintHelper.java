package name.zeno.android.tint;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.StyleableRes;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class TintHelper
{

  public static void loadFromAttributes(View view, AttributeSet attrs, int defStyleAttr, @StyleableRes int[] styleable, int tintIndex, int tintModeIndex)
  {
    TypedArray a = view.getContext().obtainStyledAttributes(attrs, styleable, defStyleAttr, 0);
    try {
      if (a.hasValue(tintIndex)) {
        ViewCompat.setBackgroundTintList(view, a.getColorStateList(tintIndex));
      }
      if (a.hasValue(tintModeIndex)) {
        ViewCompat.setBackgroundTintMode(view, parseTintMode(a.getInt(tintModeIndex, -1), null));
      }
    } finally {
      a.recycle();
    }
  }

  public static void onSetBackgroundResource(View view, TintInfo info, int resId)
  {
    applySupportBackgroundTint(view, info);
  }

  public static void onSetBackgroundDrawable(View view, TintInfo info, Drawable background)
  {
    applySupportBackgroundTint(view, info);
  }

  public static void setSupportBackgroundTintList(View view, TintInfo info, ColorStateList tint)
  {
    info.tintList = tint;
    applySupportBackgroundTint(view, info);
  }

  public static void setSupportDrawableTintList(ImageView view, TintInfo info, ColorStateList tint)
  {
    info.tintList = tint;
    applySupportTint(view, info);
  }

  public static void setSupportCompoundDrawableTintList(TextView view, TintInfo info, ColorStateList tint)
  {
    info.tintList = tint;
    Drawable[] drawables = view.getCompoundDrawables();
    for (Drawable drawable : drawables) {
      if (drawable != null) {
        TintManager.tintDrawable(drawable, info, view.getDrawableState());
      }
    }
  }

  public static ColorStateList getSupportTintList(TintInfo info)
  {
    return info != null ? info.tintList : null;
  }

  public static void setSupportTintMode(View view, TintInfo info, PorterDuff.Mode tintMode)
  {
    if (info == null) {
      info = new TintInfo();
    }
    info.tintMode = tintMode;
    info.hasTintMode = true;

    applySupportBackgroundTint(view, info);
  }

  public static PorterDuff.Mode getSupportTintMode(TintInfo info)
  {
    return info != null ? info.tintMode : null;
  }

  static void applySupportBackgroundTint(View view, TintInfo info)
  {
    final Drawable background = view.getBackground();
    if (background != null) {
      if (info != null) {
        TintManager.tintDrawable(background, info, view.getDrawableState());
      }
    }
  }

  static void applySupportTint(ImageView view, TintInfo info)
  {
    final Drawable drawable = view.getDrawable();
    if (drawable != null && info != null) {
      TintManager.tintDrawable(drawable, info, view.getDrawableState());
    }
  }

  static PorterDuff.Mode parseTintMode(int value, PorterDuff.Mode defaultMode)
  {
    switch (value) {
      case 3:
        return PorterDuff.Mode.SRC_OVER;
      case 5:
        return PorterDuff.Mode.SRC_IN;
      case 9:
        return PorterDuff.Mode.SRC_ATOP;
      case 14:
        return PorterDuff.Mode.MULTIPLY;
      case 15:
        return PorterDuff.Mode.SCREEN;
      case 16:
        return Build.VERSION.SDK_INT >= 11
            ? PorterDuff.Mode.valueOf("ADD")
            : defaultMode;
      default:
        return defaultMode;
    }
  }
}

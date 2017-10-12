package name.zeno.android.tint;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.support.annotation.Nullable;

/**
 * Create Date: 16/6/23
 *
 * @author 陈治谋 (513500085@qq.com)
 */
public interface TintableDrawableView
{
  @Nullable ColorStateList getSupportDrawableTintList();

  void setSupportDrawableTintList(@Nullable ColorStateList tint);

  void setSupportDrawableTintMode(@Nullable PorterDuff.Mode tintMode);

  @Nullable PorterDuff.Mode getSupportDrawableTintMode();
}

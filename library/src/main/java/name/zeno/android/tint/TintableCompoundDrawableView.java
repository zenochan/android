package name.zeno.android.tint;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.support.annotation.Nullable;

/**
 * Create Date: 16/6/23
 *
 * @author 陈治谋 (513500085@qq.com)
 */
public interface TintableCompoundDrawableView
{

  void setSupportCompoundDrawableTintList(@Nullable ColorStateList tint);

  void setSupportCompoundDrawableTintMode(@Nullable PorterDuff.Mode tintMode);

  @Nullable ColorStateList getSupportCompoundDrawableTintList();

  @Nullable PorterDuff.Mode getSupportCompoundDrawableTintMode();
}

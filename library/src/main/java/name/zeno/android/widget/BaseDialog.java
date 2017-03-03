package name.zeno.android.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/1/10.
 */
public class BaseDialog extends Dialog
{
  public BaseDialog(Context context)
  {
    super(context);
  }

  public BaseDialog(Context context, int themeResId)
  {
    super(context, themeResId);
  }

  public BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener)
  {
    super(context, cancelable, cancelListener);
  }

  protected void transparentBackground()
  {
    Window window = getWindow();
    if (window != null) {
      window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
  }

  protected void setLayout(int w, int h)
  {
    Window window = getWindow();
    if (window != null) {
      window.setLayout(w, h);
    }
  }
}

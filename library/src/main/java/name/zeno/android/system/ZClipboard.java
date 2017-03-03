package name.zeno.android.system;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import name.zeno.android.util.ZLog;

/**
 * 剪贴板
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/12/13.
 */
public class ZClipboard
{
  public static void setText(Context context, String txt)
  {
    setText(context, null, txt);
  }

  // 设置剪贴板内容
  public static void setText(Context context, String label, String txt)
  {
    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    clipboard.setPrimaryClip(ClipData.newPlainText(label, txt));
  }

  // 获取剪贴板内容
  public static String[] getText(Context context)
  {
    String           result    = null;
    String           label     = null;
    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    ClipData         data      = clipboard.getPrimaryClip();
    if (data != null && data.getItemCount() > 0 && data.getItemAt(0).getText() != null) {
      result = data.getItemAt(0).getText().toString();
      CharSequence l = data.getDescription().getLabel();
      label = l == null ? null : l.toString();
    }

    return new String[]{label, result};
  }
}

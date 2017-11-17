package name.zeno.android.system

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

/**
 * 剪贴板
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/12/13.
 */
object ZClipboard {
  // 设置剪贴板内容
  fun setText(context: Context, clipboard: Clipboard?) {
    val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    cm.primaryClip = ClipData.newPlainText(clipboard?.label, clipboard?.txt)
  }

  /** # 获取剪贴板内容 */
  fun getText(context: Context): Clipboard? {
    var result: String? = null
    var label: String? = null
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val data = clipboard.primaryClip
    if (data != null && data.itemCount > 0 && data.getItemAt(0).text != null) {
      result = data.getItemAt(0).text.toString()
      val l = data.description.label
      label = l?.toString()
    }

    return when {
      result != null -> Clipboard(result, label)
      else -> null
    }
  }
}

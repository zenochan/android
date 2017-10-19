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
  fun setText(context: Context, txt: String?, label: String? = null) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.primaryClip = ClipData.newPlainText(label, txt)
  }

  //
  /**
   * # 获取剪贴板内容
   * @return [label,result]
   */
  fun getText(context: Context): Array<String?> {
    var result: String? = null
    var label: String? = null
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val data = clipboard.primaryClip
    if (data != null && data.itemCount > 0 && data.getItemAt(0).text != null) {
      result = data.getItemAt(0).text.toString()
      val l = data.description.label
      label = l?.toString()
    }

    return arrayOf(label, result)
  }
}

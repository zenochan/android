package name.zeno.android.util

import android.app.NotificationManager
import android.content.Context
import android.net.Uri
import android.support.annotation.RawRes

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 16/8/19
 */
object NotiUtils {
  fun sound(context: Context, @RawRes soundId: Int): Uri {
    return Uri.parse("android.resource://" + context.packageName + "/" + soundId)
  }

  fun cancel(context: Context, id: Int) {
    val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    nm.cancel(id)
  }

  fun cancelAll(context: Context) {
    val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    nm.cancelAll()
  }
}

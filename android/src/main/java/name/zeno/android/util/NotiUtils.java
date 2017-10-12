package name.zeno.android.util;

import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.RawRes;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 16/8/19
 */
public class NotiUtils
{
  public static Uri sound(Context context, @RawRes int soundId)
  {
    return Uri.parse("android.resource://" + context.getPackageName() + "/" + soundId);
  }

  public static void cancel(Context context, int id)
  {
    NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    nm.cancel(id);
  }

  public static void cancelAll(Context context)
  {
    NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    nm.cancelAll();
  }
}

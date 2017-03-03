package name.zeno.android.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Process;
import android.support.v4.app.Fragment;

import name.zeno.android.data.models.UpdateInfo;
import name.zeno.android.presenter.activities.UpdateActivity;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/9/27.
 */
public class ZNav
{
  public static void nav(Fragment fragment, Class<? extends Activity> activityClass)
  {
    Intent intent = new Intent(fragment.getContext(), activityClass);
    fragment.startActivity(intent);
  }

  public static void nav(Activity activity, Class<? extends Activity> activityClass)
  {
    Intent intent = new Intent(activity, activityClass);
    activity.startActivity(intent);
  }

  public static void navForResult(Fragment fragment, Class<? extends Activity> activityClass, int requestCode)
  {
    Intent intent = new Intent(fragment.getContext(), activityClass);
    fragment.startActivityForResult(intent, requestCode);
  }

  public static void navForResult(Activity activity, Class<? extends Activity> activityClass, int requestCode)
  {
    Intent intent = new Intent(activity, activityClass);
    activity.startActivityForResult(intent, requestCode);
  }

  /** 后台运行,回到安卓首页 */
  public static void home(Activity activity)
  {
    Intent intent = new Intent(Intent.ACTION_MAIN);
    intent.addCategory(Intent.CATEGORY_HOME);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    activity.startActivity(intent);
//    activity.moveTaskToBack(true);
  }

  /** 后台运行,回到安卓首页 */
  public static void runOnBack(Activity activity)
  {
    activity.moveTaskToBack(true);
  }

  public static void exit(Activity activity)
  {
    home(activity);
    Process.killProcess(Process.myPid());
  }

  public static void update(Activity activity, UpdateInfo updateInfo)
  {
    Intent intent = UpdateActivity.getCallingIntent(activity, updateInfo);
    activity.startActivity(intent);
  }
}

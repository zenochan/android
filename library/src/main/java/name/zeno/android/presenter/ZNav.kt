package name.zeno.android.presenter

import android.app.Activity
import android.content.Intent
import android.os.Parcelable
import android.os.Process
import android.support.v4.app.Fragment

import name.zeno.android.data.models.UpdateInfo
import name.zeno.android.presenter.activities.UpdateActivity

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/9/27.
 */
object ZNav {
  fun nav(fragment: Fragment, activityClass: Class<out Activity>) {
    val intent = Intent(fragment.context, activityClass)
    fragment.startActivity(intent)
  }


  fun nav(fragment: Fragment, activityClass: Class<out Activity>, data: Parcelable) {
    val intent = Intent(fragment.context, activityClass)
    Extra.setData(intent, data)
    fragment.startActivity(intent)
  }

  fun nav(activity: Activity, activityClass: Class<out Activity>) {
    val intent = Intent(activity, activityClass)
    activity.startActivity(intent)
  }

  fun nav(activity: Activity, activityClass: Class<out Activity>, data: Parcelable) {
    val intent = Intent(activity, activityClass)
    Extra.setData(intent, data)
    activity.startActivity(intent)
  }

  fun navForResult(fragment: Fragment, activityClass: Class<out Activity>, requestCode: Int) {
    val intent = Intent(fragment.context, activityClass)
    fragment.startActivityForResult(intent, requestCode)
  }

  fun navForResult(activity: Activity, activityClass: Class<out Activity>, requestCode: Int) {
    val intent = Intent(activity, activityClass)
    activity.startActivityForResult(intent, requestCode)
  }

  /** 后台运行,回到安卓首页  */
  fun home(activity: Activity) {
    val intent = Intent(Intent.ACTION_MAIN)
    intent.addCategory(Intent.CATEGORY_HOME)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    activity.startActivity(intent)
    //    activity.moveTaskToBack(true);
  }

  /** 后台运行,回到安卓首页  */
  fun runOnBack(activity: Activity) {
    activity.moveTaskToBack(true)
  }

  fun exit(activity: Activity) {
    home(activity)
    Process.killProcess(Process.myPid())
  }

  fun update(activity: Activity, updateInfo: UpdateInfo) {
    val intent = UpdateActivity.getCallingIntent(activity, updateInfo)
    activity.startActivity(intent)
  }
}

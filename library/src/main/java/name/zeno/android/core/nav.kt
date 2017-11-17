@file:Suppress("unused")

package name.zeno.android.core

import android.app.Activity
import android.app.Fragment
import android.app.FragmentManager
import android.content.Intent
import android.os.Parcelable
import android.os.Process
import name.zeno.android.data.models.UpdateInfo
import name.zeno.android.presenter.Extra
import name.zeno.android.presenter.ZActivity
import name.zeno.android.presenter.ZFragment
import name.zeno.android.presenter.activities.UpdateActivity
import kotlin.reflect.KClass

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/10/18
 */

fun Activity.nav(clazz: KClass<out Activity>, data: Parcelable? = null) = nav(clazz.java, data)

fun Activity.nav(clazz: Class<out Activity>, data: Parcelable? = null) = nav<Parcelable>(clazz, data)

fun <T : Parcelable> Activity.nav(clazz: KClass<out Activity>, data: Parcelable? = null, onResult: ((Boolean, T?) -> Unit)? = null)
    = nav(clazz.java, data, onResult)

fun <T : Parcelable> Activity.nav(clazz: Class<out Activity>, data: Parcelable? = null, onResult: ((Boolean, T?) -> Unit)? = null) {
  val intent = Intent(this, clazz)
  Extra.setData(intent, data)
  if (onResult != null) {
    val next = { ok: Boolean, intentData: Intent? -> onResult(ok, Extra.getData(intentData)) }

    when {
      this is ZActivity -> startActivityForResult(intent, next)
      else -> navigator().startActivityForResult(intent, next)
    }
  } else {
    startActivity(intent)
  }
}

fun Fragment.nav(clazz: KClass<out Activity>, data: Parcelable? = null) = nav(clazz.java, data)

fun Fragment.nav(clazz: Class<out Activity>, data: Parcelable? = null) = nav<Parcelable>(clazz, data)

fun <T : Parcelable> Fragment.nav(clazz: KClass<out Activity>, data: Parcelable? = null, onResult: ((Boolean, T?) -> Unit)? = null)
    = nav(clazz.java, data, onResult)

fun <T : Parcelable> Fragment.nav(clazz: Class<out Activity>, data: Parcelable? = null, onResult: ((Boolean, T?) -> Unit)? = null) {
  val intent = Intent(this.activity, clazz)
  Extra.setData(intent, data)
  if (onResult != null) {

    val next = { ok: Boolean, intentData: Intent? -> onResult(ok, Extra.getData(intentData)) }
    if (this is ZFragment) {
      startActivityForResult(intent, next)
    } else {
      navigator().startActivityForResult(intent, next)
    }
  } else {
    startActivity(intent)
  }
}

fun Fragment.navigator() = fragmentManager.navigator()
fun Activity.navigator() = fragmentManager.navigator()

private fun FragmentManager.navigator(): ZFragment {
  val tag = "zeno:nav"
  var fragment: ZFragment? = findFragmentByTag(tag) as? ZFragment
  if (fragment == null) {
    fragment = ZFragment()
    val transaction = beginTransaction()
    transaction.add(fragment, tag)
    transaction.commit()
  }

  return fragment
}


/** 后台运行,回到安卓首页  */
fun Activity.home() {
  val intent = Intent(Intent.ACTION_MAIN)
  intent.addCategory(Intent.CATEGORY_HOME)
  intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
  startActivity(intent)
  //    activity.moveTaskToBack(true);
}

/** 后台运行,回到安卓首页  */
fun Activity.runOnBack() {
  moveTaskToBack(true)
}

/** 退出 App */
fun Activity.exit() {
  home()
  Process.killProcess(Process.myPid())
}

fun Activity.update(updateInfo: UpdateInfo) {
  val intent = UpdateActivity.getCallingIntent(this, updateInfo)
  startActivity(intent)
}

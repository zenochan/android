@file:Suppress("unused")

package name.zeno.android.core

import android.app.Activity
import android.app.Fragment
import android.app.FragmentManager
import android.content.Intent
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import name.zeno.android.presenter.Extra
import name.zeno.android.presenter.ZActivity
import name.zeno.android.presenter.ZFragment
import kotlin.reflect.KClass

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/10/18
 */

fun AppCompatActivity.nav(clazz: KClass<out Activity>, data: Parcelable? = null) = nav(clazz.java, data)

fun AppCompatActivity.nav(clazz: Class<out Activity>, data: Parcelable? = null) = nav<Parcelable>(clazz, data)

fun <T : Parcelable> AppCompatActivity.nav(clazz: KClass<out Activity>, data: Parcelable? = null, onResult: ((Boolean, T?) -> Unit)? = null)
    = nav(clazz.java, data, onResult)

fun <T : Parcelable> AppCompatActivity.nav(clazz: Class<out Activity>, data: Parcelable? = null, onResult: ((Boolean, T?) -> Unit)? = null) {
  val intent = Intent(this, clazz)
  Extra.setData(intent, data)
  if (onResult != null) {

    val next = { ok: Boolean, intentData: Intent? -> onResult(ok, Extra.getData(intentData)) }

    if (this is ZActivity) {
      startActivityForResult(intent, next)
    } else {
      navigator().startActivityForResult(intent, next)
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
fun AppCompatActivity.navigator() = fragmentManager.navigator()

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

package name.zeno.android.core

import android.app.Activity
import android.content.Intent
import android.os.Parcelable
import android.support.v4.app.Fragment
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
    if (this is ZActivity) {
      startActivityForResult(intent) { ok, intentData -> onResult(ok, Extra.getData(intentData)) }
    } else {
      // get or register a ZFragment
      val tag = "zeno:nav"
      var fragment: ZFragment? = supportFragmentManager.findFragmentByTag(tag) as? ZFragment
      if (fragment == null) {
        fragment = ZFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(fragment, tag)
        transaction.commit()
      }

      fragment.startActivityForResult(intent) { ok, intentData -> onResult(ok, Extra.getData(intentData)) }
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
    if (this is ZFragment) {
      startActivityForResult(intent) { ok, intentData -> onResult(ok, Extra.getData(intentData)) }
    } else {
      // get or register a ZFragment
      val tag = "zeno:nav"
      var fragment: ZFragment? = fragmentManager.findFragmentByTag(tag) as? ZFragment
      if (fragment == null) {
        fragment = ZFragment()
        val transaction = fragmentManager.beginTransaction()
        transaction.add(fragment, tag)
        transaction.commit()
      }

      fragment.startActivityForResult(intent) { ok, intentData -> onResult(ok, Extra.getData(intentData)) }
    }
  } else {
    startActivity(intent)
  }
}

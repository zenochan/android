package name.zeno.android.util

import android.app.Application
import android.content.ContentProvider
import android.content.Context
import android.content.SharedPreferences

import com.alibaba.fastjson.JSON

/**
 * Create Date: 16/6/1
 *
 * @author 陈治谋 (513500085@qq.com)
 */
object ZCookie {
  private val TAG = "ZCookie"

  private var sp: SharedPreferences? = null

  fun init(application: Application) {
    sp = application.getSharedPreferences(TAG, Context.MODE_MULTI_PROCESS)
  }

  /**
   * @return versionCode 版本第一次打开
   */
  fun firstOpen(versionCode: Int): Boolean {
    var isFirstOpen = false

    if (!sp!!.getBoolean("versionCode_" + versionCode, false)) {
      val editor = sp!!.edit()
      editor.putBoolean("versionCode_" + versionCode, true)
      editor.apply()
      isFirstOpen = true
    }

    return isFirstOpen
  }

  operator fun <T> get(key: String, clazz: Class<T>): T? {
    var t: T?
    try {
      if (!spInited()) {
        t = null
      } else if (clazz == Long::class.java) {
        t = sp!!.getLong(key, 0).toLong() as T
      } else if (clazz == String::class.java) {
        t = sp!!.getString(key, null) as T
      } else if (clazz == Float::class.java) {
        t = sp!!.getFloat(key, 0f).toFloat() as T
      } else if (clazz == Int::class.java) {
        t = sp!!.getInt(key, 0).toInt() as T
      } else if (clazz == Boolean::class.java) {
        t = sp!!.getBoolean(key, false) as T
      } else {
        val jsonStr = sp!!.getString(key, null)
        t = if (jsonStr == null) null else JSON.parseObject(jsonStr, clazz)
      }
    } catch (e: Exception) {
      ZLog.e(TAG, e.message, e)
      t = null
    }

    return t
  }

  //获取列表
  fun <T> getList(key: String, clazz: Class<T>): List<T>? {
    if (!spInited()) {
      return null
    }

    val jsonStr = sp!!.getString(key, null)
    var list: List<T>? = null
    try {
      list = if (jsonStr == null) null else JSON.parseArray(jsonStr, clazz)
    } catch (e: Exception) {
      ZLog.e("数据格式过期", e.message)
    }

    return list
  }

  fun put(k: String, v: Any?) {
    if (!spInited()) {
      return
    }

    val editor = sp!!.edit()
    if (v == null) {
      editor.remove(k).apply()
      return
    }

    if (v is Long) {
      editor.putLong(k, (v as Long?)!!)
    } else if (v is String) {
      editor.putString(k, v as String?)
    } else if (v is Float) {
      editor.putFloat(k, (v as Float?)!!)
    } else if (v is Int) {
      editor.putInt(k, (v as Int?)!!)
    } else if (v is Boolean) {
      editor.putBoolean(k, (v as Boolean?)!!)
    } else {
      val jsonStr = JSON.toJSONString(v)
      editor.putString(k, jsonStr)
    }

    editor.apply()
  }

  fun getBoolean(key: String): Boolean {
    return sp!!.getBoolean(key, false)
  }

  fun putBoolean(key: String, v: Boolean) {
    sp!!.edit().putBoolean(key, v).apply()
  }

  private fun spInited(): Boolean {
    if (sp == null) {
      ZLog.e("calling method init(Application) before use ZCookie")
    }
    return sp != null
  }

}

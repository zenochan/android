package name.zeno.android.util

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.alibaba.fastjson.JSON
import name.zeno.android.util.ZCookie.get
import name.zeno.android.util.ZCookie.getBoolean
import name.zeno.android.util.ZCookie.put
import name.zeno.android.util.ZCookie.putBoolean

/**
 * - [get]
 * - [put]
 *
 * - [getBoolean]
 * - [putBoolean]
 *
 * - [Context.MODE_MULTI_PROCESS] 多进程 sp
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since  16/6/1
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

  @Suppress("UNCHECKED_CAST")
  fun <T> get(key: String, clazz: Class<T>): T? {
    val sp = sp ?: return null

    return try {
      when (clazz) {
        Long::class.java -> sp.getLong(key, 0) as T
        String::class.java -> sp.getString(key, null) as T
        Float::class.java -> sp.getFloat(key, 0f) as T
        Int::class.java -> sp.getInt(key, 0) as T
        Boolean::class.java -> sp.getBoolean(key, false) as T
        else -> {
          val jsonStr = sp.getString(key, null)
          if (jsonStr == null) null else JSON.parseObject(jsonStr, clazz)
        }
      }
    } catch (e: Exception) {
      sp.edit().remove(key).apply()
      null
    }
  }

  //获取列表
  fun <T> getList(key: String, clazz: Class<T>): List<T>? {
    val sp = this.sp ?: return null

    val jsonStr = sp.getString(key, null)
    var list: List<T>? = null
    try {
      list = if (jsonStr == null) null else JSON.parseArray(jsonStr, clazz)
    } catch (e: Exception) {
      ZLog.e("数据格式过期", e.message)
    }

    return list
  }

  fun put(k: String, v: Any?) {
    val sp = this.sp ?: return

    val editor = sp.edit()
    when (v) {
      null -> editor.remove(k)
      is Long -> editor.putLong(k, v)
      is String -> editor.putString(k, v)
      is Float -> editor.putFloat(k, v)
      is Int -> editor.putInt(k, v)
      is Boolean -> editor.putBoolean(k, v)
      else -> editor.putString(k, JSON.toJSONString(v))
    }
    editor.apply()
  }

  @Deprecated("")
  fun getBoolean(key: String): Boolean = this[key]

  @Deprecated("")
  fun putBoolean(key: String, v: Boolean) = set(key, v)

  operator fun get(key: String): Boolean = sp?.getBoolean(key, false) ?: false

  operator fun set(key: String, value: Boolean) {
    val sp = this.sp ?: return
    sp.edit().putBoolean(key, value).apply()
  }
}

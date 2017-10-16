package name.zeno.android.third.umeng

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.umeng.analytics.MobclickAgent
import name.zeno.android.app.ZApplication
import name.zeno.android.util.ZLog
import name.zeno.android.util.ZString
import org.json.JSONObject
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.IOException
import java.util.*

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/9/7
 */
object ZUmeng {
  val supported: Boolean
  private val TAG = ZUmeng::class.java.simpleName

  init {
    var support = false
    try {
      Class.forName("com.umeng.analytics.MobclickAgent")
      support = true

      //2.5.2  包含Activity、Fragment或View的应用
      //统计应用中包含Fragment的情况比较复杂，首先要明确一些概念。
      //1. MobclickAgent.onResume()  和MobclickAgent.onPause()  方法是用来统计应用时长的(也就是Session时长,当然还包括一些其他功能)
      //2.MobclickAgent.onPageStart() 和MobclickAgent.onPageEnd() 方法是用来统计页面跳转的
      //在仅有Activity的应用中，SDK 自动帮助开发者调用了 2  中的方法，并把Activity 类名作为页面名称统计。但是在包含fragment的程序中我们希望统计更详细的页面，所以需要自己调用方法做更详细的统计。
      //首先，需要在程序入口处，调用 MobclickAgent.openActivityDurationTrack(false)  禁止默认的页面统计方式，这样将不会再自动统计Activity。
      //然后需要手动添加以下代码：
      //1. 使用 MobclickAgent.onResume 和 MobclickAgent.onPause 方法统计时长, 这和基本统计中的情况一样(针对Activity)
      //2. 使用 MobclickAgent.onPageStart 和 MobclickAgent.onPageEnd 方法统计页面(针对页面,页面可能是Activity 也可能是Fragment或View)
      MobclickAgent.openActivityDurationTrack(false)
    } catch (e: ClassNotFoundException) {
      ZLog.i(TAG, "umeng module not supported")
    }

    supported = support
  }

  //<editor-fold desc="友盟页面基础统计">
  fun onResume(activity: Activity) {
    if (supported) MobclickAgent.onResume(activity)
  }

  fun onPause(activity: Activity) {
    if (supported) MobclickAgent.onPause(activity)
  }

  fun onPageStart(page: String) {
    if (supported) MobclickAgent.onPageStart(page)
  }

  fun onPageEnd(page: String) {
    if (supported) MobclickAgent.onPageEnd(page)
  }
  //</editor-fold>

  //友盟事件,带参数
  fun onEvent(event: String, vararg param: String) {
    if (supported) return

    if (param.size < 2) {
      MobclickAgent.onEvent(ZApplication.application, event)
    } else {
      val params = HashMap<String, String>()
      for (i in 0 until param.size / 2) {
        params.put(param[i * 2], param[i * 2 + 1])
      }
      MobclickAgent.onEvent(ZApplication.application, event, params)
    }
  }

  // 获取设备信息
  @SuppressLint("HardwareIds", "MissingPermission")
  fun getDeviceInfo(context: Context): String? {
    try {
      val json = JSONObject()
      val tm = context
          .getSystemService(Context.TELEPHONY_SERVICE) as android.telephony.TelephonyManager
      var device_id: String? = null
      if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
        device_id = tm.deviceId
      }
      var mac: String? = null
      var fstream: FileReader?
      try {
        fstream = FileReader("/sys/class/net/wlan0/address")
      } catch (e: FileNotFoundException) {
        fstream = FileReader("/sys/class/net/eth0/address")
      }

      var reader: BufferedReader? = null
      if (fstream != null) {
        try {
          reader = BufferedReader(fstream, 1024)
          mac = reader.readLine()
        } finally {
          try {
            fstream.close()
          } catch (e: IOException) {
            e.printStackTrace()
          }

          if (reader != null) {
            try {
              reader.close()
            } catch (e: IOException) {
              e.printStackTrace()
            }

          }
        }
      }
      json.put("mac", mac)
      if (ZString.isEmpty(device_id)) {
        device_id = mac
      }
      if (ZString.isEmpty(device_id)) {
        device_id = android.provider.Settings.Secure.getString(context.contentResolver,
            android.provider.Settings.Secure.ANDROID_ID)
      }
      json.put("device_id", device_id)
      return json.toString()
    } catch (e: Exception) {
      e.printStackTrace()
    }

    return null
  }

  private fun checkPermission(context: Context, permission: String): Boolean {
    var result = false
    if (Build.VERSION.SDK_INT >= 23) {
      try {
        val clazz = Class.forName("android.content.Context")
        val method = clazz.getMethod("checkSelfPermission", String::class.java)
        val rest = method.invoke(context, permission) as Int
        result = rest == PackageManager.PERMISSION_GRANTED
      } catch (e: Exception) {
        result = false
      }

    } else {
      val pm = context.packageManager
      if (pm.checkPermission(permission, context.packageName) == PackageManager.PERMISSION_GRANTED) {
        result = true
      }
    }
    return result
  }
}

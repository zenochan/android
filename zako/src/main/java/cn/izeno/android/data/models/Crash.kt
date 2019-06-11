package cn.izeno.android.data.models

import android.os.Build

import com.alibaba.fastjson.JSONObject

import cn.izeno.android.app.AppInfo
import cn.izeno.android.common.annotations.DataClass
import cn.izeno.android.exception.ZException
import cn.izeno.android.util.ZDate

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/8/19
 */
@DataClass data class Crash(
    val appName: String? = AppInfo.appName,
    val packageName: String? = AppInfo.packageName,
    val versionCode: Int = AppInfo.versionCode,
    val versionName: String? = AppInfo.versionName,

    // 设备信息
    val manufacturer: String? = Build.MANUFACTURER,
    val mode: String? = Build.MODEL,
    val version: String? = Build.VERSION.RELEASE,
    val sdkInt: Int = Build.VERSION.SDK_INT,
    val imei: String? = AppInfo.imei,
    val phoneNumber: String? = AppInfo.phoneNumber,

    // 账户
    var account: JSONObject? = null,

    // 错误信息
    var type: String? = null,
    var message: String? = null,
    var stack: String? = null,
    val time: String? = ZDate.nowString("E yyyy/MM/dd HH:mm:ss")
) {
  constructor(throwable: Throwable) : this(
      type = throwable.javaClass.name,
      message = throwable.message,
      stack = ZException.stack(throwable)
  )
}

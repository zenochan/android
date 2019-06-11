package cn.izeno.android.app

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.TELEPHONY_SERVICE
import android.content.res.Resources
import android.os.Build
import android.telephony.TelephonyManager
import com.orhanobut.logger.Logger
import cn.izeno.android.data.CommonConnector
import cn.izeno.android.util.PrintUtils
import cn.izeno.android.util.SystemUtils
import cn.izeno.ktrxpermission.ZPermission

/**
 * Create Date: 16/6/9
 *
 * @author 陈治谋 (513500085@qq.com)
 */
object AppInfo {
  private val TAG = "AppInfo"

  lateinit var dType: String
  lateinit var dVersion: String
  var imei: String? = null
  var phoneNumber: String? = null

  lateinit var appName: String
  lateinit var packageName: String
  var versionCode: Int = 0
  lateinit var versionName: String

  /** 屏幕宽度  */
  var width: Int = 0
  /** 屏幕高度  */
  var height: Int = 0
  var density: Float = 0F
  var densityDpi: Int = 0

  lateinit var btAddress: String //蓝牙地址
  lateinit var btName: String    //蓝牙名称

  lateinit var downloadFileProvider: String

  init {
    initDisplay()
  }

  @SuppressLint("HardwareIds", "MissingPermission")
  fun init(context: Context) {
    dType = Build.MODEL
    dVersion = Build.VERSION.SDK_INT.toString() + "_" + Build.VERSION.RELEASE

    if (ZPermission.granted(context, ZPermission.READ_PHONE_STATE)) {
      try {
        val tm = context.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        imei = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
          tm.imei
        } else {
          @Suppress("DEPRECATION")
          tm.deviceId
        }
        phoneNumber = tm.line1Number
      } catch (ignore: Exception) {
      }
    }

    try {
      val pi = context.packageManager.getPackageInfo(context.packageName, 0)

      versionName = pi.versionName
      versionCode = pi.versionCode
      packageName = pi.packageName

      downloadFileProvider = packageName + ".fileprovider"
    } catch (e: Exception) {
      CommonConnector.sendCrash(e, null)

      versionName = ""
      versionCode = 0
    }

    appName = SystemUtils.getApplicationName(context)
    btAddress = PrintUtils.getDefaultBluetoothDeviceAddress(context)
    btName = PrintUtils.getDefaultBluetoothDeviceName(context)

    printAppInfo()
  }

  fun initDisplay() {
    val metrics = Resources.getSystem().displayMetrics
    width = metrics.widthPixels
    height = metrics.heightPixels
    density = metrics.density
    densityDpi = metrics.densityDpi
  }

  private fun printAppInfo() {
    Logger.t(TAG).d(
        """
        -------------   app info  -------------------
        dType       : $dType
        dVersion    : $dVersion
        versionCode : $versionCode
        versionName : $versionName
        width       : $width
        height      : $height
        density     : $density
        densityDpi  : $densityDpi
        ---------------------------------------------
        """
    )
  }
}


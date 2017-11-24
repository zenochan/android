package name.zeno.android.system

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import name.zeno.android.core.isPermissionGranted

/**
 * 需要动态申请的权限
 * [Android 6.0 RunTimePermission（运行时授权）](http://blog.csdn.net/qq_19862039/article/details/50812477)
 *
 * @author 陈治谋 (513500085@qq.com)
 * @see {@link android.Manifest.permission}
 *
 * @since 2016/11/21.
 */
object ZPermission {
  const val GRANTED = PackageManager.PERMISSION_GRANTED
  const val DENIED = PackageManager.PERMISSION_DENIED

  //  日历 calendar
  const val READ_CALENDER = Manifest.permission.READ_CALENDAR
  const val WRITE_CALENDER = Manifest.permission.WRITE_CALENDAR

  //  相机 camera
  const val CAMERA = Manifest.permission.CAMERA

  //  联系人 contacts
  const val READ_CONTACTS = Manifest.permission.READ_CONTACTS
  const val WRITE_CONTACTS = Manifest.permission.WRITE_CONTACTS
  const val GET_CONTACTS = Manifest.permission.GET_ACCOUNTS

  //  定位 location
  const val ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
  const val ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION

  //  麦克风 microphone
  const val RECORD_AUDIO = Manifest.permission.RECORD_AUDIO

  //  电话 phone
  const val READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE
  const val CALL_PHONE = Manifest.permission.CALL_PHONE
  const val READ_CALL_LOG = "android.permission.READ_CALL_LOG"
  const val WRITE_CALL_LOG = "android.permission.WRITE_CALL_LOG"
  const val ADD_VOICEMAIL = Manifest.permission.ADD_VOICEMAIL
  const val USE_SIP = Manifest.permission.USE_SIP
  const val PROCESS_OUTGOING_CALLS = Manifest.permission.PROCESS_OUTGOING_CALLS

  //  传感器 sensors
  const val BODY_SENSORS = "android.permission.BODY_SENSORS"

  //  短信 sms
  const val SEND_SMS = Manifest.permission.SEND_SMS
  const val RECEIVE_SMS = Manifest.permission.RECEIVE_SMS
  const val READ_SMS = Manifest.permission.READ_SMS
  const val RECEIVE_WAP_PUSH = Manifest.permission.RECEIVE_WAP_PUSH
  const val RECEIVE_MMS = Manifest.permission.RECEIVE_MMS

  // 存储 storage
  const val READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE"
  const val WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE

  fun granted(context: Context, vararg permissions: String) = context.isPermissionGranted(*permissions)
}


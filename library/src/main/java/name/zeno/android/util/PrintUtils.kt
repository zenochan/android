package name.zeno.android.util

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.SharedPreferences
import android.support.annotation.RequiresPermission
import android.text.TextUtils

import name.zeno.android.app.AppInfo

/**
 * 打印工具类
 * Create Date: 16/6/9
 *
 * @author 陈治谋 (513500085@qq.com)
 */
object PrintUtils {
  private val FILENAME = "bt"
  private val DEFAULT_BLUETOOTH_DEVICE_ADDRESS = "default_bluetooth_device_address"
  private val DEFAULT_BLUETOOTH_DEVICE_NAME = "default_bluetooth_device_name"

  val ACTION_PRINT_TEST = "action_print_test"
  val ACTION_PRINT = "action_print"
  val ACTION_PRINT_TICKET = "action_print_ticket"
  val ACTION_PRINT_BITMAP = "action_print_bitmap"
  val ACTION_PRINT_PAINTING = "action_print_painting"

  val PRINT_EXTRA = "print_extra"

  /**
   * 设置默认蓝牙设备地址
   */
  fun setDefaultBluetoothDeviceAddress(mContext: Context, value: String) {
    val sharedPreferences = mContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString(DEFAULT_BLUETOOTH_DEVICE_ADDRESS, value)
    editor.apply()
    AppInfo.btAddress = value
  }

  fun setDefaultBluetoothDeviceName(mContext: Context, value: String) {
    val sharedPreferences = mContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString(DEFAULT_BLUETOOTH_DEVICE_NAME, value)
    editor.apply()
    AppInfo.btName = value
  }

  @RequiresPermission(Manifest.permission.BLUETOOTH)
  fun isBondPrinter(mContext: Context, bluetoothAdapter: BluetoothAdapter): Boolean {
    if (!BluetoothUtils.isOpen(bluetoothAdapter)) {
      return false
    }
    val defaultBluetoothDeviceAddress = getDefaultBluetoothDeviceAddress(mContext)
    if (TextUtils.isEmpty(defaultBluetoothDeviceAddress)) {
      return false
    }
    val deviceSet = bluetoothAdapter.bondedDevices
    if (deviceSet == null || deviceSet.isEmpty()) {
      return false
    }
    for (bluetoothDevice in deviceSet) {
      if (bluetoothDevice.address == defaultBluetoothDeviceAddress) {
        return true
      }
    }
    return false

  }

  fun getDefaultBluetoothDeviceAddress(mContext: Context): String {
    val sharedPreferences = mContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)
    return sharedPreferences.getString(DEFAULT_BLUETOOTH_DEVICE_ADDRESS, "")
  }

  fun isBondPrinterIgnoreBluetooth(mContext: Context): Boolean {
    val defaultBluetoothDeviceAddress = getDefaultBluetoothDeviceAddress(mContext)
    return !(TextUtils.isEmpty(defaultBluetoothDeviceAddress) || TextUtils.isEmpty(getDefaultBluetoothDeviceName(mContext)))
  }

  fun getDefaultBluetoothDeviceName(mContext: Context): String {
    val sharedPreferences = mContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)
    return sharedPreferences.getString(DEFAULT_BLUETOOTH_DEVICE_NAME, "")
  }

}

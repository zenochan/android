package name.zeno.android.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.support.annotation.IntRange

interface BluetoothListener {

  /**
   * 开始查找蓝牙设备
   *
   * @see BluetoothAdapter.ACTION_DISCOVERY_STARTED
   */
  fun onStartDiscovery()

  /**
   * 结束查找蓝牙设备
   *
   * @see BluetoothAdapter.ACTION_DISCOVERY_FINISHED
   */
  fun onFinishDiscovery()

  /**
   * 蓝牙状态改变
   *
   * @see BluetoothAdapter.ACTION_STATE_CHANGED
   */
  fun onStatusChanged(isBluetoothEnable: Boolean)

  /**
   * 发现蓝牙设备
   *
   * @see BluetoothDevice.ACTION_FOUND
   */
  fun onFoundDevice(device: BluetoothDevice)

  /**
   * 设备绑定状态改变
   *
   * @see BluetoothDevice.ACTION_BOND_STATE_CHANGED
   */
  fun onBondStatusChange(intent: Intent)

  /**
   * 蓝牙配对结果
   *
   * @see BluetoothDevice.ACTION_PAIRING_REQUEST
   */
  fun btPairingRequest(intent: Intent)


  /**
   * @param errCode 0 : success, -1: 设备不支持， -2： 用户拒绝
   * @param errMsg  错误信息
   */
  fun onOpenBluetoothResult(@IntRange(from = -2, to = 0) errCode: Int, errMsg: String)

  companion object {
    val CODE_SUCCESS = 0
    val CODE_NOT_SUPPORT = -1
    val CODE_USER_DENIED = -2
  }
}

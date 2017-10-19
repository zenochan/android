package name.zeno.android.util

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.support.annotation.RequiresPermission

/**
 * Create Date: 16/6/9
 *
 * @author 陈治谋 (513500085@qq.com)
 */
object BluetoothUtils {

  /**
   * 判断蓝牙是否打开
   *
   * @return boolean
   */
  @RequiresPermission(Manifest.permission.BLUETOOTH)
  fun isOpen(adapter: BluetoothAdapter?): Boolean {
    return adapter?.isEnabled ?: false
  }

  /**
   * 搜索蓝牙设备
   */
  @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
  fun searchDevices(adapter: BluetoothAdapter?) {
    // 寻找蓝牙设备，android会将查找到的设备以广播形式发出去
    adapter?.startDiscovery()
  }

  /**
   * 取消搜索蓝牙设备
   */
  @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
  fun cancelDiscovery(adapter: BluetoothAdapter?) {
    adapter?.cancelDiscovery()
  }

  /**
   * register bluetooth receiver
   *
   * @param receiver bluetooth broadcast receiver
   * @param activity activity
   */
  fun registerBluetoothReceiver(receiver: BroadcastReceiver?, activity: Activity?) {
    if (null == receiver || null == activity) {
      return
    }
    val intentFilter = IntentFilter()
    //start discovery
    intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
    //finish discovery
    intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
    //bluetooth status change
    intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
    //found device
    intentFilter.addAction(BluetoothDevice.ACTION_FOUND)
    //bond status change
    intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
    //pairing device
    intentFilter.addAction("android.bluetooth.device.action.PAIRING_REQUEST")
    activity.registerReceiver(receiver, intentFilter)
  }

  /**
   * unregister bluetooth receiver
   *
   * @param receiver bluetooth broadcast receiver
   * @param activity activity
   */
  fun unregisterBluetoothReceiver(receiver: BroadcastReceiver?, activity: Activity?) {
    if (null == receiver || null == activity) {
      return
    }
    activity.unregisterReceiver(receiver)
  }
}

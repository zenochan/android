package name.zeno.android.bluetooth

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.support.annotation.RequiresPermission
import android.support.v4.app.Fragment

/**
 * Create Date: 16/6/9
 *
 * @author 陈治谋 (513500085@qq.com)
 */
class BluetoothHelper {

  private var activity: Activity? = null
  private var fragment: Fragment? = null

  var adapter: BluetoothAdapter? = null
    private set
  var listener: BluetoothListener? = null
    set(listener) {
      field = listener
      btReceiver.listener = listener
    }

  private val bluetoothListener: BluetoothListener? = null


  private val btReceiver = BtBroadcastReceiver()

  constructor(activity: Activity) {
    this.activity = activity
  }

  constructor(fragment: Fragment) {
    this.fragment = fragment
  }

  fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {

    if (requestCode == REQUEST_CODE_OPEN_BLUETOOTH && resultCode == Activity.RESULT_OK) {
      adapter = bluetoothAdapter
      if (this.listener != null) {
        this.listener!!.onOpenBluetoothResult(BluetoothListener.CODE_SUCCESS, "打开蓝牙成功")
      }
    } else if (requestCode == REQUEST_CODE_OPEN_BLUETOOTH && resultCode == Activity.RESULT_CANCELED) {
      if (this.listener != null) {
        this.listener!!.onOpenBluetoothResult(BluetoothListener.CODE_USER_DENIED, "已拒绝使用蓝牙")
      }
    }

  }

  fun onCreate() {
    registerReceiver()
  }

  @RequiresPermission(anyOf = arrayOf(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN))
  fun onDestroy() {
    cancelDiscovery()
    unregisterReceiver()
    activity = null
    fragment = null
    adapter = null
  }

  @RequiresPermission(anyOf = arrayOf(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN))
  fun openBluetooth() {
    if (!isBluetoothEnable) {
      if (this.listener != null) {
        this.listener!!.onOpenBluetoothResult(-1, "该设备不支持蓝牙功能")
      }
    } else if (isBluetoothOpened) {
      adapter = bluetoothAdapter
      if (this.listener != null) {
        this.listener!!.onOpenBluetoothResult(0, "蓝牙功能已打开")
      }
    } else {
      val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
      if (activity != null) {
        activity!!.startActivityForResult(enableBtIntent, REQUEST_CODE_OPEN_BLUETOOTH)
      } else {
        fragment!!.startActivityForResult(enableBtIntent, REQUEST_CODE_OPEN_BLUETOOTH)
      }
    }
  }

  /**
   * Android 6.0 需要位置访问权限才能扫描到蓝牙
   */
  @RequiresPermission(anyOf = arrayOf(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION))
  fun discoveryDevices() {
    if (adapter != null && !adapter!!.isDiscovering) {
      adapter!!.startDiscovery()
    }
  }

  @RequiresPermission(anyOf = arrayOf(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN))
  fun cancelDiscovery() {
    if (adapter != null && adapter!!.isDiscovering) {
      adapter!!.cancelDiscovery()
    }
  }

  private fun registerReceiver() {
    if (activity != null) {
      activity!!.registerReceiver(btReceiver, btReceiver.filter)
    } else {
      fragment!!.activity.registerReceiver(btReceiver, btReceiver.filter)
    }
  }

  private fun unregisterReceiver() {
    if (activity != null) {
      activity!!.unregisterReceiver(btReceiver)
    } else {
      fragment!!.activity.unregisterReceiver(btReceiver)
    }
  }

  companion object {
    private val REQUEST_CODE_OPEN_BLUETOOTH = 0x1001

    val isBluetoothEnable: Boolean
      get() = bluetoothAdapter != null

    val isBluetoothOpened: Boolean
      @RequiresPermission(Manifest.permission.BLUETOOTH)
      get() = isBluetoothEnable && bluetoothAdapter!!.isEnabled

    private val bluetoothAdapter: BluetoothAdapter?
      get() = BluetoothAdapter.getDefaultAdapter()
  }

}

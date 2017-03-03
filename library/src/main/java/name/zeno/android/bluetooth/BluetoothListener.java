package name.zeno.android.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.annotation.IntRange;

public interface BluetoothListener
{
  int CODE_SUCCESS     = 0;
  int CODE_NOT_SUPPORT = -1;
  int CODE_USER_DENIED = -2;

  /**
   * 开始查找蓝牙设备
   *
   * @see BluetoothAdapter#ACTION_DISCOVERY_STARTED
   */
  void onStartDiscovery();

  /**
   * 结束查找蓝牙设备
   *
   * @see BluetoothAdapter#ACTION_DISCOVERY_FINISHED
   */
  void onFinishDiscovery();

  /**
   * 蓝牙状态改变
   *
   * @see BluetoothAdapter#ACTION_STATE_CHANGED
   */
  void onStatusChanged(boolean isBluetoothEnable);

  /**
   * 发现蓝牙设备
   *
   * @see BluetoothDevice#ACTION_FOUND
   */
  void onFoundDevice(BluetoothDevice device);

  /**
   * 设备绑定状态改变
   *
   * @see BluetoothDevice#ACTION_BOND_STATE_CHANGED
   */
  void onBondStatusChange(Intent intent);

  /**
   * 蓝牙配对结果
   *
   * @see BluetoothDevice#ACTION_PAIRING_REQUEST
   */
  void btPairingRequest(Intent intent);


  /**
   * @param errCode 0 : success, -1: 设备不支持， -2： 用户拒绝
   * @param errMsg  错误信息
   */
  void onOpenBluetoothResult(@IntRange(from = -2, to = 0) int errCode, String errMsg);
}

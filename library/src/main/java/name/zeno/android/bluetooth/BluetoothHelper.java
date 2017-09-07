package name.zeno.android.bluetooth;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.Fragment;

/**
 * Create Date: 16/6/9
 *
 * @author 陈治谋 (513500085@qq.com)
 */
@SuppressWarnings("unused")
public class BluetoothHelper
{
  private static final int REQUEST_CODE_OPEN_BLUETOOTH = 0x1001;

  private Activity activity;
  private Fragment fragment;

  private BluetoothAdapter  adapter;
  private BluetoothListener listener;

  private BluetoothListener bluetoothListener;


  private BtBroadcastReceiver btReceiver = new BtBroadcastReceiver();

  public BluetoothHelper(Activity activity)
  {
    this.activity = activity;
  }

  public BluetoothHelper(Fragment fragment)
  {
    this.fragment = fragment;
  }

  public void onActivityResult(int requestCode, int resultCode, Intent data)
  {

    if (requestCode == REQUEST_CODE_OPEN_BLUETOOTH && resultCode == Activity.RESULT_OK) {
      adapter = getBluetoothAdapter();
      if (listener != null) {
        listener.onOpenBluetoothResult(BluetoothListener.CODE_SUCCESS, "打开蓝牙成功");
      }
    } else if (requestCode == REQUEST_CODE_OPEN_BLUETOOTH && resultCode == Activity.RESULT_CANCELED) {
      if (listener != null) {
        listener.onOpenBluetoothResult(BluetoothListener.CODE_USER_DENIED, "已拒绝使用蓝牙");
      }
    }

  }

  public void onCreate()
  {
    registerReceiver();
  }

  @RequiresPermission(anyOf = {
      Manifest.permission.BLUETOOTH,
      Manifest.permission.BLUETOOTH_ADMIN
  })
  public void onDestroy()
  {
    cancelDiscovery();
    unregisterReceiver();
    activity = null;
    fragment = null;
    adapter = null;
  }

  public static boolean isBluetoothEnable()
  {
    return getBluetoothAdapter() != null;
  }

  @RequiresPermission(Manifest.permission.BLUETOOTH)
  public static boolean isBluetoothOpened()
  {
    return isBluetoothEnable() && getBluetoothAdapter().isEnabled();
  }

  @RequiresPermission(anyOf = {
      Manifest.permission.BLUETOOTH,
      Manifest.permission.BLUETOOTH_ADMIN
  })
  public void openBluetooth()
  {
    if (!isBluetoothEnable()) {
      if (listener != null) {
        listener.onOpenBluetoothResult(-1, "该设备不支持蓝牙功能");
      }
    } else if (isBluetoothOpened()) {
      adapter = getBluetoothAdapter();
      if (listener != null) {
        listener.onOpenBluetoothResult(0, "蓝牙功能已打开");
      }
    } else {
      Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
      if (activity != null) {
        activity.startActivityForResult(enableBtIntent, REQUEST_CODE_OPEN_BLUETOOTH);
      } else {
        fragment.startActivityForResult(enableBtIntent, REQUEST_CODE_OPEN_BLUETOOTH);
      }
    }
  }

  /**
   * Android 6.0 需要位置访问权限才能扫描到蓝牙
   */
  @RequiresPermission(anyOf = {
      Manifest.permission.BLUETOOTH,
      Manifest.permission.BLUETOOTH_ADMIN,
      Manifest.permission.ACCESS_COARSE_LOCATION,
      Manifest.permission.ACCESS_FINE_LOCATION
  })
  public void discoveryDevices()
  {
    if (adapter != null && !adapter.isDiscovering()) {
      adapter.startDiscovery();
    }
  }

  @RequiresPermission(anyOf = {
      Manifest.permission.BLUETOOTH,
      Manifest.permission.BLUETOOTH_ADMIN
  })
  public void cancelDiscovery()
  {
    if (adapter != null && adapter.isDiscovering()) {
      adapter.cancelDiscovery();
    }
  }

  public BluetoothListener getListener()
  {
    return listener;
  }

  public void setListener(BluetoothListener listener)
  {
    this.listener = listener;
    btReceiver.setListener(listener);
  }

  public BluetoothAdapter getAdapter()
  {
    return adapter;
  }

  private static BluetoothAdapter getBluetoothAdapter()
  {
    return BluetoothAdapter.getDefaultAdapter();
  }

  private void registerReceiver()
  {
    if (activity != null) {
      activity.registerReceiver(btReceiver, btReceiver.getFilter());
    } else {
      fragment.getActivity().registerReceiver(btReceiver, btReceiver.getFilter());
    }
  }

  private void unregisterReceiver()
  {
    if (activity != null) {
      activity.unregisterReceiver(btReceiver);
    } else {
      fragment.getActivity().unregisterReceiver(btReceiver);
    }
  }

}

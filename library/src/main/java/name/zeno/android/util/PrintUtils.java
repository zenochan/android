package name.zeno.android.util;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.RequiresPermission;
import android.text.TextUtils;

import java.util.Set;

import name.zeno.android.app.AppInfo;

/**
 * 打印工具类
 * Create Date: 16/6/9
 *
 * @author 陈治谋 (513500085@qq.com)
 */
public class PrintUtils
{
  private static final String FILENAME                         = "bt";
  private static final String DEFAULT_BLUETOOTH_DEVICE_ADDRESS = "default_bluetooth_device_address";
  private static final String DEFAULT_BLUETOOTH_DEVICE_NAME    = "default_bluetooth_device_name";

  public static final String ACTION_PRINT_TEST     = "action_print_test";
  public static final String ACTION_PRINT          = "action_print";
  public static final String ACTION_PRINT_TICKET   = "action_print_ticket";
  public static final String ACTION_PRINT_BITMAP   = "action_print_bitmap";
  public static final String ACTION_PRINT_PAINTING = "action_print_painting";

  public static final String PRINT_EXTRA = "print_extra";

  /**
   * 设置默认蓝牙设备地址
   */
  public static void setDefaultBluetoothDeviceAddress(Context mContext, String value)
  {
    SharedPreferences sharedPreferences = mContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(DEFAULT_BLUETOOTH_DEVICE_ADDRESS, value);
    editor.apply();
    AppInfo.btAddress = value;
  }

  public static void setDefaultBluetoothDeviceName(Context mContext, String value)
  {
    SharedPreferences sharedPreferences = mContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(DEFAULT_BLUETOOTH_DEVICE_NAME, value);
    editor.apply();
    AppInfo.btName = value;
  }

  @RequiresPermission(Manifest.permission.BLUETOOTH)
  public static boolean isBondPrinter(Context mContext, BluetoothAdapter bluetoothAdapter)
  {
    if (!BluetoothUtils.isOpen(bluetoothAdapter)) {
      return false;
    }
    String defaultBluetoothDeviceAddress = getDefaultBluetoothDeviceAddress(mContext);
    if (TextUtils.isEmpty(defaultBluetoothDeviceAddress)) {
      return false;
    }
    Set<BluetoothDevice> deviceSet = bluetoothAdapter.getBondedDevices();
    if (deviceSet == null || deviceSet.isEmpty()) {
      return false;
    }
    for (BluetoothDevice bluetoothDevice : deviceSet) {
      if (bluetoothDevice.getAddress().equals(defaultBluetoothDeviceAddress)) {
        return true;
      }
    }
    return false;

  }

  public static String getDefaultBluetoothDeviceAddress(Context mContext)
  {
    SharedPreferences sharedPreferences = mContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
    return sharedPreferences.getString(DEFAULT_BLUETOOTH_DEVICE_ADDRESS, "");
  }

  public static boolean isBondPrinterIgnoreBluetooth(Context mContext)
  {
    String defaultBluetoothDeviceAddress = getDefaultBluetoothDeviceAddress(mContext);
    return !(TextUtils.isEmpty(defaultBluetoothDeviceAddress)
        || TextUtils.isEmpty(getDefaultBluetoothDeviceName(mContext)));
  }

  public static String getDefaultBluetoothDeviceName(Context mContext)
  {
    SharedPreferences sharedPreferences = mContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
    return sharedPreferences.getString(DEFAULT_BLUETOOTH_DEVICE_NAME, "");
  }

}

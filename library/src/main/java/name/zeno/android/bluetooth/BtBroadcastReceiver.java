package name.zeno.android.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

/**
 * Create Date: 16/6/9
 *
 * @author 陈治谋 (513500085@qq.com)
 */
public class BtBroadcastReceiver extends BroadcastReceiver
{
  private BluetoothListener listener;

  public BluetoothListener getListener()
  {
    return listener;
  }

  public void setListener(BluetoothListener listener)
  {
    this.listener = listener;
  }

  @Override public void onReceive(Context context, Intent intent)
  {
    if (null == intent) {
      return;
    }
    String action = intent.getAction();
    if (TextUtils.isEmpty(action)) {
      return;
    }

    if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
      startDiscover();
    } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
      finishDiscover();
    } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
      stateChanged();
    } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
      foundDevice(intent);
    } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
      bondStateChanged(intent);
    } else if ("android.bluetooth.device.action.PAIRING_REQUEST".equals(action)) {
      pairingRequest(intent);
    }
  }

  public void startDiscover()
  {
    if (listener != null) {
      listener.onStartDiscovery();
    }
  }

  public void finishDiscover()
  {
    if (listener != null) {
      listener.onFinishDiscovery();
    }
  }

  public void stateChanged()
  {
    if (listener != null) {
      boolean btEnable = BluetoothHelper.isBluetoothEnable();
      listener.onStatusChanged(btEnable);
    }
  }

  public void foundDevice(Intent intent)
  {
    if (listener != null) {
      BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
      listener.onFoundDevice(device);
    }
  }

  public void bondStateChanged(Intent intent)
  {
    if (listener != null) {
      listener.onBondStatusChange(intent);
    }
  }

  private void pairingRequest(Intent intent)
  {
    if (listener != null) {
      listener.btPairingRequest(intent);
    }
  }

  public IntentFilter getFilter()
  {
    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
    intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
    intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
    intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
    intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
    intentFilter.addAction("android.bluetooth.device.action.PAIRING_REQUEST");

    return intentFilter;
  }

}

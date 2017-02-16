package com.dqc.smartblue.service;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dqc.smartblue.activity.MainActivity;
import com.dqc.smartblue.utils.RealmUtils;

/**
 * 蓝牙连接状态监听 BroadcastReceiver
 */
public class SmartBlueReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String          action = intent.getAction();
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        switch (action) {
            case BluetoothDevice.ACTION_FOUND:
                //设备已发现
                break;
            case BluetoothDevice.ACTION_ACL_CONNECTED:
                //设备已连接
                MainActivity.getInstance().mConnectedDevicesAddress.add(device.getAddress());
                MainActivity.getInstance().mAdapter.notifyDataSetChanged();
                MainActivity.getInstance().notification(RealmUtils.getInstance().find(device.getAddress()), device, true);
                break;
            case BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED:
                //正在断开蓝牙连接...
                break;
            case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                //蓝牙连接已断开
                MainActivity.getInstance().mConnectedDevicesAddress.remove(device.getAddress());
                MainActivity.getInstance().mAdapter.notifyDataSetChanged();
                MainActivity.getInstance().notification(RealmUtils.getInstance().find(device.getAddress()), device, false);
                break;
        }

    }


}

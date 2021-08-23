package com.askia.common.util.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;
import android.widget.Toast;

import com.askia.common.util.MyToastUtils;
import com.ttsea.jrxbus2.RxBus2;

import static com.askia.coremodel.rtc.Constants.ACTION_USB_PERMISSION;

/**
 * Created by yuanpk on 2018/8/2  14:22
 * <p>
 * Description:usb插拔广播接受
 */
public class UsbStateChangeReceiver extends BroadcastReceiver {
    private static final String TAG = "UsbStateChangeReceiver";

    private boolean isConnected;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
            isConnected = true;
            MyToastUtils.success("USB设备已连接", Toast.LENGTH_SHORT);

            UsbDevice device_add = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            if (device_add != null) {
                RxBus2.getInstance().post(new UsbStatusChangeEvent(isConnected));
            } else {
//                MyToastUtils.success("onReceive: device is null",Toast.LENGTH_SHORT);
            }


        } else if (action.equals(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
            //Log.i(TAG, "onReceive: USB设备已分离");
            isConnected = false;
            MyToastUtils.success("USB设备已拔出",Toast.LENGTH_SHORT);

            RxBus2.getInstance().post(new UsbStatusChangeEvent(isConnected));
        } else if (action.equals(ACTION_USB_PERMISSION)) {

            UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            //允许权限申请
            if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                if (usbDevice != null) {
                    Log.i(TAG, "权限已获取");
                    RxBus2.getInstance().post(new UsbStatusChangeEvent(true, usbDevice));
                } else {
                    MyToastUtils.success("没有插入U盘",Toast.LENGTH_SHORT);
                }
            } else {
                MyToastUtils.success("未获取到U盘权限",Toast.LENGTH_SHORT);
            }
        } else {
//            Log.i(TAG, "onReceive: action=" + action);
//            MyToastUtils.success("action= " + action,Toast.LENGTH_SHORT);
        }


    }
}

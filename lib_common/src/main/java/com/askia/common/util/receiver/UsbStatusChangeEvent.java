package com.askia.common.util.receiver;

import android.hardware.usb.UsbDevice;

public class UsbStatusChangeEvent {
    public boolean isConnected = false;

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public boolean isGetPermission() {
        return isGetPermission;
    }

    public void setGetPermission(boolean getPermission) {
        isGetPermission = getPermission;
    }

    public UsbDevice getUsbDevice() {
        return usbDevice;
    }

    public void setUsbDevice(UsbDevice usbDevice) {
        this.usbDevice = usbDevice;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isGetPermission = false;
    public UsbDevice usbDevice;

    public String filePath = "";


    public UsbStatusChangeEvent(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public UsbStatusChangeEvent(String filePath) {
        this.filePath = filePath;
    }

    public UsbStatusChangeEvent(boolean isGetPermission, UsbDevice usbDevice) {

        this.isGetPermission = isGetPermission;
        this.usbDevice = usbDevice;
    }


}

package com.future.getd.ui.bean;

import android.bluetooth.BluetoothDevice;

import com.jieli.bluetooth.bean.BleScanMessage;

public class ScanDevice {
    String name;
    String mac;
    int rssi;

    private BluetoothDevice device;
    private BleScanMessage mBleScanMessage;

    public ScanDevice(String name, String mac, int rssi, BluetoothDevice device, BleScanMessage mBleScanMessage) {
        this.name = name;
        this.mac = mac;
        this.rssi = rssi;
        this.device = device;
        this.mBleScanMessage = mBleScanMessage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public BleScanMessage getmBleScanMessage() {
        return mBleScanMessage;
    }

    public void setmBleScanMessage(BleScanMessage mBleScanMessage) {
        this.mBleScanMessage = mBleScanMessage;
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }
}

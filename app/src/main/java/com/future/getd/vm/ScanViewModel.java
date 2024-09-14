package com.future.getd.vm;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;

import androidx.lifecycle.MutableLiveData;

import com.future.getd.base.BaseViewModel;
import com.future.getd.base.BtBasicVM;
import com.future.getd.config.AppConstant;
import com.future.getd.log.LogUtils;
import com.future.getd.ui.bean.ScanDevice;
import com.jieli.bluetooth.bean.BleScanMessage;
import com.jieli.bluetooth.interfaces.rcsp.callback.BTRcspEventCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ScanViewModel extends BtBasicVM {
    public final MutableLiveData<Boolean> isHasPermission = new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> isScanning = new MutableLiveData<>(false);
    public MutableLiveData<List<ScanDevice>> scanDeviceList = new MutableLiveData<>();
    private BTRcspEventCallback btRcspEventCallback;
    private List<String> scanDeviceAddressList = new ArrayList<>();
    public ScanViewModel() {
        btRcspEventCallback = new BTRcspEventCallback() {
            //扫描回调onDiscoveryStatus、onShowDialog、onDiscovery、onDiscovery
            @Override
            public void onDiscoveryStatus(boolean bBle, boolean bStart) {
                super.onDiscoveryStatus(bBle, bStart);
                LogUtils.i("scan BTRcspEventCallback onDiscoveryStatus2 bBle : " + bBle + " , bStart : " + bStart);
                isScanning.setValue(bStart);
            }

            @Override
            public void onDiscovery(BluetoothDevice device) {
                super.onDiscovery(device);
                LogUtils.i("scan BTRcspEventCallback onDiscovery bBle : " + device);
            }

            @Override
            public void onDiscovery(BluetoothDevice device, BleScanMessage bleScanMessage) {
                super.onDiscovery(device, bleScanMessage);
                if (!scanDeviceAddressList.contains(device.getAddress())) {
                    scanDeviceAddressList.add(device.getAddress());
                    updateScanList(device, bleScanMessage);
                }
                LogUtils.i("scan BTRcspEventCallback onDiscovery bleScanMessage : " + bleScanMessage);
            }

            @Override
            public void onShowDialog(BluetoothDevice device, BleScanMessage bleScanMessage) {
                super.onShowDialog(device, bleScanMessage);
            }
        };
        mRCSPController.addBTRcspEventCallback(btRcspEventCallback);
    }

    public void resetCallback(){
        scanDeviceAddressList.clear();
//        if (btRcspEventCallback != null) {
//            mRCSPController.removeBTRcspEventCallback(btRcspEventCallback);
//        }
        mRCSPController.addBTRcspEventCallback(btRcspEventCallback);
    }

    @SuppressLint("MissingPermission")
    private void updateScanList(BluetoothDevice device, BleScanMessage bleScanMessage) {
        List<ScanDevice> scanDevices = scanDeviceList.getValue();
        if (scanDevices == null) {
            scanDevices = new ArrayList<>();
        }
        scanDevices.add(new ScanDevice(device.getName(),device.getAddress(),bleScanMessage.getRssi(),device,bleScanMessage));
        //将集合排序
        if (scanDevices != null) {
            Collections.sort(scanDevices, new order());
        }
        scanDeviceList.setValue(scanDevices);
    }
    public class order implements Comparator<ScanDevice> {
        @Override
        public int compare(ScanDevice lhs, ScanDevice rhs) {
            // TODO Auto-generated method stub
            return rhs.getRssi() - lhs.getRssi();
        }
    }

    private void startScan() {
        mRCSPController.startBleScan(AppConstant.SCAN_TIME);
    }
}

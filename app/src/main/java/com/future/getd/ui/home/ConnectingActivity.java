package com.future.getd.ui.home;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.future.getd.R;
import com.future.getd.base.BaseActivity;
import com.future.getd.databinding.ActivityConnectingBinding;
import com.future.getd.jl.rcsp.BTRcspHelper;
import com.future.getd.log.LogUtils;
import com.future.getd.ui.bean.DeviceSettings;
import com.future.getd.utils.SharePreferencesUtil;
import com.jieli.bluetooth.constant.StateCode;
import com.jieli.bluetooth.interfaces.rcsp.callback.BTRcspEventCallback;

import java.util.ArrayList;
import java.util.List;

public class ConnectingActivity extends BaseActivity<ActivityConnectingBinding> {
    private BTRcspEventCallback btConnectRcspEventCallback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected ActivityConnectingBinding getBinding() {
        return ActivityConnectingBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        Glide.with(this)
                .asGif()
                .load(R.raw.connecting)
                .into(binding.ivGif);

        binding.btReconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startConnect();
            }
        });
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void initData() {
        btConnectRcspEventCallback = new BTRcspEventCallback() {
            @Override
            public void onConnection(BluetoothDevice device, int status) {
                super.onConnection(device, status);
                //    public static final int CONNECTION_OK = 1;
                //    public static final int CONNECTION_FAILED = 2;
                //    public static final int CONNECTION_DISCONNECT = 0;
                //    public static final int CONNECTION_CONNECTING = 3;
                //    public static final int CONNECTION_CONNECTED = 4;
                LogUtils.e("ConnectingActivity onConnection: " + device.getName() + " , status : " + status);
                if (status == StateCode.CONNECTION_DISCONNECT) {//判断回连设备断开
//                    mUIHandler.postDelayed(() -> {
//                        if (!mUIHandler.hasMessages(MSG_RECONNECT_DEVICE_TIMEOUT)) { //未开始回连设备
//                            //回连设备
//                            mRCSPController.connectDevice(mReConnectDevice);
//                            //开始回连设备超时任务
//                            mUIHandler.sendEmptyMessageDelayed(MSG_RECONNECT_DEVICE_TIMEOUT, RECONNECT_TIMEOUT);
//                        }
//                    }, 1000);
                    binding.rlConnecting.setVisibility(View.GONE);
                    binding.llConnectFail.setVisibility(View.VISIBLE);
                } else if (status == StateCode.CONNECTION_OK || status == StateCode.CONNECTION_CONNECTED) {
                    //已连接
                    binding.rlConnecting.setVisibility(View.VISIBLE);
                    binding.llConnectFail.setVisibility(View.GONE);
                    List<DeviceSettings> deviceSettingsList = new ArrayList<>();
//                     deviceSettingsList = SharePreferencesUtil.getInstance().getDevicess();
                    DeviceSettings deviceSettings = new DeviceSettings(BTRcspHelper.getTargetDevice().getName()
                            ,BTRcspHelper.getTargetDevice().getAddress(),BTRcspHelper.getTargetBleScanMessage().getEdrAddr());
                    deviceSettings.setPrimary(true);
                    deviceSettings.setBleScanMessage(BTRcspHelper.getTargetBleScanMessage());
                    deviceSettingsList.add(deviceSettings);
                    SharePreferencesUtil.getInstance().setDevices(deviceSettingsList);
                    finish();
                }
            }

            @Override
            public void onA2dpStatus(BluetoothDevice device, int status) {
                super.onA2dpStatus(device, status);
                //     //BluetoothProfile
                //            //  int STATE_CONNECTED = 2;
                //            //    int STATE_CONNECTING = 1;
                //            //    int STATE_DISCONNECTED = 0;
                //            //    int STATE_DISCONNECTING = 3;
                //            //BluetoothProfile.STATE_DISCONNECTED
                LogUtils.i("ConnectingActivity onA2dpStatus: " + device.getName() + " , status : " + status);
            }

            @Override
            public void onSppStatus(BluetoothDevice device, int status) {
                super.onSppStatus(device, status);
                LogUtils.i("ConnectingActivity onSppStatus: " + device.getName() + " , status : " + status);
            }
        };
        rcspController.addBTRcspEventCallback(btConnectRcspEventCallback);
        startConnect();
    }

    private void startConnect() {
//        connectDevice();
        connectDeviceWithMessage();
    }

    @SuppressLint("MissingPermission")
    private void connectDevice() {
        if(BTRcspHelper.getTargetDevice() != null && BTRcspHelper.getTargetBleScanMessage() != null){
            LogUtils.i("ConnectingActivity start connectDevice name : " + BTRcspHelper.getTargetDevice().getName() + " , BLE address : " + BTRcspHelper.getTargetDevice().getAddress());
            rcspController.connectDevice(BTRcspHelper.getTargetDevice());
        }
    }
    @SuppressLint("MissingPermission")
    private void connectDeviceWithMessage() {
        if(BTRcspHelper.getTargetDevice() != null && BTRcspHelper.getTargetBleScanMessage() != null){
            LogUtils.i("ConnectingActivity start connectDeviceWithMessage name : " + BTRcspHelper.getTargetDevice().getName() + " , BLE address : " + BTRcspHelper.getTargetDevice().getAddress());
            BTRcspHelper.connectDeviceByMessage(rcspController,this,BTRcspHelper.getTargetDevice(),BTRcspHelper.getTargetBleScanMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rcspController.removeBTRcspEventCallback(btConnectRcspEventCallback);
    }
}
package com.future.getd.base;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.future.getd.MyApplication;
import com.future.getd.log.LogUtils;
import com.future.getd.utils.PermissionUtil;
import com.future.getd.utils.UIHelper;
import com.jieli.bluetooth.bean.BleScanMessage;
import com.jieli.bluetooth.bean.BluetoothOption;
import com.jieli.bluetooth.bean.base.CommandBase;
import com.jieli.bluetooth.bean.command.tws.NotifyAdvInfoCmd;
import com.jieli.bluetooth.bean.command.tws.RequestAdvOpCmd;
import com.jieli.bluetooth.bean.device.DeviceInfo;
import com.jieli.bluetooth.bean.parameter.NotifyAdvInfoParam;
import com.jieli.bluetooth.bean.parameter.RequestAdvOpParam;
import com.jieli.bluetooth.bean.response.ADVInfoResponse;
import com.jieli.bluetooth.constant.Command;
import com.jieli.bluetooth.constant.Constants;
import com.jieli.bluetooth.constant.StateCode;
import com.jieli.bluetooth.impl.BluetoothOperationImpl;
import com.jieli.bluetooth.impl.JL_BluetoothManager;
import com.jieli.bluetooth.impl.rcsp.RCSPController;
import com.jieli.bluetooth.interfaces.rcsp.callback.BTRcspEventCallback;
import com.jieli.bluetooth.utils.BluetoothUtil;

import java.util.List;

/**
 * 蓝牙操作基础类
 */
public class BtBasicVM extends BaseViewModel {
    protected final RCSPController mRCSPController = RCSPController.getInstance();
    public final MutableLiveData<Boolean> btAdapterMLD = new MutableLiveData<>(false);
    public final MutableLiveData<DeviceConnectionData> deviceConnectionMLD = new MutableLiveData<>();
    public final MutableLiveData<BluetoothDevice> switchDeviceMLD = new MutableLiveData<>();
    public final MutableLiveData<Integer> leftBattery = new MutableLiveData<>(0);
    public final MutableLiveData<Integer> rightBattery = new MutableLiveData<>(0);

    public BtBasicVM() {
        mRCSPController.addBTRcspEventCallback(mEventCallback);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        release();
    }

    public JL_BluetoothManager getBtManager() {
        return mRCSPController.getBluetoothManager();
    }

    public BluetoothOperationImpl getBtOp() {
        return (BluetoothOperationImpl) getBtManager().getBluetoothOperation();
    }

    public BluetoothOption getBluetoothOption() {
        return getBtManager().getBluetoothOption();
    }

    public BluetoothDevice getConnectedDevice() {
        return mRCSPController.getUsingDevice();
    }

    public DeviceInfo getDeviceInfo() {
        return mRCSPController.getDeviceInfo();
    }

    public DeviceInfo getDeviceInfo(String address){
        BluetoothDevice device = BluetoothUtil.getRemoteDevice(address);
        if(null == device) return null;
        return mRCSPController.getDeviceInfo(device);
    }

    public ADVInfoResponse getADVInfo(BluetoothDevice device){
        return mRCSPController.getADVInfo(device);
    }

    public boolean isDevConnected() {
        return mRCSPController.isDeviceConnected();
    }

    public boolean isDevConnecting() {
        return mRCSPController.isConnecting();
    }

    public boolean isConnectedDevice(String addr) {
        if(!BluetoothAdapter.checkBluetoothAddress(addr)) return false;
        List<BluetoothDevice> connectedDevices = getBtManager().getConnectedDeviceList();
        if(connectedDevices == null || connectedDevices.isEmpty()) return false;
        for (BluetoothDevice device : connectedDevices){
            if(null == device) continue;
            if(getBtManager().isMatchDevice(device.getAddress(), addr)){
                return true;
            }
        }
        return false;
    }

    public boolean isConnectingDevice(BluetoothDevice device) {
        if (null == device) return false;
        boolean ret = BluetoothUtil.deviceEquals(getBtOp().getConnectingDevice(), device);
        if (!ret) {
            ret = BluetoothUtil.deviceEquals(getBtOp().getConnectingBrEdrDevice(), device);
        }
        return ret;
    }

    public boolean isUsingDevice(String addr){
        boolean isConnected = false;
        if (addr != null && getConnectedDevice() != null) {
            isConnected = getBtManager().isMatchDevice(getConnectedDevice().getAddress(), addr);
        }
        return isConnected;
    }

    public int getDeviceConnection(BluetoothDevice device) {
        if (null == device) return -1;
        if (isConnectedDevice(device.getAddress())) {
            return StateCode.CONNECTION_OK;
        } else if (isConnectingDevice(device)) {
            return StateCode.CONNECTION_CONNECTING;
        } else {
            return StateCode.CONNECTION_DISCONNECT;
        }
    }

    public BluetoothDevice getMappedEdrDevice(BluetoothDevice device) {
        if (!PermissionUtil.checkHasConnectPermission(MyApplication.getInstance()))
            return null;
        if (null == device) return null;
        BluetoothDevice edrDevice;
        if (device.getType() != BluetoothDevice.DEVICE_TYPE_CLASSIC) {
            String edrAddr = getBtManager().getMappedDeviceAddress(device.getAddress());
            if (BluetoothAdapter.checkBluetoothAddress(edrAddr)) {
                edrDevice = BluetoothUtil.getRemoteDevice(edrAddr);
            } else {
                edrDevice = null;
            }
        } else {
            edrDevice = device;
        }
        return edrDevice;
    }

    protected void release() {
        if (mRCSPController != null) {
            mRCSPController.removeBTRcspEventCallback(mEventCallback);
        }
    }

    private final BTRcspEventCallback mEventCallback = new BTRcspEventCallback() {
        @Override
        public void onAdapterStatus(boolean bEnabled, boolean bHasBle) {
            btAdapterMLD.setValue(bEnabled);
        }

        @Override
        public void onConnection(BluetoothDevice device, int status) {
            //    //    public static final int CONNECTION_OK = 1;
            //                //    public static final int CONNECTION_FAILED = 2;
            //                //    public static final int CONNECTION_DISCONNECT = 0;
            //                //    public static final int CONNECTION_CONNECTING = 3;
            //                //    public static final int CONNECTION_CONNECTED = 4;
            if (null == device) return;
            deviceConnectionMLD.setValue(new DeviceConnectionData(device, status));
        }

        @Override
        public void onSwitchConnectedDevice(BluetoothDevice device) {
            switchDeviceMLD.setValue(device);
        }

        @Override
        public void onDeviceCommand(BluetoothDevice device, CommandBase cmd) {
            super.onDeviceCommand(device, cmd);
            LogUtils.i("basicVM BTRcspEventCallback onDeviceCommand device : " + device + " , cmd : " + cmd);
            try {
                if (cmd.getId() == Command.CMD_ADV_DEVICE_NOTIFY) {
                    NotifyAdvInfoCmd advInfoCmd = (NotifyAdvInfoCmd) cmd;
                    NotifyAdvInfoParam param = advInfoCmd.getParam();
                    BleScanMessage bleScanMessage = UIHelper.convertBleScanMsgFromNotifyADVInfo(param);
                    ADVInfoResponse response = UIHelper.convertADVInfoFromBleScanMessage(bleScanMessage);
                    int left = advInfoCmd.getParam().getLeftDeviceQuantity();
                    int right = advInfoCmd.getParam().getRightDeviceQuantity();

                    LogUtils.i("onDeviceCommand leftBattery : " + left + " , rightBattery : " + right);
                    leftBattery.setValue(left);
                    rightBattery.setValue(right);
//                mAdapter.updateHistoryDeviceByBtDevice(device, response);
                } else if (cmd.getId() == Command.CMD_ADV_DEV_REQUEST_OPERATION) {
                    RequestAdvOpCmd requestAdvOpCmd = (RequestAdvOpCmd) cmd;
                    RequestAdvOpParam param = requestAdvOpCmd.getParam();
                    if (param != null) {
                        switch (param.getOp()) {
                            case Constants.ADV_REQUEST_OP_UPDATE_CONFIGURE: {
                                getAdvInfo(device);
                                break;
                            }
                            case Constants.ADV_REQUEST_OP_UPDATE_AFTER_REBOOT: {
//                                mView.devNeedReboot();
                                break;
                            }
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    //         //advInfoResponse :ADVInfoResponse{pid=51, vid=1494, uid=2,
    //            // leftDeviceQuantity=0, isLeftCharging=false, rightDeviceQuantity=60, isRightCharging=false, chargingBinQuantity=0,
    //            // isDeviceCharging=false, deviceName='null', micChannel=0, workModel=0, mKeySettingsList=null, mLedSettingsList=null,
    //            // inEarSettings=0, language=null, modes=} CommonResponse{xmOpCode=-1}
    //            ADVInfoResponse advInfoResponse = RCSPController.getInstance().getADVInfo(RCSPController.getInstance().getUsingDevice());
    //            LogUtils.e("advInfoResponse :" + (advInfoResponse == null ? "" : advInfoResponse.toString()) );
    private void getAdvInfo(BluetoothDevice device) {
        ADVInfoResponse advInfoResponse = RCSPController.getInstance().getADVInfo(RCSPController.getInstance().getUsingDevice());
        LogUtils.e("advInfoResponse :" + (advInfoResponse == null ? "" : advInfoResponse.toString()) );
    }

    public static class DeviceConnectionData {
        @NonNull
        final BluetoothDevice device;
        final int status;

        public DeviceConnectionData(@NonNull BluetoothDevice device, int status) {
            this.device = device;
            this.status = status;
        }

        @NonNull
        public BluetoothDevice getDevice() {
            return device;
        }

        public int getStatus() {
            return status;
        }
    }
}

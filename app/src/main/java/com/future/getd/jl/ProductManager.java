package com.future.getd.jl;

import android.bluetooth.BluetoothDevice;

import com.future.getd.jl.rcsp.BTRcspHelper;
import com.future.getd.log.LogUtils;
import com.future.getd.ui.bean.DeviceSettings;
import com.jieli.bluetooth.bean.BleScanMessage;
import com.jieli.bluetooth.bean.VoiceData;
import com.jieli.bluetooth.bean.base.AttrBean;
import com.jieli.bluetooth.bean.base.BaseError;
import com.jieli.bluetooth.bean.base.CommandBase;
import com.jieli.bluetooth.bean.base.VoiceMode;
import com.jieli.bluetooth.bean.command.GetSysInfoCmd;
import com.jieli.bluetooth.bean.device.DevBroadcastMsg;
import com.jieli.bluetooth.bean.device.alarm.AlarmBean;
import com.jieli.bluetooth.bean.device.alarm.AlarmListInfo;
import com.jieli.bluetooth.bean.device.alarm.DefaultAlarmBell;
import com.jieli.bluetooth.bean.device.double_connect.ConnectedBtInfo;
import com.jieli.bluetooth.bean.device.double_connect.DoubleConnectionState;
import com.jieli.bluetooth.bean.device.eq.DynamicLimiterParam;
import com.jieli.bluetooth.bean.device.eq.EqInfo;
import com.jieli.bluetooth.bean.device.eq.EqPresetInfo;
import com.jieli.bluetooth.bean.device.eq.ReverberationParam;
import com.jieli.bluetooth.bean.device.fm.ChannelInfo;
import com.jieli.bluetooth.bean.device.fm.FmStatusInfo;
import com.jieli.bluetooth.bean.device.hearing.HearingAssistInfo;
import com.jieli.bluetooth.bean.device.hearing.HearingChannelsStatus;
import com.jieli.bluetooth.bean.device.light.LightControlInfo;
import com.jieli.bluetooth.bean.device.music.ID3MusicInfo;
import com.jieli.bluetooth.bean.device.music.MusicNameInfo;
import com.jieli.bluetooth.bean.device.music.MusicStatusInfo;
import com.jieli.bluetooth.bean.device.music.PlayModeInfo;
import com.jieli.bluetooth.bean.device.status.BatteryInfo;
import com.jieli.bluetooth.bean.device.status.DevStorageInfo;
import com.jieli.bluetooth.bean.device.voice.VoiceFunc;
import com.jieli.bluetooth.bean.device.voice.VolumeInfo;
import com.jieli.bluetooth.bean.parameter.SearchDevParam;
import com.jieli.bluetooth.bean.response.ADVInfoResponse;
import com.jieli.bluetooth.bean.response.SysInfoResponse;
import com.jieli.bluetooth.constant.Command;
import com.jieli.bluetooth.constant.StateCode;
import com.jieli.bluetooth.impl.rcsp.RCSPController;
import com.jieli.bluetooth.interfaces.rcsp.callback.BTRcspEventCallback;
import com.jieli.bluetooth.interfaces.rcsp.callback.OnRcspActionCallback;
import com.jieli.bluetooth.utils.CommandBuilder;
import com.jieli.bluetooth.utils.JL_Log;

import java.util.ArrayList;
import java.util.List;

public class ProductManager {
    private static ProductManager instance;
    private GetDRcspCallback callback;
    private final List<String> blockList = new ArrayList<>();
    public static DeviceSettings currentDevice;
    public static ProductManager getInstance() {
        if (instance == null) {
            instance = new ProductManager();
        }
        return instance;
    }

    public GetDRcspCallback getCallback(){
        if (callback == null) {
            callback = new GetDRcspCallback();
        }
        return callback;
    }

    class GetDRcspCallback extends BTRcspEventCallback{
        @Override
        public void onDeviceModeChange(BluetoothDevice device, int mode) {
            super.onDeviceModeChange(device, mode);
            LogUtils.i("BTRcspEventCallback onDeviceModeChange device : " + device + " , mode : " + mode);
        }

        @Override
        public void onVolumeChange(BluetoothDevice device, VolumeInfo volume) {
            super.onVolumeChange(device, volume);
            LogUtils.i("BTRcspEventCallback onVolumeChange device : " + device + " , mode : " + volume);
        }

        @Override
        public void onEqChange(BluetoothDevice device, EqInfo eqInfo) {
            super.onEqChange(device, eqInfo);
            LogUtils.i("BTRcspEventCallback onVolumeChange device : " + device + " , eqInfo : " + eqInfo);
        }

        @Override
        public void onDevStorageInfoChange(BluetoothDevice device, DevStorageInfo storageInfo) {
            super.onDevStorageInfoChange(device, storageInfo);
            LogUtils.i("BTRcspEventCallback onDevStorageInfoChange device : " + device + " , storageInfo : " + storageInfo);
        }

        @Override
        public void onFileFormatChange(BluetoothDevice device, String fileFormat) {
            super.onFileFormatChange(device, fileFormat);
            LogUtils.i("BTRcspEventCallback onFileFormatChange device : " + device + " , fileFormat : " + fileFormat);
        }

        @Override
        public void onMusicNameChange(BluetoothDevice device, MusicNameInfo nameInfo) {
            super.onMusicNameChange(device, nameInfo);
            LogUtils.i("BTRcspEventCallback onMusicNameChange device : " + device + " , nameInfo : " + nameInfo);
        }

        @Override
        public void onMusicStatusChange(BluetoothDevice device, MusicStatusInfo statusInfo) {
            super.onMusicStatusChange(device, statusInfo);
            LogUtils.i("BTRcspEventCallback BTRcspEventCallback device : " + device + " , statusInfo : " + statusInfo);
        }

        @Override
        public void onPlayModeChange(BluetoothDevice device, PlayModeInfo playModeInfo) {
            super.onPlayModeChange(device, playModeInfo);
            LogUtils.i("BTRcspEventCallback onPlayModeChange device : " + device + " , playModeInfo : " + playModeInfo);
        }

        @Override
        public void onBatteryChange(BluetoothDevice device, BatteryInfo batteryInfo) {
            super.onBatteryChange(device, batteryInfo);
            LogUtils.i("BTRcspEventCallback onBatteryChange device : " + device + " , batteryInfo : " + batteryInfo);
        }

        @Override
        public void onAuxStatusChange(BluetoothDevice device, boolean isPlay) {
            super.onAuxStatusChange(device, isPlay);
            LogUtils.i("BTRcspEventCallback onAuxStatusChange device : " + device + " , isPlay : " + isPlay);
        }

        @Override
        public void onFmChannelsChange(BluetoothDevice device, List<ChannelInfo> channels) {
            super.onFmChannelsChange(device, channels);
            LogUtils.i("BTRcspEventCallback onFmChannelsChange device : " + device + " , channels : " + channels);
        }

        @Override
        public void onFmStatusChange(BluetoothDevice device, FmStatusInfo fmStatusInfo) {
            super.onFmStatusChange(device, fmStatusInfo);
            LogUtils.i("BTRcspEventCallback onFmStatusChange device : " + device + " , fmStatusInfo : " + fmStatusInfo);
        }

        @Override
        public void onAlarmListChange(BluetoothDevice device, AlarmListInfo alarmListInfo) {
            super.onAlarmListChange(device, alarmListInfo);
            LogUtils.i("BTRcspEventCallback onAlarmListChange device : " + device + " , alarmListInfo : " + alarmListInfo);
        }

        @Override
        public void onAlarmDefaultBellListChange(BluetoothDevice device, List<DefaultAlarmBell> bells) {
            super.onAlarmDefaultBellListChange(device, bells);
            LogUtils.i("BTRcspEventCallback onAlarmDefaultBellListChange device : " + device + " , bells : " + bells);
        }

        @Override
        public void onAlarmNotify(BluetoothDevice device, AlarmBean alarmBean) {
            super.onAlarmNotify(device, alarmBean);
            LogUtils.i("BTRcspEventCallback onAlarmNotify device : " + device + " , alarmBean : " + alarmBean);
        }

        @Override
        public void onAlarmStop(BluetoothDevice device, AlarmBean alarmBean) {
            super.onAlarmStop(device, alarmBean);
            LogUtils.i("BTRcspEventCallback onAlarmStop device : " + device + " , alarmBean : " + alarmBean);
        }

        @Override
        public void onFrequencyTx(BluetoothDevice device, float frequency) {
            super.onFrequencyTx(device, frequency);
            LogUtils.i("BTRcspEventCallback onFrequencyTx device : " + device + " , frequency : " + frequency);
        }

        @Override
        public void onPeripheralsModeChange(BluetoothDevice device, int mode) {
            super.onPeripheralsModeChange(device, mode);
            LogUtils.i("BTRcspEventCallback onPeripheralsModeChange device : " + device + " , mode : " + mode);
        }

        @Override
        public void onPeripheralsConnectStatusChange(BluetoothDevice device, boolean connect, String mac) {
            super.onPeripheralsConnectStatusChange(device, connect, mac);
            LogUtils.i("BTRcspEventCallback onPeripheralsConnectStatusChange device : " + device + " , connect : " + connect);
        }

        @Override
        public void onID3MusicInfo(BluetoothDevice device, ID3MusicInfo id3MusicInfo) {
            super.onID3MusicInfo(device, id3MusicInfo);
            LogUtils.i("BTRcspEventCallback onID3MusicInfo device : " + device + " , id3MusicInfo : " + id3MusicInfo);
        }

        @Override
        public void onHighAndBassChange(BluetoothDevice device, int high, int bass) {
            super.onHighAndBassChange(device, high, bass);
            LogUtils.i("BTRcspEventCallback onHighAndBassChange device : " + device + " , high : " + high);
        }

        @Override
        public void onEqPresetChange(BluetoothDevice device, EqPresetInfo eqPresetInfo) {
            super.onEqPresetChange(device, eqPresetInfo);
            LogUtils.i("BTRcspEventCallback onEqPresetChange device : " + device + " , EqPresetInfo : " + eqPresetInfo);
        }

        @Override
        public void onPhoneCallStatusChange(BluetoothDevice device, int status) {
            super.onPhoneCallStatusChange(device, status);
            LogUtils.i("BTRcspEventCallback onPhoneCallStatusChange device : " + device + " , status : " + status);
        }

        @Override
        public void onExpandFunction(BluetoothDevice device, int type, byte[] data) {
            super.onExpandFunction(device, type, data);
            LogUtils.i("BTRcspEventCallback onExpandFunction device : " + device + " , type : " + type);
        }

        @Override
        public void onReverberation(BluetoothDevice device, ReverberationParam param) {
            super.onReverberation(device, param);
            LogUtils.i("BTRcspEventCallback onReverberation device : " + device + " , param : " + param);
        }

        @Override
        public void onDynamicLimiter(BluetoothDevice device, DynamicLimiterParam param) {
            super.onDynamicLimiter(device, param);
            LogUtils.i("BTRcspEventCallback onDynamicLimiter device : " + device + " , param : " + param);
        }

        @Override
        public void onLightControlInfo(BluetoothDevice device, LightControlInfo lightControlInfo) {
            super.onLightControlInfo(device, lightControlInfo);
            LogUtils.i("BTRcspEventCallback onLightControlInfo device : " + device + " , lightControlInfo : " + lightControlInfo);
        }

        @Override
        public void onSoundCardEqChange(BluetoothDevice device, EqInfo eqInfo) {
            super.onSoundCardEqChange(device, eqInfo);
            LogUtils.i("BTRcspEventCallback onSoundCardEqChange device : " + device + " , eqInfo : " + eqInfo);
        }

        @Override
        public void onSoundCardStatusChange(BluetoothDevice device, long mask, byte[] values) {
            super.onSoundCardStatusChange(device, mask, values);
            LogUtils.i("BTRcspEventCallback onSoundCardStatusChange device : " + device + " , mask : " + mask);
        }

        @Override
        public void onCurrentVoiceMode(BluetoothDevice device, VoiceMode voiceMode) {
            super.onCurrentVoiceMode(device, voiceMode);
            LogUtils.i("BTRcspEventCallback onCurrentVoiceMode device : " + device + " , voiceMode : " + voiceMode);
        }

        @Override
        public void onVoiceModeList(BluetoothDevice device, List<VoiceMode> voiceModes) {
            super.onVoiceModeList(device, voiceModes);
            LogUtils.i("BTRcspEventCallback onVoiceModeList device : " + device + " , voiceModes : " + voiceModes);
        }

        @Override
        public void onHearingAssistInfo(BluetoothDevice device, HearingAssistInfo hearingAssistInfo) {
            super.onHearingAssistInfo(device, hearingAssistInfo);
            LogUtils.i("onHearingAssistInfo onBatteryChange device : " + device + " , hearingAssistInfo : " + hearingAssistInfo);
        }

        @Override
        public void onHearingChannelsStatus(BluetoothDevice device, HearingChannelsStatus hearingChannelsStatus) {
            super.onHearingChannelsStatus(device, hearingChannelsStatus);
            LogUtils.i("onHearingChannelsStatus onBatteryChange device : " + hearingChannelsStatus + " , statusInfo : " + hearingChannelsStatus);
        }

        @Override
        public void onVoiceFunctionChange(BluetoothDevice device, VoiceFunc voiceFunc) {
            super.onVoiceFunctionChange(device, voiceFunc);
            LogUtils.i("onVoiceFunctionChange onBatteryChange device : " + device + " , voiceFunc : " + voiceFunc);
        }

        @Override
        public void onDoubleConnectionChange(BluetoothDevice device, DoubleConnectionState state) {
            super.onDoubleConnectionChange(device, state);
            LogUtils.i("onDoubleConnectionChange onBatteryChange device : " + device + " , state : " + state);
        }

        @Override
        public void onConnectedBtInfo(BluetoothDevice device, ConnectedBtInfo info) {
            super.onConnectedBtInfo(device, info);
            LogUtils.i("BTRcspEventCallback onConnectedBtInfo device : " + device + " , info : " + info);
        }

        public GetDRcspCallback() {
            super();
        }

        @Override
        public void onAdapterStatus(boolean bEnabled, boolean bHasBle) {
            super.onAdapterStatus(bEnabled, bHasBle);
            LogUtils.i("BTRcspEventCallback onAdapterStatus bEnabled : " + bEnabled + " , bHasBle : " + bHasBle);
        }

        @Override
        public void onDiscoveryStatus(boolean bBle, boolean bStart) {
            super.onDiscoveryStatus(bBle, bStart);
            LogUtils.i("BTRcspEventCallback onDiscoveryStatus bBle : " + bBle + " , bStart : " + bStart);
        }

        @Override
        public void onDiscovery(BluetoothDevice device) {
            super.onDiscovery(device);
//            LogUtils.i("BTRcspEventCallback onDiscovery device : " + device);
        }

        @Override
        public void onDiscovery(BluetoothDevice device, BleScanMessage bleScanMessage) {
            super.onDiscovery(device, bleScanMessage);
            //扫描回调设备用这里
//            LogUtils.i("BTRcspEventCallback onDiscovery device : " + device + " , bleScanMessage : " + bleScanMessage);
        }

        @Override
        public void onShowDialog(BluetoothDevice device, BleScanMessage bleScanMessage) {
            super.onShowDialog(device, bleScanMessage);
//            LogUtils.i("BTRcspEventCallback onShowDialog device : " + device + " , bleScanMessage : " + bleScanMessage);
        }

        @Override
        public void onBondStatus(BluetoothDevice device, int status) {
            super.onBondStatus(device, status);
            LogUtils.i("BTRcspEventCallback onBondStatus device : " + device + " , status : " + status);
        }

        @Override
        public void onConnection(BluetoothDevice device, int status) {
            super.onConnection(device, status);
            blockList.remove(device.getAddress());
            //CONNECTION_OK
            LogUtils.e("BTRcspEventCallback onConnection device : " + device + " , status : " + status);
            BTRcspHelper.setCurrentDevice(device);
        }

        @Override
        public void onSwitchConnectedDevice(BluetoothDevice device) {
            super.onSwitchConnectedDevice(device);
            LogUtils.i("BTRcspEventCallback onSwitchConnectedDevice device : " + device);
        }

        @Override
        public void onA2dpStatus(BluetoothDevice device, int status) {
            super.onA2dpStatus(device, status);
            //BluetoothProfile
            //  int STATE_CONNECTED = 2;
            //    int STATE_CONNECTING = 1;
            //    int STATE_DISCONNECTED = 0;
            //    int STATE_DISCONNECTING = 3;
            //BluetoothProfile.STATE_DISCONNECTED
            LogUtils.i("BTRcspEventCallback onA2dpStatus device : " + device + " , status : " + status);
        }

        @Override
        public void onHfpStatus(BluetoothDevice device, int status) {
            super.onHfpStatus(device, status);
            LogUtils.i("BTRcspEventCallback onHfpStatus device : " + device + " , status : " + status);
        }

        @Override
        public void onSppStatus(BluetoothDevice device, int status) {
            super.onSppStatus(device, status);
            LogUtils.i("BTRcspEventCallback onSppStatus device : " + device + " , status : " + status);
        }

        @Override
        public void onDeviceCommand(BluetoothDevice device, CommandBase cmd) {
            super.onDeviceCommand(device, cmd);
            //电量变化 onDeviceCommand: BTRcspEventCallback onDeviceCommand device : 73:A7:AE:AF:F3:1C ,
            // cmd : CommandBase{OpCodeSn=82, opCode=194, name='NotifyAdvInfoCmd', type=1, status=0,
            // param=NotifyAdvInfoParam{pid=51, vid=1494, uid=2, chipType=2, version=2,
            // showDialog=false, edrAddr='73:A7:AE:AF:F3:1C', seq=118, action=2, leftDeviceQuantity=0, isLeftCharging=false, rightDeviceQuantity=50,
            // isRightCharging=false, chargingBinQuantity=0, isDeviceCharging=false} BaseParameter{xmOpCode=0, paramData=null}, response=null}

            LogUtils.i("BTRcspEventCallback onDeviceCommand device : " + device + " , cmd : " + cmd);
            if (cmd.getId() == Command.CMD_ADV_DEVICE_NOTIFY) {
                if (blockList.contains(device.getAddress())) {
                    return;
                }
                blockList.add(device.getAddress());
                RCSPController.getInstance().controlAdvBroadcast(device, false, new OnRcspActionCallback<Boolean>() {
                    @Override
                    public void onSuccess(BluetoothDevice bluetoothDevice, Boolean aBoolean) {
                        LogUtils.i("controlAdvBroadcast onSuccess : " + aBoolean);
                    }

                    @Override
                    public void onError(BluetoothDevice bluetoothDevice, BaseError baseError) {
                        LogUtils.i("controlAdvBroadcast onError : " + baseError);
                    }
                });
            }else if (cmd.getId() == Command.CMD_GET_SYS_INFO) {
//                GetSysInfoCmd getSysInfoCmd = (GetSysInfoCmd) cmd;
//                if (getSysInfoCmd.getStatus() == StateCode.STATUS_SUCCESS) {
//                    SysInfoResponse sysInfoResponse = getSysInfoCmd.getResponse();
//                    List<AttrBean> list = sysInfoResponse.getAttrs();
//                } else {
//                    JL_Log.w("sen", cmd.toString());
//                }
            }
        }

        @Override
        public void onDeviceData(BluetoothDevice device, byte[] data) {
            super.onDeviceData(device, data);
            LogUtils.i("BTRcspEventCallback onDeviceData device : " + device + " , data : " + data.length);
        }

        @Override
        public void onDeviceVoiceData(BluetoothDevice device, byte[] data) {
            super.onDeviceVoiceData(device, data);
            LogUtils.i("BTRcspEventCallback onDeviceVoiceData device : " + device + " , data : " + data);
        }

        @Override
        public void onDeviceVoiceData(BluetoothDevice device, VoiceData data) {
            super.onDeviceVoiceData(device, data);
            LogUtils.i("BTRcspEventCallback onDeviceVoiceData device : " + device + " , data : " + data);
        }

        @Override
        public void onDeviceVadEnd(BluetoothDevice device) {
            super.onDeviceVadEnd(device);
            LogUtils.i("BTRcspEventCallback onDeviceVadEnd device : " + device);
        }

        @Override
        public void onDeviceResponse(BluetoothDevice device, CommandBase response) {
            super.onDeviceResponse(device, response);
            LogUtils.i("BTRcspEventCallback onDeviceResponse device : " + device + " , response : " + response);
        }

        @Override
        public void onError(BaseError error) {
            super.onError(error);
            // BTRcspEventCallback onError error : BaseError{code=5, subCode=20481, opCode=0, message='auth device failed.'}
            LogUtils.i("BTRcspEventCallback onError error : " + error);
        }

        @Override
        public void onDeviceBroadcast(BluetoothDevice device, DevBroadcastMsg broadcast) {
            super.onDeviceBroadcast(device, broadcast);
//            LogUtils.i("BTRcspEventCallback onDeviceBroadcast device : " + device + " , broadcast : " + broadcast);
        }

        @Override
        public void onDeviceSettingsInfo(BluetoothDevice device, int mask, ADVInfoResponse dataInfo) {
            super.onDeviceSettingsInfo(device, mask, dataInfo);
            LogUtils.i("BTRcspEventCallback onDeviceSettingsInfo device : " + device + " , dataInfo : " + dataInfo);
        }

        @Override
        public void onTwsStatusChange(BluetoothDevice device, boolean isTwsConnected) {
            super.onTwsStatusChange(device, isTwsConnected);
            LogUtils.i("BTRcspEventCallback onTwsStatusChange device : " + device + " , isTwsConnected : " + isTwsConnected);
        }

        @Override
        public void onDeviceRequestOp(BluetoothDevice device, int op) {
            super.onDeviceRequestOp(device, op);
            LogUtils.i("BTRcspEventCallback onDeviceRequestOp device : " + device + " , op : " + op);
        }

        @Override
        public void onMandatoryUpgrade(BluetoothDevice device) {
            super.onMandatoryUpgrade(device);
            LogUtils.i("BTRcspEventCallback onMandatoryUpgrade device : " + device);
        }

        @Override
        public void onSearchDevice(BluetoothDevice device, SearchDevParam searchDevParam) {
            super.onSearchDevice(device, searchDevParam);
            LogUtils.i("BTRcspEventCallback onSearchDevice device : " + device + " , searchDevParam : " + searchDevParam);
            //到时停止寻找设备上报
            //onSearchDevice: BTRcspEventCallback onSearchDevice device : 73:A7:AE:AF:F3:1C ,
            // searchDevParam : SearchDevParam{type=0, op=0, timeoutSec=0} BaseParameter{xmOpCode=0, paramData=null}
        }
    }

}

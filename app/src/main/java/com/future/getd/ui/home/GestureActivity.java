package com.future.getd.ui.home;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.View;

import com.future.getd.R;
import com.future.getd.base.BaseActivity;
import com.future.getd.databinding.ActivityGestureBinding;
import com.future.getd.jl.ProductManager;
import com.future.getd.log.LogUtils;
import com.future.getd.ui.bean.ButtonFunction;
import com.future.getd.ui.bean.DeviceSettings;
import com.future.getd.ui.view.pop.SelectListPop;
import com.future.getd.utils.SharePreferencesUtil;
import com.jieli.bluetooth.bean.base.BaseError;
import com.jieli.bluetooth.bean.response.ADVInfoResponse;
import com.jieli.bluetooth.interfaces.rcsp.callback.BTRcspEventCallback;
import com.jieli.bluetooth.interfaces.rcsp.callback.OnRcspActionCallback;

import java.util.ArrayList;
import java.util.List;

public class GestureActivity extends BaseActivity<ActivityGestureBinding> {
    private DeviceSettings deviceSettings;
    private List<ADVInfoResponse.KeySettings> keySettings = new ArrayList<>();
    BTRcspEventCallback callback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected ActivityGestureBinding getBinding() {
        return ActivityGestureBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        binding.title.tvTitle.setText(getString(R.string.gesture));
        binding.title.ivBack.setOnClickListener(v -> {
            finish();
        });
        binding.title.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //test
                List<ADVInfoResponse.KeySettings> keySettings = new ArrayList<>();
                ADVInfoResponse.KeySettings keySettings1 = new ADVInfoResponse.KeySettings();
                keySettings1.setKeyNum(ButtonFunction.DEVICE_KEY_NUM_LEFT);
                keySettings1.setAction(ButtonFunction.DEVICE_KEY_ACTION_SINGLE_TAP);
                keySettings1.setFunction(ButtonFunction.DEVICE_KEY_FUNCTION_NONE);
                ADVInfoResponse.KeySettings keySettings2 = new ADVInfoResponse.KeySettings();
                keySettings2.setKeyNum(ButtonFunction.DEVICE_KEY_NUM_RIGHT);
                keySettings2.setAction(ButtonFunction.DEVICE_KEY_ACTION_SINGLE_TAP);
                keySettings2.setFunction(ButtonFunction.DEVICE_KEY_FUNCTION_PREVIOUS);
                ADVInfoResponse.KeySettings keySettings3 = new ADVInfoResponse.KeySettings();
                keySettings3.setKeyNum(ButtonFunction.DEVICE_KEY_NUM_LEFT);
                keySettings3.setAction(ButtonFunction.DEVICE_KEY_ACTION_DOUBLE_TAP);
                keySettings3.setFunction(ButtonFunction.DEVICE_KEY_FUNCTION_NEXT);
                ADVInfoResponse.KeySettings keySettings4 = new ADVInfoResponse.KeySettings();
                keySettings4.setKeyNum(ButtonFunction.DEVICE_KEY_NUM_RIGHT);
                keySettings4.setAction(ButtonFunction.DEVICE_KEY_ACTION_DOUBLE_TAP);
                keySettings4.setFunction(ButtonFunction.DEVICE_KEY_FUNCTION_PLAY_PAUSE);
                keySettings.add(keySettings1);
                keySettings.add(keySettings2);
                keySettings.add(keySettings3);
                keySettings.add(keySettings4);
                LogUtils.i("gesture configKeySettings  keySettings:" + keySettings);
                rcspController.configKeySettings(rcspController.getUsingDevice(), keySettings, new OnRcspActionCallback<Integer>() {
                    @Override
                    public void onSuccess(BluetoothDevice bluetoothDevice, Integer integer) {
                        LogUtils.e("gesture configKeySettings onSuccess integer :" + integer);
                    }

                    @Override
                    public void onError(BluetoothDevice bluetoothDevice, BaseError baseError) {
                        LogUtils.e("gesture configKeySettings onError baseError :" + baseError);
                    }
                });
            }
        });
    }

    @Override
    protected void initData() {
        deviceSettings = SharePreferencesUtil.getInstance().getDevicesByClassMac(ProductManager.currentDevice.getClassicMac());
        keySettings = deviceSettings.getKeySettingsList();
        if(keySettings == null || keySettings.isEmpty()){
            keySettings = getDefaultKeyList();
        }

        updateKeySettingsUI(keySettings);
        callback = new BTRcspEventCallback() {
            @Override
            public void onDeviceSettingsInfo(BluetoothDevice device, int mask, ADVInfoResponse dataInfo) {
                super.onDeviceSettingsInfo(device, mask, dataInfo);
                LogUtils.i("GestureActivity onDeviceSettingsInfo device : " + device + " , dataInfo : " + dataInfo);
                List<ADVInfoResponse.KeySettings> settings = dataInfo.getKeySettingsList();
                keySettings = settings;
                deviceSettings.setKeySettingsList(settings);
                updateKeySettingsUI(settings);
                SharePreferencesUtil.getInstance().updateSettingsByClassMac(GestureActivity.this,deviceSettings.getClassicMac(),deviceSettings);
            }
        };
        //左耳机单击
        binding.ctlLeft.setOnClickListener(v -> {
            SelectListPop selectListPopup = new SelectListPop(GestureActivity.this,getString(R.string.single_click),
                    getListByMap(ButtonFunction.DEVICE_KEY_NUM_LEFT,ButtonFunction.DEVICE_KEY_ACTION_SINGLE_TAP,
                            searchFunction(keySettings,ButtonFunction.DEVICE_KEY_NUM_LEFT,ButtonFunction.DEVICE_KEY_ACTION_SINGLE_TAP)));
            selectListPopup.setOnSelectListener(new SelectListPop.OnSelectListener() {
                @Override
                public void onSelect(int position, String text,int value) {
                    binding.tvLeft.setText(text);
                    updateKeySettingsData(ButtonFunction.DEVICE_KEY_NUM_LEFT,ButtonFunction.DEVICE_KEY_ACTION_SINGLE_TAP,value);
                    //set to device
                    setToDevice();
                }
            });
            selectListPopup.show(binding.main);
        });
        //右耳机单击
        binding.ctlRight.setOnClickListener(v -> {
            SelectListPop selectListPopup = new SelectListPop(GestureActivity.this,getString(R.string.single_click),
                    getListByMap(ButtonFunction.DEVICE_KEY_NUM_RIGHT,ButtonFunction.DEVICE_KEY_ACTION_SINGLE_TAP,
                            searchFunction(keySettings,ButtonFunction.DEVICE_KEY_NUM_LEFT,ButtonFunction.DEVICE_KEY_ACTION_SINGLE_TAP)));
            selectListPopup.setOnSelectListener(new SelectListPop.OnSelectListener() {
                @Override
                public void onSelect(int position, String text,int value) {
                    binding.tvRight.setText(text);
                    updateKeySettingsData(ButtonFunction.DEVICE_KEY_NUM_RIGHT,ButtonFunction.DEVICE_KEY_ACTION_SINGLE_TAP,value);
                    //set to device
                    setToDevice();
                }
            });
            selectListPopup.show(binding.main);
        });
        //左耳机双击
        binding.ctlDoubleLeft.setOnClickListener(v -> {
            SelectListPop selectListPopup = new SelectListPop(GestureActivity.this,getString(R.string.double_click),
                    getListByMap(ButtonFunction.DEVICE_KEY_NUM_LEFT,ButtonFunction.DEVICE_KEY_ACTION_DOUBLE_TAP,
                            searchFunction(keySettings,ButtonFunction.DEVICE_KEY_NUM_LEFT,ButtonFunction.DEVICE_KEY_ACTION_DOUBLE_TAP)));
            selectListPopup.setOnSelectListener(new SelectListPop.OnSelectListener() {
                @Override
                public void onSelect(int position, String text,int value) {
                    binding.tvDoubleLeft.setText(text);
                    updateKeySettingsData(ButtonFunction.DEVICE_KEY_NUM_LEFT,ButtonFunction.DEVICE_KEY_ACTION_DOUBLE_TAP,value);
                    //set to device
                    setToDevice();
                }
            });
            selectListPopup.show(binding.main);
        });

        //左耳机双击
        binding.ctlDoubleRight.setOnClickListener(v -> {
            SelectListPop selectListPopup = new SelectListPop(GestureActivity.this,getString(R.string.double_click),
                    getListByMap(ButtonFunction.DEVICE_KEY_NUM_RIGHT,ButtonFunction.DEVICE_KEY_ACTION_DOUBLE_TAP,
                            searchFunction(keySettings,ButtonFunction.DEVICE_KEY_NUM_RIGHT,ButtonFunction.DEVICE_KEY_ACTION_DOUBLE_TAP)));
            selectListPopup.setOnSelectListener(new SelectListPop.OnSelectListener() {
                @Override
                public void onSelect(int position, String text,int value) {
                    binding.tvDoubleRight.setText(text);
                    updateKeySettingsData(ButtonFunction.DEVICE_KEY_NUM_RIGHT,ButtonFunction.DEVICE_KEY_ACTION_DOUBLE_TAP,value);
                    //set to device
                    setToDevice();
                }
            });
            selectListPopup.show(binding.main);
        });

        //gesture advInfoResponse :ADVInfoResponse{pid=51, vid=1494, uid=2, leftDeviceQuantity=80,
        // isLeftCharging=false, rightDeviceQuantity=80, isRightCharging=false, chargingBinQuantity=0, isDeviceCharging=false, deviceName='null',
        // micChannel=0, workModel=0, mKeySettingsList=null, mLedSettingsList=null, inEarSettings=0, language=null, modes=} CommonResponse{xmOpCode=-1}


        //private int keyNum; //按键序号
        //        private int action;//动作标识
        //        private int function;//功能标识
        //updateDeviceSettings :: device : DB:9A:DF:3E:E5:B7,
        //advInfo : ADVInfoResponse{pid=51, vid=1494, uid=2, leftDeviceQuantity=90, isLeftCharging=false, rightDeviceQuantity=80, isRightCharging=false,
        // chargingBinQuantity=0, isDeviceCharging=false, deviceName='GetD Eyewear', micChannel=0, workModel=0,
        // mKeySettingsList=[KeySettings{, keyNum=1, action=1, function=3}, KeySettings{, keyNum=2, action=1, function=7},
        // KeySettings{, keyNum=1, action=2, function=10}, KeySettings{, keyNum=2, action=2, function=9}],
        // mLedSettingsList=[LedSettings{scene=1, effect=6}, LedSettings{scene=2, effect=5},
        // LedSettings{scene=3, effect=4}], inEarSettings=0, language=null, modes=000102}
        // CommonResponse{xmOpCode=-1}, true
        rcspController.addBTRcspEventCallback(callback);
        ADVInfoResponse advInfoResponse = rcspController.getADVInfo(rcspController.getUsingDevice());
        LogUtils.e("gesture advInfoResponse :" + (advInfoResponse == null ? "null" : advInfoResponse.toString()));
        rcspController.getDeviceSettingsInfo(rcspController.getUsingDevice(), 0xffffffff,null);
    }

    private void setToDevice() {
        if(rcspController.isDeviceConnected()){
            rcspController.configKeySettings(rcspController.getUsingDevice(), keySettings, new OnRcspActionCallback<Integer>() {
                @Override
                public void onSuccess(BluetoothDevice bluetoothDevice, Integer integer) {
                        LogUtils.i("configKeySettings onSuccess");
                }

                @Override
                public void onError(BluetoothDevice bluetoothDevice, BaseError baseError) {
                    LogUtils.i("configKeySettings onError : " + baseError);
                }
            });
        }
    }

    private void updateKeySettingsData(int num, int action, int value) {
        try {
            List<ADVInfoResponse.KeySettings> keySettingsList = keySettings;
            LogUtils.i("updateKeySettingsData before : " + keySettings + " , num :" + num  + " , action : " + action + " , value : " + value);
            for (ADVInfoResponse.KeySettings keySetting : keySettings) {
                if(keySetting != null){
                    if(num == keySetting.getKeyNum() && action == keySetting.getAction()){
                        int functionId = keySetting.getFunction();
                        keySetting.setFunction(value);
                    }
                }
            }
            deviceSettings.setKeySettingsList(keySettings);
            LogUtils.i("updateKeySettingsData before : " + keySettings + " , num :" + num  + " , action : " + action + " , value : " + value);
        } catch (Exception e) {
            LogUtils.e("updateKeySettingsData callback onButtonFunctionChange error : " + e);
        }
    }

    private List<ADVInfoResponse.KeySettings> getDefaultKeyList() {
        List<ADVInfoResponse.KeySettings> keySettings = new ArrayList<>();
        ADVInfoResponse.KeySettings keySettings1 = new ADVInfoResponse.KeySettings();
        keySettings1.setKeyNum(ButtonFunction.DEVICE_KEY_NUM_LEFT);
        keySettings1.setAction(ButtonFunction.DEVICE_KEY_ACTION_SINGLE_TAP);
        keySettings1.setFunction(ButtonFunction.DEVICE_KEY_FUNCTION_NONE);
        ADVInfoResponse.KeySettings keySettings2 = new ADVInfoResponse.KeySettings();
        keySettings2.setKeyNum(ButtonFunction.DEVICE_KEY_NUM_RIGHT);
        keySettings2.setAction(ButtonFunction.DEVICE_KEY_ACTION_SINGLE_TAP);
        keySettings2.setFunction(ButtonFunction.DEVICE_KEY_FUNCTION_PREVIOUS);
        ADVInfoResponse.KeySettings keySettings3 = new ADVInfoResponse.KeySettings();
        keySettings3.setKeyNum(ButtonFunction.DEVICE_KEY_NUM_LEFT);
        keySettings3.setAction(ButtonFunction.DEVICE_KEY_ACTION_DOUBLE_TAP);
        keySettings3.setFunction(ButtonFunction.DEVICE_KEY_FUNCTION_NEXT);
        ADVInfoResponse.KeySettings keySettings4 = new ADVInfoResponse.KeySettings();
        keySettings4.setKeyNum(ButtonFunction.DEVICE_KEY_NUM_RIGHT);
        keySettings4.setAction(ButtonFunction.DEVICE_KEY_ACTION_DOUBLE_TAP);
        keySettings4.setFunction(ButtonFunction.DEVICE_KEY_FUNCTION_PLAY_PAUSE);
        keySettings.add(keySettings1);
        keySettings.add(keySettings2);
        keySettings.add(keySettings3);
        keySettings.add(keySettings4);
        return keySettings;
    }

    private void updateKeySettingsUI(List<ADVInfoResponse.KeySettings> keySettings) {
        if(keySettings == null || keySettings.isEmpty()){
            return;
        }
        for (int i = 0; i < keySettings.size(); i++) {
            ADVInfoResponse.KeySettings settings = keySettings.get(i);
            int num = settings.getKeyNum();
            int action = settings.getAction();
            int function = settings.getFunction();
            if (num == ButtonFunction.DEVICE_KEY_NUM_LEFT) {
                if (action == ButtonFunction.DEVICE_KEY_ACTION_SINGLE_TAP) {
                    binding.tvLeft.setText(getFunctionStr(function));
                } else if (action == ButtonFunction.DEVICE_KEY_ACTION_DOUBLE_TAP) {
                    binding.tvDoubleLeft.setText(getFunctionStr(function));
                }
            } else if (num == ButtonFunction.DEVICE_KEY_NUM_RIGHT) {
                if (action == ButtonFunction.DEVICE_KEY_ACTION_SINGLE_TAP) {
                    binding.tvRight.setText(getFunctionStr(function));
                } else if (action == ButtonFunction.DEVICE_KEY_ACTION_DOUBLE_TAP) {
                    binding.tvDoubleRight.setText(getFunctionStr(function));
                }
            }
        }
    }

    private int searchFunction(List<ADVInfoResponse.KeySettings> keySettings,int numIndex,int actionIndex) {
        int function = ButtonFunction.DEVICE_KEY_FUNCTION_NONE;
        if(keySettings == null || keySettings.isEmpty()){
            return function;
        }
        for (int i = 0; i < keySettings.size(); i++) {
            ADVInfoResponse.KeySettings settings = keySettings.get(i);
            int num = settings.getKeyNum();
            int action = settings.getAction();
            function = settings.getFunction();
            if(num == numIndex && action == actionIndex){
                return function;
            }
        }
        return function;
    }

    private String getFunctionStr(int function){
        String functionStr = getString(R.string.fun_none);
        if (function == ButtonFunction.DEVICE_KEY_FUNCTION_NONE){
            functionStr = getString(R.string.fun_none);
        }else if (function == ButtonFunction.DEVICE_KEY_FUNCTION_PREVIOUS){
            functionStr = getString(R.string.fun_pre);
        }else if (function == ButtonFunction.DEVICE_KEY_FUNCTION_NEXT){
            functionStr = getString(R.string.fun_next);
        }else if (function == ButtonFunction.DEVICE_KEY_FUNCTION_PLAY_PAUSE){
            functionStr = getString(R.string.fun_play_pause);
        }else if (function == ButtonFunction.DEVICE_KEY_FUNCTION_ANSWER_CALL){
            functionStr = getString(R.string.fun_answer);
        }else if (function == ButtonFunction.DEVICE_KEY_FUNCTION_HANG_UP){
            functionStr = getString(R.string.fun_hand_up);
        }else if (function == ButtonFunction.DEVICE_KEY_FUNCTION_VOLUME_UP){
            functionStr = getString(R.string.fun_volume_up);
        }else if (function == ButtonFunction.DEVICE_KEY_FUNCTION_VOLUME_DOWN){
            functionStr = getString(R.string.fun_volume_down);
        }
        return functionStr;
    }


    private List<SelectListPop.Item> getListByMap(int num, int action, int function) {
        int index = 0;
        List<SelectListPop.Item> list = new ArrayList<>();
        list.add(new SelectListPop.Item(getResources().getString(R.string.fun_none), false, ButtonFunction.DEVICE_KEY_FUNCTION_NONE));
        list.add(new SelectListPop.Item(getResources().getString(R.string.fun_pre), false, ButtonFunction.DEVICE_KEY_FUNCTION_PREVIOUS));
        list.add(new SelectListPop.Item(getResources().getString(R.string.fun_next), false, ButtonFunction.DEVICE_KEY_FUNCTION_NEXT));
        list.add(new SelectListPop.Item(getResources().getString(R.string.fun_play_pause), false, ButtonFunction.DEVICE_KEY_FUNCTION_PLAY_PAUSE));
        list.add(new SelectListPop.Item(getResources().getString(R.string.fun_answer),false,ButtonFunction.DEVICE_KEY_FUNCTION_ANSWER_CALL));
        list.add(new SelectListPop.Item(getResources().getString(R.string.fun_hand_up),false,ButtonFunction.DEVICE_KEY_FUNCTION_HANG_UP));
//        list.add(new SelectListPop.Item(getResources().getString(R.string.fun_callback),false,ButtonFunction.FUNID_CALLBACK_CALL));
        list.add(new SelectListPop.Item(getResources().getString(R.string.fun_volume_up), false, ButtonFunction.DEVICE_KEY_FUNCTION_VOLUME_UP));
        list.add(new SelectListPop.Item(getResources().getString(R.string.fun_volume_down), false, ButtonFunction.DEVICE_KEY_FUNCTION_VOLUME_DOWN));
        try {
            List<ADVInfoResponse.KeySettings> settingsList = keySettings;
            for (ADVInfoResponse.KeySettings keySetting : settingsList) {
                if(keySetting != null){
                    if(num == keySetting.getKeyNum() && action == keySetting.getAction()){
                        int functionId = keySetting.getFunction();
                        if (functionId == ButtonFunction.DEVICE_KEY_FUNCTION_NONE) {
                            index = 0;
                        } else if (functionId == ButtonFunction.DEVICE_KEY_FUNCTION_PREVIOUS) {
                            index = 1;
                        } else if (functionId == ButtonFunction.DEVICE_KEY_FUNCTION_NEXT) {
                            index = 2;
                        } else if (functionId == ButtonFunction.DEVICE_KEY_FUNCTION_PLAY_PAUSE) {
                            index = 3;
                        } else if (functionId == ButtonFunction.DEVICE_KEY_FUNCTION_ANSWER_CALL) {
                            index = 4;
                        } else if (functionId == ButtonFunction.DEVICE_KEY_FUNCTION_HANG_UP) {
                            index = 5;
                        } else if (functionId == ButtonFunction.DEVICE_KEY_FUNCTION_VOLUME_UP) {
                            index = 6;
                        } else if (functionId == ButtonFunction.DEVICE_KEY_FUNCTION_VOLUME_DOWN) {
                            index = 7;
                        }
                    }
                }
            }
        } catch (Exception e) {
            LogUtils.e("headset callback onButtonFunctionChange error : " + e);
        }

        if (index < list.size()) {
            list.get(index).isSelect = true;
        }
        return list;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deviceSettings.setKeySettingsList(keySettings);
        SharePreferencesUtil.getInstance().updateSettingsByClassMac(GestureActivity.this,deviceSettings.getClassicMac(),deviceSettings);
    }
}
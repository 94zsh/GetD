package com.future.getd.vm;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.future.getd.base.BtBasicVM;
import com.future.getd.database.DataRepository;
import com.future.getd.database.entity.BindDevice;
import com.future.getd.jl.ProductManager;
import com.future.getd.log.LogUtils;
import com.future.getd.ui.bean.DeviceSettings;
import com.future.getd.utils.SharePreferencesUtil;

import java.util.List;

public class MainViewModel extends BtBasicVM {
    public final MutableLiveData<Boolean> isBindDevice = new MutableLiveData<>(false);
    public MutableLiveData<List<DeviceSettings>> mBindDeviceList = new MutableLiveData<>();
    public MutableLiveData<DeviceSettings> mainBindDevice = new MutableLiveData<>();
    public MainViewModel() {

    }

    public MainViewModel(Application application) {
    }

    public void updateBindState(boolean isBind){
        isBindDevice.setValue(isBind);
    }

    public void getBindDevice(){
        List<DeviceSettings> deviceSettingsList = SharePreferencesUtil.getInstance().getDevicess();
        if(!deviceSettingsList.isEmpty()){
            isBindDevice.setValue(true);
            mBindDeviceList.setValue(deviceSettingsList);
        }
    }
    public void getMainBindDevice(){
        List<DeviceSettings> deviceSettingsList = SharePreferencesUtil.getInstance().getDevicess();
        if(!deviceSettingsList.isEmpty()){
            for (int i = 0; i < deviceSettingsList.size(); i++) {
                DeviceSettings deviceSettings = deviceSettingsList.get(i);
                LogUtils.e("绑定设备  :  " + i + " , " + deviceSettings);
                if (deviceSettings.isPrimary() || deviceSettingsList.size() == 1) {
                    mainBindDevice.setValue(deviceSettings);
                    ProductManager.currentDevice = deviceSettings;
                }
            }
        }
    }
}
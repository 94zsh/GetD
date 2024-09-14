package com.future.getd.ui.home;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.future.getd.base.BtBasicVM;
import com.future.getd.database.DataRepository;
import com.future.getd.database.entity.BindDevice;
import com.future.getd.ui.bean.DeviceSettings;
import com.future.getd.utils.SharePreferencesUtil;

import java.util.List;

public class HomeViewModel extends BtBasicVM {
    public DataRepository mRepository;
    public final MutableLiveData<Boolean> isBindDevice = new MutableLiveData<>(false);
    public LiveData<List<BindDevice>> mBindDevice = new MutableLiveData<>();
    public MutableLiveData<List<DeviceSettings>> mBindDeviceList = new MutableLiveData<>();
    public HomeViewModel() {

    }

    public HomeViewModel(Application application) {
        mRepository = new DataRepository(application);
        this.mBindDevice = mRepository.getAllBindDevices();
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
}
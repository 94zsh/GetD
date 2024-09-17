package com.future.getd.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.alibaba.fastjson.JSON;
import com.future.getd.log.LogUtils;
import com.future.getd.ui.bean.DeviceSettings;
import com.future.getd.ui.bean.EqData;

import java.util.ArrayList;
import java.util.List;


public class SharePreferencesUtil {
    private static SharePreferencesUtil spUtil = null;

    public static SharePreferencesUtil getSharedPreferences(Context context) {
        if (spUtil == null) {
            spUtil = new SharePreferencesUtil(context);
        }
        return spUtil;
    }
    public static void init(Context context) {
        if (spUtil == null) {
            spUtil = new SharePreferencesUtil(context);
        }
    }
    public static SharePreferencesUtil getInstance() {
        return spUtil;
    }

    private static SharedPreferences sp = null;
    private SharePreferencesUtil(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences(
                    context.getPackageName(), Context.MODE_PRIVATE);
        }
    }

    //是否显示隐私政策
    private static final String PRIVACY_POLICY_SHOW = "privacy_policy_show";
    public void setPrivacyPolicy(boolean show) {
        if (sp != null) {
            Editor editor = sp.edit();
            editor.putBoolean(PRIVACY_POLICY_SHOW, show);
            editor.commit();
        }
    }

    public boolean getPrivacyPolicy() {
        boolean show = true;
        if (sp != null) {
            show = sp.getBoolean(PRIVACY_POLICY_SHOW, true);
        }
        return show;
    }


    //存储设备和设置
    private static final String DEVICE_SETTINGS = "DEVICE_SETTINGS_";
    public Boolean setDevices(List<DeviceSettings> list) {
        boolean result = false;
        try {
            if (sp != null && list != null) {
                Editor editor = sp.edit();
                String str = JSON.toJSONString(list);
                for (int i = 0; i < list.size(); i++) {
                    LogUtils.e("bind 存储设备数据 " + i + " "  + list.get(i).toString());//存储设备数据  [{"bleMac":"","classicMac":"1C:52:16:FA:FA:71","name":"NULL"}]
                }
//                HyLog.e("bind 存储设备数据  " + str);//存储设备数据  [{"bleMac":"","classicMac":"1C:52:16:FA:FA:71","name":"NULL"}]
                editor.putString(DEVICE_SETTINGS, str);
                result = editor.commit();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    public List<DeviceSettings> getDevicess() {
        List<DeviceSettings> list = new ArrayList<>();
        try {
            if (sp != null) {
                String str = sp.getString(DEVICE_SETTINGS, null);
                if (str != null) {
//                list = JSON.parseObject(str, List<DeviceSettings>);
                    list = JSON.parseArray(str,DeviceSettings.class);
//                HyLog.e("bind 获取设备数据  " + str);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public DeviceSettings getDevicesByMac(String bleMac) {
        DeviceSettings deviceSettings = null;
        List<DeviceSettings> list = getDevicess();
        for (int i = 0; i < list.size(); i++) {
            DeviceSettings settings = list.get(i);
            LogUtils.e("bind device : " + settings.toString());
            if(settings.getBleMac().equalsIgnoreCase(bleMac)){
                deviceSettings = settings;
                break;
            }
        }
        return deviceSettings;
    }

    public DeviceSettings getDevicesByClassMac(String classMac) {
        DeviceSettings deviceSettings = null;
        List<DeviceSettings> list = getDevicess();
        for (int i = 0; i < list.size(); i++) {
            DeviceSettings settings = list.get(i);
            LogUtils.e("bind device : " + settings.toString());
            if(settings.getClassicMac().equalsIgnoreCase(classMac)){
                deviceSettings = settings;
                break;
            }
        }
        return deviceSettings;
    }

    public DeviceSettings getMainBindDevice(){
        List<DeviceSettings> deviceSettingsList = SharePreferencesUtil.getInstance().getDevicess();
        if(!deviceSettingsList.isEmpty()){
            for (int i = 0; i < deviceSettingsList.size(); i++) {
                DeviceSettings deviceSettings = deviceSettingsList.get(i);
                LogUtils.e("绑定设备  :  " + i + " , " + deviceSettings);
                if (deviceSettings.isPrimary() || deviceSettingsList.size() == 1) {
                    return deviceSettings;
                }
            }
        }
        return null;
    }


    public void updateSettings(Context context,String mac,DeviceSettings settings){
        List<DeviceSettings> list = SharePreferencesUtil.getSharedPreferences(context).getDevicess();
        int index = -1;
        for (int i = 0; i < list.size(); i++) {
            DeviceSettings deviceSettings = list.get(i);
            if(mac.equalsIgnoreCase(deviceSettings.getClassicMac())){
                index = i;
                break;
            }
        }
        if(index != -1){
            list.set(index,settings);
            SharePreferencesUtil.getSharedPreferences(context).setDevices(list);
        }
    }

    public void updateSettingsByBleMac(Context context,String mac,DeviceSettings settings){
        List<DeviceSettings> list = SharePreferencesUtil.getSharedPreferences(context).getDevicess();
        int index = -1;
        for (int i = 0; i < list.size(); i++) {
            DeviceSettings deviceSettings = list.get(i);
            if(mac.equalsIgnoreCase(deviceSettings.getBleMac())){
                index = i;
                break;
            }
        }
        if(index != -1){
            list.set(index,settings);
            SharePreferencesUtil.getSharedPreferences(context).setDevices(list);
        }
    }

    public static DeviceSettings getSettingsByMac(Context context,String mac){
        List<DeviceSettings> list = SharePreferencesUtil.getSharedPreferences(context).getDevicess();
        for (int i = 0; i < list.size(); i++) {
            DeviceSettings deviceSettings = list.get(i);
            if(mac.equalsIgnoreCase(deviceSettings.getBleMac())){
                return deviceSettings;
            }
        }
        return null;
    }


    //存储EQ设置
    private static final String EQ_SETTINGS = "EQ_SETTINGS_";
    public Boolean saveEqData(String bleAddress,List<EqData> list) {
        boolean result = false;
        try {
            if (sp != null && list != null) {
                Editor editor = sp.edit();
                String str = JSON.toJSONString(list);
                for (int i = 0; i < list.size(); i++) {
                    LogUtils.e("bind 存储设备数据 " + i + " "  + list.get(i).toString());//存储设备数据  [{"bleMac":"","classicMac":"1C:52:16:FA:FA:71","name":"NULL"}]
                }
//                HyLog.e("bind 存储设备数据  " + str);//存储设备数据  [{"bleMac":"","classicMac":"1C:52:16:FA:FA:71","name":"NULL"}]
                editor.putString(EQ_SETTINGS + bleAddress, str);
                result = editor.commit();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    public List<EqData> getEqData(String address) {
        List<EqData> list = new ArrayList<>();
        try {
            if (sp != null) {
                String str = sp.getString(EQ_SETTINGS + address, null);
                if (str != null) {
//                list = JSON.parseObject(str, List<DeviceSettings>);
                    list = JSON.parseArray(str,EqData.class);
//                HyLog.e("bind 获取设备数据  " + str);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
}

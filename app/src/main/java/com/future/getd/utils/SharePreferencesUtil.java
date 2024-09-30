package com.future.getd.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.alibaba.fastjson.JSON;
import com.future.getd.log.LogUtils;
import com.future.getd.net.bean.account.User;
import com.future.getd.ui.bean.DeviceSettings;
import com.future.getd.ui.bean.EqData;
import com.future.getd.ui.bean.StepData;
import com.future.getd.ui.bean.VoiceToTextBean;

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
                LogUtils.e("setDevices size " + list.size());
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

    public void updateSettingsByClassMac(Context context,String classMac,DeviceSettings settings){
        List<DeviceSettings> list = SharePreferencesUtil.getSharedPreferences(context).getDevicess();
        int index = -1;
        for (int i = 0; i < list.size(); i++) {
            DeviceSettings deviceSettings = list.get(i);
            if(classMac.equalsIgnoreCase(deviceSettings.getClassicMac())){
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


    //存储语音转录历史
    private static final String VOICE_TO_TEXT = "VOICE_TO_TEXT_";
    public Boolean saveVTT(List<VoiceToTextBean> list) {
        boolean result = false;
        try {
            if (sp != null && list != null) {
                Editor editor = sp.edit();
                String str = JSON.toJSONString(list);
                for (int i = 0; i < list.size(); i++) {
                    LogUtils.e("存储语音转录历史 " + i + " "  + list.get(i).toString());//存储设备数据  [{"bleMac":"","classicMac":"1C:52:16:FA:FA:71","name":"NULL"}]
                }
                editor.putString(VOICE_TO_TEXT, str);
                result = editor.commit();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public List<VoiceToTextBean> getVtt() {
        List<VoiceToTextBean> list = new ArrayList<>();
        try {
            if (sp != null) {
                String str = sp.getString(VOICE_TO_TEXT, null);
                if (str != null) {
//                list = JSON.parseObject(str, List<DeviceSettings>);
                    list = JSON.parseArray(str,VoiceToTextBean.class);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public boolean deleteAllVtt() {
        List<VoiceToTextBean> list = new ArrayList<>();
        boolean result = false;
        try {
            if (sp != null && list != null) {
                Editor editor = sp.edit();
                String str = JSON.toJSONString(list);
                for (int i = 0; i < list.size(); i++) {
                    LogUtils.e("存储语音转录历史 " + i + " "  + list.get(i).toString());//存储设备数据  [{"bleMac":"","classicMac":"1C:52:16:FA:FA:71","name":"NULL"}]
                }
                editor.putString(VOICE_TO_TEXT, str);
                result = editor.commit();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }


    public Boolean updateVttByTime(long time,String newContent) {
        List<VoiceToTextBean> list = getVtt();
        for (int i = 0; i < list.size(); i++) {
            VoiceToTextBean voiceToTextBean = list.get(i);
            if(voiceToTextBean.getTime() == time){
                voiceToTextBean.setContent(newContent);
            }
        }

        boolean result = false;
        try {
            if (sp != null && list != null) {
                Editor editor = sp.edit();
                String str = JSON.toJSONString(list);
                for (int i = 0; i < list.size(); i++) {
                    LogUtils.e("存储语音转录历史 " + i + " "  + list.get(i).toString());//存储设备数据  [{"bleMac":"","classicMac":"1C:52:16:FA:FA:71","name":"NULL"}]
                }
                editor.putString(VOICE_TO_TEXT, str);
                result = editor.commit();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    private static final String LANGUAGE_ = "LANGUAGE_";

    /**
     * @param lanType 语言类型
     * @return
     */
    public static final int LAN_EN = 0;
    public static final int LAN_CN = 1;
    public Boolean saveLanguage(int lanType) {
        boolean result = false;
        if (sp != null) {
            Editor editor = sp.edit();
            editor.putInt(LANGUAGE_, lanType);
            result = editor.commit();
        }
        return result;
    }

    public int getLanguage() {
        int result = LAN_EN;
        if (sp != null) {
            result = sp.getInt(LANGUAGE_,LAN_EN);
        }
        return result;
    }


    private static final String STEP_DATA = "STEP_DATA_";
    public Boolean saveGoogleStepData(StepData stepData) {
        boolean result = false;
        try {
            if (sp != null && stepData != null) {
                Editor editor = sp.edit();
                String str = JSON.toJSONString(stepData);
                editor.putString(STEP_DATA, str);
                result = editor.commit();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public StepData getGoogleStepData() {
        StepData stepData = new StepData();
        try {
            if (sp != null) {
                String str = sp.getString(STEP_DATA, null);
                if (str != null) {
//                list = JSON.parseObject(str, List<DeviceSettings>);
                    stepData = JSON.parseObject(str,StepData.class);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return stepData;
    }


    private static final String USER_DATA = "USER_DATA_";
    public Boolean saveUser(User user) {
        boolean result = false;
        try {
            if (sp != null && user != null) {
                Editor editor = sp.edit();
                String str = JSON.toJSONString(user);
                editor.putString(USER_DATA, str);
                result = editor.commit();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public User getUser() {
        User user = new User();
        try {
            if (sp != null) {
                String str = sp.getString(USER_DATA, null);
                if (str != null) {
//                list = JSON.parseObject(str, List<DeviceSettings>);
                    user = JSON.parseObject(str,User.class);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }
}

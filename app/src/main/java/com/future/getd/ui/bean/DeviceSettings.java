package com.future.getd.ui.bean;

import androidx.annotation.NonNull;

import com.jieli.bluetooth.bean.BleScanMessage;
import com.jieli.bluetooth.bean.device.eq.EqInfo;
import com.jieli.bluetooth.bean.response.ADVInfoResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DeviceSettings implements Serializable {
    // BleScanMessage{flagContent='null',
    // pairedFlag=0,
    // phoneVirtualAddress=null,
    // pid=51, vid=1494, uid=2,
    // seq=249, deviceType=2, version=2,
    // showDialog=true, edrAddr='11:81:9B:3A:56:F6',
    // edrStatus=1,
    // leftDeviceQuantity=50, isLeftCharging=false, rightDeviceQuantity=70, isRightCharging=false,
    // chargingBinQuantity=0, isDeviceCharging=false, twsFlag=0, chargingBinStatus=0,
    // mainDevFlag=0, action=1, chargingBinMode=0, hash=[-25, -78, 52, 44, -64, -67, 115, -108],
    // rssi=-68, isEnableConnect=true, connectWay=1}

    //    // eqInfos=[EqInfo{mode=0, isNew=false, value=[-4, -3, 0, 3, 2, 2, 0, 3, -3, 2], freqs=[180, 234, 100, 453, 2500, 3924, 8000, 850, 6000, 11000]},
    //        // EqInfo{mode=1, isNew=false, value=[-3, 0, 0, 2, 2, 2, 4, 1, -3, 4], freqs=[180, 234, 100, 453, 2500, 3924, 8000, 850, 6000, 11000]},
    //        // EqInfo{mode=2, isNew=false, value=[-6, -2, 0, 0, 0, 2, 2, 0, -3, 4], freqs=[180, 234, 100, 453, 2500, 3924, 8000, 850, 6000, 11000]},
    //        // EqInfo{mode=3, isNew=false, value=[-4, 2, 2, 3, 2, 2, 3, 3, -3, 5], freqs=[180, 234, 100, 453, 2500, 3924, 8000, 850, 6000, 11000]},
    //        // EqInfo{mode=4, isNew=false, value=[-4, 0, 0, 6, 2, 4, 3, 6, -3, 4], freqs=[180, 234, 100, 453, 2500, 3924, 8000, 850, 6000, 11000]},
    //        // EqInfo{mode=5, isNew=false, value=[-2, -3, 0, 5, 2, 2, 4, 3, -3, 5], freqs=[180, 234, 100, 453, 2500, 3924, 8000, 850, 6000, 11000]},
    //        // EqInfo{mode=6, isNew=false, value=[0, 8, -8, -4, 4, -1, 1, 0, 0, 0], freqs=[180, 234, 100, 453, 2500, 3924, 8000, 850, 6000, 11000]}],
    String name = "";
    String nickName = "";
    String bleMac = "";
    String classicMac = "";
    String boxMac = "";
    EqInfo eqInfo = new EqInfo();//mode 6 是自定义
    int eqMode = EqItem.MODE_CUSTOM;//app  UI的模式 和设备模式不一样
    List<EqData> eqData = new ArrayList<>();
    float latitude = 0f;//最后纬度
    float longitude = 0f;//最后经度
    String address = "";//地理位置信息
    private long locationTimestamp;
    String sn = "";
    boolean isPrimary = false;//是否是主设备
    List<ADVInfoResponse.KeySettings> keySettingsList = new ArrayList<>();

    private BleScanMessage bleScanMessage;

    public DeviceSettings() {
    }

    public DeviceSettings(String name, String mac, String classicMac) {
        this.name = name;
        this.bleMac = mac;
        this.classicMac = classicMac;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getBleMac() {
        return bleMac;
    }

    public void setBleMac(String bleMac) {
        this.bleMac = bleMac;
    }

    public String getClassicMac() {
        return classicMac;
    }

    public void setClassicMac(String classicMac) {
        this.classicMac = classicMac;
    }

    public String getBoxMac() {
        return boxMac;
    }

    public void setBoxMac(String boxMac) {
        this.boxMac = boxMac;
    }

    public List<EqData> getEqData() {
        return eqData;
    }

    public void setEqData(List<EqData> eqData) {
        this.eqData = eqData;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

    public BleScanMessage getBleScanMessage() {
        return bleScanMessage;
    }

    public void setBleScanMessage(BleScanMessage bleScanMessage) {
        this.bleScanMessage = bleScanMessage;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getLocationTimestamp() {
        return locationTimestamp;
    }

    public void setLocationTimestamp(long locationTimestamp) {
        this.locationTimestamp = locationTimestamp;
    }

    public EqInfo getEqInfo() {
        return eqInfo;
    }

    public void setEqInfo(EqInfo eqInfo) {
        this.eqInfo = eqInfo;
    }

    public int getEqMode() {
        return eqMode;
    }

    public void setEqMode(int eqMode) {
        this.eqMode = eqMode;
    }

    public List<ADVInfoResponse.KeySettings> getKeySettingsList() {
        return keySettingsList;
    }

    public void setKeySettingsList(List<ADVInfoResponse.KeySettings> keySettingsList) {
        this.keySettingsList = keySettingsList;
    }

    @NonNull
    @Override
    public String toString() {
        return "DeviceSettings{" +
                "name='" + name + '\'' +
                ", nickName='" + nickName + '\'' +
                ", bleMac='" + bleMac + '\'' +
                ", classicMac='" + classicMac + '\'' +
                ", boxMac='" + boxMac + '\'' +
                ", eqData=" + eqData +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", isPrimary=" + isPrimary +
                ", address=" + address +
                ", locationTimestamp=" + locationTimestamp +
                ", eqInfo=" + eqInfo +
                ", eqMode=" + eqMode +
                ", bleScanMessage=" + bleScanMessage +
                ", keySettingsList=" + keySettingsList +
                ", sn='" + sn + '\'' +
                '}';
    }
}

package com.future.getd.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Created by corn on 2017/2/6.
 */
@Entity(tableName = "table_bind_device")
public class BindDevice {
    private String userId = "";
    private String name = "";//
    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String address = "";//设备蓝牙地址
    private String version = "";//
    private boolean isMain = true;

    public BindDevice() {
    }

    @Ignore
    public BindDevice(String userId, String name, @NonNull String address, String version, boolean isMain) {
        this.userId = userId;
        this.name = name;
        this.address = address;
        this.version = version;
        this.isMain = isMain;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isMain() {
        return isMain;
    }

    public void setMain(boolean main) {
        isMain = main;
    }

    @Override
    public String toString() {
        return "BindDevice{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", version='" + version + '\'' +
                ", isMain=" + isMain +
                '}';
    }
}

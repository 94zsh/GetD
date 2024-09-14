package com.future.getd.ui.bean;

import java.util.Arrays;

public class EqData {
    String mac;
    public static final int EQTYPE_DEFAULT = 1;
    public static final int EQTYPE_POP = 2;
    public static final int EQTYPE_BASS = 3;
    public static final int EQTYPE_ROCK = 4;
    public static final int EQTYPE_SOFT = 5;
    public static final int EQTYPE_CLASSICAL = 6;
    public static final int EQTYPE_USER = 255;
    int index;
    String name;
    byte[] eqData = new byte[10];

    public EqData() {
    }

    public EqData(String mac, int index, byte[] eqData, String name) {
        this.mac = mac;
        this.index = index;
        this.eqData = eqData;
        this.name = name;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public byte[] getEqData() {
        return eqData;
    }

    public void setEqData(byte[] eqData) {
        this.eqData = eqData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "EqData{" +
                "mac='" + mac + '\'' +
                ", index=" + index +
                ", name='" + name + '\'' +
                ", eqData=" + Arrays.toString(eqData) +
                '}';
    }
}

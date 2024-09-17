package com.future.getd.ui.bean;

import java.util.HashMap;

public class ButtonFunction {
    String mac = "";
    HashMap hashMap = new HashMap();

    public static final int KEYID_MUSIC_LEFT_SINGLE = 1;
    public static final int KEYID_MUSIC_RIGHT_SINGLE = 2;
    public static final int KEYID_MUSIC_LEFT_DOUBLE = 3;
    public static final int KEYID_MUSIC_RIGHT_DOUBLE = 4;
    public static final int KEYID_MUSIC_LEFT_TRIPLE = 5;
    public static final int KEYID_MUSIC_RIGHT_THREE = 6;
    public static final int KEYID_MUSIC_LEFT_FOUR = 7;
    public static final int KEYID_MUSIC_RIGHT_FOUR = 8;
    public static final int KEYID_MUSIC_LEFT_LONG = 9;
    public static final int KEYID_MUSIC_RIGHT_LONG = 10;
    //富韵二期新增
    public static final int KEYID_MUSIC_LEFT_SLIDE = 11;
    public static final int KEYID_MUSIC_RIGHT_SLIDE = 12;

    public static final int FUNID_NONE = 0;
    public static final int FUNID_PLAYPAUSE = 1;
    public static final int FUNID_PERVIOUS = 2;
    public static final int FUNID_NEXT = 3;
    public static final int FUNID_ASSISTANT = 4;
    public static final int FUNID_ADD = 5;
    public static final int FUNID_SUB = 6;
    public static final int FUNID_GAME = 7;
    //富韵二期新增
    public static final int FUNID_ANC_ON = 8;
    public static final int FUNID_ANC_OFF = 9;
    public static final int FUNID_TRANSPARENT = 10;
    public static final int FUNID_REFUSE_CALL = 11;
    public static final int FUNID_RECEIVE_CALL = 12;
    public static final int FUNID_CALLBACK_CALL = 13;

    public static final int FUNID_NONE_HYPROTOCOL = 0xaf;

    public ButtonFunction() {
    }

    public ButtonFunction(String mac, HashMap hashMap) {
        this.mac = mac;
        this.hashMap = hashMap;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public HashMap getHashMap() {
        return hashMap;
    }

    public void setHashMap(HashMap hashMap) {
        this.hashMap = hashMap;
    }

    @Override
    public String toString() {
        return "ButtonFunction{" +
                "mac='" + mac + '\'' +
                ", hashMap=" + hashMap +
                '}';
    }
}

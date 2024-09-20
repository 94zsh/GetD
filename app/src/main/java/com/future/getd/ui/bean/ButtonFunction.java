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

//    public static final int FUNID_NONE = 0;
//    public static final int FUNID_PERVIOUS = 1;
//    public static final int FUNID_NEXT = 2;
//    public static final int FUNID_PLAYPAUSE = 3;
//    public static final int FUNID_CALL_RECEIVE = 4;
//    public static final int FUNID_CALL_HAND_UP = 5;
//    public static final int FUNID_CALL_RECALL = 6;
//    public static final int FUNID_ADD = 7;
//    public static final int FUNID_SUB = 8;

    public static final int FUNID_NONE_HYPROTOCOL = 0xaf;


    public static final int DEVICE_KEY_NUM_LEFT = 1;
    public static final int DEVICE_KEY_NUM_RIGHT = 2;
    public static final int DEVICE_KEY_ACTION_SINGLE_TAP = 1;
    public static final int DEVICE_KEY_ACTION_DOUBLE_TAP = 2;

    public static final int DEVICE_KEY_FUNCTION_NONE = 0;
    public static final int DEVICE_KEY_FUNCTION_PREVIOUS = 3;
    public static final int DEVICE_KEY_FUNCTION_NEXT = 4;
    public static final int DEVICE_KEY_FUNCTION_PLAY_PAUSE = 5;
    public static final int DEVICE_KEY_FUNCTION_ANSWER_CALL = 6;
    public static final int DEVICE_KEY_FUNCTION_HANG_UP = 7;
    public static final int DEVICE_KEY_FUNCTION_CALL_BACK = 8;
    public static final int DEVICE_KEY_FUNCTION_VOLUME_UP = 9;
    public static final int DEVICE_KEY_FUNCTION_VOLUME_DOWN = 10;
    public static final int DEVICE_KEY_FUNCTION_TAKE_PHOTO = 11;

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

package com.future.getd.ui.bean;

import com.jieli.bluetooth.bean.device.eq.EqInfo;

public class EqItem {
    public static final int MODE_CUSTOM = 0;
    public static final int MODE_POP = 1;
    public static final int MODE_DANCE = 2;
    public static final int MODE_BLUE = 3;
    public static final int MODE_JAZZ = 4;
    public static final int MODE_VILLAGE = 5;
    public static final int MODE_ROLL = 6;
    public static final int MODE_ELECTRONIC = 7;
    public static final int MODE_CLASS = 8;
    //设备模式 自然 摇滚 流行 经典 爵士 乡村 自定义
    public static final int DEV_MODE_ROCK = 1;
    public static final int DEV_MODE_POP = 2;
    public static final int DEV_MODE_CLASS = 3;
    public static final int DEV_MODE_JAZZ = 4;
    public static final int DEV_MODE_VILLAGE = 5;
    public static final int DEV_MODE_CUSTOM = 6;
    int mode;
    String typeDescribe = "";
    public boolean isSelect;
    EqInfo eqInfo;

    public EqItem(int type, String typeDescribe, boolean isSelect) {
        this.mode = type;
        this.typeDescribe = typeDescribe;
        this.isSelect = isSelect;
    }

    public EqItem(int mode, String typeDescribe, boolean isSelect, EqInfo eqInfo) {
        this.mode = mode;
        this.typeDescribe = typeDescribe;
        this.isSelect = isSelect;
        this.eqInfo = eqInfo;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getTypeDescribe() {
        return typeDescribe;
    }

    public void setTypeDescribe(String typeDescribe) {
        this.typeDescribe = typeDescribe;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public EqInfo getEqInfo() {
        return eqInfo;
    }

    public void setEqInfo(EqInfo eqInfo) {
        this.eqInfo = eqInfo;
    }
}

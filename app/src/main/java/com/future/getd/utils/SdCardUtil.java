package com.future.getd.utils;

import android.os.Environment;

import java.io.File;


public class SdCardUtil {
    public static boolean ExistSDCard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }


    public static final int SAVE_PATH_LOG = 0;
    public static final int SAVE_PATH_PICTURE = 1;
    public static final int SAVE_PATH_OTA = 2;
    public static String filePath;
    public static String getSdCardPath(int pathType) {
        String appName = "GetD";
        String path = "default";
        switch (pathType){
            case SAVE_PATH_LOG:
                path = "log";
                break;
            case SAVE_PATH_PICTURE:
                path = "picture";
                break;
            case SAVE_PATH_OTA:
                path = "update";
                break;
        }
        String pathFinal;
        pathFinal = filePath + "/" + appName + "/" + path + "/";
        File file = new File(pathFinal);
        if (file.mkdirs()) {
            return path;
        }
        return pathFinal;
    }

}

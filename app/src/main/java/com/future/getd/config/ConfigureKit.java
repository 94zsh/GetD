package com.future.getd.config;

import com.future.getd.MyApplication;
import com.future.getd.utils.SharePreferencesUtil;

public class ConfigureKit {
    private static final String TAG = "ConfigureKit";
    private static final ConfigureKit ourInstance = new ConfigureKit();

    public static ConfigureKit getInstance() {
        return ourInstance;
    }
    private final SharePreferencesUtil spUtils;

    private ConfigureKit() {
        spUtils = SharePreferencesUtil.getSharedPreferences(MyApplication.getInstance());
    }
    public boolean isAgreeAgreement() {
        return spUtils.getPrivacyPolicy();
    }
}

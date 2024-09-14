package com.future.getd.ui.launcher;

import androidx.lifecycle.ViewModel;

import com.future.getd.base.BaseViewModel;
import com.future.getd.config.ConfigureKit;
import com.future.getd.utils.SharePreferencesUtil;

public class LauncherVM extends BaseViewModel {
    protected String tag = getClass().getSimpleName();
    public boolean isAgreeUserAgreement() {
        return ConfigureKit.getInstance().isAgreeAgreement();
    }
}

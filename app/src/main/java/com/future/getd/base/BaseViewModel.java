package com.future.getd.base;

import androidx.lifecycle.ViewModel;

import com.jieli.bluetooth.impl.rcsp.RCSPController;

/**
 * ViewModel基类
 */
public class BaseViewModel extends ViewModel {
    protected String tag = getClass().getSimpleName();
//    protected RCSPController mRCSPController = RCSPController.getInstance();
}

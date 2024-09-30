package com.future.getd;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;


import com.amap.api.location.AMapLocationClient;
import com.future.getd.base.SysConstant;
import com.future.getd.database.AppDataBase;
import com.future.getd.jl.ProductManager;
import com.future.getd.log.LogUtils;
import com.future.getd.net.bean.account.User;
import com.future.getd.ui.bean.DeviceSettings;
import com.future.getd.utils.LocationUtils;
import com.future.getd.utils.RetrofitUtil;
import com.future.getd.utils.SdCardUtil;
import com.future.getd.utils.SharePreferencesUtil;
import com.future.getd.utils.SpeechUtils;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.jieli.bluetooth.bean.BluetoothOption;
import com.jieli.bluetooth.impl.rcsp.RCSPController;
import com.jieli.component.utils.PreferencesHelper;
import com.jieli.component.utils.ToastUtil;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MyApplication extends Application {
    /**
     * 版本控制
     * DEBUG_VISION:内部测试服务器版本，输出log，写log文本，使用测试服务器SdCardUtil
     * DEBUG_RELEASE_VISION:内部正式服务器版本，输出log，写log文本，使用正式服务器
     * RELEASE_VISION:正式对外版本，关闭log输出，不写log文本，使用正式服务器
     */

    public final static int DEBUG_VISION = 0;//debug local test version 内部测试服务器版本
    public final static int DEBUG_RELEASE_VISION = 1;//debug release version 内部正式服务器版本
    public final static int RELEASE_VISION = 2;//外部上架版本

    public static int HY_HEALTHY_VISION = DEBUG_VISION;
    private static MyApplication mApplication = null;
    public static MyApplication getInstance() {
        return mApplication;
    }

    @Override
    public void onCreate() {
        // 程序创建的时候执行
        super.onCreate();
        mApplication = this;
        if (HY_HEALTHY_VISION == RELEASE_VISION) {//如果是上架版本的话，直接不再输出log
            LogUtils.setDEBUG(false);
//            HyLog.setDEBUG(true);//给客户测试用 保留日志 发布版本时需要设置为false
        }else{
            LogUtils.setDEBUG(true);//给客户测试用 保留日志 发布版本时需要设置为false
        }
        SharePreferencesUtil.init(MyApplication.this);
        initLanguage();
        initPath();
        setServerLanguage();
        initServiceChannel();
        CrashReport.initCrashReport(getApplicationContext(), "dafce52c78",
                /*HY_HEALTHY_VISION != RELEASE_VISION*/false);
        initCrashExtraInfo();
        LogUtils.e("phone model:" + Build.MODEL + " , " + Build.BRAND+ " , " + Build.VERSION.SDK_INT);
        try {
            String str = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            LogUtils.e("app version:" + str);
        } catch (Exception var3) {
            var3.printStackTrace();
        }
        registerLifecycle(this);
        initJLSDK();
        initAMap();
        initUtils();
    }

    private void initLanguage() {
        int lanType = SharePreferencesUtil.getInstance().getLanguage();
        Configuration config = new Configuration();
        if(lanType == SharePreferencesUtil.LAN_CN){
            config.locale = Locale.CHINESE;
        }else{
            config.locale = Locale.ENGLISH;
        }
        createConfigurationContext(config);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    private void initUtils() {
        LocationUtils.getInstance().init(MyApplication.this);
        ToastUtil.init(MyApplication.this);
        RetrofitUtil.initHttpClient(MyApplication.this);
        AppDataBase.getInstance(MyApplication.this);
        SpeechUtils.getInstance().initTextSpeech(this);
    }

    private void initAMap() {
        AMapLocationClient.updatePrivacyShow(MyApplication.this,true,true);
        AMapLocationClient.updatePrivacyAgree(MyApplication.this,true);
    }

    private void initJLSDK() {
        if (!RCSPController.isInit()) {
            //config RCSPController
            BluetoothOption bluetoothOption = BluetoothOption.createDefaultOption()
                    .setPriority(BluetoothOption.PREFER_BLE)
                    .setUseDeviceAuth(true);
            RCSPController.init(this, bluetoothOption);
        }
        ProductManager.getInstance().setContext(MyApplication.this);
        RCSPController.getInstance().addBTRcspEventCallback(ProductManager.getInstance().getCallback());
//        RCSPController controller = RCSPController.getInstance();
//        controller.addBTRcspEventCallback(ProductCacheManager.getInstance());
//        controller.addBTRcspEventCallback(new RcspEventHandleTask(controller));
//        controller.addBTRcspEventCallback(new ScanBleDeviceTask(this, controller));
//        controller.addBTRcspEventCallback(new UploadDeviceInfoTask());
//        controller.addBTRcspEventCallback(BleScanMsgCacheManager.getInstance());
    }

    private void setServerLanguage() {
        Locale locale = getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        LogUtils.e(" app start current language = " + language);
    }

    private void initCrashExtraInfo() {
        try {
            //配置Bugly上传附加信息 用户名、绑定设备、语言、地区

            LogUtils.e("phone model:" + Build.MODEL + " , " + Build.BRAND+ " , " + Build.VERSION.SDK_INT);
            CrashReport.putUserData(this, "phoneInfo", " model : " +Build.MODEL + " , BRAND: " + Build.BRAND+ " , SDK_INT:" + Build.VERSION.SDK_INT);

            Locale locale = getResources().getConfiguration().locale;
            String language = locale.getLanguage();
            String country = locale.getCountry();
            CrashReport.putUserData(this,"language",language);
            CrashReport.putUserData(this,"country",country);

            User user = SharePreferencesUtil.getInstance().getUser();
            if (user != null && user.getEmail() != null && !user.getEmail().isEmpty()) {
                CrashReport.putUserData(this,"user",user.toString());
            }

            List<DeviceSettings> deviceSettingsList = SharePreferencesUtil.getInstance().getDevicess();
            if(deviceSettingsList != null && !deviceSettingsList.isEmpty()){
                for (int i = 0; i < deviceSettingsList.size(); i++) {
                    DeviceSettings settings = deviceSettingsList.get(i);
                    CrashReport.putUserData(this,"bindDeviceInfo" + i + ": ",settings.toString());
                }
            }
        }catch (Exception e){
            LogUtils.e("initCrashExtraInfo Exception :" + e);
        }
    }


    private void initServiceChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationManager mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            NotificationChannel channel = null;
//            if (mManager != null) {
//                channel = mManager.getNotificationChannel(HyBluetoothLoaderService.CHANNEL_ID);
//            }
//            if (channel == null) {
//                channel = new NotificationChannel(HyBluetoothLoaderService.CHANNEL_ID,
//                        HyBluetoothLoaderService.CHANNEL_NAME, NotificationManager.IMPORTANCE_MIN);
//            }
//            channel.enableLights(false);
//            channel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
//            channel.enableVibration(false);
//
//            if (mManager != null) {
//                mManager.createNotificationChannel(channel);
//            }
//        }
    }


    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        LogUtils.d("MyApplication onTerminate");
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        // 低内存的时候执行
        LogUtils.d("MyApplication onLowMemory");
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        // 程序在内存清理的时候执行
        LogUtils.e("onTrimMemory ");

        super.onTrimMemory(level);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        LogUtils.d("MyApplication onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }


    /**
     * 活跃Activity的数量
     */
    private int mActiveActivityCount = 0;

    /**
     * 注册生命周期
     */
    private void registerLifecycle(Context context) {
        Application application = (Application) context.getApplicationContext();
        application.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
                if (mActiveActivityCount == 0) { //后台切换到前台
                    LogUtils.e("app switch to Foreground " );
                }
                mActiveActivityCount++;
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
                mActiveActivityCount--;
//                LogUtils.e("活跃的activity数量 =" + mActiveActivityCount);
                if (mActiveActivityCount == 0) { //前台切换到后台
                    LogUtils.e("app switch to BackGround " );
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    public boolean isBackground() {
        if (mActiveActivityCount <= 0) {
//            LogUtils.e("启动前台服务 " );
            return true;
        } else {
//            LogUtils.e("启动普通服务 " );
            return false;
        }
    }

    //存储目录
    private void initPath() {
        LogUtils.setContext(this);
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            //外部存储可用
            SdCardUtil.filePath = Objects.requireNonNull(getExternalFilesDir(null)).getAbsolutePath();
        }else{
            SdCardUtil.filePath = getFilesDir().getAbsolutePath();
        }
        String audioSaveDir = SdCardUtil.getSdCardPath(SdCardUtil.SAVE_PATH_AUDIO);
    }
}




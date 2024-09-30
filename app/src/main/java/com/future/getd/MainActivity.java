package com.future.getd;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.service.restrictions.RestrictionsReceiver;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.future.getd.base.BaseActivity;
import com.future.getd.base.BtBasicVM;
import com.future.getd.base.SysConstant;
import com.future.getd.databinding.ActivityMainBinding;
import com.future.getd.jl.ProductManager;
import com.future.getd.jl.rcsp.BTRcspHelper;
import com.future.getd.log.LogUtils;
import com.future.getd.net.model.CompletionChoice;
import com.future.getd.net.model.GptRequest;
import com.future.getd.net.model.GptResponse;
import com.future.getd.net.model.Message;
import com.future.getd.net.model.TranscriptionResponse;
import com.future.getd.ui.ai.ChatActivity;
import com.future.getd.ui.ai.FragmentAI;
import com.future.getd.ui.ai.TranslateActivity;
import com.future.getd.ui.bean.DeviceSettings;
import com.future.getd.ui.health.FragmentHealth;
import com.future.getd.ui.home.FragmentHome;
import com.future.getd.ui.home.HomeViewModel;
import com.future.getd.ui.my.FragmentMy;
import com.future.getd.ui.my.TestActivity;
import com.future.getd.ui.statusbar.StatusBarCompat;
import com.future.getd.ui.view.pop.AudioRecordPopNew;
import com.future.getd.ui.view.pop.RecordingGifPop;
import com.future.getd.utils.LocationUtils;
import com.future.getd.utils.RecordManager;
import com.future.getd.utils.RetrofitUtil;
import com.future.getd.utils.SharePreferencesUtil;
import com.future.getd.utils.SpeechUtils;
import com.future.getd.utils.StatusBarUtil;
import com.future.getd.vm.MainViewModel;
import com.google.android.material.navigation.NavigationBarView;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.jieli.bluetooth.bean.BleScanMessage;
import com.jieli.bluetooth.bean.base.BaseError;
import com.jieli.bluetooth.bean.history.HistoryBluetoothDevice;
import com.jieli.bluetooth.bean.response.ADVInfoResponse;
import com.jieli.bluetooth.constant.StateCode;
import com.jieli.bluetooth.impl.rcsp.RCSPController;
import com.jieli.bluetooth.interfaces.OnReconnectHistoryRecordListener;
import com.jieli.bluetooth.utils.BluetoothUtil;
import com.jieli.component.utils.SystemUtil;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity<ActivityMainBinding> {
    private final String TAG = MainActivity.class.getSimpleName();
    private MainViewModel mainViewModel;
    public static boolean isNeedReloadDevices = false;
    boolean isSupportVoiceExchange = false;
    private final Fragment[] fragments = new Fragment[]{
            FragmentHome.newInstance(),
            FragmentAI.newInstance(),
            FragmentHealth.newInstance(),
            FragmentMy.newInstance(),
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.i("Main onCreate");
        initView();
        requestPermission();
    }

    private void requestPermission() {
        List<String> permissions = getPermissions();
        PermissionX.init(this)
                .permissions(permissions)
                .request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, @NonNull List<String> grantedList, @NonNull List<String> deniedList) {
                        LogUtils.e("permission initPermission onResult");
                        for (int i = 0; i < grantedList.size(); i++) {
                            LogUtils.e("permission initPermission granted :" + grantedList.get(i));
                        }
                        for (int i = 0; i < deniedList.size(); i++) {
                            LogUtils.e("permission initPermission denied :" + deniedList.get(i));
                        }
                        if (allGranted) {

                        }else{
                            showCommonPop(getString(R.string.permission), getString(R.string.permission_tip),getString(R.string.setting), new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    requestPermission();
                                }
                            });
                        }
                    }
                });
    }

    private List<String> getPermissions() {
        List<String> permissions = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions.add(Manifest.permission.BLUETOOTH_SCAN);
            permissions.add(Manifest.permission.BLUETOOTH_CONNECT);
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }else{
            permissions.add(Manifest.permission.BLUETOOTH);
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        return permissions;
    }

    @Override
    protected ActivityMainBinding getBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initData() {
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getMainBindDevice();
        mainViewModel.deviceConnectionMLD.observe(this, new Observer<BtBasicVM.DeviceConnectionData>() {
            @SuppressLint("MissingPermission")
            @Override
            public void onChanged(BtBasicVM.DeviceConnectionData deviceConnectionData) {
                LogUtils.e("deviceConnectionMLD onChanged deviceConnectionData : " + deviceConnectionData.getStatus());
                final BluetoothDevice device = deviceConnectionData.getDevice();
                final int status = deviceConnectionData.getStatus();
                if (status == StateCode.CONNECTION_OK || status == StateCode.CONNECTION_CONNECTED) {
                    LogUtils.e("deviceConnectionMLD onChanged deviceConnectionData : connected" );
                    //test
//                    FragmentHome fragmentHomes = (FragmentHome) fragments[0];
//                    fragmentHomes.changeUI(true,true);

                    DeviceSettings deviceSettings = SharePreferencesUtil.getInstance().getDevicesByClassMac(deviceConnectionData.getDevice().getAddress());
//                    //未绑定设备 系统却配对了设备  自动绑定新设备 此时没有BLE地址需要获取
                    if(deviceSettings == null){
                        List<DeviceSettings> deviceSettingsList = SharePreferencesUtil.getInstance().getDevicess();
                        int index = -1;
                        for (int i = 0; i < deviceSettingsList.size(); i++) {
                            DeviceSettings settings = deviceSettingsList.get(i);
                            if(settings.getClassicMac().equals(device.getAddress())){
                                index = i;
                            }
                        }
                        deviceSettings = new DeviceSettings(deviceConnectionData.getDevice().getName()
                                ,deviceConnectionData.getDevice().getAddress(),deviceConnectionData.getDevice().getAddress()/*BTRcspHelper.getTargetBleScanMessage().getEdrAddr()*/);
                        deviceSettings.setPrimary(true);
                        deviceSettings.setBleScanMessage(BTRcspHelper.getTargetBleScanMessage());
                        if(index != -1 && index < deviceSettingsList.size()){
                            deviceSettingsList.set(index,deviceSettings);
                        }else{
                            deviceSettingsList.add(deviceSettings);
                        }
                        SharePreferencesUtil.getInstance().setDevices(deviceSettingsList);
                    }
                    ProductManager.currentDevice = deviceSettings;
                    DeviceSettings finalDeviceSettings = deviceSettings;
                    LocationUtils.getInstance().startOnceLocation(new LocationUtils.LocationResultListener() {
                        @Override
                        public void onResult(double lon, double lat,String address) {
                            LogUtils.e("startOnceLocation onResult lon : " + lon + " lat : " + lat +" , address : " + address);
                            if(finalDeviceSettings != null){
                                finalDeviceSettings.setLongitude((float) lon);
                                finalDeviceSettings.setLatitude((float) lat);
                                finalDeviceSettings.setAddress(address);
                                finalDeviceSettings.setLocationTimestamp(System.currentTimeMillis());
                                ProductManager.currentDevice.setLongitude((float) lon);
                                ProductManager.currentDevice.setLatitude((float) lat);
                                ProductManager.currentDevice.setAddress(address);
                                ProductManager.currentDevice.setLocationTimestamp(System.currentTimeMillis());
                                SharePreferencesUtil.getInstance().updateSettings(MainActivity.this,deviceConnectionData.getDevice().getAddress(), finalDeviceSettings);
                            }
                        }
                    });
                }
                else if (status == StateCode.CONNECTION_FAILED || status == StateCode.CONNECTION_DISCONNECT) {
                    LogUtils.e("deviceConnectionMLD onChanged deviceConnectionData : disconnect" );
//                    rcspController.fastConnect();
                }
            }
        });
        mainViewModel.mainBindDevice.observe(this, new Observer<DeviceSettings>() {
            @Override
            public void onChanged(DeviceSettings deviceSettings) {
                LogUtils.e("main device deviceSettings : " + deviceSettings);
                if(deviceSettings != null){
                    List<HistoryBluetoothDevice> historyBluetoothDevices = rcspController.getHistoryBluetoothDeviceList();
                    LogUtils.e("main historyBluetoothDevices : " + historyBluetoothDevices);
                    if(historyBluetoothDevices == null || historyBluetoothDevices.isEmpty()){
                        LogUtils.e("main historyBluetoothDevices null");
                        //没有历史设备
                        BluetoothDevice device = BluetoothUtil.getRemoteDevice(deviceSettings.getBleScanMessage().getEdrAddr());
                        rcspController.connectDevice(device);
                    }else{
                        LogUtils.e("main historyBluetoothDevices size : " + historyBluetoothDevices.size());
                        boolean isSameDevice = false;
                        HistoryBluetoothDevice sameHistoryBluetoothDevice = null;
                        for (int i = 0; i < historyBluetoothDevices.size(); i++) {
                            HistoryBluetoothDevice historyBluetoothDevice = historyBluetoothDevices.get(i);
                            LogUtils.e("main historyBluetoothDevices : " + historyBluetoothDevice.toString());
                            if(deviceSettings.getClassicMac().equals(historyBluetoothDevice.getAddress())){
                                isSameDevice = true;
                                sameHistoryBluetoothDevice = historyBluetoothDevice;
                            }
                        }
                        LogUtils.e("main isSameDevice : " + isSameDevice);
                        if (isSameDevice) {
//                            rcspController.fastConnect();
                            rcspController.connectHistoryBtDevice(sameHistoryBluetoothDevice, 15000, new OnReconnectHistoryRecordListener() {
                                @Override
                                public void onSuccess(HistoryBluetoothDevice historyBluetoothDevice) {
                                    LogUtils.e("connectHistoryBtDevice onSuccess ");
                                }

                                @Override
                                public void onFailed(HistoryBluetoothDevice historyBluetoothDevice, BaseError baseError) {
                                    LogUtils.e("connectHistoryBtDevice onFailed ");
                                }
                            });
                        } else {
                            BleScanMessage bleScanMessage = deviceSettings.getBleScanMessage();
                            if (bleScanMessage != null) {
                                BluetoothDevice device = BluetoothUtil.getRemoteDevice(bleScanMessage.getEdrAddr());
                                BTRcspHelper.connectDeviceByMessage(rcspController, MainActivity.this, device, bleScanMessage);
                            }
                        }
                    }
                }
            }
        });
        mainViewModel.mBindDeviceList.observe(this, new Observer<List<DeviceSettings>>() {
            @Override
            public void onChanged(List<DeviceSettings> deviceSettings) {
                if (deviceSettings.isEmpty()) {
                    LogUtils.i("local deviceSettings isEmpty");
                } else {
                    LogUtils.i("local deviceSettings : " + deviceSettings.size());
                }
            }
        });
    }
    protected void initView() {
        binding.vp2Home.setOffscreenPageLimit(3);
        binding.vp2Home.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragments[position];
            }

            @Override
            public int getItemCount() {
                return fragments.length;
            }
        });
        View childView = binding.vp2Home.getChildAt(0);
        if (childView instanceof RecyclerView) {
            childView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }
        binding.vp2Home.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            public void onPageScrollStateChanged(@ViewPager2.ScrollState int state) {
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
//                    updateToolBarUI(binding.vp2Home.getCurrentItem() == 2);
                    binding.bottomNavigation.setSelectedItemId(binding.bottomNavigation.getMenu().getItem(binding.vp2Home.getCurrentItem()).getItemId());
                }
            }
        });
        //禁用ViewPager2的所有用户输入，包括滑动
        binding.vp2Home.setUserInputEnabled(false);
        binding.bottomNavigation.setItemIconTintList(null);
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            item.setChecked(true);
            int pos = 0;
            if(item.getItemId() == R.id.item_home){
                pos = 0;
            }else if(item.getItemId() == R.id.item_ai){
                pos = 1;
            }else if(item.getItemId() == R.id.item_health){
                pos = 2;
            }else if(item.getItemId() == R.id.item_me){
                pos = 3;
            }
            binding.vp2Home.setCurrentItem(pos, false);
            return false;
        });

        IntentFilter filter = new IntentFilter(SysConstant.BROADCAST_LANGUAGE_CHANGE);
        filter.addAction(SysConstant.BROADCAST_VOICE_INTERACTION_BEGIN);
        filter.addAction(SysConstant.BROADCAST_VOICE_INTERACTION_END);
        registerReceiver(receiver,filter);
    }

    RecordingGifPop recordingGifPop;
    RecordManager recordManager = new RecordManager();
    boolean isRecording = false;
    String audioFilePath = "";
    AudioRecordPopNew audioRecordPopNew;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(SysConstant.BROADCAST_LANGUAGE_CHANGE)){
                LogUtils.e("receive language change");
                recreate();
            }else if(intent.getAction().equals(SysConstant.BROADCAST_VOICE_INTERACTION_BEGIN)){
                if (!isSupportVoiceExchange) {
                    return;
                }
                LogUtils.e("receive BROADCAST_VOICE_INTERACTION_BEGIN");
                if (checkAudioPermission()) {
                    LogUtils.e("checkAudioPermission true ");
                }
                if (rcspController.isDeviceConnected()) {
                    recordManager.startRecordVoiceRecognition(MainActivity.this);
                    audioFilePath = recordManager.getFilePath();
                    if (audioRecordPopNew == null) {
                        audioRecordPopNew = new AudioRecordPopNew(MainActivity.this);
                        audioRecordPopNew.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                recordManager.stopRecord();
                            }
                        });
//                            audioRecordPopNew.setOnSelectListener(new AudioRecordPopNew.OnSelectListener() {
//                                @Override
//                                public void onSelect(int position, String text, int value) {
//
//                                }
//                            });
                    }
                    audioRecordPopNew.showStart();
                }

            }else if(intent.getAction().equals(SysConstant.BROADCAST_VOICE_INTERACTION_END)){
                if(!isSupportVoiceExchange){
                    return;
                }
                LogUtils.e("receive BROADCAST_VOICE_INTERACTION_END state " + recordManager.isRecording());
                LogUtils.d("stopRecord");
                audioFilePath = recordManager.stopRecord();
                if(audioRecordPopNew != null){
                    audioRecordPopNew.dismiss();
                }
                LogUtils.i("recordManager filePath ： " + audioFilePath);
                //语音转文本
                File audioFile = new File(audioFilePath);
                //test
//                audioFile = new File("/storage/emulated/0/Android/data/com.future.getd/files/GetD/audio/2024-09-29_10:16:16.m4a");//也好吗
//                audioFile = new File("/storage/emulated/0/Android/data/com.future.getd/files/GetD/audio/2024-09-29_10:35:14.m4a");//你好吗 翻译成了字幕
                ///storage/emulated/0/Android/data/com.future.getd/files/GetD/audio/2024-09-29_11:28:39.m4a  //你好吗 翻译成了沒人算吧
//                audioFile = new File("storage/emulated/0/Android/data/com.future.getd/files/GetD/audio/2024-09-29_13:43:37.wav");//你好吗

                LogUtils.i("翻译文件  file path : " + audioFile.getAbsolutePath() + " , isExists : " + audioFile.exists());
                if(!audioFile.exists()){
                    showToast("file does not exist");
                    return;
                }
//                RequestBody requestFile = RequestBody.create(MediaType.parse("audio/m4a"), audioFile);
                RequestBody requestFile = RequestBody.create(MediaType.parse("audio/wav"), audioFile);
                MultipartBody.Part body = MultipartBody.Part.createFormData("file", audioFile.getName(), requestFile);
                RequestBody model = RequestBody.create(MediaType.parse("text/plain"), "whisper-1");
                RetrofitUtil.getAiApiService().transcribeAudio(body,model).enqueue(new Callback<TranscriptionResponse>() {
                    @Override
                    public void onResponse(Call<TranscriptionResponse> call, Response<TranscriptionResponse> response) {
                        LogUtils.e("test getTranscriptions response : " + response + " , body : " + response.body());
                        if (response.code() == 200){
                            TranscriptionResponse gptResponse = response.body();
                            String resultText = gptResponse.getText();
                            sendMsg(resultText);
                        } else {
                            showToast(response.code() + " " + response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<TranscriptionResponse> call, Throwable t) {
                        LogUtils.e("test getTranscriptions onFailure : " + t);
                        showToast(getString(R.string.net_error));
                    }
                });

            }
            }
    };

    private void sendMsg(String content) {
        LogUtils.i("发送消息 : " + content);
        if(content.isEmpty()){
            return;
        }
        Message messageUser = new Message(Message.ROLE_USER,content);
        ArrayList<Message> tempSendList = new ArrayList<>();
        tempSendList.add(messageUser);

        GptRequest request = new GptRequest(tempSendList);
        RetrofitUtil.getAiApiService().getAiMessage(request).enqueue(new Callback<GptResponse>() {
            @Override
            public void onResponse(Call<GptResponse> call, Response<GptResponse> response) {
                LogUtils.e("getAiMessage response : " + response+ " , body : " + response.body());
                if (response.code() == 200){
                    GptResponse gptResponse = response.body();
                    if (gptResponse != null) {
                        List<CompletionChoice> completionChoices = gptResponse.getChoices();
                        if(!completionChoices.isEmpty()){
                            CompletionChoice choice = completionChoices.get(0);
                            Message message = choice.getMessage();
                            LogUtils.i("返回消息 : " + message);
                            if(SpeechUtils.getInstance().isInitSuccess()){
                                SpeechUtils.getInstance().speak(message.content,SpeechUtils.TYPE_DEFAULT);
                            }
                        }
                    }
                }else{
                    showToast(getString(R.string.net_error));
                }
            }

            @Override
            public void onFailure(Call<GptResponse> call, Throwable t) {
                LogUtils.e("test getAiMessage onFailure : " + t);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private boolean exit = false;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogUtils.e(TAG,"KEYCODE_BACK currentState =" + keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
//                if (exit) {
                    Intent intentLauncher = new Intent(Intent.ACTION_MAIN);
                    intentLauncher.addCategory(Intent.CATEGORY_HOME);
                    startActivity(intentLauncher);
//                } else {
//                    exit = true;
//            ToastUtil.shortShow(TabMainActivity.this, getResources().getString(R.string.main_back_tip));
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            exit = false;
//                        }
//                    }, 1500);
//                }
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean checkAudioPermission(){
        String[] PermissionString = {
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.MODIFY_AUDIO_SETTINGS,
        };
        boolean isAllGranted = checkPermissionAllGranted(PermissionString);
        if (isAllGranted) {
            Log.e("err","所有权限已经授权！");
            return true;
        }
        // 一次请求多个权限, 如果其他有权限是已经授予的将会自动忽略掉
        ActivityCompat.requestPermissions(MainActivity.this,
                PermissionString, 1);
        return false;
    }
}
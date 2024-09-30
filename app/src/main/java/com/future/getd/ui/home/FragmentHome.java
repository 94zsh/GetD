package com.future.getd.ui.home;


import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;

import com.future.getd.MainActivity;
import com.future.getd.R;
import com.future.getd.base.BaseFragment;
import com.future.getd.base.BtBasicVM;
import com.future.getd.database.entity.BindDevice;
import com.future.getd.databinding.FragmentHomeBinding;
import com.future.getd.jl.ProductManager;
import com.future.getd.jl.rcsp.BTRcspHelper;
import com.future.getd.log.LogUtils;
import com.future.getd.ui.bean.DeviceSettings;
import com.future.getd.ui.view.EditNamePop;
import com.future.getd.utils.ScreenUtil;
import com.future.getd.utils.SharePreferencesUtil;
import com.future.getd.utils.StatusBarUtil;
import com.jieli.bluetooth.bean.base.BaseError;
import com.jieli.bluetooth.bean.response.ADVInfoResponse;
import com.jieli.bluetooth.constant.Constants;
import com.jieli.bluetooth.constant.StateCode;
import com.jieli.bluetooth.impl.JL_BluetoothManager;
import com.jieli.bluetooth.impl.rcsp.RCSPController;
import com.jieli.bluetooth.interfaces.rcsp.callback.OnRcspActionCallback;
import com.jieli.bluetooth.utils.BluetoothUtil;
import com.jieli.bluetooth.utils.JL_Log;

import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends BaseFragment {
    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private BroadcastReceiver mVolumeReceiver;
    public static FragmentHome newInstance() {
        return new FragmentHome();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        init();
        initData();
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        setMainStatusBar();

        if(MainActivity.isNeedReloadDevices){
            LogUtils.i("HOME onResume NeedReloadDevices");
            homeViewModel.getBindDevice();
            MainActivity.isNeedReloadDevices = false;
        }
    }

    private void initData() {
        homeViewModel.getBindDevice();
        homeViewModel.deviceConnectionMLD.observe(getViewLifecycleOwner(), new Observer<BtBasicVM.DeviceConnectionData>() {
            @Override
            public void onChanged(BtBasicVM.DeviceConnectionData deviceConnectionData) {
                LogUtils.e("deviceConnectionMLD onChanged deviceConnectionData : " + deviceConnectionData.getStatus());
                final BluetoothDevice device = deviceConnectionData.getDevice();
                List<DeviceSettings> list = SharePreferencesUtil.getInstance().getDevicess();
                boolean isBind = true;
                if(list == null || list.isEmpty()){
                    isBind = false;
                }
                final int status = deviceConnectionData.getStatus();
                if (status == StateCode.CONNECTION_OK || status == StateCode.CONNECTION_CONNECTED) {
                    changeUI(isBind,true);
                }
                else if (status == StateCode.CONNECTION_FAILED || status == StateCode.CONNECTION_DISCONNECT) {
                    LogUtils.e("disconnect  getUsingDevice : " + RCSPController.getInstance().getUsingDevice());
                    changeUI(isBind,false);
                }
            }
        });
        homeViewModel.mBindDeviceList.observe(getViewLifecycleOwner(), new Observer<List<DeviceSettings>>() {
            @Override
            public void onChanged(List<DeviceSettings> deviceSettings) {
                if (deviceSettings.isEmpty()) {
                    LogUtils.i("local deviceSettings isEmpty");
                    changeUI(false, false);
                } else {
                    LogUtils.i("local deviceSettings : " + deviceSettings.size());
                    changeUI(true,false);
                }
            }
        });
        homeViewModel.leftBattery.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer battery) {
                LogUtils.i("onChanged leftBattery: " + battery );
                binding.layoutDevice.tvLeftValue.setText(battery + "%");
            }
        });
        homeViewModel.rightBattery.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer battery) {
                LogUtils.i("onChanged rightBattery: " + battery );
                binding.layoutDevice.tvRightValue.setText(battery + "%");
            }
        });
    }

    private void init() {
        binding.viewToolbar.ivSettings.setOnClickListener(view -> startActivity(new Intent(getActivity(),DeviceListActivity.class)));
        binding.layoutNone.btAdd.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(),ScanActivity.class));
        });
        binding.viewToolbar.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //test
            }
        });
        binding.layoutDevice.llFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtils.i("llFunction onclick state : " + RCSPController.getInstance().isDeviceConnected());
                if (!RCSPController.getInstance().isDeviceConnected()){
                    reConnect();
                }
            }
        });

        binding.layoutDevice.llBalance.setOnClickListener(view -> {
            startActivity(new Intent(requireContext(),EqSettingActivity.class));
        });

        binding.layoutDevice.llFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireContext(), FindDeviceActivity.class));
            }
        });
        binding.layoutDevice.llGesture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireContext(),GestureActivity.class));
            }
        });

        //音量调节
        mVolumeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                AudioManager audioManager = (AudioManager) requireContext().getSystemService(Context.AUDIO_SERVICE);
                int current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                float currentPer = ((float) current / (float)max) * 100;
                binding.layoutDevice.seekbar.setProgress((int) currentPer);
            }
        };
        requireContext().registerReceiver(mVolumeReceiver, new IntentFilter("android.media.VOLUME_CHANGED_ACTION"));
        AudioManager audioManager = (AudioManager) requireContext().getSystemService(Context.AUDIO_SERVICE);
        int current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float currentPer = ((float) current / (float)max) * 100;
        binding.layoutDevice.seekbar.setProgress((int) currentPer);
        binding.layoutDevice.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float v = progress * ((max) / (float) 100);
                int value = (int) v;
                LogUtils.i("onProgressChanged progress : " + progress + " , current : " + current + " , max : " + max + " , value:" + value + " , fromUser : " + fromUser);
                if(fromUser){
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, value, /*AudioManager.FLAG_SHOW_UI*/0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //修改名称
        binding.layoutDevice.llEditName.setOnClickListener( v -> {
            EditNamePop editNamePop = new EditNamePop(requireContext());
            editNamePop.setContent(ProductManager.currentDevice.getName());
            editNamePop.setOnSelectListener(new EditNamePop.SelectListener() {
                @Override
                public void onConfirm(String name) {
                    if(RCSPController.getInstance().isDeviceConnected()){
                        RCSPController.getInstance().configDeviceName(RCSPController.getInstance().getUsingDevice(), name, new OnRcspActionCallback<Integer>() {
                            @Override
                            public void onSuccess(BluetoothDevice bluetoothDevice, Integer integer) {
                                LogUtils.i("configDeviceName onSuccess : " + integer);
                                ProductManager.currentDevice.setName(name);
                                SharePreferencesUtil.getInstance().updateSettings(requireContext(),RCSPController.getInstance().getUsingDevice().getAddress(),ProductManager.currentDevice);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        RCSPController.getInstance().rebootDevice(RCSPController.getInstance().getUsingDevice(), null);
                                    }
                                },1500);
                            }

                            @Override
                            public void onError(BluetoothDevice bluetoothDevice, BaseError baseError) {

                            }
                        });
                    }
                }

                @Override
                public void onCancel() {

                }
            });
            editNamePop.show();
        });
        binding.layoutDevice.llUnpair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCommonPop(getString(R.string.clear_pair), getString(R.string.clear_pair_tip), getString(R.string.confirm), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LogUtils.i("unpair device : " + RCSPController.getInstance().isDeviceConnected() + " , " + RCSPController.getInstance().getUsingDevice());
                        if (RCSPController.getInstance().isDeviceConnected()){
//                            RCSPController.getInstance().disconnectDevice(/*BTRcspHelper.getCurrentDevice()*/RCSPController.getInstance().getUsingDevice());
                            JL_BluetoothManager.getInstance(requireContext()).unPair(RCSPController.getInstance().getUsingDevice());

                            List<DeviceSettings> list = SharePreferencesUtil.getSharedPreferences(getContext()).getDevicess();
                            int dataPosition = -1;
                            for (int i = 0; i < list.size(); i++) {
                                DeviceSettings item = list.get(i);
                                if(RCSPController.getInstance().getUsingDevice().getAddress().equals(item.getClassicMac())) {
                                    dataPosition = i;
                                }
                            }
                            if(dataPosition != -1){
                                list.remove(dataPosition);
                                SharePreferencesUtil.getSharedPreferences(getContext()).setDevices(list);
                            }

                            changeUI(false,false);
                        }
                    }
                });
            }
        });
    }


    private void reConnect() {
        DeviceSettings deviceSettings =SharePreferencesUtil.getInstance().getMainBindDevice();
        LogUtils.i("reConnect deviceSettings : " + deviceSettings.toString());
        if(deviceSettings != null){
            BluetoothDevice device = BluetoothUtil.getRemoteDevice(deviceSettings.getBleScanMessage().getEdrAddr());
            RCSPController.getInstance().connectDevice(device);
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            LogUtils.i("reconnect : " + device.getName() + " ,  " + device.getAddress());
        }
    }

    public void changeUI(boolean isBind,boolean isConnect){
        if (isBind) {
            binding.layoutNone.getRoot().setVisibility(View.GONE);
            binding.layoutDevice.getRoot().setVisibility(View.VISIBLE);
        } else {
            binding.layoutNone.getRoot().setVisibility(View.VISIBLE);
            binding.layoutDevice.getRoot().setVisibility(View.GONE);
        }
        if(isConnect){
            binding.layoutDevice.llBalance.setEnabled(true);
            binding.layoutDevice.llFind.setEnabled(true);
            binding.layoutDevice.llGesture.setEnabled(true);
            binding.layoutDevice.llVoice.setEnabled(true);
            binding.layoutDevice.llEditName.setEnabled(true);
            binding.layoutDevice.llUnpair.setEnabled(true);
//            setClickable(binding.layoutDevice.llFunction,true);
            binding.layoutDevice.tvState.setText(getString(R.string.state_connected));
            binding.layoutDevice.tvLeft.setBackgroundResource(R.drawable.bg_round_battery_left);
            binding.layoutDevice.tvRight.setBackgroundResource(R.drawable.bg_round_battery_right);
            binding.layoutDevice.ivBalance.setImageResource(R.drawable.ic_balance);
            binding.layoutDevice.ivFind.setImageResource(R.drawable.ic_find);
            binding.layoutDevice.ivGesture.setImageResource(R.drawable.ic_gesture);
            binding.layoutDevice.ivVoice.setImageResource(R.drawable.ic_voice);
        }else{
            binding.layoutDevice.llBalance.setEnabled(false);
            binding.layoutDevice.llFind.setEnabled(false);
            binding.layoutDevice.llGesture.setEnabled(false);
            binding.layoutDevice.llVoice.setEnabled(false);
            binding.layoutDevice.llEditName.setEnabled(false);
            binding.layoutDevice.llUnpair.setEnabled(false);
//            setClickable(binding.layoutDevice.llFunction,false);
            binding.layoutDevice.tvState.setText(getString(R.string.state_disconnected));
            binding.layoutDevice.tvLeft.setBackgroundResource(R.drawable.bg_round_battery_left_disable);
            binding.layoutDevice.tvRight.setBackgroundResource(R.drawable.bg_round_battery_left_disable);
            binding.layoutDevice.ivBalance.setImageResource(R.drawable.ic_balance_disable);
            binding.layoutDevice.ivFind.setImageResource(R.drawable.ic_find_disable);
            binding.layoutDevice.ivGesture.setImageResource(R.drawable.ic_gesture_disable);
            binding.layoutDevice.ivVoice.setImageResource(R.drawable.ic_voice_disable);
        }
    }

    public void setClickable(View view,boolean enable) {
        if (view != null) {
            view.setClickable(enable);
            if (view instanceof ViewGroup) {
                ViewGroup vg = ((ViewGroup) view);
                for (int i = 0; i < vg.getChildCount(); i++) {
                    setClickable(vg.getChildAt(i), enable);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(requireContext() != null){
            requireContext().unregisterReceiver(mVolumeReceiver);
        }
    }
}
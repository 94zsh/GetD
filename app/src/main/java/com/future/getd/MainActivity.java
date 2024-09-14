package com.future.getd;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.future.getd.base.BaseActivity;
import com.future.getd.base.BtBasicVM;
import com.future.getd.databinding.ActivityMainBinding;
import com.future.getd.jl.ProductManager;
import com.future.getd.jl.rcsp.BTRcspHelper;
import com.future.getd.log.LogUtils;
import com.future.getd.ui.ai.FragmentAI;
import com.future.getd.ui.bean.DeviceSettings;
import com.future.getd.ui.health.FragmentHealth;
import com.future.getd.ui.home.FragmentHome;
import com.future.getd.ui.home.HomeViewModel;
import com.future.getd.ui.my.FragmentMy;
import com.future.getd.ui.statusbar.StatusBarCompat;
import com.future.getd.utils.LocationUtils;
import com.future.getd.utils.SharePreferencesUtil;
import com.future.getd.utils.StatusBarUtil;
import com.future.getd.vm.MainViewModel;
import com.google.android.material.navigation.NavigationBarView;
import com.gyf.immersionbar.ImmersionBar;
import com.jieli.bluetooth.bean.BleScanMessage;
import com.jieli.bluetooth.bean.history.HistoryBluetoothDevice;
import com.jieli.bluetooth.bean.response.ADVInfoResponse;
import com.jieli.bluetooth.constant.StateCode;
import com.jieli.bluetooth.impl.rcsp.RCSPController;
import com.jieli.bluetooth.utils.BluetoothUtil;
import com.jieli.component.utils.SystemUtil;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity<ActivityMainBinding> {
    private final String TAG = MainActivity.class.getSimpleName();
    private MainViewModel mainViewModel;
    private final Fragment[] fragments = new Fragment[]{
            FragmentHome.newInstance(),
            FragmentAI.newInstance(),
            FragmentHealth.newInstance(),
            FragmentMy.newInstance(),
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
//        EdgeToEdge.enable(this);
//        SystemUtil.setImmersiveStateBar(getWindow(), true);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestPermission();
//        StatusBarUtil.setStatusBarColor(R.color.bg_main,this);
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
            @Override
            public void onChanged(BtBasicVM.DeviceConnectionData deviceConnectionData) {
                LogUtils.e("deviceConnectionMLD onChanged deviceConnectionData : " + deviceConnectionData.getStatus());
                final BluetoothDevice device = deviceConnectionData.getDevice();
                final int status = deviceConnectionData.getStatus();
                if (status == StateCode.CONNECTION_OK || status == StateCode.CONNECTION_CONNECTED) {
                    LogUtils.e("deviceConnectionMLD onChanged deviceConnectionData : connected" );
                    DeviceSettings deviceSettings = SharePreferencesUtil.getInstance().getDevicesByClassMac(deviceConnectionData.getDevice().getAddress());
                    ProductManager.currentDevice = deviceSettings;
                    LocationUtils.getInstance().startOnceLocation(new LocationUtils.LocationResultListener() {
                        @Override
                        public void onResult(double lon, double lat,String address) {
                            LogUtils.e("startOnceLocation onResult lon : " + lon + " lat : " + lat +" , address : " + address);
                            if(deviceSettings != null){
                                deviceSettings.setLongitude((float) lon);
                                deviceSettings.setLatitude((float) lat);
                                deviceSettings.setAddress(address);
                                deviceSettings.setLocationTimestamp(System.currentTimeMillis());
                                ProductManager.currentDevice.setLongitude((float) lon);
                                ProductManager.currentDevice.setLatitude((float) lat);
                                ProductManager.currentDevice.setAddress(address);
                                ProductManager.currentDevice.setLocationTimestamp(System.currentTimeMillis());
                                SharePreferencesUtil.getInstance().updateSettings(MainActivity.this,deviceConnectionData.getDevice().getAddress(),deviceSettings);
                            }
                        }
                    });
                }
                else if (status == StateCode.CONNECTION_FAILED || status == StateCode.CONNECTION_DISCONNECT) {
                    LogUtils.e("deviceConnectionMLD onChanged deviceConnectionData : disconnect" );
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
                        for (int i = 0; i < historyBluetoothDevices.size(); i++) {
                            HistoryBluetoothDevice historyBluetoothDevice = historyBluetoothDevices.get(i);
                            LogUtils.e("main historyBluetoothDevices : " + historyBluetoothDevice.toString());
                            if(deviceSettings.getClassicMac().equals(historyBluetoothDevice.getAddress())){
                                isSameDevice = true;
                            }
                        }
                        LogUtils.e("main isSameDevice : " + isSameDevice);
                        if (isSameDevice) {
                            rcspController.fastConnect();
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
    }
}
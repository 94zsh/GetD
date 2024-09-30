package com.future.getd.ui.home;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapWrapper;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.future.getd.R;
import com.future.getd.base.BaseActivity;
import com.future.getd.database.converter.DateConverter;
import com.future.getd.databinding.ActivityFindDeviceBinding;
import com.future.getd.jl.ProductManager;
import com.future.getd.log.LogUtils;
import com.future.getd.ui.bean.DeviceSettings;
import com.future.getd.ui.view.MAWebViewWrapper;
import com.future.getd.utils.SharePreferencesUtil;
import com.future.getd.utils.SystemLanguageUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.jieli.bluetooth.bean.base.BaseError;
import com.jieli.bluetooth.bean.parameter.SearchDevParam;
import com.jieli.bluetooth.constant.Constants;
import com.jieli.bluetooth.impl.rcsp.RCSPController;
import com.jieli.bluetooth.interfaces.rcsp.callback.BTRcspEventCallback;
import com.jieli.bluetooth.interfaces.rcsp.callback.OnRcspActionCallback;

import java.util.Date;

public class FindDeviceActivity extends BaseActivity<ActivityFindDeviceBinding> {
    private boolean isSearching = false;
    private DeviceSettings deviceSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusColor(R.color.bg_main);
    }

    @Override
    protected ActivityFindDeviceBinding getBinding() {
        return ActivityFindDeviceBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        initMap();
        init();
    }

    @Override
    protected void initData() {

    }

    private void init() {
//        deviceSettings =
        RCSPController controller = RCSPController.getInstance();
        controller.addBTRcspEventCallback(new BTRcspEventCallback() {
            @Override
            public void onSearchDevice(BluetoothDevice device, SearchDevParam searchDevParam) {
                super.onSearchDevice(device, searchDevParam);
                //到时停止寻找设备上报
                //onSearchDevice: BTRcspEventCallback onSearchDevice device : 73:A7:AE:AF:F3:1C ,
                // searchDevParam : SearchDevParam{type=0, op=0, timeoutSec=0} BaseParameter{xmOpCode=0, paramData=null}
                if (searchDevParam.getOp() == Constants.RING_OP_OPEN) { //open ring
                    int timeout = searchDevParam.getTimeoutSec();//timeout, unit : second
                    int player = searchDevParam.getPlayer(); //player (0 --- app play ring  1 --- device play ring)
                } else {
                    //close ring
                    updateState(true);
                }
            }
        });
        OnRcspActionCallback callback = new OnRcspActionCallback() {
            @Override
            public void onSuccess(BluetoothDevice bluetoothDevice, Object o) {
                isSearching = !isSearching;
            }

            @Override
            public void onError(BluetoothDevice bluetoothDevice, BaseError baseError) {

            }
        };
        binding.ivState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateState(isSearching);
                if(isSearching){
                    controller.stopSearchDevice(controller.getUsingDevice(),callback);
                }else{
                    //获取RCSPController对象
                    //int type = 1;         //查找类型 ： 0 --- 查找手机  1 --- 查找设备
                    //int op = 1;           //铃声操作 ： 0 --- 关闭铃声  1 --- 播放铃声
                    //int timeoutSec = 60;  //超时时间 ： 0 --- 不限制时间
                    //int playWay = 0;      //播放方式： 0 -- 全部播放 1 -- 左侧播放 2 -- 右侧播放
                    //int player = 0;       //播放源: 0 -- App端播放 1 -- 设备端播放
                    //way : 0 -- all  1 -- left  2 -- right
                    //执行搜索设备功能并等待结果回调
                    controller.searchDev(controller.getUsingDevice(), Constants.RING_OP_OPEN, 5, 0, Constants./*RING_PLAYER_APP*/RING_PLAYER_DEVICE,callback);
                }

            }
        });
    }

    private void initMap() {
        boolean isSupportGoogleService = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS;
        if(SystemLanguageUtil.isZh(this) || !isSupportGoogleService){
            binding.mywebview.setVisibility(View.VISIBLE);
            binding.rlGoogle.setVisibility(View.GONE);
        }else{
            binding.mywebview.setVisibility(View.GONE);
            binding.rlGoogle.setVisibility(View.VISIBLE);

        }
        LogUtils.i("MAP current deviceSettings : " + ProductManager.currentDevice + " , isSupportGoogleService: " + isSupportGoogleService);
//        DeviceSettings deviceSettings = SharePreferencesUtil.getSettingsByMac(FindDeviceActivity.this,ProductManager.currentDevice.getBleMac());
//        LogUtils.i("MAP dataBase deviceSettings : " + deviceSettings);
        if(ProductManager.currentDevice != null && ProductManager.currentDevice.getLatitude() != 0){
            binding.tvAddress.setText(ProductManager.currentDevice.getAddress());
            binding.time.setText(DateConverter.fromDate(new Date(ProductManager.currentDevice.getLocationTimestamp())));
        }
        //initAmap
        MAWebViewWrapper webViewWrapper = new MAWebViewWrapper(binding.mywebview);
        AMapWrapper aMapWrapper = new AMapWrapper(this, webViewWrapper);
        aMapWrapper.onCreate();
        aMapWrapper.getMapAsyn(new AMap.OnMapReadyListener() {
            @Override
            public void onMapReady(AMap map) {
                if(ProductManager.currentDevice != null){
                    LatLng latLng = new LatLng(ProductManager.currentDevice.getLatitude(), ProductManager.currentDevice.getLongitude());
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    final Marker marker = map.addMarker(new MarkerOptions().position(latLng).title(ProductManager.currentDevice.getAddress()).snippet(ProductManager.currentDevice.getAddress()));
                }

            }
        });
        //initGoogleMap
        //google map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    com.google.android.gms.maps.model.LatLng latLng = new com.google.android.gms.maps.model.LatLng(ProductManager.currentDevice.getLatitude(), ProductManager.currentDevice.getLongitude());
                    googleMap.moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    googleMap.addMarker(new com.google.android.gms.maps.model.MarkerOptions().position(latLng).title(ProductManager.currentDevice.getAddress()).snippet(ProductManager.currentDevice.getAddress()));
                }
            });
        }
    }

    private void updateState(boolean isSearching){
        binding.ivState.setImageResource(isSearching ? R.drawable.ic_play : R.drawable.audio_pause);
    }
}
package com.future.getd.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.future.getd.R;
import com.future.getd.base.BaseActivity;
import com.future.getd.base.BtBasicVM;
import com.future.getd.config.AppConstant;
import com.future.getd.databinding.ActivityScanBinding;
import com.future.getd.jl.ProductManager;
import com.future.getd.jl.rcsp.BTRcspHelper;
import com.future.getd.log.LogUtils;
import com.future.getd.ui.adapter.ScanAdapter;
import com.future.getd.ui.bean.ScanDevice;
import com.future.getd.vm.ScanViewModel;
import com.jieli.bluetooth.constant.StateCode;
import com.jieli.bluetooth.impl.rcsp.RCSPController;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;

import java.util.ArrayList;
import java.util.List;

public class ScanActivity extends BaseActivity<ActivityScanBinding> {
    List<ScanDevice> scanList = new ArrayList<>();
    private ScanViewModel scanViewModel;
    ScanAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusColor(R.color.white);
        initTitle();
    }

    @Override
    protected ActivityScanBinding getBinding() {
        return ActivityScanBinding.inflate(getLayoutInflater());
    }
    private void initTitle() {
        binding.title.ivBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void initView() {
        binding.title.tvTitle.setText(getResources().getString(R.string.scan_add));

        binding.waveView.setDuration(5000);
        binding.waveView.setStyle(Paint.Style.FILL);
        binding.waveView.setColor(getResources().getColor(R.color.bg_blue));
        binding.waveView.setInterpolator(new LinearOutSlowInInterpolator());

        adapter = new ScanAdapter(scanList);
        binding.rvScan.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        binding.rvScan.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener<ScanDevice>() {
            @Override
            public void onClick(@NonNull BaseQuickAdapter<ScanDevice, ?> baseQuickAdapter, @NonNull View view, int i) {
                LogUtils.d("audio onclick item " + i);
            }
        });
        adapter.setListener(new ScanAdapter.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(int position) {
                ScanDevice scanDevice = scanViewModel.scanDeviceList.getValue().get(position);
                LogUtils.d("audio onclick item " + position + " , name : " + scanDevice.getDevice().getName() + " , address : " + scanDevice.getDevice().getAddress());
                BTRcspHelper.setTargetDevice(scanDevice.getDevice());
                BTRcspHelper.setTargetBleScanMessage(scanDevice.getmBleScanMessage());
                startActivity(new Intent(ScanActivity.this, ConnectingActivity.class));
                if(rcspController.isScanning()){
                    rcspController.stopScan();
                }
            }
        });

        binding.tvReScan.setOnClickListener( v -> {
            startScan();
        });
    }

    @Override
    protected void initData() {
        scanViewModel = new ViewModelProvider(this).get(ScanViewModel.class);
        requestPermission();
        scanViewModel.isScanning.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isScanning) {
                LogUtils.i("isScanning : " + isScanning);
                switchScanUI(isScanning);
            }
        });
        scanViewModel.scanDeviceList.observe(this, new Observer<List<ScanDevice>>() {
            @Override
            public void onChanged(List<ScanDevice> scanDevices) {
                LogUtils.i("scanDeviceList size : " + scanDevices.size());
                scanList = scanDevices;
                //1
                adapter.setItems(scanList);
                adapter.notifyDataSetChanged();
                //2 DiffUtil
//                adapter.updateList(scanDevices);
            }
        });
        scanViewModel.deviceConnectionMLD.observe(this, new Observer<BtBasicVM.DeviceConnectionData>() {
            @Override
            public void onChanged(BtBasicVM.DeviceConnectionData deviceConnectionData) {
                final BluetoothDevice device = deviceConnectionData.getDevice();
                final int status = deviceConnectionData.getStatus();
                if (status == StateCode.CONNECTION_OK || status == StateCode.CONNECTION_CONNECTED) {
                    finish();
                }
            }
        });
    }

    private void requestPermission(){
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
                            startScan();
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

    private void switchScanUI(boolean isScanning){
        if (isScanning) {
            binding.waveView.start();
            binding.tvScanTip.setText(getString(R.string.scanning_tip));
            binding.llReScan.setVisibility(View.INVISIBLE);
        } else {
            binding.waveView.pause();
            binding.tvScanTip.setText(!scanList.isEmpty() ? getString(R.string.scanning_tip) : getString(R.string.device_empty));
            binding.llReScan.setVisibility(View.VISIBLE);
        }
    }
    private void startScan() {
        scanViewModel.scanDeviceList.setValue(new ArrayList<>());
        scanViewModel.resetCallback();
        rcspController.startBleScan(AppConstant.SCAN_TIME);
//        scanViewModel.isScanning.setValue(true);
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
    protected void onDestroy() {
        super.onDestroy();
        if(rcspController.isScanning()){
            rcspController.stopScan();
        }
    }
}
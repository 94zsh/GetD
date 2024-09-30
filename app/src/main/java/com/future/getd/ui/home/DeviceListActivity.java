package com.future.getd.ui.home;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewbinding.ViewBinding;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.future.getd.MainActivity;
import com.future.getd.R;
import com.future.getd.base.BaseActivity;
import com.future.getd.databinding.ActivityDeviceListBinding;
import com.future.getd.log.LogUtils;
import com.future.getd.ui.adapter.DeviceListAdapter;
import com.future.getd.ui.bean.DeviceSettings;
import com.future.getd.utils.SharePreferencesUtil;
import com.jieli.bluetooth.constant.StateCode;
import com.jieli.bluetooth.impl.JL_BluetoothManager;
import com.jieli.bluetooth.impl.rcsp.RCSPController;
import com.jieli.bluetooth.interfaces.rcsp.callback.BTRcspEventCallback;

import java.util.List;

public class DeviceListActivity extends BaseActivity<ActivityDeviceListBinding> {
    List<DeviceSettings> deviceSettingsList;
    DeviceListAdapter adapter;
    private BTRcspEventCallback connectStateCallback;
    private int lastStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusColor(R.color.bg_main);
    }

    @Override
    protected ActivityDeviceListBinding getBinding() {
        return ActivityDeviceListBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        binding.title.ivBack.setOnClickListener( v -> {finish();});
        binding.title.tvTitle.setText(getString(R.string.device_list));
        binding.title.ivAdd.setImageResource(R.drawable.ic_add);
    }

    @Override
    protected void initData() {
        binding.title.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DeviceListActivity.this,ScanActivity.class));
            }
        });

        lastStatus = rcspController.isDeviceConnected() ? StateCode.CONNECTION_CONNECTED : StateCode.CONNECTION_DISCONNECT;
        deviceSettingsList = SharePreferencesUtil.getInstance().getDevicess();
        adapter = new DeviceListAdapter(deviceSettingsList);
        binding.rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        binding.rv.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener<DeviceSettings>() {
            @Override
            public void onClick(@NonNull BaseQuickAdapter<DeviceSettings, ?> baseQuickAdapter, @NonNull View view, int i) {

            }
        });
        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener<DeviceSettings>() {
            @Override
            public boolean onLongClick(@NonNull BaseQuickAdapter<DeviceSettings, ?> baseQuickAdapter, @NonNull View view, int i) {
                showCommonPop(getString(R.string.delete_device), getString(R.string.delete_device_tip), getString(R.string.confirm), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(rcspController.isDeviceConnected()){
                            if(rcspController.getUsingDevice().getAddress().equals(deviceSettingsList.get(i).getClassicMac())){
                                LogUtils.i("disconnect " + rcspController.getUsingDevice().getAddress());
//                                rcspController.disconnectDevice(rcspController.getUsingDevice());
                                JL_BluetoothManager.getInstance(DeviceListActivity.this).unPair(RCSPController.getInstance().getUsingDevice());
                            }
                        }
                        deviceSettingsList.remove(i);
                        SharePreferencesUtil.getInstance().setDevices(deviceSettingsList);
                        adapter.notifyDataSetChanged();

                        MainActivity.isNeedReloadDevices = true;
                    }
                });
                return false;
            }
        });

        connectStateCallback = new BTRcspEventCallback() {
            @Override
            public void onConnection(BluetoothDevice device, int status) {
                super.onConnection(device, status);
                if (status == StateCode.CONNECTION_OK || status == StateCode.CONNECTION_CONNECTED) {
                    HomeViewModel homeViewModel = new ViewModelProvider(DeviceListActivity.this).get(HomeViewModel.class);
//                        HomeViewModel homeViewModel = new ViewModelProvider(DeviceListActivity.this).get(HomeViewModel.class);
                    homeViewModel.getBindDevice();
                    finish();
                }
//                else if (status == StateCode.CONNECTION_FAILED || status == StateCode.CONNECTION_DISCONNECT) {
//
//                }
                LogUtils.i("devicelist ac  device : " + device.getAddress() + " , statu : " + status + " , lastStatus : " + lastStatus);
                if(lastStatus == status){
                    return;
                }else{
//                    for (int i = 0; i < deviceSettingsList.size(); i++) {
//                        DeviceSettings deviceSettings = deviceSettingsList.get(i);
//                    }
                    lastStatus = status;
                    adapter.notifyDataSetChanged();
                }
            }
        };
        rcspController.addBTRcspEventCallback(connectStateCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rcspController.removeBTRcspEventCallback(connectStateCallback);
    }
}
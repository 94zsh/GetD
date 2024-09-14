package com.future.getd.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewbinding.ViewBinding;

import com.future.getd.R;
import com.future.getd.base.BaseActivity;
import com.future.getd.databinding.ActivityDeviceListBinding;

public class DeviceListActivity extends BaseActivity<ActivityDeviceListBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected ActivityDeviceListBinding getBinding() {
        return ActivityDeviceListBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        binding.title.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DeviceListActivity.this,ScanActivity.class));
            }
        });
    }
}
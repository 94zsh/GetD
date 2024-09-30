package com.future.getd.ui.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.future.getd.MainActivity;
import com.future.getd.R;
import com.future.getd.base.BaseActivity;
import com.future.getd.base.SysConstant;
import com.future.getd.databinding.ActivityLanguageSetBinding;
import com.future.getd.log.LogUtils;
import com.future.getd.utils.SharePreferencesUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class LanguageSetActivity extends BaseActivity<ActivityLanguageSetBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusColor(R.color.bg_main);
    }

    @Override
    protected ActivityLanguageSetBinding getBinding() {
        return ActivityLanguageSetBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
            binding.title.ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            binding.title.tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //test
                }
            });

        binding.llCn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.cvEn.setVisibility(View.GONE);
                binding.cvCn.setVisibility(View.VISIBLE);

                SharePreferencesUtil.getInstance().saveLanguage(SharePreferencesUtil.LAN_CN);
                setLanguage();
                reStartMain();
            }
        });
        binding.llEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.cvEn.setVisibility(View.VISIBLE);
                binding.cvCn.setVisibility(View.GONE);

                SharePreferencesUtil.getInstance().saveLanguage(SharePreferencesUtil.LAN_EN);
                setLanguage();
                reStartMain();
            }
        });
    }

    private void reStartMain() {
//        Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
////        Intent intent = new Intent(LanguageSetActivity.this,MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
        sendBroadcast(new Intent(SysConstant.BROADCAST_LANGUAGE_CHANGE));
        finish();


//        recreate();
//        Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//        finish();
    }

    @Override
    protected void initData() {
        int lanType = SharePreferencesUtil.getInstance().getLanguage();
        LogUtils.i("lanType : " + lanType);
        if (lanType == SharePreferencesUtil.LAN_CN){
            binding.cvEn.setVisibility(View.GONE);
            binding.cvCn.setVisibility(View.VISIBLE);
        }else{
            binding.cvEn.setVisibility(View.VISIBLE);
            binding.cvCn.setVisibility(View.GONE);
        }
    }
}
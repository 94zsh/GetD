package com.future.getd.ui.my;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.fragment.app.Fragment;

import com.future.getd.R;
import com.future.getd.base.BaseFragment;
import com.future.getd.databinding.FragmentHealthBinding;
import com.future.getd.databinding.FragmentMyBinding;
import com.future.getd.log.LogUtils;
import com.future.getd.net.bean.account.User;
import com.future.getd.ui.home.FragmentHome;
import com.future.getd.utils.SharePreferencesUtil;

public class FragmentMy extends BaseFragment {
    private FragmentMyBinding binding;
    User user;

    public static FragmentMy newInstance() {
        return new FragmentMy();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        setMyStatusBar();
        initUser();
    }

    private void initUser() {
        user = SharePreferencesUtil.getInstance().getUser();
        if (user != null && user.getEmail() != null && !user.getEmail().isEmpty()) {
            binding.tvName.setText(user.getEmail());
        } else {
            binding.tvName.setText(getString(R.string.login_now));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyBinding.inflate(getLayoutInflater());
        init();
        return binding.getRoot();
    }

    private void init() {
        if (getActivity() != null) {
//            binding.vBgTop.setTranslationY(-getStatusBarHeight(getActivity()));
        }

        binding.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(requireContext(),TestActivity.class));
            }
        });

        binding.llInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (user != null && user.getEmail() != null && !user.getEmail().isEmpty()) {
//
//                }else{
                    startActivity(new Intent(requireContext(),LoginActivity.class));
//                }
            }
        });

        String version = "";
        try {
            version = requireContext().getPackageManager().getPackageInfo(requireContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        binding.tvVer.setText("V" + version);
        binding.ctlVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireContext(),VersionActivity.class));
            }
        });
        binding.ctlLan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireContext(),LanguageSetActivity.class));
            }
        });
        binding.ctlFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireContext(),FeedBackActivity.class));
            }
        });
        binding.ctlPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(),WebActivity.class);
                intent.putExtra("title",getString(R.string.privacy));
                intent.putExtra("url","");
                startActivity(intent);
            }
        });
        binding.ctlAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(),WebActivity.class);
                intent.putExtra("title",getString(R.string.user_agreement));
                intent.putExtra("url","");
                startActivity(intent);
            }
        });
    }
}
package com.future.getd.ui.my;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.fragment.app.Fragment;

import com.future.getd.base.BaseFragment;
import com.future.getd.databinding.FragmentHealthBinding;
import com.future.getd.databinding.FragmentMyBinding;
import com.future.getd.ui.home.FragmentHome;
import com.gyf.immersionbar.ImmersionBar;

public class FragmentMy extends BaseFragment {
    private FragmentMyBinding binding;

    public static FragmentMy newInstance() {
        return new FragmentMy();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ImmersionBar.with(this).init();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyBinding.inflate(getLayoutInflater());
        initView();
        return binding.getRoot();
    }

    private void initView() {
        if (getActivity() != null) {
            binding.vBgTop.setTranslationY(-getStatusBarHeight(getActivity()));
        }
    }
    private  int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources()
                .getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
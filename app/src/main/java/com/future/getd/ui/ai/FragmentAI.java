package com.future.getd.ui.ai;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.future.getd.R;
import com.future.getd.base.BaseFragment;
import com.future.getd.databinding.FragmentAiBinding;
import com.future.getd.databinding.FragmentHomeBinding;
import com.future.getd.ui.home.FragmentHome;
import com.future.getd.utils.ScreenUtil;

public class FragmentAI extends BaseFragment {
    private FragmentAiBinding binding;
    public static FragmentAI newInstance() {
        return new FragmentAI();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAiBinding.inflate(getLayoutInflater());

        ViewGroup.LayoutParams params = binding.vBar.getLayoutParams();
        params.height = ScreenUtil.getStatusHeight(requireContext());
        binding.vBar.setLayoutParams(params);

        init();
        return binding.getRoot();
    }

    private void init() {
        binding.rlChat.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), ChatActivity.class));
        });
    }
}
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
        init();
        return binding.getRoot();
    }

    private void init() {
        binding.viewToolbar.ivSettings.setVisibility(View.GONE);
        binding.rlChat.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), ChatActivity.class));
        });
        binding.rlTranslate.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), TranslateActivity.class));
        });
        binding.rlText.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), VoiceToTextActivity.class));
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        setMainStatusBar();
    }
}
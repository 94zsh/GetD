package com.future.getd.ui.ai;

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
        return binding.getRoot();
    }
}
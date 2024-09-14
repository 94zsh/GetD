package com.future.getd.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.future.getd.R;
import com.future.getd.databinding.FragmentHomeBinding;
import com.future.getd.databinding.FragmentNoneDeviceBinding;

public class NoneDeviceFragment extends Fragment {
    public static NoneDeviceFragment newInstance() {
        return new NoneDeviceFragment();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentNoneDeviceBinding binding = FragmentNoneDeviceBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
}
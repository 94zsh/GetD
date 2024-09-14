package com.future.getd.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.future.getd.R;
import com.future.getd.databinding.FragmentDeviceBinding;
import com.future.getd.databinding.FragmentNoneDeviceBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeviceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeviceFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public DeviceFragment() {
    }

    public static DeviceFragment newInstance() {
        DeviceFragment fragment = new DeviceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentDeviceBinding binding = FragmentDeviceBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
}
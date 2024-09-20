package com.future.getd.ui.health;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.future.getd.R;
import com.future.getd.base.BaseFragment;
import com.future.getd.databinding.FragmentAiBinding;
import com.future.getd.databinding.FragmentHealthBinding;
import com.future.getd.log.LogUtils;
import com.future.getd.ui.adapter.HealthAudioAdapter;
import com.future.getd.ui.bean.AudioItem;
import com.future.getd.ui.my.FragmentMy;
import com.future.getd.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

public class FragmentHealth extends BaseFragment {
    private FragmentHealthBinding binding;
    public static FragmentHealth newInstance() {
        return new FragmentHealth();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHealthBinding.inflate(getLayoutInflater());
        ViewGroup.LayoutParams params = binding.vBar.getLayoutParams();
        params.height = ScreenUtil.getStatusHeight(requireContext());
        binding.vBar.setLayoutParams(params);
        init();
        return binding.getRoot();
    }

    private void init() {
        List<AudioItem> datas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            AudioItem item = new AudioItem(i,getString(R.string.rain),i);
            datas.add(item);
        }
        HealthAudioAdapter adapter = new HealthAudioAdapter(datas);
        binding.rv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        binding.rv.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener<AudioItem>() {
            @Override
            public void onClick(@NonNull BaseQuickAdapter<AudioItem, ?> baseQuickAdapter, @NonNull View view, int i) {
                LogUtils.d("audio onclick item " + i);
            }
        });
    }
}
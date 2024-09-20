package com.future.getd.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.chad.library.adapter4.viewholder.QuickViewHolder;
import com.future.getd.R;
import com.future.getd.ui.bean.DeviceSettings;
import com.future.getd.ui.view.CircleView;
import com.jieli.bluetooth.impl.rcsp.RCSPController;

import java.util.List;

public class DeviceListAdapter extends BaseQuickAdapter<DeviceSettings, QuickViewHolder> {

    public DeviceListAdapter(@NonNull List<? extends DeviceSettings> items) {
        super(items);
    }

    @Override
    protected void onBindViewHolder(@NonNull QuickViewHolder quickViewHolder, int i, @Nullable DeviceSettings settings) {
        // 设置item数据
        if (settings != null) {
            quickViewHolder.setText(R.id.name,settings.getName());
            CircleView circleView = quickViewHolder.getView(R.id.cv);

            circleView.setColor(getContext().getResources().getColor(R.color.bga8));
            quickViewHolder.setText(R.id.tv_state,getContext().getString(R.string.state_disconnected));
            if (RCSPController.getInstance().isDeviceConnected()){
                if(RCSPController.getInstance().getUsingDevice() != null){
                    if(settings.getClassicMac().equals(RCSPController.getInstance().getUsingDevice().getAddress())){
                        circleView.setColor(getContext().getResources().getColor(R.color.state_connected));
                        quickViewHolder.setText(R.id.tv_state,getContext().getString(R.string.state_connected));
                    }
                }
            }
        }
    }

    @NonNull
    @Override
    protected QuickViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
        // 返回一个 ViewHolder
        return new QuickViewHolder(R.layout.item_device,viewGroup);
    }
}

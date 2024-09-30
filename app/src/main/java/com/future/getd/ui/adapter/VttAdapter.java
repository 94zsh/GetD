package com.future.getd.ui.adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.future.getd.R;
import com.future.getd.databinding.ItemAudioBinding;
import com.future.getd.databinding.ItemVttBinding;
import com.future.getd.ui.bean.VoiceToTextBean;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class VttAdapter extends BaseQuickAdapter<VoiceToTextBean, VttAdapter.viewHolder> {
    public VttAdapter(@NonNull List<? extends VoiceToTextBean> items) {
        super(items);
    }

    @NonNull
    @Override
    protected viewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup parent, int i) {
        ItemVttBinding binding = ItemVttBinding.inflate(LayoutInflater.from(context),parent,false);
        viewHolder viewHolder = new viewHolder(binding.getRoot(),binding);
        return viewHolder;
    }

    @Override
    protected void onBindViewHolder(@NonNull viewHolder viewHolder, int i, @Nullable VoiceToTextBean bean) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        SimpleDateFormat sdfSub = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if (bean != null) {
            String now = sdf.format(new Date(bean.getTime()));
            viewHolder.title.setText(String.format("%s%s", getContext().getResources().getString(R.string.real_time_translate), sdf.format(new Date(bean.getTime()))));
            viewHolder.time.setText(sdfSub.format(new Date(bean.getTime())));
        }

    }

    class viewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView time;
        ImageView delete;
        public viewHolder(@NonNull View itemView,ItemVttBinding vb) {
            super(itemView);
            title = vb.title;
            time = vb.time;
            delete = vb.delete;
        }
    }
}

package com.future.getd.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.chad.library.adapter4.viewholder.QuickViewHolder;
import com.future.getd.base.BaseViewModel;
import com.future.getd.databinding.ItemAudioBinding;
import com.future.getd.ui.bean.AudioItem;

import java.util.List;

public class HealthAudioAdapter extends BaseQuickAdapter<AudioItem, HealthAudioAdapter.viewHolder> {
    public HealthAudioAdapter(@NonNull List<? extends AudioItem> items) {
        super(items);
    }

    @NonNull
    @Override
    protected viewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup parent, int i) {
        ItemAudioBinding binding = ItemAudioBinding.inflate(LayoutInflater.from(context),parent,false);
        viewHolder viewHolder = new viewHolder(binding.getRoot(),binding);
        return viewHolder;
    }

    @Override
    protected void onBindViewHolder(@NonNull viewHolder viewHolder, int i, @Nullable AudioItem audioItem) {
//            viewHolder.binding.tvContent.setText(audioItem.getContent());
    }

    class viewHolder extends RecyclerView.ViewHolder {
        ImageView ivType;
        TextView tvContent;

        public viewHolder(@NonNull View itemView,ItemAudioBinding vb) {
            super(itemView);
            ivType = vb.ivType;
        }
    }
}

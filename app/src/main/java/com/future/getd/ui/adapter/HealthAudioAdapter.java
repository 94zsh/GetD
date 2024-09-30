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
import com.future.getd.R;
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
        viewHolder.ivType.setImageResource(getImage(audioItem.getType()));
        viewHolder.tvContent.setText(audioItem.getContent());
        viewHolder.state.setImageResource(audioItem.isPlaying() ? R.drawable.audio_pause : R.drawable.audio_play);
    }

    class viewHolder extends RecyclerView.ViewHolder {
        ImageView ivType;
        TextView tvContent;
        ImageView state;
        public viewHolder(@NonNull View itemView,ItemAudioBinding vb) {
            super(itemView);
            ivType = vb.ivType;
            tvContent = vb.content;
            state = vb.ivState;
        }
    }

    private int getImage(int type){
        int res = R.drawable.meditation_rain;
        if (type == 0) {
            res = R.drawable.meditation_rain;
        } else if (type == 1) {
            res = R.drawable.meditation_thunder;
        } else if (type == 2) {
            res = R.drawable.meditation_mdeditation;
        } else if (type == 3) {
            res = R.drawable.meditation_rivers;
        } else if (type == 4) {
            res = R.drawable.meditation_sleep;
        } else if (type == 5) {
            res = R.drawable.meditation_fire;
        } else if (type == 6) {
            res = R.drawable.meditation_frog;
        } else if (type == 7) {
            res = R.drawable.meditation_wave;
        } else if (type == 8) {
            res = R.drawable.meditation_forest;
        }
        return res;
    }
}

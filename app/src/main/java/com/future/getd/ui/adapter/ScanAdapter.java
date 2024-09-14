package com.future.getd.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.future.getd.databinding.ItemScanDeviceBinding;
import com.future.getd.log.LogUtils;
import com.future.getd.ui.bean.ScanDevice;
import com.future.getd.ui.home.ConnectingActivity;

import java.util.List;

public class ScanAdapter extends BaseQuickAdapter<ScanDevice, ScanAdapter.ViewHolder> {
    private List<ScanDevice> mOldList;
    private List<ScanDevice> mNewList;

//    public ScanAdapter(@NonNull List<? extends ScanDevice> items) {
//        super(items);
//    }

    public ScanAdapter(List<ScanDevice> mNewList) {
        this.mOldList = mNewList;
        this.mNewList = mNewList;
        setItems(mNewList);
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup parent, int position) {
        ItemScanDeviceBinding binding = ItemScanDeviceBinding.inflate(LayoutInflater.from(context),parent,false);
        ViewHolder vh = new ViewHolder(binding.getRoot(),binding);
        return vh;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder vh, int i, @Nullable ScanDevice item) {
        vh.tvName.setText(item.getName());
        vh.tvMac.setText(item.getMac());
        vh.btConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onClick(vh.getLayoutPosition());
                }
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView tvName;
        TextView tvMac;
        TextView btConnect;

        public ViewHolder(@NonNull View itemView,ItemScanDeviceBinding vb) {
            super(itemView);
            icon = vb.icon;
            tvName = vb.tvName;
            tvMac = vb.tvMac;
            btConnect = vb.btConnect;
        }
    }

    private OnClickListener listener;
    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    public interface OnClickListener {
        public void onClick(int position);
    }

    // 更新数据集
    public void updateList(List<ScanDevice> newList) {
        // 计算差异
        DiffUtil.Callback callback = new MyCallback(mOldList, newList);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback,true);

        // 更新数据集
        setItems(newList);
        result.dispatchUpdatesTo(this);
//        notifyDataSetChanged();
    }

    // 自定义的 DiffUtil.Callback
    private class MyCallback extends DiffUtil.Callback {
        private List<ScanDevice> mOldList;
        private List<ScanDevice> mNewList;

        public MyCallback(List<ScanDevice> oldList, List<ScanDevice> newList) {
            mOldList = oldList;
            mNewList = newList;
        }

        @Override
        public int getOldListSize() {
            LogUtils.i("getOldListSize :  " + mOldList.size());
            return mOldList.size();
        }

        @Override
        public int getNewListSize() {
            LogUtils.i("getNewListSize :  " + mNewList.size());
            return mNewList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return mOldList.get(oldItemPosition).getMac().equals(mNewList.get(newItemPosition).getMac());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return mOldList.get(oldItemPosition).getMac().equals(mNewList.get(newItemPosition).getMac());
        }
    }


}

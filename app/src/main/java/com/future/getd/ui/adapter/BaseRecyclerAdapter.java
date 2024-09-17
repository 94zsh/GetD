package com.future.getd.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    private Context mContext;
    private int mLayoutId;
    private List<T> mData;


    protected BaseRecyclerAdapter(Context mContext, int mLayoutId, List<T> mData) {
        this.mContext = mContext;
        this.mLayoutId = mLayoutId;
        this.mData = mData;
    }

    public void setData( List<T> mData){
        this.mData = mData;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(mLayoutId, parent, false);
        BaseViewHolder viewHolder = new BaseViewHolder(mContext, itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(clickListener != null)
                    clickListener.onClick(view, (int) view.getTag());
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(clickLongListener != null)
                    clickLongListener.onLongClick(view, (int) view.getTag());
                return true;
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.itemView.setTag(position);
        convert(holder, mData.get(position),position);
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * 对外提供的方法
     */
    public abstract void convert(BaseViewHolder holder, T t,int position);


    /**
     * 针对多种类型的itemView
     *
     * @param <T>
     */
    public interface ConmonItemType<T> {
        int getLayoutId(int itemType); //不同的Item的布局

        int getItemViewType(int position, T t); //type
    }

    private OnItemClickListener clickListener;
    private OnItemLongClickListener clickLongListener;

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener clickLongListener) {
        this.clickLongListener = clickLongListener;
    }

    public static interface OnItemClickListener {
        void onClick(View view, int position);
    }
    public static interface OnItemLongClickListener {
        void onLongClick(View view, int position);
    }
}

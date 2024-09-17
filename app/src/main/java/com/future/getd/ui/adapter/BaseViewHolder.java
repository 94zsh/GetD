package com.future.getd.ui.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


public class BaseViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews; //用来存储控件
    private View mConvertView;
    private Context mContext;


    public BaseViewHolder(Context context, View itemView) {
        super(itemView);
        this.mContext = context;
        mConvertView = itemView;
        mViews = new SparseArray<View>();
    }


    /**
     * 提供一个获取ViewHolder的方法
     */
    public static BaseViewHolder getRecyclerHolder(Context context, ViewGroup parent, int layoutId) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        BaseViewHolder viewHolder = new BaseViewHolder(context, itemView);
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
    private static BaseRecyclerAdapter.OnItemClickListener clickListener;
    private static BaseRecyclerAdapter.OnItemLongClickListener clickLongListener;

    public void setLisener(BaseRecyclerAdapter.OnItemClickListener lisener){
        this.clickListener = lisener;
    }
    public void setLongLisener(BaseRecyclerAdapter.OnItemLongClickListener lisener){
        this.clickLongListener = lisener;
    }

    /**
     * 获取控件
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }


    /**
     * 给TextView设置setText方法
     */
    public BaseViewHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }


    /**
     * 给ImageView设置setImageResource方法
     */
    public BaseViewHolder setImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }


    /**
     * 给TextView设置setText方法
     */
    public BaseViewHolder setVisibility(int viewId, boolean isShow) {
        View view = getView(viewId);
        view.setVisibility(isShow?View.VISIBLE:View.GONE);
        return this;
    }

    /**
     * 添加点击事件
     */
    public BaseViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }


}
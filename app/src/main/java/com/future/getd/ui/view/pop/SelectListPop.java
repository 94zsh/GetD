package com.future.getd.ui.view.pop;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.future.getd.R;
import com.future.getd.log.LogUtils;
import com.future.getd.ui.adapter.BaseRecyclerAdapter;
import com.future.getd.ui.adapter.BaseViewHolder;
import com.lxj.xpopup.core.BottomPopupView;

import java.util.ArrayList;
import java.util.List;

public class SelectListPop extends BottomPopupView {
    private String title = "";
    private List<Item> data = new ArrayList<>();
    BaseRecyclerAdapter<Item> adapter;
    public SelectListPop(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.popup_select_list;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(title);
        RecyclerView rv_list = findViewById(R.id.rv_list);
        adapter = new BaseRecyclerAdapter<Item>(this.getContext(),R.layout.item_select_pop,data) {
            @Override
            public void convert(BaseViewHolder holder, Item item, int position) {
                LinearLayout ll_item = holder.getView(R.id.ll_item);
                ImageView iv_state = holder.getView(R.id.iv_state);
                TextView tv_item = holder.getView(R.id.tv_item);
                tv_item.setText(item.name);
                if(item.isSelect){
                    iv_state.setImageResource(R.drawable.pic_check_checked);
//                    tv_item.setTextColor(getResources().getColor(R.color.blue));
                }else{
                    iv_state.setImageResource(R.drawable.pic_check_uncheck);
                    tv_item.setTextColor(getResources().getColor(R.color.black));
                }
                if(position == data.size() - 1){
                    holder.setVisibility(R.id.line,false);
                }
//                ll_item.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        onSelectListener.onSelect(position,item.name);
//                    }
//                });
            }
        };
        rv_list.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false));
        rv_list.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                LogUtils.d("selectlist OnItemClickListener:" + position);
                Item item = data.get(position);
                for (int i = 0; i < data.size(); i++) {
                    if(i == position){
                        data.get(i).isSelect = true;
                    }else{
                        data.get(i).isSelect = false;
                    }
                }
                adapter.notifyDataSetChanged();
                if(onSelectListener != null){
                    onSelectListener.onSelect(position,item.name,item.value);
                }
                dismiss();
            }
        });
        TextView tv_cancel = findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private OnSelectListener onSelectListener = null;
    public interface OnSelectListener {
        void onSelect(int position, String text,int value);
    }
    public void  setOnSelectListener(OnSelectListener listener) {
        this.onSelectListener = listener;
    }

    public void setData(String title,List<Item> data){
        this.data = data;
        this.title = title;
    }


    public static class Item{
        public String name;
        public boolean isSelect;
        public int value;

        public Item(String name, boolean isSelect,int value) {
            this.name = name;
            this.isSelect = isSelect;
            this.value = value;
        }
    }
}

package com.future.getd.ui.view.pop;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.future.getd.R;
import com.future.getd.log.LogUtils;
import com.future.getd.ui.adapter.BaseRecyclerAdapter;
import com.future.getd.ui.adapter.BaseViewHolder;
import com.future.getd.ui.bean.ButtonFunction;
import com.future.getd.utils.DrawUtil;

import java.util.ArrayList;
import java.util.List;

public class SelectListPop extends Dialog {
    private String title = "";
    private List<Item> data = new ArrayList<>();
    BaseRecyclerAdapter<Item> adapter;
    private Context mContext;
    private View mContentView;
    public SelectListPop(@NonNull Context context,String title,List<Item> data) {
        super(context);
        mContext = context;
        this.title = title;
        this.data = data;
        init();
    }

//    @Override
//    protected int getImplLayoutId() {
//        return R.layout.popup_select_list;
//    }

//    public SelectListPop(View contentView) {
//        super(contentView);
//    }

    protected void init() {
//        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
//        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        mContentView = layoutInflater.inflate(R.layout.popup_select_list,null);
        TextView tv_title = mContentView.findViewById(R.id.tv_title);
        tv_title.setText(title);
        RecyclerView rv_list = mContentView.findViewById(R.id.rv_list);
        adapter = new BaseRecyclerAdapter<Item>(mContext,R.layout.item_select_pop,data) {
            @Override
            public void convert(BaseViewHolder holder, Item item, int position) {
                LinearLayout ll_item = holder.getView(R.id.ll_item);
                ImageView ivType = holder.getView(R.id.iv_type);
                ImageView iv_state = holder.getView(R.id.iv_state);
                TextView tv_item = holder.getView(R.id.tv_item);
                tv_item.setText(item.name);
                ivType.setImageResource(getIcByType(data.get(position).value));
                if(item.isSelect){
                    iv_state.setImageResource(R.drawable.pic_check_checked);
//                    tv_item.setTextColor(getResources().getColor(R.color.blue));
                }else{
                    iv_state.setImageResource(R.drawable.pic_check_uncheck);
                    tv_item.setTextColor(mContext.getResources().getColor(R.color.black));
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
        rv_list.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL, false));
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
        TextView tv_cancel = mContentView.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setContentView(mContentView);
//        setOutsideTouchable(true);
//        setFocusable(true);
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

    public void show(View anchor){
//        showAtLocation(anchor, Gravity.BOTTOM,0,0);
        //set position
        Window dialogWindow = getWindow();
        int padding = DrawUtil.dp2px(mContext,10);
        dialogWindow.getDecorView().setPadding(padding,padding,padding,padding);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        dialogWindow.setAttributes(lp);
        show();
    }
    public void dismiss(){
        super.dismiss();
    }

    //   public static final int FUNID_NONE = 0;
    //    public static final int FUNID_PERVIOUS = 1;
    //    public static final int FUNID_NEXT = 2;
    //    public static final int FUNID_PLAYPAUSE = 3;
    //    public static final int FUNID_CALL_RECEIVE = 4;
    //    public static final int FUNID_CALL_HAND_UP = 5;
    //    public static final int FUNID_CALL_RECALL = 6;
    //    public static final int FUNID_ADD = 7;
    //    public static final int FUNID_SUB = 8;
    private int getIcByType(int type){
        int icon = R.drawable.ic_fun_none;
        if (type == ButtonFunction.DEVICE_KEY_FUNCTION_NONE) {
            icon = R.drawable.ic_fun_none;
        }else if (type == ButtonFunction.DEVICE_KEY_FUNCTION_PREVIOUS) {
            icon = R.drawable.ic_fun_pre;
        }else if (type == ButtonFunction.DEVICE_KEY_FUNCTION_NEXT) {
            icon = R.drawable.ic_fun_next;
        }else if (type == ButtonFunction.DEVICE_KEY_FUNCTION_PLAY_PAUSE) {
            icon = R.drawable.ic_fun_play_pause;
        }else if (type == ButtonFunction.DEVICE_KEY_FUNCTION_ANSWER_CALL) {
            icon = R.drawable.ic_fun_receive;
        }else if (type == ButtonFunction.DEVICE_KEY_FUNCTION_HANG_UP) {
            icon = R.drawable.ic_fun_handup;
        }else if (type == ButtonFunction.DEVICE_KEY_FUNCTION_CALL_BACK) {
            icon = R.drawable.ic_fun_receive;
        }else if (type == ButtonFunction.DEVICE_KEY_FUNCTION_VOLUME_UP) {
            icon = R.drawable.ic_fun_add;
        }else if (type == ButtonFunction.DEVICE_KEY_FUNCTION_VOLUME_DOWN) {
            icon = R.drawable.ic_fun_sub;
        }
        return icon;
    }
}

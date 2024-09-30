package com.future.getd.ui.view.pop;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.future.getd.R;
import com.future.getd.ui.adapter.BaseRecyclerAdapter;
import com.future.getd.ui.ai.ChatActivity;
import com.future.getd.utils.DrawUtil;

public class RecordingGifPop extends Dialog {
    BaseRecyclerAdapter<Item> adapter;
    private Context mContext;
    private View mContentView;
    public RecordingGifPop(@NonNull Context context) {
        super(context);
        mContext = context;
        init();
    }


    protected void init() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        mContentView = layoutInflater.inflate(R.layout.popup_recording_gif,null);
        mContentView.findViewById(R.id.bt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onSelectListener != null){
                    onSelectListener.onCancel();
                }
                dismiss();
            }
        });
        Glide.with(mContext)
                .asGif()
                .load(R.raw.recording4)
                .into((ImageView) mContentView.findViewById(R.id.iv_recording));
        setContentView(mContentView);
//        setOutsideTouchable(true);
//        setFocusable(true);

    }

    private OnSelectListener onSelectListener = null;
    public interface OnSelectListener {
        void onCancel();
    }
    public void  setOnSelectListener(OnSelectListener listener) {
        this.onSelectListener = listener;
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

    public void showStart(){
        Window dialogWindow = getWindow();
//        int padding = DrawUtil.dp2px(mContext,0);
//        dialogWindow.getDecorView().setPadding(padding,padding,padding,padding);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);
        dialogWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        startAmina();
        show();
    }
    public void dismiss(){
        super.dismiss();
    }

    public void startAmina(){
    }

    public void stopAmina(){
    }
}

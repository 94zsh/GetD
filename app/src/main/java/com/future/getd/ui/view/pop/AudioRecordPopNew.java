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
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.future.getd.R;
import com.future.getd.ui.adapter.BaseRecyclerAdapter;
import com.future.getd.ui.ai.ChatActivity;
import com.future.getd.utils.DrawUtil;

public class AudioRecordPopNew extends Dialog {
    private Context mContext;
    private View mContentView;
    public AudioRecordPopNew(@NonNull Context context) {
        super(context);
        mContext = context;
        init();
    }


    protected void init() {
//        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
//        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        mContentView = layoutInflater.inflate(R.layout.popup_recording_press,null);
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
        TextView tip = mContentView.findViewById(R.id.tip);
        tip.setText(mContext.getString(R.string.press_tip));
        show();
    }
    public void dismiss(){
        super.dismiss();
    }

    public void startAmina(){
        ImageView gif = mContentView.findViewById(R.id.gif);
        Glide.with(mContext)
                .asGif()
                .load(R.raw.recording2)
                .into(gif);
    }

    public void stopAmina(){
        ImageView gif = mContentView.findViewById(R.id.gif);
        Glide.with(mContext)
                .clear(gif);
    }

    public void setCancelText(){
        TextView tip = mContentView.findViewById(R.id.tip);
        tip.setText(mContext.getString(R.string.cancel));
    }
    public void setTipText(String content){
        TextView tip = mContentView.findViewById(R.id.tip);
        tip.setText(content);
    }

}

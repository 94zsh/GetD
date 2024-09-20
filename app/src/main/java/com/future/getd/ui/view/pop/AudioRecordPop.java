package com.future.getd.ui.view.pop;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.future.getd.ui.bean.ButtonFunction;
import com.future.getd.utils.DrawUtil;

import java.util.ArrayList;
import java.util.List;

public class AudioRecordPop extends Dialog {
    BaseRecyclerAdapter<Item> adapter;
    private Context mContext;
    private View mContentView;
    private int[] res = { R.drawable.amp1, R.drawable.amp2,
            R.drawable.amp3, R.drawable.amp4, R.drawable.amp5, R.drawable.amp6,
            R.drawable.amp7 };
    public AudioRecordPop(@NonNull Context context) {
        super(context);
        mContext = context;
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
        mContentView = layoutInflater.inflate(R.layout.popup_audio_recording,null);
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
        int padding = DrawUtil.dp2px(mContext,10);
        dialogWindow.getDecorView().setPadding(padding,padding,padding,padding);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);
        startAmina();
        show();
    }
    public void dismiss(){
        super.dismiss();
    }

    public void startAmina(){
        ImageView ivAnima = mContentView.findViewById(R.id.iv_animation);
        AnimationDrawable frameAnimation = (AnimationDrawable) ivAnima.getBackground();
        frameAnimation.start();
    }

    public void stopAmina(){
        ImageView ivAnima = mContentView.findViewById(R.id.iv_animation);
        AnimationDrawable frameAnimation = (AnimationDrawable) ivAnima.getBackground();
        if(frameAnimation.isRunning()){
            frameAnimation.stop();
        }
//        ivAnima.clearAnimation();
    }
}

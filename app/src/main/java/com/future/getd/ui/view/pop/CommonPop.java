package com.future.getd.ui.view.pop;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.future.getd.R;
import com.future.getd.ui.adapter.BaseRecyclerAdapter;
import com.future.getd.utils.DrawUtil;

public class CommonPop extends Dialog {
    BaseRecyclerAdapter<Item> adapter;
    private Context mContext;
    private View mContentView;
    private String title, content,confirmText;
    public CommonPop(@NonNull Context context) {
        super(context);
        mContext = context;
        init();
    }

    public CommonPop(@NonNull Context context,String title,String content,String confirmText) {
        super(context);
        mContext = context;
        this.title = title;
        this.content = content;
        this.confirmText = confirmText;
        init();
    }

    protected void init() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        mContentView = layoutInflater.inflate(R.layout.pop_common,null);
        TextView tv_title = mContentView.findViewById(R.id.tv_title);
        TextView tv_content = mContentView.findViewById(R.id.tv_content);
        TextView tv_cancel = mContentView.findViewById(R.id.tv_cancel);
        TextView tv_confirm = mContentView.findViewById(R.id.tv_confirm);

        tv_title.setText(title);
        tv_content.setText(content);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(listener != null)
                    listener.onCancel();
            }
        });
        tv_confirm.setText(confirmText);
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(listener != null)
                    listener.onConfirm();
            }
        });
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        setContentView(mContentView);
    }

    private OnSelectListener listener = null;
    public interface OnSelectListener {
        void onCancel();
        void onConfirm();
    }
    public void  setOnSelectListener(OnSelectListener listener) {
        this.listener = listener;
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
        lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
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

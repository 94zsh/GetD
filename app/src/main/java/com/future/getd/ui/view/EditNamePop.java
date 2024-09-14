package com.future.getd.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.future.getd.R;
import com.future.getd.log.LogUtils;
import com.future.getd.utils.DrawUtil;
import com.jieli.bluetooth.utils.JL_Log;
import com.jieli.component.utils.ToastUtil;


public class EditNamePop extends Dialog {
    private String name = "";
    public EditNamePop(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set content
        View contentView = getLayoutInflater().inflate(R.layout.pop_edit_name, null);
        EditText etName = contentView.findViewById(R.id.et_name);
        ImageView iv_clear = contentView.findViewById(R.id.iv_clear);
        TextView tv_cancel = contentView.findViewById(R.id.tv_cancel);
        TextView tv_confirm = contentView.findViewById(R.id.tv_confirm);

        etName.setText(name);
        iv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etName.setText("");
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = etName.getText().toString();
                LogUtils.d("set name : " + value);
                if (TextUtils.isEmpty(value)) {
                    ToastUtil.showToastShort(R.string.tip_empty_device_name);
                } else if (value.equals(name)) {
                    ToastUtil.showToastShort(R.string.tip_same_device_name);
                } else if (value.getBytes().length > 20) {

                } else {
                    if (onSelectListener != null) {
                        onSelectListener.onConfirm(value);
                    }
                    dismiss();
                }

            }
        });
        setContentView(contentView);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        //set position
        Window dialogWindow = getWindow();
        int padding = DrawUtil.dp2px(getContext(),10);
        dialogWindow.getDecorView().setPadding(padding,padding,padding,padding);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);
    }

    private SelectListener onSelectListener = null;
    public void  setOnSelectListener(SelectListener listener) {
        this.onSelectListener = listener;
    }

    public interface SelectListener{
        void onConfirm(String name);
        void onCancel();
    }
    public void setContent(String name){
        this.name = name;
    }
}

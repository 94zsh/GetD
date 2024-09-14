package com.future.getd.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.fragment.app.FragmentActivity;
import androidx.viewbinding.ViewBinding;

import com.future.getd.MainActivity;
import com.future.getd.R;
import com.future.getd.ui.statusbar.StatusBarCompat;
import com.future.getd.utils.DrawUtil;
import com.gyf.immersionbar.ImmersionBar;
import com.jieli.bluetooth.impl.rcsp.RCSPController;

public abstract class BaseActivity<vb extends ViewBinding> extends FragmentActivity {
    public vb binding;
    private boolean onSaveInstanceState = false;
    private String TAG = MainActivity.class.getSimpleName();
    public RCSPController rcspController = RCSPController.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        ImmersionBar.with(this).transparentBar().statusBarDarkFont(true).init();
        //        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.white));
        binding = getBinding();
        setContentView(binding.getRoot());
        initView();
        initData();

        //        ImmersionBar.with(this)
        //                .statusBarDarkFont(true,0.2f)
        //                .navigationBarColor(R.color.f5f5f5_121212)
        ////                .transparentStatusBar()
        ////                .transparentNavigationBar()
        ////                .fitsSystemWindows(true)
        //                .init();
    }

    protected abstract vb getBinding();
    protected abstract void initView();
    protected abstract void initData();

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        onSaveInstanceState=true;
    }

    public boolean isStateSave()
    {
        return onSaveInstanceState;
    }


    @Override
    protected void onResume() {
        super.onResume();
        onSaveInstanceState=false;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    AlertDialog loadingPop;
    public void showLoadingPop(String content,boolean enable){
    }

    public void setStatusColor(int color){
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(color));
    }


    public void showToast(String content){
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    public void reConnect() {
    }

    public void showCommonPop(String title,String content,String confirmText,View.OnClickListener listener){
        //set content
        View contentView = getLayoutInflater().inflate(R.layout.pop_common, null);
        Dialog dialog = new Dialog(this,R.style.BaseDialog);
        TextView tv_title = contentView.findViewById(R.id.tv_title);
        TextView tv_content = contentView.findViewById(R.id.tv_content);
        TextView tv_cancel = contentView.findViewById(R.id.tv_cancel);
        TextView tv_confirm = contentView.findViewById(R.id.tv_confirm);

        tv_title.setText(title);
        tv_content.setText(content);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_confirm.setText(confirmText);
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(listener != null)
                    listener.onClick(v);
            }
        });
        dialog.setContentView(contentView);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        //set position
        Window dialogWindow = dialog.getWindow();
        int padding = DrawUtil.dp2px(this,50);
        dialogWindow.getDecorView().setPadding(padding,padding,padding,padding);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);
        if(!this.isFinishing() && !dialog.isShowing())
            dialog.show();
    }
}

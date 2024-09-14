package com.future.getd.base;

import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.future.getd.R;
import com.future.getd.utils.DrawUtil;

public class BaseFragment extends Fragment {
    public void showToast(String content){
        Toast.makeText(getActivity(), content, Toast.LENGTH_SHORT).show();
    }


    public void showCommonPop(String title, String content, String confirmText, View.OnClickListener listener){
        //set content
        View contentView = getLayoutInflater().inflate(R.layout.pop_common, null);
        Dialog dialog = new Dialog(requireContext(),R.style.BaseDialog);
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
        int padding = DrawUtil.dp2px(requireContext(),10);
        dialogWindow.getDecorView().setPadding(padding,padding,padding,padding);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);
        if(!requireActivity().isFinishing() && !dialog.isShowing())
            dialog.show();
    }
}

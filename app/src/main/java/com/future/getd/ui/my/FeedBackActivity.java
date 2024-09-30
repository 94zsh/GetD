package com.future.getd.ui.my;

import android.os.Bundle;
import android.view.View;

import com.future.getd.R;
import com.future.getd.base.BaseActivity;
import com.future.getd.databinding.ActivityFeedBackBinding;
import com.future.getd.log.LogUtils;
import com.future.getd.ui.view.pop.SelectListPop;
import com.future.getd.utils.EmailUtils;

import java.util.ArrayList;
import java.util.List;

public class FeedBackActivity extends BaseActivity<ActivityFeedBackBinding> {
    int currentType = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusColor(R.color.bg_main);
    }

    @Override
    protected ActivityFeedBackBinding getBinding() {
        return ActivityFeedBackBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        binding.title.ivBack.setOnClickListener( v -> {finish();});
        binding.title.tvTitle.setText(getString(R.string.feedback));

        binding.ctlCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectListPop.Item item = new SelectListPop.Item(getString(R.string.feedback_category_quality),currentType == 0,0);
                SelectListPop.Item item2 = new SelectListPop.Item(getString(R.string.feedback_category_volume),currentType == 1,1);
                SelectListPop.Item item3 = new SelectListPop.Item(getString(R.string.feedback_category_connect),currentType == 2,2);
                List<SelectListPop.Item> itemList = new ArrayList<>();
                itemList.add(item);
                itemList.add(item2);
                itemList.add(item3);
                SelectListPop selectListPopup = new SelectListPop(FeedBackActivity.this,getString(R.string.feedback_category),itemList);
                selectListPopup.setHideIcon(true);
                selectListPopup.setOnSelectListener(new SelectListPop.OnSelectListener() {
                    @Override
                    public void onSelect(int position, String text,int value) {
                        currentType = value;
                        binding.tvCategory.setText(text);
                    }
                });
                selectListPopup.show(binding.main);
            }
        });

        binding.commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = binding.content.getText().toString();
//                currentType;
                String email = binding.tvContact.getText().toString();
                LogUtils.i("feedback : " + content + " , " + currentType + " , " + email);
                if (checkInput(content, email)) {
                    showToastLong(getString(R.string.commit_success));
                    finish();
                }
            }
        });
    }

    @Override
    protected void initData() {

    }

    private boolean checkInput(String content,String email){
        boolean result = true;
        if(content.isEmpty()){
            showToast(getString(R.string.empty_tip));
            return false;
        }
        if(email.isEmpty()){
            showToast(getString(R.string.email_input_tip));
            return false;
        }
        if(!EmailUtils.isEmail(email)){
            showToast(getString(R.string.invalid_email));
            return false;
        }
        return result;

    }

}
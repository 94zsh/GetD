package com.future.getd.ui.my;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.future.getd.MainActivity;
import com.future.getd.R;
import com.future.getd.base.BaseActivity;
import com.future.getd.databinding.ActivityRegisterBinding;
import com.future.getd.log.LogUtils;
import com.future.getd.net.bean.account.CommonResponse;
import com.future.getd.net.bean.account.RegisterRequest;
import com.future.getd.net.bean.account.RegisterResponse;
import com.future.getd.utils.EmailUtils;
import com.future.getd.utils.RetrofitUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity<ActivityRegisterBinding> {
    int type = 0;// 0 注册  1 找回密码
    private boolean isShow,isShowConfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusColor(R.color.white);
    }

    @Override
    protected ActivityRegisterBinding getBinding() {
        return ActivityRegisterBinding.inflate(getLayoutInflater());
    }

    //倒计时
    private final CountDownTimer timer = new CountDownTimer(60 * 1000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            binding.sendCode.setText(millisUntilFinished / 1000 + "");
        }

        @Override
        public void onFinish() {
            //倒计时完成时候调用。
            binding.sendCode.setClickable(true);
            binding.sendCode.setText(R.string.send_code);
        }
    };
    @Override
    protected void initView() {
        binding.sendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String  email = binding.email.getText().toString().trim();
                if (email.isEmpty()) {
                    showToast(getString(R.string.email_input_tip));
                    return;
                }

                if (!EmailUtils.isEmail(email)) {
                    showToast(getString(R.string.invalid_email));
                    return;
                }

                timer.cancel();
                timer.start();
                binding.sendCode.setClickable(false);
                RetrofitUtil.getApiService().sendMailCode(email,type).enqueue(new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                        LogUtils.i("sendMailCode response : " + response.body());
                        if(response.code() == 200){
                            if(response.body().getCode() == 200){
                                showToast(getString(R.string.commit_success));
                            }else{
                                showToast(response.body().getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t) {
                        LogUtils.i("sendMailCode onFailure : " + t);
                        showToast(t.getMessage());
                    }
                });
            }
        });

        binding.ivPsw.setOnClickListener(v -> {
            if (isShow) {
                binding.ivPsw.setImageResource(R.drawable.ic_pwd_show_24dp);
                binding.psw.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                binding.ivPsw.setImageResource(R.drawable.ic_pwd_hide_24dp);
                binding.psw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            binding.psw.setSelection(binding.psw.getText().toString().trim().length());
            isShow = !isShow;
        });
        binding.ivPswConfirm.setOnClickListener(v -> {
            if (isShowConfirm) {
                binding.ivPswConfirm.setImageResource(R.drawable.ic_pwd_show_24dp);
                binding.pswConfirm.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                binding.ivPswConfirm.setImageResource(R.drawable.ic_pwd_hide_24dp);
                binding.pswConfirm.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            binding.pswConfirm.setSelection(binding.pswConfirm.getText().toString().trim().length());
            isShowConfirm = !isShowConfirm;
        });

        binding.commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.email.getText().toString();
                String code = binding.code.getText().toString();
                String psw = binding.psw.getText().toString();
                String pswConfirm = binding.pswConfirm.getText().toString();
                LogUtils.i("login : " + email + " , " + psw + " , " + psw);
                if (checkInput(email, code, psw, pswConfirm)) {
                    // TODO: 2024/9/25 注册、找回密码
                    if(type == 0){
                        RegisterRequest request = new RegisterRequest(email,psw,code);
                        RetrofitUtil.getApiService().registerEmail(request).enqueue(new Callback<RegisterResponse>() {
                            @Override
                            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                                LogUtils.i("registerEmail response : " + response.body());
                                if(response.code() == 200){
                                    if(response.body().getCode() == 200){
                                        showToast(getString(R.string.commit_success));
                                        finish();
                                    }else{
                                        showToast(response.body().getMessage());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                                LogUtils.i("registerEmail onFailure : " + t);
                                showToast(t.getMessage());
                            }
                        });
                    }else if(type == 1){
                        RegisterRequest request = new RegisterRequest(email,psw,code);
                        RetrofitUtil.getApiService().forgetPsw(request).enqueue(new Callback<RegisterResponse>() {
                            @Override
                            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                                LogUtils.i("forgetPsw response : " + response.body());
                                if(response.code() == 200){
                                    if(response.body().getCode() == 200){
                                        showToast(getString(R.string.commit_success));
                                        finish();
                                    }else{
                                        showToast(response.body().getMessage());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                                LogUtils.i("forgetPsw onFailure : " + t);
                                showToast(t.getMessage());
                            }
                        });
                    }

                }
            }
        });
    }

    @Override
    protected void initData() {
        type = getIntent().getIntExtra("type",0);
        if(type == 0){
            binding.title.setText(getString(R.string.register));
        }else{
            binding.title.setText(getString(R.string.psw_forget));
        }
    }


    private boolean checkInput(String email,String code ,String psw,String confirmPsw){
        boolean result = true;
        if(email.isEmpty()){
            showToast(getString(R.string.email_input_tip));
            return false;
        }
        if(!EmailUtils.isEmail(email)){
            showToast(getString(R.string.invalid_email));
            return false;
        }

        if(code.isEmpty()){
            showToast(getString(R.string.input_code));
            return false;
        }

        if(psw.isEmpty()){
            showToast(getString(R.string.psw_input_tip));
            return false;
        }

        if(confirmPsw.isEmpty()){
            showToast(getString(R.string.comfirm_psw_tip));
            return false;
        }
        if(!psw.equals(confirmPsw)){
            showToast(getString(R.string.comfirm_psw_tip2));
            return false;
        }
        return result;
    }
}
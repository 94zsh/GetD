package com.future.getd.ui.my;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.future.getd.MainActivity;
import com.future.getd.R;
import com.future.getd.base.BaseActivity;
import com.future.getd.databinding.ActivityLoginBinding;
import com.future.getd.log.LogUtils;
import com.future.getd.net.bean.account.CommonResponse;
import com.future.getd.net.bean.account.LoginRequest;
import com.future.getd.net.bean.account.LoginResponse;
import com.future.getd.net.bean.account.User;
import com.future.getd.utils.EmailUtils;
import com.future.getd.utils.RetrofitUtil;
import com.future.getd.utils.SharePreferencesUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity<ActivityLoginBinding> {
    private boolean isShow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusColor(R.color.white);
    }

    @Override
    protected ActivityLoginBinding getBinding() {
        return ActivityLoginBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        binding.skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });

        binding.regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                intent.putExtra("type",0);
                startActivity(intent);
            }
        });
        binding.forgetPsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                intent.putExtra("type",1);
                startActivity(intent);
            }
        });

        String str = getResources().getString(R.string.privacy_policy_hint);
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(str);
        int start = str.indexOf("《");//第一个出现的位置
        int end = str.indexOf("》");//第一个出现的位置
        if(start > 0 && end > 0){
            ssb.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    Intent intent = new Intent(LoginActivity.this, WebActivity.class);
                    intent.putExtra("title",getString(R.string.user_agreement));
                    intent.putExtra("url","");
                    startActivity(intent);
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(getResources().getColor(R.color.blue));
                    ds.setUnderlineText(false);
                }
            }, start, end + 1, 0);
        }
        int start2 = str.lastIndexOf("《");//第一个出现的位置
        int end2 = str.lastIndexOf("》");//第一个出现的位置
        if(start > 0 && end > 0){
            ssb.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    Intent intent = new Intent(LoginActivity.this, WebActivity.class);
                    intent.putExtra("title",getString(R.string.privacy));
                    intent.putExtra("url","");
                    startActivity(intent);
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(getResources().getColor(R.color.blue));
                    ds.setUnderlineText(false);
                }
            }, start2, end2 + 1, 0);
        }
        binding.tvPrivacyTip.setMovementMethod(LinkMovementMethod.getInstance());
        binding.tvPrivacyTip.setText(ssb, TextView.BufferType.SPANNABLE);

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
    }

    @Override
    protected void initData() {
        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.email.getText().toString();
                String psw = binding.psw.getText().toString();
                LogUtils.i("login : " + email + " , " + psw + " , " + psw);
                if (checkInput(email, psw)) {
                    // TODO: 2024/9/25 登陆
//                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    LoginRequest request = new LoginRequest();
                    request.setUsername(email);
                    request.setPassword(psw);
                    RetrofitUtil.getApiService().login(request).enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            LogUtils.i("login response : " + response.body());
                            if(response.code() == 200){
                                if(response.body().getCode() == 200){
                                    showToast(getString(R.string.commit_success));
                                    if(response.body().getData() != null){
                                        SharePreferencesUtil.getInstance().saveUser(response.body().getData());
                                    }else{
                                        User user = new User();
                                        user.setUsername(email);
                                        user.setEmail(email);
                                        user.setPassword(psw);
                                        SharePreferencesUtil.getInstance().saveUser(user);
                                    }
                                    finish();
                                }else{
                                    showToast(response.body().getMessage());
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            LogUtils.i("registerEmail onFailure : " + t);
                            showToast(t.getMessage());
                        }
                    });
                }
            }
        });
    }
    private boolean checkInput(String email,String psw){
        boolean result = true;
        if(email.isEmpty()){
            showToast(getString(R.string.email_input_tip));
            return false;
        }
        if(!EmailUtils.isEmail(email)){
            showToast(getString(R.string.invalid_email));
            return false;
        }
        if(psw.isEmpty()){
            showToast(getString(R.string.psw_input_tip));
            return false;
        }
        if(!binding.cbAgree.isChecked()){
            showToast(getString(R.string.privacy_agree_check));
            return false;
        }
        return result;
    }
}
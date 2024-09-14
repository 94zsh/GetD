package com.future.getd.ui.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.future.getd.MainActivity;
import com.future.getd.R;
import com.future.getd.base.BaseActivity;
import com.future.getd.databinding.ActivitySplashBinding;

public class SplashActivity extends BaseActivity<ActivitySplashBinding> {
    private LauncherVM launcherVM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        launcherVM = new ViewModelProvider(this).get(LauncherVM.class);
    }

    @Override
    protected ActivitySplashBinding getBinding() {
        return ActivitySplashBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        AlphaAnimation alphaAnimation = (AlphaAnimation) AnimationUtils.loadAnimation(SplashActivity.this, R.anim.guide_fade_in);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                updateView();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        binding.root.startAnimation(alphaAnimation);
    }
    private void updateView() {
        if (!launcherVM.isAgreeUserAgreement()) {
            showPrivacy();
        }
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    private void showPrivacy(){

    }
}
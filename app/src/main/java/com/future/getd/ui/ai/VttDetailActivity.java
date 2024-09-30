package com.future.getd.ui.ai;

import static com.future.getd.utils.SpeechUtils.TYPE_CN;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.future.getd.R;
import com.future.getd.base.BaseActivity;
import com.future.getd.databinding.ActivityVttDetailBinding;
import com.future.getd.log.LogUtils;
import com.future.getd.utils.SharePreferencesUtil;
import com.future.getd.utils.SpeechUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class VttDetailActivity extends BaseActivity<ActivityVttDetailBinding> {

    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusColor(R.color.bg_main);
    }

    @Override
    protected ActivityVttDetailBinding getBinding() {
        return ActivityVttDetailBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        binding.ivBack.setOnClickListener(v -> {finish();});
    }

    long time;
    String path;
    String content;
    @Override
    protected void initData() {
        time = getIntent().getLongExtra("time",0);
        path = getIntent().getStringExtra("path");
        content = getIntent().getStringExtra("content");
        LogUtils.i("time : " + time + " , path : " + path + " , content :" + content);

        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        SimpleDateFormat sdfSub = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if (time != 0) {
            binding.tvTitle.setText(String.format("%s%s", getResources().getString(R.string.real_time_translate), sdf.format(new Date(time))));
        }
        if(!path.isEmpty()){
            mediaPlayer = new MediaPlayer();
            File file = new File(path);
            LogUtils.i("file exists : " + file.exists());
            try {
                FileInputStream fis = new FileInputStream(file);
                mediaPlayer.setDataSource(fis.getFD());
//                mediaPlayer.setDataSource(file.getAbsolutePath());
                mediaPlayer.prepare(); // 准备播放器
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        LogUtils.i("onPrepared");
                    }
                });
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        LogUtils.i("onCompletion");
                        binding.play.setImageResource(R.drawable.ic_play);
                    }
                });
                mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                        LogUtils.i("onError");
                        return false;
                    }
                });
            } catch (Exception e) {
                LogUtils.e("mediaPlayer e : " + e);
                e.printStackTrace();
            }

        }
        binding.content.setText(content);
        binding.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (!content.isEmpty()){
//
//                }
                if(mediaPlayer.isPlaying()){
                    pauseAudio();
                    binding.play.setImageResource(R.drawable.ic_play);
                }else{
                    playAudio();
                    binding.play.setImageResource(R.drawable.ic_speech_pause);
                }

            }
        });

        binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.content.setEnabled(true);
                binding.content.requestFocus();
                binding.content.setSelection(binding.content.getText().length());
                binding.edit.setVisibility(View.GONE);
                binding.complete.setVisibility(View.VISIBLE);
            }
        });
        binding.complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.content.setEnabled(false);
                binding.edit.setVisibility(View.VISIBLE);
                binding.complete.setVisibility(View.GONE);

                LogUtils.i("content : " + content + " , new content : " + binding.content.getText().toString());
                if (!binding.content.getText().toString().equals(content)){
                    SharePreferencesUtil.getInstance().updateVttByTime(time,binding.content.getText().toString());
                }
            }
        });
    }

    private void playAudio() {
            mediaPlayer.start(); // 开始播放
    }
    private void pauseAudio() {
        mediaPlayer.pause(); // 开始播放
    }

    private void stopAudio() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAudio();
    }

}
package com.future.getd.ui.my;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.future.getd.R;
import com.future.getd.base.BaseActivity;
import com.future.getd.databinding.ActivityWebBinding;
import com.future.getd.log.LogUtils;
import com.future.getd.net.AIChatGptApiService;
import com.future.getd.net.model.TranscriptionResponse;
import com.future.getd.utils.RetrofitUtil;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebActivity extends BaseActivity<ActivityWebBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusColor(R.color.bg_main);
    }

    @Override
    protected ActivityWebBinding getBinding() {
        return ActivityWebBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
            binding.title.ivBack.setOnClickListener( v -> {
                isRecording = false;
                audioRecord.stop();;
//                finish();
            });
    }

    @Override
    protected void initData() {
        String title = getIntent().getStringExtra("title");
        binding.title.tvTitle.setText(title);

        binding.title.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRecording = true;
//                startRecording();
            }
        });
    }

    boolean isRecording = false;
    AudioRecord audioRecord;
    public void startRecording() {
        int bufferSize = AudioRecord.getMinBufferSize(320000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        LogUtils.i("bufferSize : " + bufferSize);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            LogUtils.i("no permission");
            return;
        }
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 16000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
        audioRecord.startRecording();

        byte[] buffer = new byte[bufferSize];
        while (isRecording) { // 使用一个布尔值控制录音
            int read = audioRecord.read(buffer, 0, buffer.length);
            LogUtils.i("sendToOpenAI : " + read);
            if (read > 0) {
                sendToOpenAI(buffer, read);
            }
            isRecording = false;
        }
        audioRecord.stop();
        audioRecord.release();
    }

    private void sendToOpenAI(byte[] audioData, int length) {
        AIChatGptApiService service = RetrofitUtil.getAiApiService();

//        RequestBody requestBody = RequestBody.create(MediaType.parse("audio/m4a"), audioData);
//        RequestBody requestFile = RequestBody.create(MediaType.parse("audio/m4a"), audioData);
        RequestBody requestFile = RequestBody.create(MediaType.parse("audio/wav"), audioData);
//        MultipartBody.Part body = MultipartBody.Part.createFormData("file", System.currentTimeMillis() + "_name", requestFile);
        MultipartBody.Part body = MultipartBody.Part.create(requestFile);

        RequestBody model = RequestBody.create(MediaType.parse("text/plain"), "whisper-1");


        Call<TranscriptionResponse> call = service.transcribeAudio(body,model);
        call.enqueue(new Callback<TranscriptionResponse>() {
            @Override
            public void onResponse(Call<TranscriptionResponse> call, Response<TranscriptionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String transcribedText = response.body().getText();
                    // 处理转录文本
                    LogUtils.e("transcribedText : " + transcribedText);
                    binding.title.tvTitle.append(transcribedText);
                }
            }

            @Override
            public void onFailure(Call<TranscriptionResponse> call, Throwable t) {
                // 处理错误
                LogUtils.e("onFailure : " + t);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
package com.future.getd.ui.ai;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.future.getd.R;
import com.future.getd.base.BaseActivity;
import com.future.getd.database.AppDataBase;
import com.future.getd.databinding.ActivityChatBinding;
import com.future.getd.log.LogUtils;
import com.future.getd.net.model.CompletionChoice;
import com.future.getd.net.model.GptRequest;
import com.future.getd.net.model.GptResponse;
import com.future.getd.net.model.GptTranscriptions;
import com.future.getd.net.model.TranscriptionResponse;
import com.future.getd.net.model.Message;
import com.future.getd.ui.adapter.ChatAdapter;
import com.future.getd.ui.view.pop.AudioRecordPop;
import com.future.getd.utils.AsrUtil;
import com.future.getd.utils.PermissionUtil;
import com.future.getd.utils.RecordManager;
import com.future.getd.utils.RetrofitUtil;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends BaseActivity<ActivityChatBinding> {
    private ArrayList<Message> messageList = new ArrayList<>();
    private ChatAdapter adapter;
    MutableLiveData<Boolean> isWaitResponse = new MutableLiveData<>(false);
    RecordManager recordManager = new RecordManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected ActivityChatBinding getBinding() {
        return ActivityChatBinding.inflate(getLayoutInflater());
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView() {
        binding.title.ivBack.setOnClickListener( v-> {
            finish();
        });
        binding.title.tvTitle.setText(getString(R.string.ai_chat));
        binding.title.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File audioFile = new File("/storage/emulated/0/Android/data/com.future.getd/files/GetD/audio/2024-09-20_19:58:32.m4a");
                LogUtils.i("input file : " + audioFile.exists());
                RequestBody requestFile = RequestBody.create(MediaType.parse("audio/m4a"), audioFile);
                MultipartBody.Part body = MultipartBody.Part.createFormData("file", audioFile.getName(), requestFile);
                RequestBody model = RequestBody.create(MediaType.parse("text/plain"), "whisper-1");

                RetrofitUtil.getAiApiService().transcribeAudio(body,model).enqueue(new Callback<TranscriptionResponse>() {
                    @Override
                    public void onResponse(Call<TranscriptionResponse> call, Response<TranscriptionResponse> response) {
                        isWaitResponse.setValue(false);
                        LogUtils.e("getTranscriptions response : " + response+ " , body : " + response.body());
                        if (response.code() == 200){
                            TranscriptionResponse gptResponse = response.body();
                        }
                    }

                    @Override
                    public void onFailure(Call<TranscriptionResponse> call, Throwable t) {
                        LogUtils.e("getTranscriptions onFailure : " + t);
                        isWaitResponse.setValue(false);
                    }
                });
            }
        });
        isWaitResponse.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    binding.title.loading.setVisibility(View.VISIBLE);
                    Glide.with(ChatActivity.this)
                            .asGif()
                            .load(R.raw.loading3)
                            .into(binding.title.loading);
                }else{
                    binding.title.loading.setVisibility(View.INVISIBLE);
                }
            }
        });
        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sendMsg = binding.etInput.getText().toString();
                if (!sendMsg.isEmpty()) {
                    sendMsg(sendMsg);
                }
            }
        });
        binding.ivSwitchAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.llInputVoice.setVisibility(View.VISIBLE);
                binding.llInputText.setVisibility(View.GONE);
                PermissionX.init(ChatActivity.this).permissions(Manifest.permission.RECORD_AUDIO)
                        .request(new RequestCallback() {
                            @Override
                            public void onResult(boolean allGranted, @NonNull List<String> grantedList, @NonNull List<String> deniedList) {
                                if(allGranted){

                                }else{

                                }
                            }
                        });
            }
        });
        binding.ivSwitchText.setOnClickListener( v -> {
            binding.llInputVoice.setVisibility(View.GONE);
            binding.llInputText.setVisibility(View.VISIBLE);
        });

        AsrUtil asrUtil = new AsrUtil(this, new AsrUtil.CallBack() {
            @Override
            public void onCallBack(String result, boolean isLast) {
                LogUtils.d("onCallBack");
            }
        });


        AudioRecordPop audioRecordPop = new AudioRecordPop(ChatActivity.this);
        audioRecordPop.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                audioRecordPop.stopAmina();
            }
        });
        binding.tvRecord.setOnTouchListener((view, motionEvent) -> {
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                LogUtils.d("ACTION_DOWN");
                audioRecordPop.showStart();
                recordManager.startRecord();
//                asrUtil.onClick(1);
            } else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                LogUtils.d("ACTION_UP");
                audioRecordPop.dismiss();
                String filePath = recordManager.stopRecord();
                LogUtils.i("filePath ： " + filePath);
//                asrUtil.onClick(2);
            } else if(motionEvent.getAction() == MotionEvent.ACTION_MOVE){
                LogUtils.d("ACTION_MOVE");
//                audioRecordPop.dismiss();
//                asrUtil.onClick(2);
            } else if(motionEvent.getAction() == MotionEvent.ACTION_CANCEL){
                LogUtils.d("ACTION_CANCEL");
                audioRecordPop.dismiss();
                recordManager.stopRecord();
//                asrUtil.onClick(2);
            }
            return true;
        });
//        binding.tvRecord.setLongClickable(true);
    }

    private void sendMsg(String content) {
        isWaitResponse.setValue(true);
        Message messageUser = new Message(Message.ROLE_USER,content);
        messageList.add(messageUser);
        adapter.notifyItemInserted(messageList.size() - 1);
        binding.rv.scrollToPosition(messageList.size() - 1);
        binding.etInput.setText("");
        AppDataBase.getInstance(this).getChatDao().insert(messageUser);

        GptRequest request = new GptRequest(messageList);
        RetrofitUtil.getAiApiService().getAiMessage(request).enqueue(new Callback<GptResponse>() {
            @Override
            public void onResponse(Call<GptResponse> call, Response<GptResponse> response) {
                isWaitResponse.setValue(false);
                LogUtils.e("getAiMessage response : " + response+ " , body : " + response.body());
                if (response.code() == 200){
                    GptResponse gptResponse = response.body();
                    if (gptResponse != null) {
                        List<CompletionChoice> completionChoices = gptResponse.getChoices();
                        if(!completionChoices.isEmpty()){
                            CompletionChoice choice = completionChoices.get(0);
                            Message message = choice.getMessage();
                            messageList.add(message);
                            adapter.notifyItemInserted(messageList.size() - 1);
                            binding.rv.scrollToPosition(messageList.size() - 1);
                            AppDataBase.getInstance().getChatDao().insert(message);
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<GptResponse> call, Throwable t) {
                LogUtils.e("getAiMessage onFailure : " + t);
                isWaitResponse.setValue(false);
            }
        });
    }

    @Override
    protected void initData() {
        checkPermission();
        messageList = (ArrayList<Message>) AppDataBase.getInstance(this).getChatDao().getAll();
        LogUtils.d("database messageList " + (messageList.isEmpty() ? 0 : messageList.size()));
        adapter = new ChatAdapter(messageList);
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        binding.rv.setLayoutManager(manager);

        binding.rv.setAdapter(adapter);
        adapter.setOnItemClickListener((baseQuickAdapter, view, i) -> LogUtils.d("audio onclick item " + i));
        manager.smoothScrollToPosition(binding.rv,null, messageList.isEmpty() ? 0 : adapter.getItemCount() - 1);
    }

    private void checkPermission(){
        if(!PermissionUtil.checkHasRecordPermission(this)){
            PermissionUtil.requestRecordPermission(this);
        }
    }



    private static final String API_URL = "https://api.openai.com/v1/audio/translations";

//    public static String translateAudio(String apiKey, String filePath) {
//        File audioFile = new File("/storage/emulated/0/Android/data/com.future.getd/files/GetD/audio/2024-09-20_19:58:32.m4a");
//        LogUtils.i("input file : " + audioFile.exists());
//
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("file", audioFile.getName(),
//                        RequestBody.create(MediaType.parse("audio/m4a"), audioFile))
//                .addFormDataPart("model", "whisper-1")
//                .build();
//    }
}
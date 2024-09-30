package com.future.getd.ui.ai;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.future.getd.R;
import com.future.getd.base.BaseActivity;
import com.future.getd.base.SysConstant;
import com.future.getd.database.AppDataBase;
import com.future.getd.databinding.ActivityChatBinding;
import com.future.getd.log.LogUtils;
import com.future.getd.net.model.CompletionChoice;
import com.future.getd.net.model.GptRequest;
import com.future.getd.net.model.GptResponse;
import com.future.getd.net.model.TranscriptionResponse;
import com.future.getd.net.model.Message;
import com.future.getd.ui.adapter.ChatAdapter;
import com.future.getd.ui.view.pop.AudioRecordPop;
import com.future.getd.ui.view.pop.AudioRecordPopNew;
import com.future.getd.utils.AsrUtil;
import com.future.getd.utils.DrawUtil;
import com.future.getd.utils.PermissionUtil;
import com.future.getd.utils.RecordManager;
import com.future.getd.utils.RetrofitUtil;
import com.future.getd.utils.SpeechUtils;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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
    String audioFilePath = "";
    boolean isSupportVoiceExchange = true;
    AudioRecordPopNew audioRecordPopNew;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusColor(R.color.white);
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
                AudioRecordPopNew audioRecordPopNew = new AudioRecordPopNew(ChatActivity.this);
                audioRecordPopNew.showStart();
            }
        });
        binding.title.ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showClearPop();
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
        binding.etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // 当文本改变之后，根据内容动态调整EditText的高度
                // 计算新的高度并设置
//                int lines = binding.etInput.getLineCount();
//                int lineHeight = binding.etInput.getLineHeight();
//                int newHeight = lines * lineHeight;

//                ViewGroup.LayoutParams params = binding.ETContainer.getLayoutParams();
//                params.height = newHeight;
//                binding.ETContainer.setLayoutParams(params);
            }
        });
        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sendMsg = binding.etInput.getText().toString();
                if (!sendMsg.isEmpty()) {
                    sendMsg(sendMsg,false);
                }
            }
        });
        binding.ivSwitchAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ViewGroup.LayoutParams params = binding.ETContainer.getLayoutParams();
//                params.height = DrawUtil.dip2px(100);
//                binding.ETContainer.setLayoutParams(params);

                binding.llInputVoice.setVisibility(View.VISIBLE);
                binding.llInputText.setVisibility(View.GONE);
                PermissionX.init(ChatActivity.this).permissions(Manifest.permission.RECORD_AUDIO)
                        .request(new RequestCallback() {
                            @Override
                            public void onResult(boolean allGranted, @NonNull List<String> grantedList, @NonNull List<String> deniedList) {
                                if (allGranted) {

                                } else {

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
        audioRecordPopNew = new AudioRecordPopNew(ChatActivity.this);
        audioRecordPopNew.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                audioRecordPopNew.stopAmina();
            }
        });
        AtomicReference<Float> startY = new AtomicReference<>((float) 0);
        AtomicReference<Float> endY = new AtomicReference<>((float) 0);
        AtomicReference<Float> currentY = new AtomicReference<>((float) 0);
        binding.tvRecord.setOnTouchListener((view, motionEvent) -> {
            int action = motionEvent.getAction();
            float x = motionEvent.getX();
            float y = motionEvent.getY();

            if(action == MotionEvent.ACTION_DOWN){
                LogUtils.d("ACTION_DOWN  y : " + y);
                startY.set(motionEvent.getY());
                audioRecordPopNew.showStart();
                recordManager.startRecord();
            } else if(action == MotionEvent.ACTION_UP){
                endY.set(motionEvent.getY());
                float offset = endY.get() - startY.get();
                LogUtils.d("ACTION_UP y : " + y + " , offset: " + offset);
                audioRecordPopNew.dismiss();
                if (offset <= -100) {
                    //上滑取消
                String filePath = recordManager.stopRecord();
                LogUtils.i("filePath ： " + filePath);
                recordManager.deleteFile();
                } else{
                    //发送语音
                    String filePath = recordManager.stopRecord();
                    LogUtils.i("filePath ： " + filePath);
                    transAudio(filePath);
                }
            } else if(action == MotionEvent.ACTION_MOVE){
//                LogUtils.d("ACTION_MOVE");
                currentY.set(motionEvent.getY());
                float offset = currentY.get() - startY.get();
//                LogUtils.d("ACTION_MOVE y : " + y + " , offset: " + offset);
                if (offset <= -100) {
                    //上滑取消
                    audioRecordPopNew.setCancelText();
                }
            } else if(action == MotionEvent.ACTION_CANCEL){
                LogUtils.d("ACTION_CANCEL");
                audioRecordPopNew.dismiss();
                //发送语音
                String filePath = recordManager.stopRecord();
                LogUtils.i("filePath ： " + filePath);
                transAudio(filePath);
            }
            return true;
        });
//        binding.tvRecord.setLongClickable(true);
    }

    PopupWindow popupWindow;
    private void showClearPop() {
        if(popupWindow == null){
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.chat_clear_msg, null);
            // Create the PopupWindow
            popupWindow = new PopupWindow(popupView,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            // Set up the close button
            TextView ivClear = popupView.findViewById(R.id.popup_text);
            ivClear.setOnClickListener(v -> {
                popupWindow.dismiss();
                AppDataBase.getInstance(ChatActivity.this).getChatDao().deleteAll();
                messageList.clear();
                adapter.notifyDataSetChanged();
            });
            // Allow outside touches to dismiss the popup
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
        }
        if(popupWindow.isShowing()){
            return;
        }
        // Show the popup window
//                popupWindow.showAtLocation(binding.title.ivMore, Gravity.BOTTOM, 0, 0);
        popupWindow.showAsDropDown(binding.title.ivMore, 0, 0);
    }

    private void transAudio(String  audioFilePath){
        File audioFile = new File(audioFilePath);
        //test
//        File audioFile = new File("/storage/emulated/0/Android/data/com.future.getd/files/GetD/audio/2024-09-23_10:38:58.m4a");
        LogUtils.i("transAudio  file path : " + audioFile.getAbsolutePath() + " , isExists : " + audioFile.exists());
        if(!audioFile.exists()){
            showToast("file does not exist");
            return;
        }
//        RequestBody requestFile = RequestBody.create(MediaType.parse("audio/m4a"), audioFile);
        RequestBody requestFile = RequestBody.create(MediaType.parse("audio/wav"), audioFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", audioFile.getName(), requestFile);
        RequestBody model = RequestBody.create(MediaType.parse("text/plain"), "whisper-1");
        RetrofitUtil.getAiApiService().transcribeAudio(body,model).enqueue(new Callback<TranscriptionResponse>() {
            @Override
            public void onResponse(Call<TranscriptionResponse> call, Response<TranscriptionResponse> response) {
                isWaitResponse.setValue(false);
                //2024-09-23 10:29:00.445 18964-18773 NetInterceptor          com.future.getd                      I  responseBody's contentType : application/json; charset=utf-8
                //2024-09-23 10:29:00.445 18964-18773 NetInterceptor          com.future.getd                      I  responseBody's content: {
                //                                                                                                      "text": "你好你好你好"
                //                                                                                                    }
                LogUtils.e("getTranscriptions response : " + response + " , body : " + response.body());
                if (response.code() == 200){
                    TranscriptionResponse gptResponse = response.body();
                    String resultText = gptResponse.getText();
//                    Message message = new Message(Message.ROLE_USER,resultText);
//                    addMessage(message);
                      sendMsg(resultText,false);

                } else {
                    showToast(response.code() + " " + response.body()) ;
                }
            }

            @Override
            public void onFailure(Call<TranscriptionResponse> call, Throwable t) {
                LogUtils.e("getTranscriptions onFailure : " + t);
                isWaitResponse.setValue(false);
                showToast(getString(R.string.net_error));
            }
        });
    }

    private void sendMsg(String content,boolean isNeedSpeak) {
        LogUtils.i("sendMsg : " + content);
        isWaitResponse.setValue(true);
        Message messageUser = new Message(Message.ROLE_USER,content);
        messageList.add(messageUser);
        adapter.notifyItemInserted(messageList.size() - 1);
        binding.rv.scrollToPosition(messageList.size() - 1);
        binding.etInput.setText("");
        AppDataBase.getInstance(this).getChatDao().insert(messageUser);

        ArrayList<Message> tempSendList = new ArrayList<>();
        tempSendList.add(messageUser);
        GptRequest request = new GptRequest(tempSendList);
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
                            LogUtils.i("add msg : " + message);
                            addMessage(message);
                            if(isNeedSpeak){
                                if(SpeechUtils.getInstance().isInitSuccess()){
                                    SpeechUtils.getInstance().speak(message.content,SpeechUtils.TYPE_DEFAULT);
                                }
                            }
                        }
                    }
                }else{
                    showToast(getString(R.string.net_error));
                }
            }

            @Override
            public void onFailure(Call<GptResponse> call, Throwable t) {
                LogUtils.e("getAiMessage onFailure : " + t);
                showToast(getString(R.string.net_error));
                isWaitResponse.setValue(false);
            }
        });
    }

    private void addMessage(Message message) {
        messageList.add(message);
        adapter.notifyItemInserted(messageList.size() - 1);
        binding.rv.scrollToPosition(messageList.size() - 1);
        AppDataBase.getInstance().getChatDao().insert(message);
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

        IntentFilter filter = new IntentFilter(SysConstant.BROADCAST_LANGUAGE_CHANGE);
        filter.addAction(SysConstant.BROADCAST_VOICE_INTERACTION_BEGIN);
        filter.addAction(SysConstant.BROADCAST_VOICE_INTERACTION_END);
        try {
            registerReceiver(receiver,filter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void checkPermission(){
        if(!PermissionUtil.checkHasRecordPermission(this)){
            PermissionUtil.requestRecordPermission(this);
        }
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(SysConstant.BROADCAST_VOICE_INTERACTION_BEGIN)){
                LogUtils.e("receive BROADCAST_VOICE_INTERACTION_BEGIN isRecording : " + recordManager.isRecording());
                if(recordManager.isRecording()){
                    return;
                }
                if (!isSupportVoiceExchange) {
                    return;
                }
                if (rcspController.isDeviceConnected()) {
                    recordManager.startRecordVoiceRecognition(ChatActivity.this);
                    audioFilePath = recordManager.getFilePath();
                    if (audioRecordPopNew == null) {
                        audioRecordPopNew = new AudioRecordPopNew(ChatActivity.this);
                        audioRecordPopNew.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                recordManager.stopRecord();
                            }
                        });
                    }
                    audioRecordPopNew.showStart();
                }

            }else if(intent.getAction().equals(SysConstant.BROADCAST_VOICE_INTERACTION_END)){
                LogUtils.e("receive BROADCAST_VOICE_INTERACTION_END state " + recordManager.isRecording());
                if(!isSupportVoiceExchange){
                    return;
                }
                if(!recordManager.isRecording()){
                    return;
                }
                LogUtils.d("stopRecord");
                audioFilePath = recordManager.stopRecord();
                if(audioRecordPopNew != null){
                    audioRecordPopNew.dismiss();
                }
                LogUtils.i("recordManager filePath ： " + audioFilePath);
                //语音转文本
                File audioFile = new File(audioFilePath);
                //test
//                audioFile = new File("storage/emulated/0/Android/data/com.future.getd/files/GetD/audio/2024-09-29_13:43:37.wav");//你好吗
                LogUtils.i("翻译文件  file path : " + audioFile.getAbsolutePath() + " , isExists : " + audioFile.exists());
                if(!audioFile.exists()){
                    showToast("file does not exist");
                    return;
                }
//                RequestBody requestFile = RequestBody.create(MediaType.parse("audio/m4a"), audioFile);
                RequestBody requestFile = RequestBody.create(MediaType.parse("audio/wav"), audioFile);
                MultipartBody.Part body = MultipartBody.Part.createFormData("file", audioFile.getName(), requestFile);
                RequestBody model = RequestBody.create(MediaType.parse("text/plain"), "whisper-1");
                RetrofitUtil.getAiApiService().transcribeAudio(body,model).enqueue(new Callback<TranscriptionResponse>() {
                    @Override
                    public void onResponse(Call<TranscriptionResponse> call, Response<TranscriptionResponse> response) {
                        LogUtils.e("test getTranscriptions response : " + response + " , body : " + response.body());
                        if (response.code() == 200){
                            TranscriptionResponse gptResponse = response.body();
                            String resultText = gptResponse.getText();
                            sendMsg(resultText,true);
                        } else {
                            showToast(response.code() + " " + response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<TranscriptionResponse> call, Throwable t) {
                        LogUtils.e("test getTranscriptions onFailure : " + t);
                        showToast(getString(R.string.net_error));
                    }
                });

            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
package com.future.getd.ui.ai;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.future.getd.R;
import com.future.getd.base.BaseActivity;
import com.future.getd.databinding.ActivityVoiceToTextBinding;
import com.future.getd.log.LogUtils;
import com.future.getd.ui.adapter.VttAdapter;
import com.future.getd.ui.bean.VoiceToTextBean;
import com.future.getd.utils.RecordManager;
import com.future.getd.utils.SharePreferencesUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VoiceToTextActivity extends BaseActivity<ActivityVoiceToTextBinding> {
    int MODE_EMPTY = 0;
    int MODE_LIST = 1;
    int MODE_RECORDING = 2;
    int STATE_IDLE = 0;
    int STATE_RECORDING = 1;
    int STATE_STOP = 2;
    MutableLiveData<Integer> currentMode = new MutableLiveData<>(0);//0无数据 1有数据 2录音模式
    MutableLiveData<Integer> recordState = new MutableLiveData<>(0);//0待机 1录音中 2录音完成或停止
    private List<VoiceToTextBean> voiceToTextBeanList = new ArrayList<>();
    VttAdapter adapter;
    String path = "";
    RecordManager recordManager = new RecordManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusColor(R.color.bg_main);
    }

    @Override
    protected ActivityVoiceToTextBinding getBinding() {
        return ActivityVoiceToTextBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        initRecognize();
        binding.title.ivBack.setOnClickListener(view -> finish());
        binding.title.tvTitle.setText(getString(R.string.ai_voice));
        binding.title.tvTitle.setOnClickListener(view -> {
            //test


        });
        binding.title.ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showClearPop();
            }
        });

        currentMode.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer mode) {
                if(mode == MODE_EMPTY){
                    binding.ivEmpty.setVisibility(View.VISIBLE);
                    binding.rv.setVisibility(View.GONE);
                    binding.llSpeech.setVisibility(View.GONE);
                    binding.title.ivMore.setVisibility(View.VISIBLE);
                    binding.title.complete.setVisibility(View.GONE);
                } else if (mode == MODE_LIST){
                    binding.ivEmpty.setVisibility(View.GONE);
                    binding.rv.setVisibility(View.VISIBLE);
                    binding.llSpeech.setVisibility(View.GONE);
                    binding.title.ivMore.setVisibility(View.VISIBLE);
                    binding.title.complete.setVisibility(View.GONE);
                }else {
                    binding.ivEmpty.setVisibility(View.GONE);
                    binding.rv.setVisibility(View.GONE);
                    binding.llSpeech.setVisibility(View.VISIBLE);
                    binding.title.ivMore.setVisibility(View.GONE);
                    binding.title.complete.setVisibility(View.GONE);
                }
            }
        });

        recordState.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer state) {
                if (state == STATE_IDLE){
                    binding.speech.setImageResource(R.drawable.ic_record);
                    binding.tip.setText(getString(R.string.speech_tip));
                } else if (state == STATE_RECORDING){
                    binding.speech.setImageResource(R.drawable.ic_stop);
                    binding.tip.setText(getString(R.string.recording));
                } else {
                    binding.speech.setImageResource(R.drawable.ic_stop);
                    binding.tip.setText("");
                }
            }
        });


        binding.speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recordState.getValue() == STATE_RECORDING) {
                    //录音中 停止录音
                    stopRecord();
                    recordState.setValue(STATE_IDLE);
                } else if (recordState.getValue() == STATE_IDLE) {
                    //开始录音
                    startSpeechRecognize();
                    currentMode.setValue(MODE_RECORDING);
                    recordState.setValue(STATE_RECORDING);
                } else {
                    stopRecord();
                    recordState.setValue(STATE_IDLE);
                }
            }
        });

        binding.title.complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void stopRecord() {
        stopSpeechRecognize();
        recordManager.stopRecord();
        //save
        String content = binding.content.getText().toString();
        if(content.isEmpty()){
            showToast(getString(R.string.empty_tip));
            recordManager.deleteFile();
        }else{
            //save
            VoiceToTextBean voiceToTextBean = new VoiceToTextBean(System.currentTimeMillis(),path,content);
            voiceToTextBeanList.add(voiceToTextBean);
            SharePreferencesUtil.getInstance().saveVTT(voiceToTextBeanList);
            currentMode.setValue(MODE_LIST);
        }
        if(!voiceToTextBeanList.isEmpty()){
            currentMode.setValue(MODE_LIST);
        }else{
            currentMode.setValue(MODE_EMPTY);
        }
    }

    @Override
    protected void initData() {
        voiceToTextBeanList = SharePreferencesUtil.getInstance().getVtt();
        LogUtils.i("vtt size : " + voiceToTextBeanList.size());
        if (!voiceToTextBeanList.isEmpty()) {
            currentMode.setValue(MODE_LIST);
        } else {
            currentMode.setValue(MODE_EMPTY);
        }
        adapter = new VttAdapter(voiceToTextBeanList);
        binding.rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        binding.rv.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener<VoiceToTextBean>() {
            @Override
            public void onClick(@NonNull BaseQuickAdapter<VoiceToTextBean, ?> baseQuickAdapter, @NonNull View view, int i) {
                LogUtils.d("VoiceToTextBean onclick item " + i);
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener<VoiceToTextBean>() {
            @Override
            public void onClick(@NonNull BaseQuickAdapter<VoiceToTextBean, ?> baseQuickAdapter, @NonNull View view, int i) {
                Intent detailIntent = new Intent(VoiceToTextActivity.this,VttDetailActivity.class);
                detailIntent.putExtra("time",voiceToTextBeanList.get(i).getTime());
                detailIntent.putExtra("path",voiceToTextBeanList.get(i).getPath());
                detailIntent.putExtra("content",voiceToTextBeanList.get(i).getContent());
                startActivity(detailIntent);
            }
        });

        listener = new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
                LogUtils.i("SpeechRecognizer onReadyForSpeech");
                recordState.setValue(STATE_RECORDING);
            }

            @Override
            public void onBeginningOfSpeech() {
                LogUtils.i("SpeechRecognizer onBeginningOfSpeech");
            }

            @Override
            public void onRmsChanged(float v) {
                LogUtils.i("SpeechRecognizer onRmsChanged : " + v);
            }

            @Override
            public void onBufferReceived(byte[] bytes) {
//                LogUtils.i("SpeechRecognizer onBufferReceived : " + bytes);
            }

            @Override
            public void onEndOfSpeech() {
                LogUtils.i("SpeechRecognizer onEndOfSpeech : ");
            }

            @Override
            public void onError(int i) {
                // /** Network operation timed out. */
                //    public static final int ERROR_NETWORK_TIMEOUT = 1;
                //    /** Other network related errors. */
                //    public static final int ERROR_NETWORK = 2;
                //    /** Audio recording error. */
                //    public static final int ERROR_AUDIO = 3;
                //    /** Server sends error status. */
                //    public static final int ERROR_SERVER = 4;
                //    /** Other client side errors. */
                //    public static final int ERROR_CLIENT = 5;
                //    /** No speech input */
                //    public static final int ERROR_SPEECH_TIMEOUT = 6;
                //    /** No recognition result matched. */
                //    public static final int ERROR_NO_MATCH = 7;
                //    /** RecognitionService busy. */
                //    public static final int ERROR_RECOGNIZER_BUSY = 8;
                //    /** Insufficient permissions */
                //    public static final int ERROR_INSUFFICIENT_PERMISSIONS = 9;
                LogUtils.i("SpeechRecognizer onError : " + i + " , " + SpeechRecognizer.ERROR_NO_MATCH);
            }

            @Override
            public void onResults(Bundle results) {
                speechRecognizer.stopListening();
                startSpeechRecognize();
                path = recordManager.getFilePath();
                LogUtils.i("SpeechRecognizer onResults : " + results);
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                LogUtils.i("SpeechRecognizer onResults : " + matches);
                if (matches != null) {
                    String spokenText = matches.get(0);
                    LogUtils.i("SpeechRecognizer spokenText : " + spokenText);
                    // 处理识别结果
                    binding.content.append(spokenText);
                }else{
//                    recordManager.deleteFile();
                }
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        startSpeechRecognize();
//                    }
//                },20);
//                //结束
//                recordState.setValue(STATE_STOP);
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                path = recordManager.getFilePath();
                LogUtils.i("SpeechRecognizer partialResults : " + partialResults);
            }

            @Override
            public void onEvent(int i, Bundle bundle) {
                LogUtils.i("SpeechRecognizer onEvent : " + i);
            }
        };
    }

    private void switchUi(boolean isHasData,boolean isRecording){
        if(isHasData){
            binding.ivEmpty.setVisibility(View.GONE);
            binding.rv.setVisibility(View.VISIBLE);
            binding.llSpeech.setVisibility(View.GONE);
        }else{
            binding.ivEmpty.setVisibility(View.VISIBLE);
            binding.rv.setVisibility(View.GONE);
            binding.llSpeech.setVisibility(View.GONE);
        }

        if(isRecording){
            binding.ivEmpty.setVisibility(View.GONE);
            binding.rv.setVisibility(View.GONE);
            binding.llSpeech.setVisibility(View.VISIBLE);
        }
    }

    private void initRecognize() {
        LogUtils.i("isRecognitionAvailable :  " + SpeechRecognizer.isRecognitionAvailable(this));
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        LogUtils.i("isRecognitionAvailable :  " + SpeechRecognizer.isRecognitionAvailable(this));
        speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
//        speechIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "zh-cn"); // 设置为中文

//        speechIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
//        speechIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
        speechIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 10000); // 设置静音时长
        speechIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 10000); // 设置可能静音时长
        speechIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 10000);
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
                SharePreferencesUtil.getInstance().deleteAllVtt();
                initData();
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

    SpeechRecognizer speechRecognizer;
    RecognitionListener listener;
    private Intent speechIntent;
    private void startSpeechRecognize(){
//        recordManager.startRecord();
        speechRecognizer.setRecognitionListener(listener);
        speechRecognizer.startListening(speechIntent);
    }

    private void stopSpeechRecognize(){
        speechRecognizer.stopListening();
        speechRecognizer.cancel();
        speechRecognizer.destroy();
    }

    @Override
    protected void onDestroy() {
        speechRecognizer.destroy();
//        recordManager.stopRecord();
        super.onDestroy();
    }
}
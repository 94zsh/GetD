package com.future.getd.ui.ai;

import static com.future.getd.utils.SpeechUtils.LAN_CN;
import static com.future.getd.utils.SpeechUtils.LAN_EN;
import static com.future.getd.utils.SpeechUtils.TYPE_CN;
import static com.future.getd.utils.SpeechUtils.TYPE_EN;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.bumptech.glide.Glide;
import com.future.getd.R;
import com.future.getd.base.BaseActivity;
import com.future.getd.databinding.ActivityTranslateBinding;
import com.future.getd.log.LogUtils;
import com.future.getd.net.model.CompletionChoice;
import com.future.getd.net.model.GptRequest;
import com.future.getd.net.model.GptResponse;
import com.future.getd.net.model.Message;
import com.future.getd.ui.view.pop.AudioRecordPop;
import com.future.getd.ui.view.pop.RecordingGifPop;
import com.future.getd.utils.RetrofitUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TranslateActivity extends BaseActivity<ActivityTranslateBinding> {
    private  PopupWindow popupWindow;
    private String fromLanguage = "Chinese";
    private String targetLanguage = "English";
    RecordingGifPop recordingGifPop;
    MutableLiveData<Integer> fromLanguageType = new MutableLiveData<>(TYPE_CN);
    MutableLiveData<Integer> targetLanguageType = new MutableLiveData<>(TYPE_EN);
    MutableLiveData<Boolean> isWaitResponse = new MutableLiveData<>(false);
    MutableLiveData<Boolean> isRecord = new MutableLiveData<>(true);
    private TextToSpeech textToSpeech;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusColor(R.color.bg_main);
    }

    @Override
    protected ActivityTranslateBinding getBinding() {
        return ActivityTranslateBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        binding.title.ivBack.setOnClickListener(v -> {finish();});
        binding.title.tvTitle.setText(getString(R.string.ai_translate));
        binding.title.ivMore.setVisibility(View.GONE);

        if(recordingGifPop == null){
            recordingGifPop = new RecordingGifPop(TranslateActivity.this);
            recordingGifPop.setOnSelectListener(new RecordingGifPop.OnSelectListener() {
                @Override
                public void onCancel() {
                    //取消录音
                    stopSpeechRecognize();
                }
            });
        }

        isWaitResponse.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    binding.title.loading.setVisibility(View.VISIBLE);
                    Glide.with(TranslateActivity.this)
                            .asGif()
                            .load(R.raw.loading3)
                            .into(binding.title.loading);
                }else{
                    binding.title.loading.setVisibility(View.INVISIBLE);
                }
            }
        });

        fromLanguageType.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer type) {
                if(type == TYPE_CN){
                    fromLanguage = LAN_CN;
                    binding.tvForm.setText(getString(R.string.lan_cn));
                }else{
                    fromLanguage = LAN_EN;
                    binding.tvForm.setText(getString(R.string.lan_en));
                }
            }
        });

        targetLanguageType.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer type) {
                if(type == TYPE_CN){
                    targetLanguage = LAN_CN;
                    binding.tvTo.setText(getString(R.string.lan_cn));
                }else{
                    targetLanguage = LAN_EN;
                    binding.tvTo.setText(getString(R.string.lan_en));
                }
            }
        });

        binding.llSelectTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View popView = getLayoutInflater().inflate(R.layout.pop_select_sport,null);
                popView.findViewById(R.id.ll_en).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fromLanguageType.setValue(TYPE_EN);
                        popupWindow.dismiss();
                    }
                });
                popView.findViewById(R.id.ll_cn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fromLanguageType.setValue(TYPE_CN);
//                        binding.tvForm.setText(getString(R.string.lan_cn));
                        popupWindow.dismiss();
                    }
                });
                popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
                popupWindow.setAnimationStyle(R.style.pop_anim_style);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);
                popupWindow.showAsDropDown(binding.llSelectTop,20,10);
            }
        });

        binding.llSelectBot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View popView = getLayoutInflater().inflate(R.layout.pop_select_sport,null);
                popView.findViewById(R.id.ll_en).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        targetLanguageType.setValue(TYPE_EN);
                        popupWindow.dismiss();
                    }
                });
                popView.findViewById(R.id.ll_cn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        targetLanguageType.setValue(TYPE_CN);
                        popupWindow.dismiss();
                    }
                });
                popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
                popupWindow.setAnimationStyle(R.style.pop_anim_style);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);
                popupWindow.showAsDropDown(binding.llSelectBot,20,10);
            }
        });

        binding.flTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.etTop.requestFocus();
                binding.etTop.setSelection(binding.etTop.getText().length());
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(binding.etTop, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        binding.flBot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.etBot.requestFocus();
                binding.etBot.setSelection(binding.etBot.getText().length());
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(binding.etBot, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        binding.ivVoiceTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = binding.etTop.getText().toString();
                if(!content.isEmpty()){
                    //播放语音
                    speak(content,fromLanguageType.getValue());
                }else{
                    //录音一次
                    if(recordingGifPop == null){
                        recordingGifPop = new RecordingGifPop(TranslateActivity.this);
                        recordingGifPop.setOnSelectListener(new RecordingGifPop.OnSelectListener() {
                            @Override
                            public void onCancel() {
                                stopSpeechRecognize();
                            }
                        });
                    }
                    recordingGifPop.showStart();
                    startSpeechRecognize();
                }
            }
        });
        binding.ivVoiceBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = binding.etBot.getText().toString();
                if(!content.isEmpty()){
                    speak(content,targetLanguageType.getValue());
                }
            }
        });

        isRecord.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isRecord) {
                if(isRecord){
                    binding.ivVoiceTop.setImageResource(R.drawable.ic_trans_mic);
                }else{
                    binding.ivVoiceTop.setImageResource(R.drawable.audio_play);
                }
            }
        });
        binding.etTop.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                  if(!editable.toString().isEmpty())  {
                      isRecord.setValue(false);
                  }else{
                      isRecord.setValue(true);
                  }
            }
        });

        binding.ivExchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int fromType = fromLanguageType.getValue();
                int toType = targetLanguageType.getValue();
                fromLanguageType.setValue(toType);
                targetLanguageType.setValue(fromType);
//                binding.etTop.setText("");
                binding.etBot.setText("");
            }
        });

        binding.btTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isWaitResponse.setValue(true);
                String transText = binding.etTop.getText().toString();
                ArrayList<Message> messages = new ArrayList<>();
                LogUtils.i("Translate " + transText + " from " + fromLanguage + " to " + targetLanguage);
                messages.add(new Message(Message.ROLE_USER, "Translate " + transText + " from " + fromLanguage + " to " + targetLanguage));
                GptRequest request = new GptRequest(messages);
                RetrofitUtil.getAiApiService().translate(request).enqueue(new Callback<GptResponse>() {
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
                                    String result = choice.getMessage().getContent();
                                    binding.etBot.setText(result);
                                }
                            }
                        } else {
                            showToast(response.code() + " " + response.body()) ;
                        }
                    }

                    @Override
                    public void onFailure(Call<GptResponse> call, Throwable t) {
                        LogUtils.e("getTranscriptions onFailure : " + t);
                        isWaitResponse.setValue(false);
//                        showToast(t.getMessage()) ;
                        showToast(getString(R.string.net_error));
                    }
                });

            }
        });
    }

    @Override
    protected void initData() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                LogUtils.e("TextToSpeech init status : " + status);
                if (status == TextToSpeech.SUCCESS) {
                    isInitSuccess = true;
                } else{
                    LogUtils.e("TextToSpeech init status : " + status);
                }
            }
        });
    }

    boolean isInitSuccess = false;
    public void speak(String text , int languageType) {
        LogUtils.e("speak : " + text + " , languageType : " + languageType);
        // 设置语言
        int result = -1;
        if(languageType == TYPE_CN){
            result = textToSpeech.setLanguage(Locale.CHINA);
            LogUtils.e("setLanguage  CHINA result : " + result);
        }else{
            result = textToSpeech.setLanguage(Locale.US);
            LogUtils.e("setLanguage  US result : " + result);
        }
        LogUtils.e("setLanguage  result : " + result);
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            // 处理错误
        }else{
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    SpeechRecognizer speechRecognizer;
    private void startSpeechRecognize(){
        LogUtils.i("isRecognitionAvailable :  " + SpeechRecognizer.isRecognitionAvailable(this));
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);

        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 30000); // 设置静音时长
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 10000); // 设置可能静音时长
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
                LogUtils.i("SpeechRecognizer onReadyForSpeech");
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
//                SpeechRecognizer.ERROR_AUDIO;
                LogUtils.i("SpeechRecognizer onError : " + i + " , " + SpeechRecognizer.ERROR_NO_MATCH);
                recordingGifPop.dismiss();
            }

            @Override
            public void onResults(Bundle results) {
                recordingGifPop.dismiss();
                LogUtils.i("SpeechRecognizer onResults : " + results);
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                LogUtils.i("SpeechRecognizer onResults : " + matches);
                if (matches != null) {
                    String spokenText = matches.get(0);
                    LogUtils.i("SpeechRecognizer spokenText : " + spokenText);
                    // 处理识别结果
                    binding.etTop.setText(spokenText);
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {
                LogUtils.i("SpeechRecognizer onPartialResults");
            }

            @Override
            public void onEvent(int i, Bundle bundle) {
                LogUtils.i("SpeechRecognizer onEvent : " + i);
            }
        });
        speechRecognizer.startListening(intent);
    }

    private void stopSpeechRecognize(){
        speechRecognizer.stopListening();
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        speechRecognizer.stopListening();
        speechRecognizer.destroy();

        super.onDestroy();
    }
}
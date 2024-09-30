package com.future.getd.utils;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import com.future.getd.log.LogUtils;

import java.util.Locale;

public class SpeechUtils {
    private static SpeechUtils instance;
    public static final String LAN_EN = "English";
    public static final String LAN_CN = "Chinese";
    public static final int TYPE_EN = 0;
    public static final int TYPE_CN = 1;
    public static final int TYPE_DEFAULT = 2;

    boolean isInitSuccess = false;

    private TextToSpeech textToSpeech;

    public static SpeechUtils getInstance() {
        if (instance == null) {
            synchronized (SpeechUtils.class) {
                if (instance == null) {
                    instance = new SpeechUtils();
                }
            }
        }
        return instance;
    }

    public void initTextSpeech(Context context){
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
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

    public boolean isInitSuccess() {
        return isInitSuccess;
    }

    public void speak(String text , int languageType) {
        LogUtils.e("speak : " + text + " , languageType : " + languageType);
        // 设置语言
        int result = -1;
        if (languageType == TYPE_CN) {
            result = textToSpeech.setLanguage(Locale.CHINA);
            LogUtils.e("setLanguage  CHINA result : " + result);
        } else if (languageType == TYPE_DEFAULT) {
            result = textToSpeech.setLanguage(Locale.getDefault());
            LogUtils.e("setLanguage  Default result : " + result);
        } else {
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

    public void stopSpeak(){
        if(textToSpeech.isSpeaking()){
            textToSpeech.stop();
        }
    }

    public void setListen(UtteranceProgressListener listener){
        textToSpeech.setOnUtteranceProgressListener(listener);
    }
}

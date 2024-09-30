package com.future.getd.ui.my;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.NoiseSuppressor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.future.getd.R;
import com.future.getd.base.BaseActivity;
import com.future.getd.base.SysConstant;
import com.future.getd.database.AppDataBase;
import com.future.getd.databinding.ActivityTestBinding;
import com.future.getd.log.LogUtils;
import com.future.getd.net.model.CompletionChoice;
import com.future.getd.net.model.GptRequest;
import com.future.getd.net.model.GptResponse;
import com.future.getd.net.model.Message;
import com.future.getd.net.model.TranscriptionResponse;
import com.future.getd.ui.view.pop.RecordingGifPop;
import com.future.getd.utils.EncryptUtil;
import com.future.getd.utils.JsonParser;
import com.future.getd.utils.RecordManager;
import com.future.getd.utils.RetrofitUtil;
import com.future.getd.utils.SdCardUtil;
import com.future.getd.utils.SpeechUtils;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestActivity extends BaseActivity<ActivityTestBinding> {
    // appid
    private static final String APPID = "ad9a0ae9";

    // appid对应的secret_key
    private static final String SECRET_KEY = "53d63a765c16b4429ca53679c035245b";

    // 请求地址
    private static final String HOST = "rtasr.xfyun.cn/v1/ws";

    private static final String BASE_URL = "wss://" + HOST;

    private static final String ORIGIN = "https://" + HOST;

    // 音频文件路径
    private String AUDIO_PATH = "./resource/test_1.pcm";

    // 每次发送的数据大小 1280 字节
    private static final int CHUNCKED_SIZE = 1280;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss.SSS");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected ActivityTestBinding getBinding() {
        return ActivityTestBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        IntentFilter filter = new IntentFilter(SysConstant.BROADCAST_LANGUAGE_CHANGE);
        filter.addAction(SysConstant.BROADCAST_VOICE_INTERACTION_BEGIN);
        filter.addAction(SysConstant.BROADCAST_VOICE_INTERACTION_END);
        registerReceiver(receiver,filter);
    }

    MediaRecorder mRecorder;
    AudioManager audioManager;
    @Override
    protected void initData() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        binding.test1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                while (true) {
                    URI url = null;
                    try {
                        url = new URI(BASE_URL + getHandShakeParams(APPID, SECRET_KEY));
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                    DraftWithOrigin draft = new DraftWithOrigin(ORIGIN);
                    CountDownLatch handshakeSuccess = new CountDownLatch(1);
                    CountDownLatch connectClose = new CountDownLatch(1);
                    MyWebSocketClient client = new MyWebSocketClient(url, draft, handshakeSuccess, connectClose);

                    client.connect();

                    while (!client.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
                        LogUtils.i(getCurrentTimeStr() + "\t连接中");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    // 等待握手成功
                    try {
                        handshakeSuccess.await();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    LogUtils.i(sdf.format(new Date()) + " 开始发送音频数据");
                    // 发送音频
                    byte[] bytes = new byte[CHUNCKED_SIZE];

                    //file path : /storage/emulated/0/Android/data/com.future.getd/files/GetD/audio/2024-09-25_19:18:52.m4a
//                    AUDIO_PATH = "/storage/emulated/0/Android/data/com.future.getd/files/GetD/audio/2024-09-25_19:18:52.m4a";//其實你們這裡像做藥品一樣 就設在這邊 裡面有幾張數據要發給我們 你要順便提供藥品 就這樣
                    AUDIO_PATH = "/storage/emulated/0/Android/data/com.future.getd/files/GetD/audio/2024-09-26_10:41:05.m4a";
                    try (RandomAccessFile raf = new RandomAccessFile(AUDIO_PATH, "r")) {
                        int len = -1;
                        long lastTs = 0;
                        while ((len = raf.read(bytes)) != -1) {
                            if (len < CHUNCKED_SIZE) {
                                send(client, bytes = Arrays.copyOfRange(bytes, 0, len));
                                break;
                            }

                            long curTs = System.currentTimeMillis();
                            if (lastTs == 0) {
                                lastTs = System.currentTimeMillis();
                            } else {
                                long s = curTs - lastTs;
                                if (s < 40) {
                                    LogUtils.i("error time interval: " + s + " ms");
                                }
                            }
                            send(client, bytes);
                            // 每隔40毫秒发送一次数据
                            Thread.sleep(40);
                        }

                        // 发送结束标识
                        send(client,"{\"end\": true}".getBytes());
                        LogUtils.i(getCurrentTimeStr() + "\t发送结束标识完成");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // 等待连接关闭
                    try {
                        connectClose.await();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
            }
        });
        binding.permission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] PermissionString = {
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.MODIFY_AUDIO_SETTINGS,
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_CONNECT,
                        Manifest.permission.BLUETOOTH_ADMIN,
//                        Manifest.permission.READ_MEDIA_IMAGES,
//                        Manifest.permission.READ_MEDIA_AUDIO,
//                        Manifest.permission.READ_MEDIA_VIDEO,
//                        Manifest.permission.READ_MEDIA_IMAGES,
                };
                boolean isAllGranted = checkPermissionAllGranted(PermissionString);
                if (isAllGranted) {
                    Log.e("err","所有权限已经授权！");
                    return;
                }
                // 一次请求多个权限, 如果其他有权限是已经授予的将会自动忽略掉
                ActivityCompat.requestPermissions(TestActivity.this,
                        PermissionString, 1);
            }
        });
        binding.test2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PASS 转录结果可能是其他失败提示
                LogUtils.d("startRecord");
                binding.output.setText("");
                recordManager.startRecord();
                audioFilePath = recordManager.getFilePath();
//                scheduler = Executors.newScheduledThreadPool(1);
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        scheduler.scheduleWithFixedDelay(new Runnable() {
//                            @Override
//                            public void run() {
//                                // 在这里执行你的任务
//                                LogUtils.d("定时任务");
//                                File audioFile = new File(audioFilePath);
//                                //test
//                                LogUtils.i("transAudio  file path : " + audioFile.getAbsolutePath() + " , isExists : " + audioFile.exists());
//                                if(!audioFile.exists()){
//                                    showToast("file does not exist");
//                                    return;
//                                }
//                                recordManager.stopRecord();
//                                recordManager.startRecord();
//                                audioFilePath = recordManager.getFilePath();
//                                RequestBody requestFile = RequestBody.create(MediaType.parse("audio/m4a"), audioFile);
//                                MultipartBody.Part body = MultipartBody.Part.createFormData("file", audioFile.getName(), requestFile);
//                                RequestBody model = RequestBody.create(MediaType.parse("text/plain"), "whisper-1");
//                                RetrofitUtil.getAiApiService().transcribeAudio(body,model).enqueue(new Callback<TranscriptionResponse>() {
//                                    @Override
//                                    public void onResponse(Call<TranscriptionResponse> call, Response<TranscriptionResponse> response) {
//                                        LogUtils.e("getTranscriptions response : " + response + " , body : " + response.body());
//                                        if (response.code() == 200){
//                                            TranscriptionResponse gptResponse = response.body();
//                                            String resultText = gptResponse.getText();
//                                            binding.output.append(resultText);
//                                        } else {
//                                            LogUtils.e(response.code() + " " + response.body());
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<TranscriptionResponse> call, Throwable t) {
//                                        LogUtils.e("getTranscriptions onFailure : " + t);
//                                    }
//                                });
//                            }
//                        }, 0, 5, TimeUnit.SECONDS);
//                    }
//                },5000);
            }
        });
        binding.test22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PASS 转录结果可能是其他失败提示
                LogUtils.d("startRecord");
                binding.output.setText("");
                recordManager.startRecordVoiceRecognition(TestActivity.this);
                audioFilePath = recordManager.getFilePath();
            }
        });

        /*binding.test23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecorder = new MediaRecorder();
                mRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION);
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

                String fileName = DateFormat.format("yyyy-MM-dd_HH:mm:ss", Calendar.getInstance()) + ".m4a";
                String audioSaveDir = SdCardUtil.getSdCardPath(SdCardUtil.SAVE_PATH_AUDIO);
                audioFilePath = audioSaveDir + fileName;
                mRecorder.setOutputFile(audioFilePath);
                LogUtils.i("recordManager setOutputFile ： " + audioFilePath);

                audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
                audioManager.startBluetoothSco();
                audioManager.setBluetoothScoOn(true);
                audioManager.setSpeakerphoneOn(false);
                try {
                    mRecorder.prepare();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                mRecorder.start();
            }
        });
        binding.test24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecorder.stop();
                mRecorder.release();
                audioManager.setMode(AudioManager.MODE_NORMAL);
                audioManager.stopBluetoothSco();
                audioManager.setBluetoothScoOn(false);
                audioManager.setSpeakerphoneOn(true);
                binding.output.setText("录音文件： "+ audioFilePath);
            }
        });*/

        binding.test3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.d("stopRecord");
                audioFilePath = recordManager.stopRecord();
                LogUtils.i("recordManager filePath ： " + audioFilePath);
                binding.output.setText("录音文件： "+ audioFilePath);
//                scheduler.shutdown();
                audioManager.setMode(AudioManager.MODE_NORMAL);
                audioManager.stopBluetoothSco();
                audioManager.setBluetoothScoOn(false);
                audioManager.setSpeakerphoneOn(true);
//
                //语音转文本
                File audioFile = new File(audioFilePath);
                //test
//                audioFile = new File("/storage/emulated/0/Android/data/com.future.getd/files/GetD/audio/2024-09-29_10:16:16.m4a");//也好吗
//                audioFile = new File("/storage/emulated/0/Android/data/com.future.getd/files/GetD/audio/2024-09-29_10:35:14.m4a");//你好吗 翻译成了字幕
                ///storage/emulated/0/Android/data/com.future.getd/files/GetD/audio/2024-09-29_11:28:39.m4a  //你好吗 翻译成了沒人算吧
                audioFile = new File("storage/emulated/0/Android/data/com.future.getd/files/GetD/audio/2024-09-29_13:43:37.wav");//你好吗

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
                        LogUtils.e("testgetTranscriptions response : " + response + " , body : " + response.body());
                        if (response.code() == 200){
                            TranscriptionResponse gptResponse = response.body();
                            String resultText = gptResponse.getText();
//                    Message message = new Message(Message.ROLE_USER,resultText);
//                    addMessage(message);
                            sendMsg(resultText);
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
        });

        LogUtils.i("是否支持噪音抑制功能：" + NoiseSuppressor.isAvailable());
        if (NoiseSuppressor.isAvailable()) {
//            NoiseSuppressor noise = NoiseSuppressor.create(ar.getAudioSessionId());
//            if (noise != null && !noise.getEnabled()) {
//                //用该噪声抑制器的启用方法，将噪声抑制功能设置为打开状态
//                noise.setEnabled(true);
//            }
        }
        LogUtils.i("是否支持音频回声消除器：" + AcousticEchoCanceler.isAvailable());
        if (AcousticEchoCanceler.isAvailable()) {
//            AcousticEchoCanceler aec = AcousticEchoCanceler.create(ar.getAudioSessionId());
//            if (aec != null && !aec.getEnabled()) {
//                //启用音频回声消除器,对麦克风接收到的音频信号进行处理，减少或消除回声效应
//                aec.setEnabled(true);
//            }
        }

        binding.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });
        binding.stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPlay();
            }
        });

        binding.test4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpeechUtility utility = SpeechUtility.createUtility(TestActivity.this, "appid=" + SysConstant.KDXF_APPID);
                if(utility == null){
                    return;
                }
                LogUtils.d( " utility.checkServiceInstalled() = " +  utility.checkServiceInstalled());
                // 初始化识别无UI识别对象
                // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
                InitListener mInitListener = new InitListener() {
                    @Override
                    public void onInit(int code) {
                        LogUtils.d( "SpeechRecognizer init() code = " + code);
                        if (code != ErrorCode.SUCCESS) {
                            LogUtils.d("初始化失败，错误码：" + code + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
                        }
                    }
                };
                mIat = SpeechRecognizer.createRecognizer(TestActivity.this, mInitListener);
                LogUtils.d("初始化结果 mIat : " + mIat);
                //        <item>zh_cn</item>
//        <item>en_us</item>
                setParam("zh_cn");
//                setParam("en_us");
            }
        });
        binding.test5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });
        binding.test6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.i("停止听写");
                mIat.stopListening();
            }
        });
        binding.test7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.i("取消听写");
                mIat.cancel();
            }
        });
    }

    //record
    RecordManager recordManager = new RecordManager();
    String audioFilePath = "";
    private ScheduledExecutorService scheduler;
    int ret = 0; // 函数调用返回值
    // 语音听写对象
    private SpeechRecognizer mIat;
    private String resultType = "json";
    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            LogUtils.i("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            LogUtils.i("onError " + error.getPlainDescription(true));
                LogUtils.i(error.getPlainDescription(true));

        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
                LogUtils.i("结束说话");
            start();
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            LogUtils.e("onResult ： " +  results.getResultString() + " , isLast: " + isLast);
            String text = JsonParser.parseIatResult(results.getResultString());
            LogUtils.i("onResult text 结果:" + text);
            binding.output.append(text);
            if (isLast) {
                LogUtils.i("onResult 结束");
            }
//            if (resultType.equals("json")) {
//                printResult(results);
//                return;
//            }
//            if (resultType.equals("plain")) {
//                buffer.append(results.getResultString());
//                LogUtils.i("onResult 结果2:" + buffer.toString());
//                mResultText.append(buffer.toString());
//                mResultText.setSelection(mResultText.length());
//            }
//            binding.output.append(results.getResultString());
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
//                LogUtils.i("当前正在说话，音量大小 = " + volume + " 返回音频数据 = " + data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		LogUtils.i("session id =" + sid);
            //	}
        }
    };

    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    /**
     * 参数设置
     *
     * @return
     */
    public void setParam(String language) {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);
        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, resultType);

//        <item>zh_cn</item>
//        <item>en_us</item>
        if (language.equals("zh_cn")) {
//            String lag = mSharedPreferences.getString("iat_language_preference",
//                    "mandarin");
            // 设置语言
//            Log.e(TAG, "language = " + language);
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
//            mIat.setParameter(SpeechConstant.ACCENT, lag);
        } else {
            mIat.setParameter(SpeechConstant.LANGUAGE, language);
        }
        LogUtils.i( "last language:" + mIat.getParameter(SpeechConstant.LANGUAGE));

        //此处用于设置dialog中不显示错误码信息
        //mIat.setParameter("view_tips_plain","false");

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS,"1000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT,"1");

        // 设置音频保存路径，保存音频格式支持pcm、wav.
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH,
                getExternalFilesDir("msc").getAbsolutePath() + "/iat.wav");
    }

    private void start() {
        LogUtils.i("开始听写。。");
        ret = mIat.startListening(mRecognizerListener);
        if (ret != ErrorCode.SUCCESS) {
                LogUtils.i("听写失败,错误码：" + ret + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
        } else {
                LogUtils.i("开始录音1");
        }
    }

    /**
     * 显示结果
     */
    private void printResult(RecognizerResult results) {
        LogUtils.i("printResult");
        String text = JsonParser.parseIatResult(results.getResultString());
        LogUtils.i("onResult text结果:" + text);
        String sn = null;
        // 读取json结果中的sn字段
        //onResult ： {"sn":1,"ls":true,"bg":0,"ed":0,"ws":[{"bg":0,"cw":[{"sc":0.00,"w":""}]}]} , isLast: true
        try {
            org.json.JSONObject resultJson = new org.json.JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        mIatResults.put(sn, text);
//
//        StringBuffer resultBuffer = new StringBuffer();
//        for (String key : mIatResults.keySet()) {
//            resultBuffer.append(mIatResults.get(key));
//        }
//        LogUtils.i("onResult resultBuffer.toString()结果1:" + resultBuffer.toString());
//        mResultText.append(resultBuffer.toString());
//        mResultText.setSelection(mResultText.length());
    }




    public static class MyWebSocketClient extends WebSocketClient {

        private CountDownLatch handshakeSuccess;
        private CountDownLatch connectClose;

        public MyWebSocketClient(URI serverUri, Draft protocolDraft, CountDownLatch handshakeSuccess, CountDownLatch connectClose) {
            super(serverUri, protocolDraft);
            this.handshakeSuccess = handshakeSuccess;
            this.connectClose = connectClose;
            if(serverUri.toString().contains("wss")){
                trustAllHosts(this);
            }
        }

        @Override
        public void onOpen(ServerHandshake handshake) {
            LogUtils.i(getCurrentTimeStr() + "\t连接建立成功！");
        }

        @Override
        public void onMessage(String msg) {
            JSONObject msgObj = JSON.parseObject(msg);
            String action = msgObj.getString("action");
            LogUtils.i(getCurrentTimeStr() + "\t  result : " + msgObj);
            if (Objects.equals("started", action)) {
                // 握手成功
                LogUtils.i(getCurrentTimeStr() + "\t握手成功！sid: " + msgObj.getString("sid"));
                handshakeSuccess.countDown();
            } else if (Objects.equals("result", action)) {
                // 转写结果
                LogUtils.i(getCurrentTimeStr() + "\tresult: " + getContent(msgObj.getString("data")));
            } else if (Objects.equals("error", action)) {
                // 连接发生错误
                LogUtils.i("Error: " + msg);
                System.exit(0);
            }
        }

        @Override
        public void onError(Exception e) {
            LogUtils.i(getCurrentTimeStr() + "\t连接发生错误：" + e.getMessage() + ", " + new Date());
            e.printStackTrace();
            System.exit(0);
        }

        @Override
        public void onClose(int arg0, String arg1, boolean arg2) {
            LogUtils.i(getCurrentTimeStr() + "\t链接关闭");
            connectClose.countDown();
        }

        @Override
        public void onMessage(ByteBuffer bytes) {
            try {
                LogUtils.i(getCurrentTimeStr() + "\t服务端返回：" + new String(bytes.array(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        public void trustAllHosts(MyWebSocketClient appClient) {
            LogUtils.i("wss");
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }

                @Override
                public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                    // TODO Auto-generated method stub

                }

                @Override
                public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                    // TODO Auto-generated method stub

                }
            }};

            try {
                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                appClient.setSocket(sc.getSocketFactory().createSocket());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 生成握手参数
    public static String getHandShakeParams(String appId, String secretKey) {
        String ts = System.currentTimeMillis()/1000 + "";
        String signa = "";
        try {
            signa = EncryptUtil.HmacSHA1Encrypt(EncryptUtil.MD5(appId + ts), secretKey);
            return "?appid=" + appId + "&ts=" + ts + "&signa=" + URLEncoder.encode(signa, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static void send(WebSocketClient client, byte[] bytes) {
        if (client.isClosed()) {
            throw new RuntimeException("client connect closed!");
        }

        client.send(bytes);
    }

    public static String getCurrentTimeStr() {
        return sdf.format(new Date());
    }

    // 把转写结果解析为句子
    public static String getContent(String message) {
        StringBuffer resultBuilder = new StringBuffer();
        try {
            JSONObject messageObj = JSON.parseObject(message);
            JSONObject cn = messageObj.getJSONObject("cn");
            JSONObject st = cn.getJSONObject("st");
            JSONArray rtArr = st.getJSONArray("rt");
            for (int i = 0; i < rtArr.size(); i++) {
                JSONObject rtArrObj = rtArr.getJSONObject(i);
                JSONArray wsArr = rtArrObj.getJSONArray("ws");
                for (int j = 0; j < wsArr.size(); j++) {
                    JSONObject wsArrObj = wsArr.getJSONObject(j);
                    JSONArray cwArr = wsArrObj.getJSONArray("cw");
                    for (int k = 0; k < cwArr.size(); k++) {
                        JSONObject cwArrObj = cwArr.getJSONObject(k);
                        String wStr = cwArrObj.getString("w");
                        resultBuilder.append(wStr);
                    }
                }
            }
        } catch (Exception e) {
            return message;
        }

        return resultBuilder.toString();
    }



    RecordingGifPop recordingGifPop;
    boolean isRecording = false;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(SysConstant.BROADCAST_LANGUAGE_CHANGE)){
                LogUtils.e("receive language change");
                recreate();
            }else if(intent.getAction().equals(SysConstant.BROADCAST_VOICE_INTERACTION_BEGIN)){
//                LogUtils.e("receive BROADCAST_VOICE_INTERACTION_BEGIN");
//                recordManager.startRecord();
//                if(recordingGifPop == null){
//                    recordingGifPop = new RecordingGifPop(MainActivity.this);
//                    recordingGifPop.setOnSelectListener(new RecordingGifPop.OnSelectListener() {
//                        @Override
//                        public void onCancel() {
//                            //取消录音
//                            recordManager.stopRecord();
//                        }
//                    });
//                }
//                recordingGifPop.showStart();
            }else if(intent.getAction().equals(SysConstant.BROADCAST_VOICE_INTERACTION_END)){
//                LogUtils.e("receive BROADCAST_VOICE_INTERACTION_END state " + recordManager.isRecording());
//                if (recordingGifPop != null) {
//                    recordingGifPop.dismiss();
//                }
//                if(recordManager.isRecording()){
//                    String filePath = recordManager.stopRecord();
//                    LogUtils.i("filePath ： " + filePath);
//                }
            }
        }
    };

    private void sendMsg(String content) {
        LogUtils.i("发送消息 : " + content);
        if(content.isEmpty()){
            return;
        }
        Message messageUser = new Message(Message.ROLE_USER,content);
        ArrayList<Message> tempSendList = new ArrayList<>();
        tempSendList.add(messageUser);

        GptRequest request = new GptRequest(tempSendList);
        RetrofitUtil.getAiApiService().getAiMessage(request).enqueue(new Callback<GptResponse>() {
            @Override
            public void onResponse(Call<GptResponse> call, Response<GptResponse> response) {
                LogUtils.e("getAiMessage response : " + response+ " , body : " + response.body());
                if (response.code() == 200){
                    GptResponse gptResponse = response.body();
                    if (gptResponse != null) {
                        List<CompletionChoice> completionChoices = gptResponse.getChoices();
                        if(!completionChoices.isEmpty()){
                            CompletionChoice choice = completionChoices.get(0);
                            Message message = choice.getMessage();
                            LogUtils.i("返回消息 : " + message);
//                            if(SpeechUtils.getInstance().isInitSuccess()){
//                                SpeechUtils.getInstance().speak(message.content,SpeechUtils.TYPE_DEFAULT);
//                            }
                        }
                    }
                }else{
                    showToast(getString(R.string.net_error));
                }
            }

            @Override
            public void onFailure(Call<GptResponse> call, Throwable t) {
                LogUtils.e("test getAiMessage onFailure : " + t);
            }
        });
    }



    MediaPlayer mediaPlayer = null;

    private void stopPlay() {
        if(mediaPlayer != null){
            try {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private void play() {
        if (audioFilePath != null) {
            File file = new File(audioFilePath);
            if (file.exists()) {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                Uri myUri = Uri.fromFile(file);
                try {
                    binding.output.append("/n 播放录音文件：" + audioFilePath);
                    mediaPlayer.setDataSource(getApplicationContext(), myUri);
                    mediaPlayer.prepare();
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mediaPlayer.start();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
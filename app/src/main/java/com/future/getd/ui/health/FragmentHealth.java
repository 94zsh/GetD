package com.future.getd.ui.health;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.future.getd.R;
import com.future.getd.base.BaseFragment;
import com.future.getd.base.SysConstant;
import com.future.getd.databinding.FragmentAiBinding;
import com.future.getd.databinding.FragmentHealthBinding;
import com.future.getd.log.LogUtils;
import com.future.getd.ui.adapter.HealthAudioAdapter;
import com.future.getd.ui.bean.AudioItem;
import com.future.getd.ui.bean.StepData;
import com.future.getd.ui.my.FragmentMy;
import com.future.getd.ui.my.VersionActivity;
import com.future.getd.ui.view.pop.CommonPop;
import com.future.getd.utils.PermissionUtil;
import com.future.getd.utils.ScreenUtil;
import com.future.getd.utils.SharePreferencesUtil;
import com.future.getd.utils.SystemLanguageUtil;
import com.future.getd.utils.TimeUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class FragmentHealth extends BaseFragment {
    private FragmentHealthBinding binding;
    public static FragmentHealth newInstance() {
        return new FragmentHealth();
    }
    private StepData stepData;
    SensorManager sensorManager;
    SensorEventListener stepCounterListener;
    Sensor stepCounter;
    GoogleSignInClient mGoogleSignInClient;
    boolean isSupportGoogleService = true;
    HealthAudioAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHealthBinding.inflate(getLayoutInflater());
//        ViewGroup.LayoutParams params = binding.vBar.getLayoutParams();
//        params.height = ScreenUtil.getStatusHeight(requireContext());
//        binding.vBar.setLayoutParams(params);
        init();
        return binding.getRoot();
    }

    List<AudioItem> datas = new ArrayList<>();
    private int currentPlay = -1;
    private MediaPlayer mediaPlayer;
    private void init() {
        isSupportGoogleService = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(requireContext()) == ConnectionResult.SUCCESS;
        binding.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtils.d("onClick ");
            }
        });
        // 初始化 MediaPlayer
        mediaPlayer = new MediaPlayer();
        AudioItem item = new AudioItem(0, getString(R.string.meditation_rain), currentPlay == 0,"rain1.mp3");
        AudioItem item1 = new AudioItem(1, getString(R.string.meditation_thunder), currentPlay == 1,"thunder2.mp3");
        AudioItem item2 = new AudioItem(2, getString(R.string.meditation), currentPlay == 2,"meditation3.mp3");
        AudioItem item3 = new AudioItem(3, getString(R.string.meditation_rivers), currentPlay == 3,"flowingwater4.mp3");
        AudioItem item4 = new AudioItem(4, getString(R.string.meditation_sleep), currentPlay == 4,"sleep5.mp3");
        AudioItem item5 = new AudioItem(5, getString(R.string.meditation_fire), currentPlay == 5,"fire6.mp3");
        AudioItem item6 = new AudioItem(6, getString(R.string.meditation_frog), currentPlay == 6,"frog7.mp3");
        AudioItem item7 = new AudioItem(7, getString(R.string.meditation_wave), currentPlay == 7,"wave8.mp3");
        AudioItem item8 = new AudioItem(8, getString(R.string.meditation_forest), currentPlay == 8,"forest9.mp3");
        datas.add(item);datas.add(item1);datas.add(item2);datas.add(item3);
        datas.add(item4);datas.add(item5);datas.add(item6);datas.add(item7);datas.add(item8);
        adapter = new HealthAudioAdapter(datas);
        binding.rv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        binding.rv.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener<AudioItem>() {
            @Override
            public void onClick(@NonNull BaseQuickAdapter<AudioItem, ?> baseQuickAdapter, @NonNull View view, int i) {
                LogUtils.d("audio onclick item " + i);
                AudioItem tempItem = datas.get(i);
                if(tempItem.isPlaying()){
                    stopPlay();
                }else{
                    if(mediaPlayer != null){
                        if(mediaPlayer.isPlaying()){
                            stopPlay();
                        }
                    }
                    playAudio(datas.get(i).getAssetName());
                }
                for (int k = 0; k < datas.size(); k++) {
                    if(k == i){
                        datas.get(k).setPlaying(!datas.get(k).isPlaying());
                    }else{
                        datas.get(k).setPlaying(false);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
//        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
//            private boolean isSwiped = false;
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                int position = viewHolder.getAdapterPosition();
////                adapter.removeItem(position); // 删除项
//                // 这里可以选择不处理删除，而是只记录状态
//                 isSwiped = true; // 可以用这个标记来决定是否删除
//            }
//
//            @Override
//            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
//                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {
//                View itemView = viewHolder.itemView;
//                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.delete);
//                float maxSwipeDistance = bitmap.getWidth();
//                if (dX < -maxSwipeDistance) {
//                    dX = -maxSwipeDistance; // 限制左滑的最大值
//                }
//                if (dX < 0) { // 左滑
//                    int width = itemView.getWidth();
//                    Paint paint = new Paint();
//                    paint.setColor(Color.RED);
////                    c.drawRect(itemView.getRight() + dX, itemView.getTop(), itemView.getRight(), itemView.getBottom(), paint);
//                    // 这里可以绘制删除按钮
//                    c.drawBitmap(bitmap,itemView.getRight() + dX, itemView.getTop(),paint);
//                }
//                // 这里可以自定义侧拉效果
//                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//            }
//            @Override
//            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
//                // 这里可以选择不调用 super.clearView() 来避免回弹效果
//                if (!isSwiped) {
////                    super.clearView(recyclerView, viewHolder); // 如果没有滑动过，则调用父类方法
//                }
//            }
//        };
//        new ItemTouchHelper(simpleCallback).attachToRecyclerView(binding.rv);

        sensorManager = (SensorManager) requireContext().getSystemService(Context.SENSOR_SERVICE);
        stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        stepCounterListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float steps = event.values[0];
                // 使用步数
                LogUtils.i("steps : " + steps);
                binding.tvStepValue.setText(steps + "");
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // 可选
            }
        };


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(SysConstant.GOOGLE_AUTH_KEY_ANDROID)
                .requestIdToken(SysConstant.GOOGLE_AUTH_KEY_WEB)
                .requestEmail()
//                        .requestProfile()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso);

    }

    @Override
    public void onResume() {
        super.onResume();
        setMainStatusBar();
        initStepData();
        if (!isSupportGoogleService) {
            sensorManager.registerListener(stepCounterListener, stepCounter, SensorManager.SENSOR_DELAY_UI);
        } else {
            try {
//                boolean isInstall = isGoogleFitInstalled(requireContext());
//                LogUtils.i("googleFit isInstall ：  " + isInstall);
                getGoogleData();
            } catch (Exception e) {
                LogUtils.i("getGoogleData err ：  " + e);
                e.printStackTrace();
            }
        }
    }

    private void initStepData() {
        stepData = SharePreferencesUtil.getInstance().getGoogleStepData();
        LogUtils.i("step data : " + stepData + " ,isToday :  " + TimeUtils.isToday(stepData.getTime()));
        if(TimeUtils.isToday(stepData.getTime())){
            updateStepData(stepData);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(!isSupportGoogleService){
            sensorManager.unregisterListener(stepCounterListener);
        }
    }

    CommonPop commonPop;
    CommonPop commonPopFit;
    private void getGoogleData(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            boolean isHasRecognitionPermission = PermissionUtil.isHasPermission(requireContext(),Manifest.permission.ACTIVITY_RECOGNITION);
            LogUtils.i("isHasRecognitionPermission : " + isHasRecognitionPermission);
            if(!isHasRecognitionPermission){
                PermissionX
                        .init(this)
                        .permissions(Manifest.permission.ACTIVITY_RECOGNITION)
                        .request(new RequestCallback() {
                            @Override
                            public void onResult(boolean allGranted, @NonNull List<String> grantedList, @NonNull List<String> deniedList) {
                                if(allGranted){

                                }else{

                                }
                            }
                        });
            }else{
                //has permission
                // 2. 检查用户是否已登录
                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(requireContext());
                LogUtils.i("GoogleSignInAccount account : " + account);
                if (account == null) {
                    //未登录
                    if(commonPop == null){
                        commonPop = new CommonPop(requireContext(),getString(R.string.permission),getString(R.string.permission_google_login_tip),getString(R.string.confirm));
                        commonPop.setOnSelectListener(new CommonPop.OnSelectListener() {
                            @Override
                            public void onCancel() {

                            }
                            @Override
                            public void onConfirm() {
                                // 引导用户登录
                                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                                startActivityForResult(signInIntent, 100);
                            }
                        });
                        commonPop.showStart();
                    }else{
                        if(!commonPop.isShowing()){
                            commonPop.showStart();
                        }
                    }

                } else {
                    LogUtils.i("last google account : " + account.getEmail());
                    //已登录谷歌
                    FitnessOptions fitnessOptions = FitnessOptions.builder()
                            .addDataType(DataType.TYPE_STEP_COUNT_DELTA,FitnessOptions.ACCESS_READ)
                            .addDataType(DataType.TYPE_STEP_COUNT_DELTA,FitnessOptions.ACCESS_READ)
                            .addDataType(DataType.TYPE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
                            .build();

                    if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {//如果没有权限
                        LogUtils.e("not google permission");
                        if(commonPopFit == null){
                            commonPopFit = new CommonPop(requireContext(),getString(R.string.permission),getString(R.string.permission_google_fit_tip),getString(R.string.confirm));
                            commonPopFit.setOnSelectListener(new CommonPop.OnSelectListener() {
                                @Override
                                public void onCancel() {
                                    //退出登录
                                    mGoogleSignInClient.signOut();
                                }
                                @Override
                                public void onConfirm() {
                                    // 授权谷歌健康敏感信息权限
                                    GoogleSignIn.requestPermissions(
                                            (Activity) getContext(),
                                            200,
                                            GoogleSignIn.getLastSignedInAccount(requireContext()),
                                            fitnessOptions);
                                }
                            });
                            commonPopFit.showStart();
                        }else{
                            if(!commonPopFit.isShowing()){
                                commonPopFit.showStart();
                            }
                        }
                    }else{
                        LogUtils.e("has google permission");
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, 0);
                        calendar.set(Calendar.MINUTE, 0);
                        calendar.set(Calendar.SECOND, 0);
                        long startTime = calendar.getTimeInMillis();
                        long endTime = System.currentTimeMillis();
                        DataReadRequest readRequest = new DataReadRequest.Builder()
                                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                                .aggregate(DataType.TYPE_CALORIES_EXPENDED, DataType.AGGREGATE_CALORIES_EXPENDED)
                                .aggregate(DataType.TYPE_DISTANCE_DELTA, DataType.AGGREGATE_DISTANCE_DELTA)
                                .bucketByTime(1, TimeUnit.DAYS)
                                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                                .build();
                        Fitness.getHistoryClient(requireContext(),account)
                                .readData(readRequest)
                                .addOnSuccessListener(new OnSuccessListener<DataReadResponse>() {
                                    @Override
                                    public void onSuccess(DataReadResponse response) {
                                        // 处理成功获取到的数据
                                        LogUtils.e("Today's response: " + response);
                                        int totalSteps = 0;
                                        float totalCalories = 0;
                                        float totalDistance = 0;
                                        for (Bucket bucket : response.getBuckets()) {
                                            for (DataSet dataSet : bucket.getDataSets()) {
                                                for (DataPoint dp : dataSet.getDataPoints()) {
                                                    if (dp.getDataType().equals(DataType.TYPE_STEP_COUNT_DELTA)) {
                                                        totalSteps += dp.getValue(Field.FIELD_STEPS).asInt();
                                                    } else if (dp.getDataType().equals(DataType.TYPE_CALORIES_EXPENDED)) {
                                                        totalCalories += dp.getValue(Field.FIELD_CALORIES).asFloat();
                                                    } else if (dp.getDataType().equals(DataType.TYPE_DISTANCE_DELTA)) {
                                                        totalDistance += dp.getValue(Field.FIELD_DISTANCE).asFloat();
                                                    }
                                                }
                                            }
                                        }
                                        // 处理获取到的数据
                                        LogUtils.i("FitnessData" +  "Today's Steps: " + totalSteps);
                                        LogUtils.i("FitnessData" +  "Today's Calories: " + totalCalories);
                                        LogUtils.d("FitnessData" + "Today's Distance: " + totalDistance + " meters");

                                        stepData.setTime(System.currentTimeMillis());
                                        stepData.setTotalSteps(totalSteps);
                                        stepData.setTotalCalories(totalCalories);
                                        stepData.setTotalDistance(totalDistance);
                                        updateStepData(stepData);
                                        SharePreferencesUtil.getInstance().saveGoogleStepData(stepData);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        LogUtils.e("GoogleFit " + "There was a problem getting the step count." + e);
                                    }
                                });


                        // 获取当前日期
                        Calendar monthCalendar = Calendar.getInstance();
                        int year = monthCalendar.get(Calendar.YEAR);
                        int month = monthCalendar.get(Calendar.MONTH);
                        // 获取本月的第一天和最后一天
                        monthCalendar.set(year, month, 1, 0, 0, 0);
                        startTime = monthCalendar.getTimeInMillis();
                        monthCalendar.set(year, month + 1, 1, 0, 0, 0);
                        endTime = monthCalendar.getTimeInMillis();

                        // 查询步数数据
                        DataReadRequest request = new DataReadRequest.Builder()
                                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                                .bucketByTime(1, TimeUnit.DAYS)
                                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                                .build();
                        Fitness.getHistoryClient(requireContext(),account)
                                .readData(request)
                                .addOnSuccessListener(dataReadResponse -> {
                                    int totalSteps = 0;
                                    for (Bucket bucket : dataReadResponse.getBuckets()) {
                                        for (DataSet dataSet : bucket.getDataSets()) {
                                            for (DataPoint dp : dataSet.getDataPoints()) {
                                                totalSteps += dp.getValue(Field.FIELD_STEPS).asInt();
                                            }
                                        }
                                    }
                                    // 计算平均步数
                                    int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                                    double averageSteps = (double) totalSteps / daysInMonth;
                                    LogUtils.i( "本月总步数：" + totalSteps +  " ， 本月平均步数：" + averageSteps);
                                    stepData.setMontTotalSteps(totalSteps);
                                    stepData.setAvgMonSteps((int) averageSteps);
                                    updateStepData(stepData);
                                    SharePreferencesUtil.getInstance().saveGoogleStepData(stepData);
                                })
                                .addOnFailureListener(e -> {
                                    // 处理错误
                                    LogUtils.i( "Failed to get step count e : " +  e);
                                });
                    }
                }
            }
        }
    }

    private void updateStepData(StepData stepData) {
        binding.tvStepValue.setText(stepData.getTotalSteps() + "");
        binding.tvCalValue.setText((int) stepData.getTotalCalories() + "");
        BigDecimal number = new BigDecimal(stepData.getTotalDistance());
        BigDecimal result = number.divide(new BigDecimal("1000"), 2, RoundingMode.HALF_UP);
        binding.tvDistanceValue.setText(result + "");
        binding.tvAvgStepValue.setText(stepData.getAvgMonSteps() + "");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.e("onActivityResult requestCode : " + requestCode);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 100) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            try {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    //授权登陆成功
                    final String personName = account.getDisplayName();
                    String personGivenName = account.getGivenName();
                    String personFamilyName = account.getFamilyName();
                    String personEmail = account.getEmail();
                    final String personId = account.getId();
                    final Uri personPhoto = account.getPhotoUrl();
                    String token = account.getIdToken();
                    LogUtils.e("getSignedInAccountFromIntent : " + personName + " , " + personGivenName + " ,personId: " + personId + " ,token: " + account.getIdToken() + " , PhotoUrl:" + account.getPhotoUrl());

                }
            } catch (ApiException e) {
                LogUtils.e("google e:" + e);
                e.printStackTrace();
            }
        }
        if (requestCode == 200) {
            LogUtils.e("onActivityResult  resultCode = " + resultCode);
            if (resultCode != Activity.RESULT_OK) {

            }else{

            }
        }
    }



    private void playAudio(String resName){
        try {
            if(mediaPlayer == null){
                mediaPlayer = new MediaPlayer();
            }
            LogUtils.i("playAudio resName : " + resName);
            mediaPlayer.setDataSource(getResources().getAssets().openFd(resName));
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    LogUtils.i("playAudio health onPrepared");
                    mediaPlayer.start();
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    LogUtils.i("playAudio health onCompletion");
                    for (int i = 0; i < datas.size(); i++) {
                       datas.get(i).setPlaying(false);
                    }
                    adapter.notifyDataSetChanged();
                }
            });
            mediaPlayer.prepare(); // 准备播放
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopPlay(){
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public static boolean isGoogleFitInstalled(Context context) {
        try {
            context.getPackageManager().getPackageInfo("com.google.android.apps.fitness", 0);
            return true; // Google Fit 已安装
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false; // Google Fit 未安装
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
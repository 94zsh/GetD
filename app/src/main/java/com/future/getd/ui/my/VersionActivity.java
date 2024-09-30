package com.future.getd.ui.my;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.GetPasswordOption;
import androidx.credentials.GetPublicKeyCredentialOption;

import com.future.getd.R;
import com.future.getd.base.BaseActivity;
import com.future.getd.databinding.ActivityVersionBinding;
import com.future.getd.log.LogUtils;
import com.future.getd.utils.FileChooser;
import com.future.getd.utils.SdCardUtil;
import com.google.android.gms.auth.api.identity.AuthorizationRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
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
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;

public class VersionActivity extends BaseActivity<ActivityVersionBinding> {
    private int clickTimes = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusColor(R.color.bg_main);
    }

    @Override
    protected ActivityVersionBinding getBinding() {
        return ActivityVersionBinding.inflate(getLayoutInflater());
    }

    GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void initView() {
        binding.title.ivBack.setOnClickListener( v -> {finish();});
        binding.title.tvTitle.setText(getString(R.string.app_version));
        binding.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VersionActivity.this,TestActivity.class));
            }
        });

//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
////                        .requestIdToken("1087203743981-i21ahou4p7bjsijsj6uk0hgaf31vsdv5.apps.googleusercontent.com")//ANDROID
//                .requestIdToken("1087203743981-221se72gonr19i509089r0vgo38cfaqm.apps.googleusercontent.com")//web
//                .requestEmail()
////                        .requestProfile()
//                .build();
//        mGoogleSignInClient = GoogleSignIn.getClient(VersionActivity.this, gso);
        binding.title.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
////                // 2. 检查用户是否已登录
//                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(VersionActivity.this);
//                LogUtils.i("GoogleSignInAccount account : " + account);
//                if (account == null) {
//                    // 引导用户登录
//                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//                    startActivityForResult(signInIntent, 100);
//                } else {
//                    FitnessOptions fitnessOptions = FitnessOptions.builder()
//                            .addDataType(DataType.TYPE_STEP_COUNT_DELTA,FitnessOptions.ACCESS_READ)
//                            .addDataType(DataType.TYPE_STEP_COUNT_DELTA,FitnessOptions.ACCESS_READ)
//                            .addDataType(DataType.TYPE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
//                            .build();
//
//                    if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {//如果没有权限
//                        LogUtils.e("not google permission");
//                        GoogleSignIn.requestPermissions(
//                                VersionActivity.this,
//                                200,
//                                GoogleSignIn.getLastSignedInAccount(VersionActivity.this),
//                                fitnessOptions);
//
//                    } else {
//                        LogUtils.e("has google permission");
//                        Calendar calendar = Calendar.getInstance();
//                        calendar.set(Calendar.HOUR_OF_DAY, 0);
//                        calendar.set(Calendar.MINUTE, 0);
//                        calendar.set(Calendar.SECOND, 0);
//                        long startTime = calendar.getTimeInMillis();
//                        long endTime = System.currentTimeMillis();
//                        DataReadRequest readRequest = new DataReadRequest.Builder()
//                                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
//                                .aggregate(DataType.TYPE_CALORIES_EXPENDED, DataType.AGGREGATE_CALORIES_EXPENDED)
//                                .aggregate(DataType.TYPE_DISTANCE_DELTA, DataType.AGGREGATE_DISTANCE_DELTA)
//                                .bucketByTime(1, TimeUnit.DAYS)
//                                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
//                                .build();
//                        Fitness.getHistoryClient(VersionActivity.this,account)
//                                .readData(readRequest)
//                                .addOnSuccessListener(new OnSuccessListener<DataReadResponse>() {
//                                    @Override
//                                    public void onSuccess(DataReadResponse response) {
//                                        // 处理成功获取到的数据
//                                        LogUtils.e("Today's response: " + response);
//                                        int totalSteps = 0;
//                                        float totalCalories = 0;
//                                        float totalDistance = 0;
//                                        for (Bucket bucket : response.getBuckets()) {
//                                            for (DataSet dataSet : bucket.getDataSets()) {
//                                                for (DataPoint dp : dataSet.getDataPoints()) {
//                                                    if (dp.getDataType().equals(DataType.TYPE_STEP_COUNT_DELTA)) {
//                                                        totalSteps += dp.getValue(Field.FIELD_STEPS).asInt();
//                                                    } else if (dp.getDataType().equals(DataType.TYPE_CALORIES_EXPENDED)) {
//                                                        totalCalories += dp.getValue(Field.FIELD_CALORIES).asFloat();
//                                                    } else if (dp.getDataType().equals(DataType.TYPE_DISTANCE_DELTA)) {
//                                                        totalDistance += dp.getValue(Field.FIELD_DISTANCE).asFloat();
//                                                    }
//                                                }
//                                            }
//                                        }
//                                        // 处理获取到的数据
//                                        LogUtils.i("FitnessData" +  "Today's Steps: " + totalSteps);
//                                        LogUtils.i("FitnessData" +  "Today's Calories: " + totalCalories);
//                                        LogUtils.d("FitnessData" + "Today's Distance: " + totalDistance + " meters");
//                                    }
//                                })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        LogUtils.e("GoogleFit " + "There was a problem getting the step count." + e);
//                                    }
//                                });
//                        Fitness.getHistoryClient(VersionActivity.this, account)
//                            .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
//                            .addOnSuccessListener(new OnSuccessListener<DataSet>() {
//                                @Override
//                                public void onSuccess(DataSet dataSet) {
//                                    int totalSteps = dataSet.isEmpty() ? 0 : dataSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
//                                    // 使用总步数
//                                    LogUtils.i("totalSteps : " + totalSteps);
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    // 处理错误
//                                    LogUtils.i("onFailure : " + e);
//                                }
//                            });
//                    }
                    // 3. 获取今天的时间戳
//                    long startTime = 0; // 当天开始时间
//                    long endTime = System.currentTimeMillis();
                    // 4. 读取步数
//                    Fitness.getHistoryClient(VersionActivity.this, account)
//                            .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
//                            .addOnSuccessListener(new OnSuccessListener<DataSet>() {
//                                @Override
//                                public void onSuccess(DataSet dataSet) {
//                                    int totalSteps = dataSet.isEmpty() ? 0 : dataSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
//                                    // 使用总步数
//                                    LogUtils.i("totalSteps : " + totalSteps);
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    // 处理错误
//                                    LogUtils.i("onFailure : " + e);
//                                }
//                            });
//                }

//                CredentialManager credentialManager = CredentialManager.create(VersionActivity.this);
//                GetPasswordOption getPasswordOption = new GetPasswordOption();
//                // Get passkey from the user's public key credential provider.
//                GetPublicKeyCredentialOption getPublicKeyCredentialOption =
//                        new GetPublicKeyCredentialOption(requestJson);
//                GetCredentialRequest getCredRequest = new GetCredentialRequest.Builder()
//                        .addCredentialOption(getPasswordOption)
//                        .addCredentialOption(getPublicKeyCredentialOption)
//                        .build();
//                GetGoogleIdOption option = new GetGoogleIdOption.Builder()
//                        .setFilterByAuthorizedAccounts(true)
//                        .setServerClientId("1087203743981-i21ahou4p7bjsijsj6uk0hgaf31vsdv5.apps.googleusercontent.com")
//                        .setAutoSelectEnabled(true)
//                        .build();
//                GetCredentialRequest request = new GetCredentialRequest.Builder().addCredentialOption(option).build();
//                GetCredentialResponse response = (GetCredentialResponse) credentialManager.getCredential(VersionActivity.this, request, new Continuation<GetCredentialResponse>() {
//                    @NonNull
//                    @Override
//                    public CoroutineContext getContext() {
//                        return null;
//                    }
//
//                    @Override
//                    public void resumeWith(@NonNull Object o) {
//
//                    }
//                });
//                Credential credential = response.getCredential();
//                LogUtils.i("credential : " + credential);
//                List<Scopes> requestedScopes = Arrays.asList(DriveScopes.DRIVE_APPDATA);
//                AuthorizationRequest authorizationRequest = AuthorizationRequest.builder().setRequestedScopes(requestedScopes).build();
//                Identity.getAuthorizationClient(VersionActivity.this)
//                        .authorize(authorizationRequest)
//                        .addOnSuccessListener(
//                                authorizationResult -> {
//                                    if (authorizationResult.hasResolution()) {
//                                        // Access needs to be granted by the user
//                                        PendingIntent pendingIntent = authorizationResult.getPendingIntent();
//                                        try {
//                                            startIntentSenderForResult(pendingIntent.getIntentSender(),
//                                                    REQUEST_AUTHORIZE, null, 0, 0, 0, null);
//                                        } catch (IntentSender.SendIntentException e) {
//                                            Log.e(TAG, "Couldn't start Authorization UI: " + e.getLocalizedMessage());
//                                        }
//                                    } else {
//                                        // Access already granted, continue with user action
//                                        saveToDriveAppFolder(authorizationResult);
//                                    }
//                                })
//                        .addOnFailureListener(e -> Log.e(TAG, "Failed to authorize", e));
//                }
            }
        });

        binding.ivLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickTimes ++;
                if(clickTimes == 5){
                    FileChooser mFileChooser = new FileChooser(VersionActivity.this).setFileListener(new FileChooser.FileSelectedListener() {
                        @Override
                        public void fileSelected(File file) {
                            if (file.exists()) {
                                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                                StrictMode.setVmPolicy(builder.build());
                                builder.detectFileUriExposure();
                                LogUtils.e("file : " + file.getAbsolutePath());
//                                shareLogs(AboutActivity.this, file.getAbsolutePath(), "Share Log");
                                shareLog(file);
                            }else{
                                Toast.makeText(VersionActivity.this, "log file not exists ",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    mFileChooser.refresh(new File(SdCardUtil.getSdCardPath(SdCardUtil.SAVE_PATH_LOG)));
                    mFileChooser.showDialog();
                    clickTimes = 0;

//                    MainActivity.isDebug = true;
//                    Toast.makeText(AboutActivity.this, "debug mode is " + MainActivity.isDebug,Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @Override
    protected void initData() {
        String version = "";
        try {
            version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        binding.tvVersion.setText("V " + version);
    }

    public void shareLog(File shareFile ) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri uri =
                        FileProvider.getUriForFile(this, getPackageName() + ".fileProvider", shareFile);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setClipData(ClipData.newRawUri(MediaStore.EXTRA_OUTPUT, uri));
            } else {
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(shareFile));
            }
            intent.setType("*/*");
            Intent chooser = Intent.createChooser(intent, "share");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(chooser);
            }
        }catch (Exception e){
            e.printStackTrace();
            LogUtils.e("shareLog error : " + e);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
 /*       LogUtils.e("onActivityResult requestCode : " + requestCode);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 100) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            try {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
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
        }*/
    }

}
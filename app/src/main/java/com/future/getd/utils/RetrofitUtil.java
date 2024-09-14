package com.future.getd.utils;

import android.content.Context;

import com.future.getd.net.AIBaiduApiService;
import com.future.getd.net.Api;
import com.future.getd.net.ApiService;
import com.future.getd.net.gson.StringConverterFactory;
import com.future.getd.net.help.NetInterceptor;

import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
//import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
//import retrofit2.converter.gson.GsonConverterFactory;

/* loaded from: classes3.dex */
public class RetrofitUtil {
    private static AIBaiduApiService mAiApiService;
    private static ApiService mApiService;
    private static volatile Retrofit sAiRetrofit;
    private static volatile Retrofit sRetrofit;

    public static AIBaiduApiService getAiApiService() {
        if (sAiRetrofit == null) {
            return null;
        }
        if (mAiApiService == null) {
            mAiApiService = (AIBaiduApiService) sAiRetrofit.create(AIBaiduApiService.class);
        }
        return mAiApiService;
    }

    public static ApiService getApiService() {
        if (sRetrofit == null) {
            return null;
        }
        if (mApiService == null) {
            mApiService = (ApiService) sRetrofit.create(ApiService.class);
        }
        return mApiService;
    }

    public static void initHttpClient(Context context) {
        OkHttpClient.Builder retryOnConnectionFailure = new OkHttpClient.Builder().retryOnConnectionFailure(false);
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        OkHttpClient build = retryOnConnectionFailure.connectTimeout(30000L, timeUnit).readTimeout(30000L, timeUnit).addInterceptor(new NetInterceptor(context, true, true)).hostnameVerifier(new HostnameVerifier() { // from class: cd6
            @Override // javax.net.ssl.HostnameVerifier
            public final boolean verify(String str, SSLSession sSLSession) {
                boolean lambda$initHttpClient$0;
                lambda$initHttpClient$0 = RetrofitUtil.lambda$initHttpClient$0(str, sSLSession);
                return lambda$initHttpClient$0;
            }
        }).build();
        build.dispatcher().setMaxRequests(128);
        build.dispatcher().setMaxRequestsPerHost(10);
        initRetrofit(build);
    }

    private static void initRetrofit(OkHttpClient okHttpClient) {
        if (sRetrofit == null) {
            synchronized (RetrofitUtil.class) {
                if (sRetrofit == null) {
//                    sRetrofit = new Retrofit.Builder().client(okHttpClient).baseUrl(Api.BASE_API).addConverterFactory(StringConverterFactory.create()).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
                }
            }
        }
        if (sAiRetrofit == null) {
            synchronized (RetrofitUtil.class) {
                if (sAiRetrofit == null) {
//                    sAiRetrofit = new Retrofit.Builder().client(okHttpClient).baseUrl(Api.AI_BASE_API).addConverterFactory(StringConverterFactory.create()).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$initHttpClient$0(String str, SSLSession sSLSession) {
        return true;
    }
}
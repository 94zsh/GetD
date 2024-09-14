package com.future.getd.net.help;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/* loaded from: classes3.dex */
public class NetInterceptor implements Interceptor {
    public static final String TAG = "NetInterceptor";
    private final Map<String, String> header = new HashMap();
    private final Context mContext;
    private final boolean mShowRequestLog;
    private final boolean mShowResponseLog;

    public NetInterceptor(Context context, boolean z, boolean z2) {
        this.mContext = context;
        this.mShowRequestLog = z;
        this.mShowResponseLog = z2;
    }

    private void LogForRequest(Request request) {
        MediaType contentType;
        StringBuilder sb = new StringBuilder();
        try {
            String httpUrl = request.url().toString();
            sb.append("\n========Request(Start)=======\n");
            sb.append("Request->Method: ");
            sb.append(request.method());
            sb.append("\n");
            sb.append("Request->URL:");
            sb.append(httpUrl);
            sb.append("\n");
            Headers headers = request.headers();
            if (headers.size() > 0) {
                sb.append("Request->headers:\n");
                sb.append(headers);
            }
            RequestBody body = request.body();
            if (body != null && (contentType = body.contentType()) != null) {
                if (isText(contentType)) {
                    sb.append("Request->parameter:");
                    sb.append(bodyToString(body, contentType));
                    sb.append("\n");
                } else {
                    sb.append("Request->parameter: RequestBody's content : maybe [file part] , too large too print , ignored! \n\n");
                }
            }
            sb.append("========Request(End)=======");
            Log.d(TAG, sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Response LogForResponse(Response response) {
        MediaType contentType;
        try {
            printResponseLog("========Response(Start)=======");
            Response build = response.newBuilder().build();
            printResponseLog("url : " + build.request().url());
            printResponseLog("code : " + build.code());
            printResponseLog("protocol : " + build.protocol());
            if (!TextUtils.isEmpty(build.message())) {
                printResponseLog("message : " + build.message());
            }
            ResponseBody body = build.body();
            if (body != null && (contentType = body.contentType()) != null) {
                printResponseLog("responseBody's contentType : " + contentType);
                if (isText(contentType)) {
                    String string = body.string();
                    printResponseLog("responseBody's content: " + string);
                    ResponseBody create = ResponseBody.create(string, contentType);
                    printResponseLog("========Response(End)=======");
                    return response.newBuilder().body(create).build();
                }
                printResponseLog("responseBody's content :  maybe [file part] , too large too print , ignored!");
            }
            printResponseLog("========Response(End)=======");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private String bodyToString(RequestBody requestBody, MediaType mediaType) {
        try {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            Charset charset = mediaType.charset(StandardCharsets.UTF_8);
            if (charset != null) {
                return buffer.readString(charset);
            }
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private boolean isText(MediaType mediaType) {
        if (mediaType.type().equals("text") || mediaType.subtype().equals("json") || mediaType.subtype().equals("xml") || mediaType.subtype().equals("html") || mediaType.subtype().equals("webviewhtml")) {
            return true;
        }
        return false;
    }

    private void printResponseLog(String str) {
        if (this.mShowResponseLog) {
            Log.i(TAG, str);
        }
    }

    @Override // okhttp3.Interceptor
    public Response intercept(Chain chain) throws IOException {
        Request build;
        Request request = chain.request();
        String httpUrl = request.url().toString();
        Request.Builder newBuilder = request.newBuilder();
        newBuilder.addHeader("phoneBrand", Build.BRAND);
        newBuilder.addHeader("phoneModel", Build.MODEL);
//        newBuilder.addHeader("appVersion", Utils.getVersion(this.mContext));
//        newBuilder.addHeader("osVersion", Build.VERSION.RELEASE);
//        newBuilder.addHeader("osType", "android");
//        String string = SPUtils.getInstance().getString(SPUtils.Key.SP_PROJECT);
//        newBuilder.addHeader("project", WindowUtils.getValueEncoded(string));
//        Log.d("aaaaaaaaaaaaa", "WindowUtils.getValueEncoded(project) =" + WindowUtils.getValueEncoded(string));
//        newBuilder.addHeader("mac", SPUtils.getInstance().getString(SPUtils.Key.SP_MAC_ADDRESS));
//        newBuilder.addHeader(DeviceEntry.COLUMN_DEVICE_UID, SPUtils.getInstance().getString(SPUtils.Key.SP_UID));
//        newBuilder.addHeader("customerId", "9");
//        newBuilder.addHeader("userId", SPUtils.getInstance().getString(SPUtils.Key.SP_USER_ID));
        RequestBody body = request.body();
        if (body != null) {
            build = newBuilder.get().url(httpUrl).post(body).build();
        } else {
            build = newBuilder.get().url(httpUrl).build();
        }
        try {
            Response build2 = chain.proceed(build).newBuilder().build();
            String str = "";
            if (build2.body() != null) {
                str = build2.body().string();
            }
            if (this.mShowRequestLog) {
                LogForRequest(build);
            }
            return LogForResponse(build2.newBuilder().body(ResponseBody.create(str, build2.body().contentType())).build());
        } catch (Exception e) {
            Log.e(TAG, "intercept  Exception", e);
            throw e;
        }
    }
}
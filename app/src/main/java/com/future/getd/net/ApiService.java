package com.future.getd.net;

import com.future.getd.net.bean.account.CommonResponse;
import com.future.getd.net.bean.account.FeedBackRequest;
import com.future.getd.net.bean.account.LoginRequest;
import com.future.getd.net.bean.account.LoginResponse;
import com.future.getd.net.bean.account.RegisterRequest;
import com.future.getd.net.bean.account.RegisterResponse;
import com.future.getd.net.model.City;
import com.future.getd.net.model.GptRequest;
import com.future.getd.net.model.GptResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @Headers({"Content-Type:application/json"})
    @POST("/user/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    //0表示注册；1表示修改
    @GET("sendMailCode")
    Call<CommonResponse> sendMailCode(@Query("to") String email,@Query("type") int type);

    //    "username":"admin",
    //    "password":"1234567",
    //    "code":"123412"

    //{"code":200,"message":"成功","data":{"id":1,"username":"532253827@qq.com","password":"123456","userAvatarUrl":null,"email":"532253827@qq.com","phoneNumber":null,"createdTime":"2024-09-29 08:43:26","updatedTime":"2024-09-29 08:43:26","lastLoginTime":null,"status":"active","gender":null,"userType":"international"}}
    @Headers({"Content-Type:application/json"})
    @POST("/user/register_email")
    Call<RegisterResponse> registerEmail(@Body RegisterRequest request);

    @Headers({"Content-Type:application/json"})
    @POST("/user/forget_password")
    Call<RegisterResponse> forgetPsw(@Body RegisterRequest request);

    @Headers({"Content-Type:application/json"})
    @POST("/feedback/addFeedback")
    Call<RegisterResponse> feedback(@Body FeedBackRequest request);
}
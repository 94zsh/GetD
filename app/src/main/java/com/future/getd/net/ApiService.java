package com.future.getd.net;
//
//import com.watchfun.future3d.bean.ProjectInfo;
//import com.watchfun.future3d.bean.adconfig.AdConfigData;
//import com.watchfun.future3d.bean.adconfig.AdConfigInfoBean;
//import com.watchfun.future3d.bean.ai.AiBody;
//import com.watchfun.future3d.bean.ai.MsgResultData;
//import com.watchfun.future3d.bean.ai.TokenInfoBean;
//import com.watchfun.future3d.bean.login.UserBean;
//import com.watchfun.future3d.bean.login.UserInfo;
//import com.watchfun.future3d.bean.login.UserTokenVO;
//import com.watchfun.future3d.bean.sleep.SleepBannerBean;
//import com.watchfun.future3d.bean.sleep.SleepListDataBean;
//import com.watchfun.future3d.bean.sleep.SleepTabItemBean;
//import com.watchfun.future3d.bean.update.VersionData;
//import com.watchfun.future3d.net.base.BaseHttpResult;
//import com.watchfun.future3d.view.main.feedback.bean.FeedbackTypeBean;
//import com.watchfun.future3d.view.main.feedback.bean.SaveFeedbackBean;
//import io.reactivex.Observable;
//import java.util.List;
//import retrofit2.http.Body;
//import retrofit2.http.GET;
//import retrofit2.http.Headers;
//import retrofit2.http.POST;
//import retrofit2.http.Query;
//
///* loaded from: classes3.dex */
public interface ApiService {
//    @GET(ApiPath.INTERSTITIAL_CONFIG_PATH)
//    Observable<BaseHttpResult<List<AdConfigData>>> getAdConfigData();
//
//    @GET(ApiPath.INTERSTITIAL_AD_PATH)
//    Observable<BaseHttpResult<AdConfigInfoBean>> getAdConfigInfo(@Query("id") int i);
//
//    @Headers({"Content-type:application/json;charset=UTF-8"})
//    @POST(ApiPath.AI_GET_CONTENT_PATH)
//    Observable<BaseHttpResult<MsgResultData>> getAiMessage(@Query("access_token") String str, @Body AiBody aiBody);
//
//    @Headers({"Content-type:application/json;charset=UTF-8"})
//    @GET(ApiPath.AI_GET_TOKEN)
//    Observable<BaseHttpResult<TokenInfoBean>> getAiToken(@Query("grant_type") String str, @Query("client_id") String str2, @Query("client_secret") String str3);
//
//    @GET(ApiPath.SLEEP_BANNER_PATH)
//    Observable<BaseHttpResult<List<SleepBannerBean>>> getBannerData();
//
//    @GET(ApiPath.CHECK_VERSION_PATH)
//    Observable<BaseHttpResult<VersionData>> getCheckVersionInfo();
//
//    @Headers({"Content-type:application/json;charset=UTF-8"})
//    @POST(ApiPath.EMAIL_LOGIN)
//    Observable<BaseHttpResult<UserTokenVO>> getEmailLoginData(@Body UserBean userBean);
//
//    @Headers({"Content-type:application/json;charset=UTF-8"})
//    @POST(ApiPath.EMAIL_CODE)
//    Observable<BaseHttpResult<String>> getEmailsCodeData(@Body UserBean userBean);
//
//    @GET(ApiPath.FEEDBACK_TYPE)
//    Observable<BaseHttpResult<List<FeedbackTypeBean>>> getFeedbackTypeData();
//
//    @Headers({"Content-type:application/json;charset=UTF-8"})
//    @POST(ApiPath.PHONE_LOGIN)
//    Observable<BaseHttpResult<UserTokenVO>> getLoginData(@Body UserBean userBean);
//
//    @GET(ApiPath.PROJECT_INFO_PATH)
//    Observable<BaseHttpResult<ProjectInfo>> getProjectData();
//
//    @GET(ApiPath.SLEEP_LIST_PATH)
//    Observable<BaseHttpResult<SleepListDataBean>> getSleepListData(@Query("categoryId") int i, @Query("pageNum") int i2, @Query("pageSize") int i3);
//
//    @GET(ApiPath.SLEEP_TAB_PATH)
//    Observable<BaseHttpResult<List<SleepTabItemBean>>> getSleepTabData();
//
//    @Headers({"Content-type:application/json;charset=UTF-8"})
//    @POST(ApiPath.PHONE_SMS_CODE)
//    Observable<BaseHttpResult<String>> getSmsCodeData(@Body UserBean userBean);
//
//    @GET(ApiPath.USER_INFO)
//    Observable<BaseHttpResult<UserInfo>> getUserInfoData();
//
//    @POST(ApiPath.FIND_PASSWORD)
//    Observable<BaseHttpResult<String>> postFindPassword(@Body UserBean userBean);
//
//    @POST(ApiPath.REGISTER_TOURIST)
//    Observable<BaseHttpResult<String>> postRegisterTourist();
//
//    @POST(ApiPath.EMAIL_REGISTER)
//    Observable<BaseHttpResult<String>> postRegisterUser(@Body UserBean userBean);
//
//    @Headers({"Content-type:application/json;charset=UTF-8"})
//    @POST(ApiPath.REFRESH_TOKEN)
//    Observable<BaseHttpResult<UserTokenVO>> refreshToken(@Body UserBean userBean);
//
//    @POST(ApiPath.FEEDBACK_SUBMIT)
//    Observable<BaseHttpResult<String>> submitFeedbackData(@Body SaveFeedbackBean saveFeedbackBean);
//
//    @POST(ApiPath.UPDATE_USER_INFO)
//    Observable<BaseHttpResult<String>> updateUserInfoData(@Body UserInfo userInfo);
}
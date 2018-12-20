package com.ss.android.gamecommon.thread;


import com.ss.android.gamecommon.applog.LogModel;
import com.ss.android.gamecommon.model.Authorize;
import com.ss.android.gamecommon.model.AutoLogin;
import com.ss.android.gamecommon.model.CreateOrder;
import com.ss.android.gamecommon.model.GameLogin;
import com.ss.android.gamecommon.model.QuickLogin;
import com.ss.android.gamecommon.model.RegistrationMobilePhone;
import com.ss.android.gamecommon.model.ResultModel;
import com.ss.android.gamecommon.model.SendCode;
import com.ss.android.gamecommon.model.TicketModel;
import com.ss.android.gamecommon.model.TokenModel;
import com.ss.android.gamecommon.model.Verified;
import com.ss.android.gamecommon.model.VisitorLogin;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface ApiService {
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("service/2/device_register/")
    Call<RegistrationMobilePhone> obtainInstallId(@Body RequestBody jsonParams);

    //需要添加头
//  @Headers({"Content-Type: application/json;charset=utf-8", "Content-Encoding: gzip"})

    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST
    Call<String> reportLog(@Url String url, @Body RequestBody requestBody);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("game_sdk/has_bound/")
    Call<ResponseBody> hasBound(@QueryMap HashMap<String, Object> map);

    @Multipart
    @POST("game_sdk/visitor_login/")
    Call<ResultModel<VisitorLogin>> visitorLogin(@PartMap Map<String, RequestBody> requestBodyMap);


    @Headers("Content-type:application/x-www-form-urlencoded;charset=UTF-8")
    @FormUrlEncoded
    @POST("game_sdk/game_login/")
    Call<ResultModel<GameLogin>> gameLogin(@FieldMap Map<String, Object> requestBodyMap);

    @Headers("Content-type:application/x-www-form-urlencoded;charset=UTF-8")
    @FormUrlEncoded
    @POST("https://open.snssdk.com/oauth/send_code/")
    Call<ResultModel<SendCode>> sendCode(@FieldMap Map<String, Object> requestBodyMap);

    @Headers("Content-type:application/x-www-form-urlencoded;charset=UTF-8")
    @FormUrlEncoded
    @POST("https://open.snssdk.com/oauth/sms/authorize/")
    Call<ResultModel<QuickLogin>> quickLogin(@FieldMap Map<String, Object> maps);

    @Headers("Content-type:application/x-www-form-urlencoded;charset=UTF-8")
    @FormUrlEncoded
    @POST("https://open.snssdk.com/oauth/authorize/")
    Call<ResultModel<Authorize>> queryOAuthCode(@FieldMap Map<String, Object> requestBodyMap);

    @Headers("Content-type:application/x-www-form-urlencoded;charset=UTF-8")
    @FormUrlEncoded
    @POST("game_sdk/access_token/")
    Call<ResultModel<TokenModel>> queryToken(@FieldMap Map<String, Object> mDataMap);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("game_sdk/has_verified/")
    Call<ResultModel<Verified>> onHasVerified(@QueryMap HashMap<String, Object> mDataMap);

    @Headers("Content-type:application/x-www-form-urlencoded;charset=UTF-8")
    @FormUrlEncoded
    @POST("https://open.snssdk.com/oauth/mobile/authorize/")
    Call<ResultModel<TicketModel>> enterGameForTicker(@FieldMap HashMap<String, Object> mDataMap);

    @Headers("Content-type:application/x-www-form-urlencoded;charset=UTF-8")
    @FormUrlEncoded
    @POST("https://open.snssdk.com/oauth/authorize/")
    Call<ResultModel<Authorize>> enterGameForCode(@FieldMap Map<String, Object> requestBodyMap);

    @Headers("Content-type:application/x-www-form-urlencoded;charset=UTF-8")
    @FormUrlEncoded
    @POST("https://i.snssdk.com/game_sdk/auto_login/")
    Call<ResultModel<AutoLogin>> autoLogin(@FieldMap Map<String, Object> requestBodyMap);

    @Headers("Content-type:application/x-www-form-urlencoded;charset=UTF-8")
    @FormUrlEncoded
    @POST("game_sdk/verify/")
    Call<ResponseBody> verifyIdentity(@QueryMap HashMap<String, Object> mDataMap, @FieldMap Map<String, Object> requestBodyMap);

    @Headers("Content-type:application/x-www-form-urlencoded;charset=UTF-8")
    @FormUrlEncoded
    @POST("https://ib.snssdk.com/pay/sdk/unified_order/")
    Call<ResultModel<CreateOrder>> createOrder(@FieldMap HashMap<String, Object> mData);

    @Headers("Content-type:application/x-www-form-urlencoded;charset=UTF-8")
    @FormUrlEncoded
    @POST("https://ib.snssdk.com/pay/sdk/create_order/")
    Call<ResponseBody> createOrderLast(@FieldMap HashMap<String, Object> mData);

    @GET("game_pay/pay_notify")
    Call<ResultModel<Boolean>> queryPaymentResults(@QueryMap HashMap<String, Object> requestHeader);

    @POST("game_sdk/log/")
    Call<ResponseBody> upLoadLog(@QueryMap HashMap<String, Object> requestHeader, @Body RequestBody log);
}

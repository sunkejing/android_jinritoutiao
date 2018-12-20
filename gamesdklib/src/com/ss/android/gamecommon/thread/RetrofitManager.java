package com.ss.android.gamecommon.thread;

import android.content.Context;
import android.text.TextUtils;

import com.ss.android.gamecommon.broadcast.NetStateChangeReceiver;
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
import com.ss.android.gamecommon.util.JsonMap;
import com.ss.android.gamecommon.util.LogUtil;
import com.ss.android.gamecommon.util.SharedPreferenceUtil;
import com.ss.android.gamecommon.util.UrlUtil;
import com.ss.android.login.sdk.model.RequestCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.ss.android.gamecommon.util.ConstantUtil.BASE_URL;
import static com.ss.android.gamecommon.util.SharedPreferenceUtil.DEVICE_ID;

public class RetrofitManager {
    private static RetrofitManager sInstance;
    private Retrofit retrofit;
    private static OkHttpClient okHttpClient;
    private OkHttpClient.Builder okHttpBuilder;
    private ApiService apiService;
    private Retrofit.Builder retrofitBuilder;
    private OkHttpRetryInterceptor okHttpRetryInterceptor;

    public static RetrofitManager getInstance() {
        if (null == sInstance) {
            synchronized (RetrofitManager.class) {
                if (null == sInstance) {
                    sInstance = new RetrofitManager();
                }
            }
        }
        return sInstance;
    }

    //定义一个信任所有证书的TrustManager
    final TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }
    };

    private RetrofitManager() {
        if (null == okHttpClient) {
            try {
                okHttpRetryInterceptor = new OkHttpRetryInterceptor(Double.POSITIVE_INFINITY, 10000);
                SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new SecureRandom());
                SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
                okHttpBuilder = new OkHttpClient.Builder();
                okHttpClient = okHttpBuilder
                        .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                        .connectTimeout(15, TimeUnit.SECONDS)//链接超时时间
                        .readTimeout(15, TimeUnit.SECONDS)//读取超时时间
                        .writeTimeout(15, TimeUnit.SECONDS)//写入超时时间
                        .sslSocketFactory(sslSocketFactory)
                        .hostnameVerifier(new TrustAllHostnameVerifier())
                        .addInterceptor(okHttpRetryInterceptor)
                        .retryOnConnectionFailure(true)
                        .cookieJar(new CookieJar() {
                            private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

                            @Override
                            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                                cookieStore.put(url.host(), cookies);
                                for (Cookie cookie : cookies) {
                                    LogUtil.e("Cookie.Builder:" + cookie.name());
                                    LogUtil.e("cookiePath:" + cookie.path());
                                }
                            }

                            @Override
                            public List<Cookie> loadForRequest(HttpUrl url) {
                                List<Cookie> cookies = cookieStore.get(url.host());
                                return cookies != null ? cookies : new ArrayList<Cookie>();
                            }
                        })
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        retrofitBuilder = new Retrofit.Builder();
        retrofit = retrofitBuilder
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        apiService = retrofit.create(ApiService.class);

    }


    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }


    /**
     * 获取到installId
     *
     * @param jsonObjectParams
     * @return
     */
    public String obtainInstallId(final Context context, String jsonObjectParams) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObjectParams);
        retrofit2.Call<RegistrationMobilePhone> call = (Call) apiService.obtainInstallId(body);
        call.enqueue(new Callback<RegistrationMobilePhone>() {
            @Override
            public void onResponse(Call<RegistrationMobilePhone> call, Response<RegistrationMobilePhone> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        RegistrationMobilePhone registrationMobilePhone = response.body();
                        if (registrationMobilePhone != null && registrationMobilePhone.getInstall_id() > 0) {
                            //获取iid成功，保存到sp中
                            NetStateChangeReceiver.getInstance().unregisterReceiver(context);
                            SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance(context);
                            sharedPreferenceUtil.putString("iid", registrationMobilePhone.getInstall_id() + "");
                            sharedPreferenceUtil.putString(DEVICE_ID, registrationMobilePhone.getDevice_id() + "");
                            return;

                        }
                    }
                    //获取iid失败
                    NetStateChangeReceiver.getInstance().registerReceiver(context);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<RegistrationMobilePhone> call, Throwable t) {
                NetStateChangeReceiver.getInstance().registerReceiver(context);

            }
        });
        return null;

    }

//    public void reportLog(final Context context, HashMap<String, Object> map, final String logInfo) {
//        final String CONTENT_TYPE = "application/json; charset=utf-8";
//        String sign = (String) map.get("sign");
//        try {
//            sign = URLEncoder.encode(sign, "utf-8");
//            map.put("sign", sign);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        final String extUrl = BASE_URL + LOG_URL + UrlUtil.buildMap(map);
//        final byte[] sendData = GZipUtil.compress(logInfo);
//        final String sendStr = new String(sendData);
//        ThreadPoolManager.getInstance().executor(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    String response = NetworkUtils.executePost(4 * 1024, extUrl, logInfo,
//                            NetworkUtils.CompressType.GZIP, CONTENT_TYPE);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
//
//    }

    public void hasBound(HashMap<String, Object> map, final RequestCallback<JSONObject> requestCallback) {
        Call<ResponseBody> call = apiService.hasBound(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response != null) {
                    if (response.isSuccessful() && response.body() != null) {
                        int error_code = -1;
                        String message = "unknown";
                        try {
                            String result = new String(response.body().bytes());
                            if (!TextUtils.isEmpty(result)) {

                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject != null) {
                                    message = jsonObject.optString("message");
                                    JSONObject data = (JSONObject) jsonObject.get("data");
                                    if (data != null) {
                                        error_code = jsonObject.optInt("error_code");
                                        int has_bound = jsonObject.optInt("has_bound");
                                        if (error_code == 0 && message.equalsIgnoreCase("success")) {
                                            //请求成功
                                            requestCallback.onRequestSuccess(data);
                                            return;
                                        }
                                    }


                                }
                            }

                            //请求失败
                            requestCallback.onRequestFailture(String.valueOf(error_code), message);
                        } catch (IOException e) {
                            e.printStackTrace();
                            //请求失败
                            requestCallback.onRequestFailture(String.valueOf(error_code), e.getMessage());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            requestCallback.onRequestFailture(String.valueOf(error_code), e.getMessage());
                        }
                    }
                    //请求失败
                    requestCallback.onRequestFailture(String.valueOf(-1), "网络异常，稍后重试");

                } else {
                    //请求失败
                    requestCallback.onRequestFailture(String.valueOf(-1), "response is null");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                //请求失败
                requestCallback.onRequestFailture(String.valueOf(-1), "没有联网，请稍后重试");
            }
        });


    }

    public void onVisitorLogin(HashMap<String, Object> map, final RequestCallback<VisitorLogin> requestCallback) {
        Map<String, RequestBody> requestDataMap = generateRequestBody(map);
        Call<ResultModel<VisitorLogin>> call = apiService.visitorLogin(requestDataMap);
        call.enqueue(new Callback<ResultModel<VisitorLogin>>() {
            @Override
            public void onResponse(Call<ResultModel<VisitorLogin>> call, Response<ResultModel<VisitorLogin>> response) {
                if (response != null && response.isSuccessful() && response.body() != null) {
                    VisitorLogin visitorLogin = response.body().getData();
                    requestCallback.onRequestSuccess(visitorLogin);
                } else {
                    //失败
                    requestCallback.onRequestFailture(String.valueOf(response.code()), response.message());
                }
            }

            @Override
            public void onFailure(Call<ResultModel<VisitorLogin>> call, Throwable t) {
                t.printStackTrace();
                requestCallback.onRequestFailture("-1", "没有联网，请稍后重试");
            }
        });
    }

    public void onGameLogin(Map<String, Object> requestDataMap, final RequestCallback<GameLogin> requestCallback) {
        Call<ResultModel<GameLogin>> call = apiService.gameLogin(requestDataMap);
        call.enqueue(new Callback<ResultModel<GameLogin>>() {
            @Override
            public void onResponse(Call<ResultModel<GameLogin>> call, Response<ResultModel<GameLogin>> response) {
                if (response != null && response.isSuccessful() && response.body() != null) {
                    String message = response.body().getMessage();
                    GameLogin gameLogin = response.body().getData();
                    if (!"success".equalsIgnoreCase(message)) {
                        requestCallback.onRequestFailture(gameLogin.getError_code() + "", response.message());
                    } else {
                        requestCallback.onRequestSuccess(gameLogin);
                    }
                } else {
                    requestCallback.onRequestFailture(String.valueOf(response.code()), response.message());
                }
            }

            @Override
            public void onFailure(Call<ResultModel<GameLogin>> call, Throwable t) {
                t.printStackTrace();
                requestCallback.onRequestFailture("-1", "没有联网，请稍后重试");
            }
        });

    }

    public void onSendCode(HashMap<String, Object> requestDataMap, final RequestCallback<SendCode> requestCallback) {
        Call<ResultModel<SendCode>> call = apiService.sendCode(requestDataMap);
        call.enqueue(new Callback<ResultModel<SendCode>>() {
            @Override
            public void onResponse(Call<ResultModel<SendCode>> call, Response<ResultModel<SendCode>> response) {
                if (response != null && response.isSuccessful() && response.body() != null) {
                    try {
                        ResultModel resultModel = response.body();
                        SendCode sendCode = (SendCode) resultModel.getData();
                        String message = resultModel.getMessage();
                        if ("success".equalsIgnoreCase(message)) {
                            requestCallback.onRequestSuccess(sendCode);
                        } else {
                            if (sendCode != null) {
                                requestCallback.onRequestFailture(sendCode.getError_code() + "", sendCode.getDescription());
                            } else {
                                requestCallback.onRequestFailture(String.valueOf(response.code()), response.message());
                            }

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        requestCallback.onRequestFailture(String.valueOf(response.code()), e.getMessage());

                    }
                } else {
                    requestCallback.onRequestFailture(String.valueOf(response.code()), response.message());
                }
            }

            @Override
            public void onFailure(Call<ResultModel<SendCode>> call, Throwable t) {
                t.printStackTrace();
                requestCallback.onRequestFailture("-1", "没有联网，请稍后重试");
            }
        });
    }

    public void onQuickLogin(Map<String, Object> headDataMap, final RequestCallback<QuickLogin> requestCallback) {
        Call<ResultModel<QuickLogin>> call = apiService.quickLogin(headDataMap);
        call.enqueue(new Callback<ResultModel<QuickLogin>>() {
            @Override
            public void onResponse(Call<ResultModel<QuickLogin>> call, Response<ResultModel<QuickLogin>> response) {
                if (response != null && response.isSuccessful() && response.body() != null) {
                    ResultModel resultModel = response.body();
                    QuickLogin quickLogin = (QuickLogin) resultModel.getData();
                    String message = resultModel.getMessage();
                    if ("success".equalsIgnoreCase(message)) {
                        requestCallback.onRequestSuccess(quickLogin);
                    } else {
                        requestCallback.onRequestFailture(String.valueOf(quickLogin.getError_code()), quickLogin.getDescription());
                    }

                }
            }

            @Override
            public void onFailure(Call<ResultModel<QuickLogin>> call, Throwable t) {
                requestCallback.onRequestFailture("-1", "没有联网，请稍后重试");
            }
        });


    }

    public void onQueryOAuthCode(HashMap<String, Object> requestDataMap, final RequestCallback<Authorize> requestCallback) {
        Call<ResultModel<Authorize>> call = apiService.queryOAuthCode(requestDataMap);
        call.enqueue(new Callback<ResultModel<Authorize>>() {
            @Override
            public void onResponse(Call<ResultModel<Authorize>> call, Response<ResultModel<Authorize>> response) {
                if (response != null && response.isSuccessful() && response.body() != null) {
                    if (response != null && response.isSuccessful() && response.body() != null) {
                        Authorize authorize = response.body().getData();
                        String message = response.body().getMessage();
                        if ("success".equalsIgnoreCase(message)) {
                            requestCallback.onRequestSuccess(authorize);
                        } else {
                            requestCallback.onRequestFailture(String.valueOf(authorize.getError_code()), authorize.getDescription());
                        }

                    } else {
                        requestCallback.onRequestFailture(String.valueOf(response.code()), response.message());
                    }
                } else {
                    requestCallback.onRequestFailture(String.valueOf(response.code()), response.message());
                }
            }

            @Override
            public void onFailure(Call<ResultModel<Authorize>> call, Throwable t) {
                requestCallback.onRequestFailture("-1", "没有联网，请稍后重试");
            }
        });


    }

    public void onQueryToken(HashMap<String, Object> mDataMap, final RequestCallback<TokenModel> requestCallback) {
        Call<ResultModel<TokenModel>> call = apiService.queryToken(mDataMap);
        call.enqueue(new Callback<ResultModel<TokenModel>>() {
            @Override
            public void onResponse(Call<ResultModel<TokenModel>> call, Response<ResultModel<TokenModel>> response) {
                if (response != null && response.isSuccessful() && response.body() != null) {
                    if (response != null && response.isSuccessful() && response.body() != null) {
                        TokenModel tokenModel = response.body().getData();
                        String message = response.body().getMessage();
                        if ("success".equalsIgnoreCase(message)) {
                            requestCallback.onRequestSuccess(tokenModel);
                        } else {
                            requestCallback.onRequestFailture(String.valueOf(tokenModel.getError_code()), tokenModel.getDescription());
                        }

                    } else {
                        requestCallback.onRequestFailture(String.valueOf(response.code()), response.message());
                    }
                } else {
                    requestCallback.onRequestFailture(String.valueOf(response.code()), response.message());
                }
            }

            @Override
            public void onFailure(Call<ResultModel<TokenModel>> call, Throwable t) {
                requestCallback.onRequestFailture("-1", "没有联网，请稍后重试");
            }
        });
    }

    public void onHasVerified(HashMap<String, Object> mDataMap, final RequestCallback<Verified> requestCallback) {
        Call<ResultModel<Verified>> call = apiService.onHasVerified(mDataMap);
        call.enqueue(new Callback<ResultModel<Verified>>() {
            @Override
            public void onResponse(Call<ResultModel<Verified>> call, Response<ResultModel<Verified>> response) {
                if (response != null && response.isSuccessful() && response.body() != null) {
                    if (response != null && response.isSuccessful() && response.body() != null) {
                        Verified verified = response.body().getData();
                        String message = response.body().getMessage();
                        if ("success".equalsIgnoreCase(message)) {
                            requestCallback.onRequestSuccess(verified);
                        } else {
                            requestCallback.onRequestFailture(String.valueOf(verified.getError_code()), message);
                        }

                    } else {
                        requestCallback.onRequestFailture(String.valueOf(response.code()), response.message());
                    }
                } else {
                    requestCallback.onRequestFailture(String.valueOf(response.code()), response.message());
                }
            }

            @Override
            public void onFailure(Call<ResultModel<Verified>> call, Throwable t) {
                requestCallback.onRequestFailture("-1", "没有联网，请稍后重试");
            }
        });

    }

    public void onEnterGameForTicker(HashMap<String, Object> mDataMap, final RequestCallback<TicketModel> requestCallback) {
        Call<ResultModel<TicketModel>> call = apiService.enterGameForTicker(mDataMap);
        call.enqueue(new Callback<ResultModel<TicketModel>>() {
            @Override
            public void onResponse(Call<ResultModel<TicketModel>> call, Response<ResultModel<TicketModel>> response) {
                if (response != null && response.isSuccessful() && response.body() != null) {
                    String message = response.body().getMessage();
                    TicketModel ticketModel = response.body().getData();
                    if ("success".equalsIgnoreCase(message)) {
                        requestCallback.onRequestSuccess(ticketModel);

                    } else {
                        requestCallback.onRequestFailture(ticketModel.getError_code() + "", ticketModel.getDescription());

                    }

                } else {
                    requestCallback.onRequestFailture(response.code() + "", response.message());

                }
            }

            @Override
            public void onFailure(Call<ResultModel<TicketModel>> call, Throwable t) {
                requestCallback.onRequestFailture("-1", "没有联网，请稍后重试");
            }
        });

    }

    public void onEnterGameForCode(HashMap<String, Object> mHashMap, final RequestCallback<Authorize> requestCallback) {
        Call<ResultModel<Authorize>> call = apiService.enterGameForCode(mHashMap);
        call.enqueue(new Callback<ResultModel<Authorize>>() {
            @Override
            public void onResponse(Call<ResultModel<Authorize>> call, Response<ResultModel<Authorize>> response) {
                if (response != null && response.isSuccessful() && response.body() != null) {
                    String message = response.body().getMessage();
                    Authorize authorize = response.body().getData();
                    if ("success".equalsIgnoreCase(message)) {
                        requestCallback.onRequestSuccess(authorize);

                    } else {
                        requestCallback.onRequestFailture(authorize.getError_code() + "", authorize.getDescription());

                    }
                }
            }

            @Override
            public void onFailure(Call<ResultModel<Authorize>> call, Throwable t) {
                requestCallback.onRequestFailture("-1", "没有联网，请稍后重试");
            }
        });


    }

    public void onEnterGameForToken(HashMap<String, Object> mHashMap, final RequestCallback<TokenModel> requestCallback) {
        Call<ResultModel<TokenModel>> call = apiService.queryToken(mHashMap);
        call.enqueue(new Callback<ResultModel<TokenModel>>() {
            @Override
            public void onResponse(Call<ResultModel<TokenModel>> call, Response<ResultModel<TokenModel>> response) {
                if (response != null && response.isSuccessful() && response.body() != null) {
                    String message = response.body().getMessage();
                    TokenModel tokenModel = response.body().getData();
                    if ("success".equalsIgnoreCase(message)) {
                        requestCallback.onRequestSuccess(tokenModel);

                    } else {
                        requestCallback.onRequestFailture(tokenModel.getError_code() + "", tokenModel.getDescription());

                    }
                } else {
                    requestCallback.onRequestFailture(response.code() + "", response.message());
                }
            }

            @Override
            public void onFailure(Call<ResultModel<TokenModel>> call, Throwable t) {
                requestCallback.onRequestFailture("-1", "没有联网，请稍后重试");
            }
        });
    }

    public void onAutoLogin(HashMap<String, Object> mDataMap, final RequestCallback<AutoLogin> requestCallback) {
        Call<ResultModel<AutoLogin>> call = apiService.autoLogin(mDataMap);
        call.enqueue(new Callback<ResultModel<AutoLogin>>() {
            @Override
            public void onResponse(Call<ResultModel<AutoLogin>> call, Response<ResultModel<AutoLogin>> response) {
                if (response != null && response.isSuccessful() && response.body() != null) {
                    String message = response.body().getMessage();
                    AutoLogin autoLogin = response.body().getData();
                    if ("success".equalsIgnoreCase(message)) {
                        requestCallback.onRequestSuccess(autoLogin);

                    } else {
                        requestCallback.onRequestFailture(autoLogin.getError_code() + "", autoLogin.getDescription());

                    }
                } else {
                    requestCallback.onRequestFailture(response.code() + "", response.message());
                }
            }

            @Override
            public void onFailure(Call<ResultModel<AutoLogin>> call, Throwable t) {
                requestCallback.onRequestFailture("-1", "没有联网，请稍后重试");
            }
        });
    }

    //验证身份证
    public void onVerifyIdentity(HashMap<String, Object> mQueryMap, HashMap<String, Object> mSendMap, final RequestCallback requestCallback) {
        Call<ResponseBody> call = apiService.verifyIdentity(mQueryMap, mSendMap);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response != null && response.isSuccessful()) {
                    try {
                        String message = response.body().string();
                        if (TextUtils.isEmpty(message)) {
                            requestCallback.onRequestFailture(response.code() + "", "server return empty");
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(message);
                                String messageInfo = jsonObject.optString("message");
                                if ("success".equalsIgnoreCase(messageInfo)) {
                                    //成功
                                    requestCallback.onRequestSuccess(jsonObject.toString());
                                } else {
                                    JSONObject dataObject = jsonObject.optJSONObject("data");
                                    int error_code = 0;
                                    if (dataObject != null) {
                                        error_code = dataObject.optInt("error_code");
                                    }
                                    requestCallback.onRequestFailture(error_code + "", messageInfo);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                requestCallback.onRequestFailture(response.code() + "", e.getMessage());
                            }

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    requestCallback.onRequestFailture(response.code() + "", response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                requestCallback.onRequestFailture("-1", "没有联网，请稍后重试");
            }
        });

    }

    //创建订单
    public void onCreateOrder(HashMap<String, Object> mData, final RequestCallback<CreateOrder> requestCallback) {
        Call<ResultModel<CreateOrder>> call = apiService.createOrder(mData);
        call.enqueue(new Callback<ResultModel<CreateOrder>>() {
            @Override
            public void onResponse(Call<ResultModel<CreateOrder>> call, Response<ResultModel<CreateOrder>> response) {
                if (response != null && response.body() != null && response.isSuccessful()) {
                    try {
                        CreateOrder createOrder = response.body().getData();
                        requestCallback.onRequestSuccess(createOrder);
                    } catch (Exception e) {
                        e.printStackTrace();
                        requestCallback.onRequestFailture(response.code() + "", e.getMessage());
                    }
                } else {
                    requestCallback.onRequestFailture(response.code() + "", response.message());
                }

            }

            @Override
            public void onFailure(Call<ResultModel<CreateOrder>> call, Throwable t) {
                t.printStackTrace();
                requestCallback.onRequestFailture("-1", "没有联网，请稍后重试");
            }
        });


    }

    //创建订单
    public void onCreateOrderLast(HashMap<String, Object> requestHeader, HashMap<String, Object> mData, final RequestCallback<String> requestCallback) {
        Call<ResponseBody> call = apiService.createOrderLast(mData);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response != null && response.body() != null && response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        if (!TextUtils.isEmpty(result) && !"{}".equals(result)) {
                            JSONObject jsonObject = new JSONObject(result);
                            String message = jsonObject.optString("message");
                            if ("success".equalsIgnoreCase(message)) {
                                requestCallback.onRequestSuccess(result);
                            } else {
                                requestCallback.onRequestFailture(response.code() + "", MessageFormat.format("result={0}", result));
                            }

                        } else {
                            requestCallback.onRequestFailture(response.code() + "", MessageFormat.format("result={0}", result));

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        requestCallback.onRequestFailture(response.code() + "", e.getMessage());
                    }
                } else {
                    requestCallback.onRequestFailture(response.code() + "", response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                requestCallback.onRequestFailture("-1", "没有联网，请稍后重试");
            }
        });
    }

    public void onQueryPaymentResults(HashMap<String, Object> requestHeader, final RequestCallback<Boolean> requestCallback) {
        Call<ResultModel<Boolean>> call = apiService.queryPaymentResults(requestHeader);
        call.enqueue(new Callback<ResultModel<Boolean>>() {
            @Override
            public void onResponse(Call<ResultModel<Boolean>> call, Response<ResultModel<Boolean>> response) {
                if (response != null && response.body() != null && response.isSuccessful()) {
                    try {
                        boolean result = response.body().getData();
                        requestCallback.onRequestSuccess(result);
                    } catch (Exception e) {
                        e.printStackTrace();
                        requestCallback.onRequestFailture(response.code() + "", e.getMessage());
                    }
                } else {
                    requestCallback.onRequestFailture(response.code() + "", response.message());
                }
            }

            @Override
            public void onFailure(Call<ResultModel<Boolean>> call, Throwable t) {
                t.printStackTrace();
                requestCallback.onRequestFailture("-1", "没有联网，请稍后重试");
            }
        });
    }

    /**
     * 发送log日志信息
     */
    public void sendLog(Context context, JSONObject jsonObject) {
        okHttpBuilder.addInterceptor(new GzipRequestInterceptor()).build();//开启Gzip压缩
        retrofit = retrofitBuilder
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        if (jsonObject != null) {
            Map<String, Object> requestBody = JsonMap.getMap(jsonObject.toString());
            if (requestBody != null) {
                Map<String, RequestBody> requestBodyMap = RetrofitManager.getInstance().generateRequestBody(requestBody);
                if (requestBodyMap != null) {
                    RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
                    Call<ResponseBody> call = apiService.upLoadLog(UrlUtil.extMap(context, true), body);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                if (response != null && response.isSuccessful() && response.body() != null) {
                                    String result = response.body().string();
                                    try {
                                        JSONObject json = new JSONObject(result);
                                        String message = json.optString("message");
                                        if ("success".equalsIgnoreCase(message)) {
                                            LogUtil.e("日志发送成功，result={0}", result);
                                        } else {
                                            LogUtil.e("日志发送失败，errorCode={0}", response.code() + "");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        LogUtil.e("日志发送失败，errorCode={0}", response.code() + "");
                                    }

                                } else {
                                    LogUtil.e("日志发送失败，errorCode={0}", response.code() + "");
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                                LogUtil.e("日志发送结果解析失败，errorMsg={0}", e.getMessage());
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            LogUtil.e("日志发送失败，errorMsg={0}", t.getMessage());
                        }
                    });
                }
            }
        }
    }

    //比如可以这样生成Map<String, RequestBody> requestBodyMap
    //Map<String, String> requestDataMap这里面放置上传数据的键值对。
    private static Map<String, RequestBody> generateRequestBody(Map<String, Object> requestDataMap) {
        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        for (String key : requestDataMap.keySet()) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),
                    requestDataMap.get(key) == null ? "" : String.valueOf(requestDataMap.get(key)));
            requestBodyMap.put(key, requestBody);
        }
        return requestBodyMap;
    }

    private static void trustAllHosts() {
        // 创建信任管理器
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {

            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }
        }};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {

        public boolean verify(String hostname, SSLSession session) {
            return true;
        }    //将所有验证的结果都设为true
    };


}

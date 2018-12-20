package com.ss.android.game.sdk;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.ss.android.login.sdk.LoginCallback;
import com.ss.android.pay.PayCallback;
import com.ss.android.pay.bean.PayRequestData;

public class SsGameApi {
    public static String CLIENTID;
    public static String PAYKEY;

    //初始化方法
    public static void init(Context context, String clientId, String payKey) {
        CLIENTID = clientId;
        PAYKEY = payKey;
        if (context == null || TextUtils.isEmpty(clientId) || TextUtils.isEmpty(payKey)) {
            return;
        }
        GameSdk.init(context, clientId, payKey);
    }

    //登录方法
    public static void login(Activity activity, LoginCallback loginCallback) {
        if (activity == null) {
            return;
        }
        GameSdk.inst().login(activity, loginCallback);
    }

    //支付
    public static void pay(Activity activity, PayRequestData payRequestData, PayCallback payCallback) {
        GameSdk.inst().pay(activity, payRequestData, payCallback);
    }

    public static void onResume(Activity activity) {
        if (activity == null) {
            return;
        }
        GameSdk.inst().onResume(activity);

    }


}

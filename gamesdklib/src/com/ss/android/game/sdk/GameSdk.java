package com.ss.android.game.sdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.ss.android.gamecommon.applog.BaseAppLog;
import com.ss.android.gamecommon.thread.SessionThread;
import com.ss.android.gamecommon.thread.ThreadPoolManager;
import com.ss.android.gamecommon.util.LogUtil;
import com.ss.android.gamecommon.util.LoginDataUtil;
import com.ss.android.gamecommon.util.SharedPreferenceUtil;
import com.ss.android.gamecommon.util.ToastUtil;
import com.ss.android.login.sdk.LoginCallback;
import com.ss.android.login.sdk.activity.MobileActivity;
import com.ss.android.pay.PayCallback;
import com.ss.android.pay.SSPayManager;
import com.ss.android.pay.VerifyIdentityCallback;
import com.ss.android.pay.bean.PayRequestData;

import static com.ss.android.gamecommon.util.ConstantUtil.LOGIN_FAIL_BECAUSE_ACTIVITY_IS_NULL;
import static com.ss.android.gamecommon.util.ConstantUtil.PAY_FAIL_NOT_LOGIN;

public class GameSdk {
    private static GameSdk sInstance;
    private static Activity loginActivity;
    public static String clientId;
    public static String payKey;

    public static GameSdk getInstance(Context context, String clientId, String payKey) {
        if (sInstance == null) {
            synchronized (GameSdk.class) {
                if (sInstance == null) {
                    sInstance = new GameSdk(context, clientId, payKey);
                }

            }
        }
        return sInstance;
    }


    public static void init(Context context, String clientId, String payKey) {
        BaseAppLog.getInstance(context);
        getInstance(context, clientId, payKey);

    }

    public static GameSdk inst() {
        if (sInstance == null) {
            throw new IllegalArgumentException("GameSdk not init");
        }
        return sInstance;
    }

    private GameSdk(Context context, String clientId, String payKey) {
        GameSdk.clientId = clientId;
        GameSdk.payKey = payKey;
    }

    //登录
    public void login(Activity activity, LoginCallback loginCallback) {
        //需要判断是否需要自动登录
        if (activity == null) {
            loginCallback.onLoginFail(LOGIN_FAIL_BECAUSE_ACTIVITY_IS_NULL, "activity is null");
            return;
        } else {
            loginActivity = activity;
        }
        boolean isAutoLogin = LoginDataUtil.isAutoLogin(activity.getApplicationContext());

        if (isAutoLogin) {
            //需要自动登录
            MobileActivity.startAutoLogin(activity, loginCallback);

        } else {
            //展示用户选择登录方式ui
            MobileActivity.showLoginBySelectUi(activity, loginCallback);

        }

    }

    public void onResume(Activity activity) {
        //开启一个初始化session的子线程
        long timestamp = System.currentTimeMillis();
        ThreadPoolManager.getInstance().executor(new SessionThread(timestamp, activity.getApplicationContext(), null));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    public void pay(final Activity activity, final PayRequestData payRequestData, final PayCallback payCallback) {
        SSPayManager.getInstance(activity).startPay(activity, payRequestData, payCallback);
    }
}

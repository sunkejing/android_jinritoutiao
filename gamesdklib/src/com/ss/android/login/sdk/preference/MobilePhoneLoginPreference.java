package com.ss.android.login.sdk.preference;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.ss.android.gamecommon.model.TokenModel;

public interface MobilePhoneLoginPreference {
    /**
     * 获取验证码的接口
     *
     * @param context
     * @param phoneNum
     */
    void sendCode(Context context, String phoneNum);

    /**
     * 验证码验证接口
     *
     * @param context
     * @param phoneNum
     * @param code
     */
    void quickLogin(Context context, String phoneNum, String code);

    void queryOAuthCode(Context context);

    //请求token
    void queryToken(Context context, String code);

    //登录游戏
    void gameLogin(Context context, TokenModel tokenModel);

}


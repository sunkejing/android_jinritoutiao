package com.ss.android.login.sdk.preference;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.ss.android.gamecommon.model.TokenModel;

public interface BindPhoneNumberPreference {
    /**
     * 获取验证码的接口
     *
     * @param context
     * @param phoneNum
     */
    void sendCode(Context context, String phoneNum);

    /**
     * 对验证码进行验证的接口
     *
     * @param context
     * @param phoneNum
     * @param code
     */
    void quickLogin(Context context, String phoneNum, String code);

    void queryOAuthCode(Context context);

    void queryToken(Context context, String code);

    void gameLogin(Context context, TokenModel tokenModel);

}

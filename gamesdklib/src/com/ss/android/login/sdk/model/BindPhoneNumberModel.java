package com.ss.android.login.sdk.model;

import android.content.Context;

import com.ss.android.gamecommon.model.Authorize;
import com.ss.android.gamecommon.model.GameLogin;
import com.ss.android.gamecommon.model.QuickLogin;
import com.ss.android.gamecommon.model.SendCode;
import com.ss.android.gamecommon.model.TokenModel;
import com.ss.android.gamecommon.model.Verified;

public interface BindPhoneNumberModel {

    void onSendCode(Context context, String phoneNum, String type, RequestCallback<SendCode> requestCallback);

    void onQuickLogin(Context context, String phoneNum, String code, String type, RequestCallback<QuickLogin> requestCallback);

    void onQueryOAuthCode(Context context, RequestCallback<Authorize> requestCallback);

    /**
     * 查询token
     *
     * @param context
     * @param code
     * @param requestCallback
     */
    void onQueryToken(Context context, String code, RequestCallback<TokenModel> requestCallback);

    void onGameLogin(Context context, TokenModel tokenModel, RequestCallback<GameLogin> requestCallback);

}

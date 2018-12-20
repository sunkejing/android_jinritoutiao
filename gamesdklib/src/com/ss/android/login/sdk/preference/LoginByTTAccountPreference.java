package com.ss.android.login.sdk.preference;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.ss.android.gamecommon.model.TokenModel;

public interface LoginByTTAccountPreference {
    void enterGameForTicker(Context context, String account, String pwd);

    void enterGameForCode(Context context);

    void enterGameForToken(Context context, String code);

    void onGameLogin(Context context, TokenModel tokenModel);

}

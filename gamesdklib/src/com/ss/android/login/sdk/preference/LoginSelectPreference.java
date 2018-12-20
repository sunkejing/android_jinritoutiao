package com.ss.android.login.sdk.preference;

import android.content.Context;

import com.ss.android.gamecommon.model.VisitorLogin;

public interface LoginSelectPreference {

    void hasBound(Context context);

    //游客登录
    void visitorLogin(Context context);

    void gameLogin(Context context, VisitorLogin visitorLogin);
}

package com.ss.android.login.sdk.model;

import android.content.Context;

import com.ss.android.gamecommon.model.GameLogin;
import com.ss.android.gamecommon.model.VisitorLogin;

public interface LoginSelectModel {
    void hasBoundAccount(Context context, RequestCallback<Integer> requestCallback);

    void visitorLogin(Context context, RequestCallback<VisitorLogin> requestCallback);

    void gameLogin(Context context, VisitorLogin visitorLogin, RequestCallback<GameLogin> requestCallback);
}

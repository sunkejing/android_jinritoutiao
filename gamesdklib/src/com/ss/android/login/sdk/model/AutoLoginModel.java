package com.ss.android.login.sdk.model;

import android.content.Context;

import com.ss.android.gamecommon.model.AutoLogin;
import com.ss.android.gamecommon.model.Verified;

public interface AutoLoginModel {
    void onAutoLogin(Context context, String loginId, RequestCallback<AutoLogin> requestCallback);

}

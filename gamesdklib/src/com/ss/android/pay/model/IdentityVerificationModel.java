package com.ss.android.pay.model;

import android.content.Context;

import com.ss.android.game.sdk.info.UserInfo;
import com.ss.android.login.sdk.model.RequestCallback;

public interface IdentityVerificationModel {
    void doVerifyIdentity(Context context, String userName, String idCrad, UserInfo userInfo, RequestCallback<String> requestCallback);
}

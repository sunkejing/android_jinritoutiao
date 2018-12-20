package com.ss.android.login.sdk;

public interface LoginCallback {
    void onLoginSuccess(String accessToken, long uid, String openId, int userType);

    void onLoginFail(int errorCode, String errorMsg);

}

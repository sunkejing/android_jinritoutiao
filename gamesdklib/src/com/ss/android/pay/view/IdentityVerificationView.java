package com.ss.android.pay.view;

public interface IdentityVerificationView {
    //验证成功
    void onVerifyIdentitySuccess();

    //验证失败
    void onVerifyIdentityFailture(String errorCode, String errorMessage);
}

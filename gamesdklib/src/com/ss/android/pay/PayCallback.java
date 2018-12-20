package com.ss.android.pay;

public interface PayCallback {
    void onPaySuccess(int errorCode, String errorMsg);

    void onPayCancel(int errorCode, String errorMsg);

    void onPayFail(int errorCode, String errorMsg);
}

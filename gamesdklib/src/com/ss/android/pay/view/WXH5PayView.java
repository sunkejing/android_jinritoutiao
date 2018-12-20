package com.ss.android.pay.view;

public interface WXH5PayView {
    public void onQueryPaymentResultSuccess(Boolean isSuccess);

    void onQueryPaymentResultFailture(String errorCode, String message);
}

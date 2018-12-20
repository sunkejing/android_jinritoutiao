package com.ss.android.pay.view;

public interface PaymentSelectView {
    void onCreateOrderSuccess(String jsonString);

    void onCreateOrderFailture(String errorCode, String message);
}

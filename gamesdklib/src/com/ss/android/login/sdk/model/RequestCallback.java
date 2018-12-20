package com.ss.android.login.sdk.model;

public interface RequestCallback<T> {
    void onRequestSuccess(T t);

    void onRequestFailture(String errorCode, String message);

}

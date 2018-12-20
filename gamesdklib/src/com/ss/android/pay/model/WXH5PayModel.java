package com.ss.android.pay.model;

import android.content.Context;

import com.ss.android.login.sdk.model.RequestCallback;

import java.util.HashMap;

public interface WXH5PayModel {
    void onQueryPaymentResults(Context context, HashMap<String, Object> map, RequestCallback<Boolean> requestCallback);
}

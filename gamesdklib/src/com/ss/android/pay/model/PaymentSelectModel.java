package com.ss.android.pay.model;

import android.content.Context;

import com.ss.android.gamecommon.model.CreateOrder;
import com.ss.android.login.sdk.model.RequestCallback;
import com.ss.android.pay.bean.PayRequestData;

import java.util.HashMap;

public interface PaymentSelectModel {
    void onCreateOrder(Context context, HashMap<String, Object> map, RequestCallback<String> requestCallback);
}

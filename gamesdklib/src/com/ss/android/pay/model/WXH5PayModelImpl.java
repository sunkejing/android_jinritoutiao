package com.ss.android.pay.model;

import android.content.Context;

import com.ss.android.gamecommon.thread.RetrofitManager;
import com.ss.android.gamecommon.util.UrlUtil;
import com.ss.android.login.sdk.model.RequestCallback;

import java.util.HashMap;

public class WXH5PayModelImpl implements WXH5PayModel {
    @Override
    public void onQueryPaymentResults(Context context,HashMap<String, Object> map, final RequestCallback<Boolean> requestCallback) {
        HashMap<String, Object> data = new HashMap<>();
        data.putAll(map);
        data.putAll(UrlUtil.extMap(context,false));
        RetrofitManager.getInstance().onQueryPaymentResults(map, new RequestCallback<Boolean>() {
            @Override
            public void onRequestSuccess(Boolean aBoolean) {
                requestCallback.onRequestSuccess(aBoolean);
            }

            @Override
            public void onRequestFailture(String errorCode, String message) {
                requestCallback.onRequestFailture(errorCode, message);
            }
        });

    }
}

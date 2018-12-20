package com.ss.android.pay.model;

import android.content.Context;

import com.ss.android.gamecommon.thread.RetrofitManager;
import com.ss.android.gamecommon.util.UrlUtil;
import com.ss.android.login.sdk.model.RequestCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class PaymentSelectModelImpl implements PaymentSelectModel {
    @Override
    public void onCreateOrder(Context context, HashMap<String, Object> map, final RequestCallback<String> requestCallback) {
        HashMap<String, Object> data = new HashMap<>();
        data.putAll(map);
        data.putAll(UrlUtil.extMap(context, true));
        RetrofitManager.getInstance().onCreateOrderLast(map, data, new RequestCallback<String>() {
            @Override
            public void onRequestSuccess(String resultData) {
                try {
                    JSONObject jsonObject = new JSONObject(resultData);
                    JSONObject data = jsonObject.optJSONObject("data");
                    if (data != null) {
                        requestCallback.onRequestSuccess(data.toString());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onRequestFailture(String errorCode, String message) {
                requestCallback.onRequestFailture(errorCode, message);
            }
        });
    }
}

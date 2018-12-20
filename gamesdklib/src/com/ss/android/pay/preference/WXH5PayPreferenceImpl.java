package com.ss.android.pay.preference;

import android.content.Context;

import com.ss.android.login.sdk.model.RequestCallback;
import com.ss.android.pay.model.WXH5PayModel;
import com.ss.android.pay.model.WXH5PayModelImpl;
import com.ss.android.pay.view.WXH5PayView;

import java.util.HashMap;

public class WXH5PayPreferenceImpl implements WXH5PayPreference {
    private WXH5PayView wxh5PayView;
    private WXH5PayModel wxh5PayModel;

    public WXH5PayPreferenceImpl(WXH5PayView wxh5PayView) {
        this.wxh5PayView = wxh5PayView;
        this.wxh5PayModel = new WXH5PayModelImpl();
    }

    @Override
    public void queryPaymentResults(Context context, HashMap<String, Object> map) {
        wxh5PayModel.onQueryPaymentResults(context, map, new RequestCallback<Boolean>() {

            @Override
            public void onRequestSuccess(Boolean object) {
                wxh5PayView.onQueryPaymentResultSuccess(object);

            }

            @Override
            public void onRequestFailture(String errorCode, String message) {
                wxh5PayView.onQueryPaymentResultFailture(errorCode, message);
            }
        });
    }
}

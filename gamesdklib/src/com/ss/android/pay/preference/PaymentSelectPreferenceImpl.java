package com.ss.android.pay.preference;

import android.content.Context;

import com.ss.android.login.sdk.model.RequestCallback;
import com.ss.android.pay.model.PaymentSelectModel;
import com.ss.android.pay.model.PaymentSelectModelImpl;
import com.ss.android.pay.view.PaymentSelectView;

import java.util.HashMap;

public class PaymentSelectPreferenceImpl implements PaymentSelectPreference {
    private PaymentSelectView paymentSelectView;
    private PaymentSelectModel paymentSelectModel;

    public PaymentSelectPreferenceImpl(PaymentSelectView paymentSelectView) {
        this.paymentSelectView = paymentSelectView;
        this.paymentSelectModel = new PaymentSelectModelImpl();

    }

    public void createOrder(Context context, HashMap<String, Object> map) {
        paymentSelectModel.onCreateOrder(context, map, new RequestCallback<String>() {
            @Override
            public void onRequestSuccess(String jsonString) {
                paymentSelectView.onCreateOrderSuccess(jsonString);
            }

            @Override
            public void onRequestFailture(String errorCode, String message) {
                paymentSelectView.onCreateOrderFailture(errorCode, message);
            }
        });
    }

}

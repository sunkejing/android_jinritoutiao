package com.ss.android.pay.preference;

import android.content.Context;

import com.ss.android.gamecommon.model.CreateOrder;
import com.ss.android.pay.bean.PayRequestData;

import java.util.HashMap;

public interface PaymentSelectPreference {
    public void createOrder(Context context, HashMap<String, Object> map);

}

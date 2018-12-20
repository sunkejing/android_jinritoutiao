package com.ss.android.pay.preference;

import android.content.Context;

import java.util.HashMap;

public interface WXH5PayPreference {
    void queryPaymentResults(Context context, HashMap<String, Object> map);
}

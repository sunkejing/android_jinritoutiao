package com.ss.android.pay.preference;

import android.content.Context;

import com.ss.android.game.sdk.info.UserInfo;
import com.ss.android.pay.bean.PayRequestData;

public interface CreateOrderPreference {
    public void onCreateOrder(Context context, PayRequestData payRequestData, UserInfo userInfo);

}

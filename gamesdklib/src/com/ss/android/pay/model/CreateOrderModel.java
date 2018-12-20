package com.ss.android.pay.model;

import android.content.Context;

import com.ss.android.game.sdk.info.UserInfo;
import com.ss.android.gamecommon.model.CreateOrder;
import com.ss.android.login.sdk.model.RequestCallback;
import com.ss.android.pay.bean.PayRequestData;

public interface CreateOrderModel {
    void createOrder(Context context, PayRequestData payRequestData, UserInfo userInfo, RequestCallback<CreateOrder> requestCallback);
}

package com.ss.android.pay.view;

import com.ss.android.gamecommon.model.CreateOrder;

public interface CreateOrderView {
    void createOrderSuccess(CreateOrder createOrder);

    void createOrderFailture(String errorCode, String errorMessage);
}

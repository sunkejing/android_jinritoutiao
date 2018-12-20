package com.ss.android.pay.preference;

import android.content.Context;

import com.ss.android.game.sdk.info.UserInfo;
import com.ss.android.gamecommon.model.CreateOrder;
import com.ss.android.login.sdk.model.RequestCallback;
import com.ss.android.pay.bean.PayRequestData;
import com.ss.android.pay.model.CreateOrderModel;
import com.ss.android.pay.model.CreateOrderModelImpl;
import com.ss.android.pay.view.CreateOrderView;

public class CreateOrderPreferenceImpl implements CreateOrderPreference {
    private CreateOrderModel createOrderModel;
    private CreateOrderView createOrderView;

    public CreateOrderPreferenceImpl(CreateOrderView createOrderView) {
        this.createOrderView = createOrderView;
        this.createOrderModel = new CreateOrderModelImpl();
    }

    @Override
    public void onCreateOrder(Context context, PayRequestData payRequestData, UserInfo userInfo) {
        createOrderModel.createOrder(context, payRequestData, userInfo, new RequestCallback<CreateOrder>() {


            @Override
            public void onRequestSuccess(CreateOrder createOrder) {
                createOrderView.createOrderSuccess(createOrder);
            }

            @Override
            public void onRequestFailture(String errorCode, String errorMessage) {
                createOrderView.createOrderFailture(errorCode, errorMessage);
            }
        });


    }
}

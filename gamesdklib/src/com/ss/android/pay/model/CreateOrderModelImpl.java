package com.ss.android.pay.model;

import android.content.Context;

import com.ss.android.game.sdk.GameSdk;
import com.ss.android.game.sdk.info.UserInfo;
import com.ss.android.gamecommon.model.CreateOrder;
import com.ss.android.gamecommon.thread.RetrofitManager;
import com.ss.android.gamecommon.util.NumberEncryption;
import com.ss.android.gamecommon.util.UrlUtil;
import com.ss.android.login.sdk.model.RequestCallback;
import com.ss.android.pay.bean.PayRequestData;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CreateOrderModelImpl implements CreateOrderModel {
    @Override
    public void createOrder(Context context, PayRequestData payRequestData, UserInfo userInfo, final RequestCallback<CreateOrder> requestCallback) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            map.put("access_token", userInfo.getAccessToken());
            map.put("client_id", GameSdk.clientId);
            map.put("out_trade_no", payRequestData.getOutTradeNo());
            map.put("subject", payRequestData.getProductName());
            map.put("total_fee", payRequestData.getProductPrice());
            JSONObject jsonObject = new JSONObject(map);
            final String sign = sign(map);
            jsonObject.put("sign", sign);
            jsonObject.put("sign_type", "MD5");
            HashMap<String, Object> mData = new HashMap<String, Object>();
            mData.put("user_type", userInfo.getUserType());
            mData.put("uid", 7);
            mData.put("content", jsonObject.toString());
            mData.put("biz_id", 10);
            mData.putAll(UrlUtil.extMap(context, true));
            RetrofitManager.getInstance().onCreateOrder(mData, new RequestCallback<CreateOrder>() {

                @Override
                public void onRequestSuccess(CreateOrder createOrder) {
                    try {
                        requestCallback.onRequestSuccess(createOrder);
                    } catch (Exception e) {
                        e.printStackTrace();
                        requestCallback.onRequestFailture(createOrder.getPrepay_id(), e.getMessage());
                    }


                }

                @Override
                public void onRequestFailture(String errorCode, String errorMessage) {
                    requestCallback.onRequestFailture(errorCode, errorMessage);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String sign(HashMap<String, Object> map) {
        StringBuffer content = new StringBuffer();
        // 按照key做首字母升序排列
        List<String> keys = new ArrayList<String>(map.keySet());
        Collections.sort(keys, String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i).toString();
            String value = map.get(key).toString();
            // 空串不参与签名
            content.append((i == 0 ? "" : "&") + key + "=" + value);
        }
        content.append("&key=").append(GameSdk.payKey);
        String unSign = content.toString();
        String sign = NumberEncryption.md5(unSign);
        return sign;

    }
}

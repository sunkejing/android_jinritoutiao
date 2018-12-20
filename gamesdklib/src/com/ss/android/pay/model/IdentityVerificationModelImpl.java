package com.ss.android.pay.model;

import android.content.Context;
import android.text.TextUtils;

import com.ss.android.game.sdk.info.UserInfo;
import com.ss.android.gamecommon.thread.RetrofitManager;
import com.ss.android.gamecommon.util.UrlUtil;
import com.ss.android.login.sdk.model.RequestCallback;

import java.util.HashMap;

public class IdentityVerificationModelImpl implements IdentityVerificationModel {
    @Override
    public void doVerifyIdentity(Context context, String userName, String idCrad, UserInfo userInfo, final RequestCallback<String> requestCallback) {
        HashMap<String, Object> mQueryMap = new HashMap<String, Object>();
        mQueryMap.put("id_no", idCrad);
        mQueryMap.put("id_name", userName);
        mQueryMap.put("open_id", userInfo.getOpenId());
        mQueryMap.put("user_type", userInfo.getUserType());
        if (!TextUtils.isEmpty(userInfo.getAccessToken())) {
            mQueryMap.put("token", userInfo.getAccessToken());
        }
        HashMap<String, Object> allData = new HashMap<>();
        allData.putAll(mQueryMap);
        allData.putAll(UrlUtil.extMap(context, true));
        RetrofitManager.getInstance().onVerifyIdentity(mQueryMap, allData, new RequestCallback<String>() {
            @Override
            public void onRequestSuccess(String object) {
                requestCallback.onRequestSuccess(object);
            }

            @Override
            public void onRequestFailture(String errorCode, String message) {
                requestCallback.onRequestFailture(errorCode, message);
            }
        });
    }
}

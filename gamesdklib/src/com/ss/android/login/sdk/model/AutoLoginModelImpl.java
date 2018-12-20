package com.ss.android.login.sdk.model;

import android.content.Context;

import com.ss.android.gamecommon.model.AutoLogin;
import com.ss.android.gamecommon.model.SendCode;
import com.ss.android.gamecommon.model.Verified;
import com.ss.android.gamecommon.thread.RetrofitManager;
import com.ss.android.gamecommon.util.UrlUtil;

import java.util.HashMap;

public class AutoLoginModelImpl implements AutoLoginModel {
    @Override
    public void onAutoLogin(Context context, String loginId, final RequestCallback<AutoLogin> requestCallback) {
        HashMap<String, Object> mHashMap = new HashMap<String, Object>();
        mHashMap.put("login_id", loginId);
        HashMap<String, Object> mDataMap = new HashMap<String, Object>();
        mDataMap.putAll(mHashMap);
        mDataMap.putAll(UrlUtil.extMap(context, true));
        RetrofitManager.getInstance().onAutoLogin(mDataMap, new RequestCallback<AutoLogin>() {
            @Override
            public void onRequestSuccess(AutoLogin sendCode) {
                requestCallback.onRequestSuccess(sendCode);
            }

            @Override
            public void onRequestFailture(String errorCode, String message) {
                requestCallback.onRequestFailture(errorCode, message);
            }
        });
    }

}

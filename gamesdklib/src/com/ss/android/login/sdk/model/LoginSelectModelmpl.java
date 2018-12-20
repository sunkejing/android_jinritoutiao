package com.ss.android.login.sdk.model;

import android.content.Context;

import com.ss.android.gamecommon.model.GameLogin;
import com.ss.android.gamecommon.model.VisitorLogin;
import com.ss.android.gamecommon.thread.RetrofitManager;
import com.ss.android.gamecommon.util.UrlUtil;

import org.json.JSONObject;

import java.util.HashMap;

public class LoginSelectModelmpl implements LoginSelectModel {
    @Override
    public void hasBoundAccount(Context context, final RequestCallback<Integer> requestCallback) {
        //
        RetrofitManager.getInstance().hasBound(UrlUtil.extMap(context, false), new RequestCallback<JSONObject>() {
            @Override
            public void onRequestSuccess(JSONObject jsonObject) {
                int hasBound = jsonObject.optInt("has_bound");
                requestCallback.onRequestSuccess(hasBound);
            }

            @Override
            public void onRequestFailture(String errorCode, String message) {
                requestCallback.onRequestFailture(errorCode, message);
            }
        });
    }

    @Override
    public void visitorLogin(Context context, final RequestCallback<VisitorLogin> requestCallback) {

        RetrofitManager.getInstance().onVisitorLogin(UrlUtil.extMap(context, true), new RequestCallback<VisitorLogin>() {
            @Override
            public void onRequestSuccess(VisitorLogin visitorLogin) {
                if (visitorLogin != null) {
                    if (visitorLogin.getError_code() == 0) {
                        requestCallback.onRequestSuccess(visitorLogin);
                    } else {
                        requestCallback.onRequestFailture(String.valueOf(visitorLogin.getError_code()), "visitorLogin接口验证失败");
                    }
                }
            }

            @Override
            public void onRequestFailture(String errorCode, String message) {
                requestCallback.onRequestFailture(errorCode, message);
            }
        });
    }

    @Override
    public void gameLogin(Context context, VisitorLogin visitorLogin, final RequestCallback<GameLogin> requestCallback) {
        HashMap<String, Object> mHashMap = new HashMap<String, Object>();
        mHashMap.put("is_bound", 0);
        mHashMap.put("open_id", visitorLogin.getOpen_id());
        mHashMap.put("user_type", visitorLogin.getUser_type());
        mHashMap.put("user_id", 0);

        HashMap<String, Object> mDataMap = new HashMap<String, Object>();
        mDataMap.putAll(mHashMap);
        mDataMap.putAll(UrlUtil.extMap(context, true));
        RetrofitManager.getInstance().onGameLogin(mDataMap, new RequestCallback<GameLogin>() {
            @Override
            public void onRequestSuccess(GameLogin gameLogin) {
                if (gameLogin != null) {
                    if (gameLogin.getError_code() == 0) {
                        requestCallback.onRequestSuccess(gameLogin);
                    } else {
                        requestCallback.onRequestFailture(String.valueOf(gameLogin.getError_code()), "gameLogin接口验证失败");
                    }
                }
            }

            @Override
            public void onRequestFailture(String errorCode, String message) {
                requestCallback.onRequestFailture(errorCode, message);
            }
        });
    }
}

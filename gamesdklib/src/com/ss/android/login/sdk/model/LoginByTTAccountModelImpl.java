package com.ss.android.login.sdk.model;

import android.content.Context;

import com.ss.android.game.sdk.GameSdk;
import com.ss.android.gamecommon.model.Authorize;
import com.ss.android.gamecommon.model.GameLogin;
import com.ss.android.gamecommon.model.TicketModel;
import com.ss.android.gamecommon.model.TokenModel;
import com.ss.android.gamecommon.model.Verified;
import com.ss.android.gamecommon.thread.RetrofitManager;
import com.ss.android.gamecommon.util.NumberEncryption;
import com.ss.android.gamecommon.util.UrlUtil;

import java.util.HashMap;

public class LoginByTTAccountModelImpl implements LoginByTTAccountModel {
    @Override
    public void onEnterGameForTicker(Context context, String account, String pwd, final RequestCallback<TicketModel> requestCallback) {
        HashMap<String, Object> mHashMap = new HashMap<String, Object>();
        mHashMap.put("mix_mode", "1");
        mHashMap.put("from", "app");
        mHashMap.put("scope", "user_info");
        mHashMap.put("password", NumberEncryption.xorEncryption(pwd, "5"));
        mHashMap.put("mobile", NumberEncryption.xorEncryption(account, "5"));
        HashMap<String, Object> mDataMap = new HashMap<String, Object>();
        mDataMap.putAll(mHashMap);
        mDataMap.putAll(UrlUtil.extMap(context, true));
        RetrofitManager.getInstance().onEnterGameForTicker(mDataMap, new RequestCallback<TicketModel>() {
            @Override
            public void onRequestSuccess(TicketModel ticketModel) {
                requestCallback.onRequestSuccess(ticketModel);
            }

            @Override
            public void onRequestFailture(String errorCode, String message) {
                requestCallback.onRequestFailture(errorCode, message);
            }
        });
    }

    @Override
    public void onEnterGameForCode(Context context, final RequestCallback<Authorize> requestCallback) {
        HashMap<String, Object> mHashMap = new HashMap<String, Object>();
        mHashMap.put("scope", "user_info");
        mHashMap.put("response_type", "code");
        mHashMap.put("client_key", GameSdk.clientId);
        RetrofitManager.getInstance().onEnterGameForCode(mHashMap, new RequestCallback<Authorize>() {

            @Override
            public void onRequestSuccess(Authorize authorize) {
                requestCallback.onRequestSuccess(authorize);

            }

            @Override
            public void onRequestFailture(String errorCode, String message) {
                requestCallback.onRequestFailture(errorCode, message);
            }
        });
    }

    @Override
    public void onEnterGameForToken(Context context, String code, final RequestCallback<TokenModel> requestCallback) {
        HashMap<String, Object> mHashMap = new HashMap<String, Object>();
        mHashMap.put("grant_type", "authorization_code");
        mHashMap.put("code", code);
        mHashMap.put("client_key", GameSdk.clientId);
        HashMap<String, Object> mDataMap = new HashMap<String, Object>();
        mDataMap.putAll(mHashMap);
        mDataMap.putAll(UrlUtil.extMap(context, true));

        RetrofitManager.getInstance().onEnterGameForToken(mDataMap, new RequestCallback<TokenModel>() {

            @Override
            public void onRequestSuccess(TokenModel tokenModel) {
                requestCallback.onRequestSuccess(tokenModel);
            }

            @Override
            public void onRequestFailture(String errorCode, String message) {
                requestCallback.onRequestFailture(errorCode, message);
            }
        });

    }

    @Override
    public void onGameLogin(Context context, TokenModel tokenModel, final RequestCallback<GameLogin> requestCallback) {
        HashMap<String, Object> mHashMap = new HashMap<String, Object>();
        mHashMap.put("user_id", tokenModel.getUser_id());
        mHashMap.put("user_type", 12);
        mHashMap.put("open_id", tokenModel.getOpen_id());
        mHashMap.put("is_bound", 0);
        mHashMap.put("access_token", tokenModel.getAccess_token());
        HashMap<String, Object> mDataMap = new HashMap<String, Object>();
        mDataMap.putAll(mHashMap);
        mDataMap.putAll(UrlUtil.extMap(context, true));

        RetrofitManager.getInstance().onGameLogin(mDataMap, new RequestCallback<GameLogin>() {
            @Override
            public void onRequestSuccess(GameLogin gameLogin) {
                requestCallback.onRequestSuccess(gameLogin);
            }

            @Override
            public void onRequestFailture(String errorCode, String message) {
                requestCallback.onRequestFailture(errorCode, message);
            }
        });


    }

}

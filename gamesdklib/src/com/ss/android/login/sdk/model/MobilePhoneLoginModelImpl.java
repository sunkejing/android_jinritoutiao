package com.ss.android.login.sdk.model;

import android.content.Context;

import com.ss.android.game.sdk.GameSdk;
import com.ss.android.gamecommon.model.Authorize;
import com.ss.android.gamecommon.model.GameLogin;
import com.ss.android.gamecommon.model.QuickLogin;
import com.ss.android.gamecommon.model.SendCode;
import com.ss.android.gamecommon.model.TokenModel;
import com.ss.android.gamecommon.model.Verified;
import com.ss.android.gamecommon.thread.RetrofitManager;
import com.ss.android.gamecommon.util.UrlUtil;

import java.util.HashMap;

import static com.ss.android.gamecommon.util.ConstantUtil.ACCOUNT_NUMBER;


public class MobilePhoneLoginModelImpl implements MobilePhoneLoginModel {
    @Override
    public void onSendCode(Context context, String phoneNum, String type, final RequestCallback<SendCode> requestCallback) {
        HashMap<String, Object> mHashMap = new HashMap<String, Object>();
        mHashMap.put("mix_mode", "1");
        mHashMap.put("type", type);
        mHashMap.put("mobile", phoneNum);
        HashMap<String, Object> mDataMap = new HashMap<String, Object>();
        mDataMap.putAll(mHashMap);
        mDataMap.putAll(UrlUtil.extMap(context, true));
        RetrofitManager.getInstance().onSendCode(mDataMap, new RequestCallback<SendCode>() {
            @Override
            public void onRequestSuccess(SendCode sendCode) {
                requestCallback.onRequestSuccess(sendCode);
            }

            @Override
            public void onRequestFailture(String errorCode, String message) {
                requestCallback.onRequestFailture(errorCode, message);
            }
        });
    }

    @Override
    public void onQuickLogin(Context context, String phoneNum, String code, String type, final RequestCallback<QuickLogin> requestCallback) {
        HashMap<String, Object> mHashMap = new HashMap<String, Object>();
        mHashMap.put("mix_mode", "1");
        mHashMap.put("from", "app");
        mHashMap.put("scope", "user_info");
        mHashMap.put("code", code);
        mHashMap.put("type", type);
        mHashMap.put("mobile", phoneNum);

        RetrofitManager.getInstance().onQuickLogin(mHashMap, new RequestCallback<QuickLogin>() {
            @Override
            public void onRequestSuccess(QuickLogin sendCode) {
                requestCallback.onRequestSuccess(sendCode);
            }

            @Override
            public void onRequestFailture(String errorCode, String message) {
                requestCallback.onRequestFailture(errorCode, message);
            }
        });
    }

    @Override
    public void onQueryOAuthCode(final Context context, final RequestCallback<Authorize> requestCallback) {
        HashMap<String, Object> mHashMap = new HashMap<String, Object>();
        mHashMap.put("scope", "user_info");
        mHashMap.put("response_type", "code");
        mHashMap.put("client_key", GameSdk.clientId);
        HashMap<String, Object> mDataMap = new HashMap<String, Object>();
        mDataMap.putAll(mHashMap);
        mDataMap.putAll(UrlUtil.extMap(context, true));

        RetrofitManager.getInstance().onQueryOAuthCode(mDataMap, new RequestCallback<Authorize>() {

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
    public void onQueryToken(Context context, String code, final RequestCallback<TokenModel> requestCallback) {
        HashMap<String, Object> mHashMap = new HashMap<String, Object>();
        mHashMap.put("client_key", GameSdk.clientId);
        mHashMap.put("code", code);
        mHashMap.put("grant_type", "authorization_code");
        HashMap<String, Object> mDataMap = new HashMap<String, Object>();
        mDataMap.putAll(mHashMap);
        mDataMap.putAll(UrlUtil.extMap(context, true));
        RetrofitManager.getInstance().onQueryToken(mDataMap, new RequestCallback<TokenModel>() {

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

    /**
     * GameLogin
     *
     * @param context
     * @param tokenModel
     * @param requestCallback
     */
    @Override
    public void onGameLogin(Context context, TokenModel tokenModel, final RequestCallback<GameLogin> requestCallback) {
        int is_bound = 0;
        String open_id = tokenModel.getOpen_id();
        String access_token = tokenModel.getAccess_token();
        int user_type = ACCOUNT_NUMBER;
        String user_id = tokenModel.getUser_id();
        HashMap<String, Object> mHashMap = new HashMap<String, Object>();
        mHashMap.put("is_bound", is_bound);
        mHashMap.put("open_id", open_id);
        mHashMap.put("access_token", access_token);
        mHashMap.put("user_type", user_type);
        mHashMap.put("user_id", user_id);
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

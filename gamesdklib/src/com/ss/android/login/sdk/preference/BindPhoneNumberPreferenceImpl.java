package com.ss.android.login.sdk.preference;

import android.content.Context;

import com.ss.android.gamecommon.model.Authorize;
import com.ss.android.gamecommon.model.GameLogin;
import com.ss.android.gamecommon.model.QuickLogin;
import com.ss.android.gamecommon.model.SendCode;
import com.ss.android.gamecommon.model.TokenModel;
import com.ss.android.gamecommon.model.Verified;
import com.ss.android.gamecommon.util.NumberEncryption;
import com.ss.android.login.sdk.model.BindPhoneNumberModel;
import com.ss.android.login.sdk.model.BindPhoneNumberModelImpl;
import com.ss.android.login.sdk.model.RequestCallback;
import com.ss.android.login.sdk.view.BindPhoneNumberView;

public class BindPhoneNumberPreferenceImpl implements BindPhoneNumberPreference {
    private BindPhoneNumberModel bindPhoneNumberModel;
    private BindPhoneNumberView bindPhoneNumberView;

    public BindPhoneNumberPreferenceImpl(BindPhoneNumberView bindPhoneNumberView) {
        this.bindPhoneNumberModel = new BindPhoneNumberModelImpl();
        this.bindPhoneNumberView = bindPhoneNumberView;
    }

    @Override
    public void sendCode(Context context, String phoneNum) {
        String numberEnc = NumberEncryption.xorEncryption(phoneNum, "5");
        String typeEnc = NumberEncryption.xorEncryption("27", "5");
        bindPhoneNumberModel.onSendCode(context, numberEnc, typeEnc, new RequestCallback<SendCode>() {
            @Override
            public void onRequestSuccess(SendCode sendCode) {
                bindPhoneNumberView.onGetCodeSuccess(sendCode);

            }

            @Override
            public void onRequestFailture(String errorCode, String message) {
                bindPhoneNumberView.onGetCodeFailture(errorCode, message);
            }
        });


    }


    @Override
    public void quickLogin(Context context, String phoneNum, String code) {
        String numberEnc = NumberEncryption.xorEncryption(phoneNum, "5");
        String typeEnc = NumberEncryption.xorEncryption("27", "5");
        String codeEnc = NumberEncryption.xorEncryption(code, "5");
        bindPhoneNumberModel.onQuickLogin(context, numberEnc, codeEnc, typeEnc, new RequestCallback<QuickLogin>() {

            @Override
            public void onRequestSuccess(QuickLogin quickLogin) {
                bindPhoneNumberView.onQuickLoginSuccess(quickLogin);
            }

            @Override
            public void onRequestFailture(String errorCode, String message) {
                bindPhoneNumberView.onQuickLoginFailture(errorCode, message);
            }
        });

    }

    @Override
    public void queryOAuthCode(Context context) {
        bindPhoneNumberModel.onQueryOAuthCode(context, new RequestCallback<Authorize>() {

            @Override
            public void onRequestSuccess(Authorize authorize) {
                bindPhoneNumberView.onQueryOAuthCodeSuccess(authorize);
            }

            @Override
            public void onRequestFailture(String errorCode, String errorMessage) {
                bindPhoneNumberView.onQueryOAuthCodeFailture(errorCode, errorMessage);
            }
        });
    }

    @Override
    public void queryToken(Context context, String code) {
        bindPhoneNumberModel.onQueryToken(context, code, new RequestCallback<TokenModel>() {

            @Override
            public void onRequestSuccess(TokenModel tokenModel) {
                bindPhoneNumberView.onQueryTokenSuccess(tokenModel);
            }

            @Override
            public void onRequestFailture(String errorCode, String errorMessage) {
                bindPhoneNumberView.onQueryTokenFailture(errorCode, errorMessage);
            }
        });

    }


    @Override
    public void gameLogin(Context context, TokenModel tokenModel) {
        bindPhoneNumberModel.onGameLogin(context, tokenModel, new RequestCallback<GameLogin>() {

            @Override
            public void onRequestSuccess(GameLogin gameLogin) {
                bindPhoneNumberView.onGameLoginSuccess(gameLogin);
            }

            @Override
            public void onRequestFailture(String errorCode, String errorMessage) {
                bindPhoneNumberView.onGameLoginFailture(errorCode, errorMessage);
            }
        });
    }

}

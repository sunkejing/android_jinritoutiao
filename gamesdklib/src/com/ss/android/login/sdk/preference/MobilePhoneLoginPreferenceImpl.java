package com.ss.android.login.sdk.preference;

import android.content.Context;

import com.ss.android.gamecommon.model.Authorize;
import com.ss.android.gamecommon.model.GameLogin;
import com.ss.android.gamecommon.model.QuickLogin;
import com.ss.android.gamecommon.model.SendCode;
import com.ss.android.gamecommon.model.TokenModel;
import com.ss.android.gamecommon.model.Verified;
import com.ss.android.gamecommon.util.NumberEncryption;
import com.ss.android.login.sdk.model.MobilePhoneLoginModel;
import com.ss.android.login.sdk.model.MobilePhoneLoginModelImpl;
import com.ss.android.login.sdk.model.RequestCallback;
import com.ss.android.login.sdk.view.MobilePhoneLoginView;

public class MobilePhoneLoginPreferenceImpl implements MobilePhoneLoginPreference {
    private MobilePhoneLoginModel mobilePhoneLoginModel;
    private MobilePhoneLoginView mobilePhoneLoginView;

    public MobilePhoneLoginPreferenceImpl(MobilePhoneLoginView mobilePhoneLoginView) {
        this.mobilePhoneLoginView = mobilePhoneLoginView;
        this.mobilePhoneLoginModel = new MobilePhoneLoginModelImpl();
    }

    @Override
    public void sendCode(Context context, String phoneNum) {
        String numberEnc = NumberEncryption.xorEncryption(phoneNum, "5");
        String typeEnc = NumberEncryption.xorEncryption("27", "5");
        mobilePhoneLoginModel.onSendCode(context, numberEnc, typeEnc, new RequestCallback<SendCode>() {
            @Override
            public void onRequestSuccess(SendCode sendCode) {
                mobilePhoneLoginView.onGetCodeSuccess(sendCode);

            }

            @Override
            public void onRequestFailture(String errorCode, String message) {
                mobilePhoneLoginView.onGetCodeFailture(errorCode, message);
            }
        });
    }


    @Override
    public void quickLogin(Context context, String phoneNum, String code) {
        String numberEnc = NumberEncryption.xorEncryption(phoneNum, "5");
        String typeEnc = NumberEncryption.xorEncryption("27", "5");
        String codeEnc = NumberEncryption.xorEncryption(code, "5");
        mobilePhoneLoginModel.onQuickLogin(context, numberEnc, codeEnc, typeEnc, new RequestCallback<QuickLogin>() {

            @Override
            public void onRequestSuccess(QuickLogin quickLogin) {
                mobilePhoneLoginView.onQuickLoginSuccess(quickLogin);
            }

            @Override
            public void onRequestFailture(String errorCode, String message) {
                mobilePhoneLoginView.onQuickLoginFailture(errorCode, message);
            }
        });
    }

    @Override
    public void queryOAuthCode(Context context) {
        mobilePhoneLoginModel.onQueryOAuthCode(context, new RequestCallback<Authorize>() {

            @Override
            public void onRequestSuccess(Authorize authorize) {
                mobilePhoneLoginView.onQueryOAuthCodeSuccess(authorize);
            }

            @Override
            public void onRequestFailture(String errorCode, String errorMessage) {
                mobilePhoneLoginView.onQueryOAuthCodeFailture(errorCode, errorMessage);
            }
        });
    }

    /**
     * 根据code请求token
     *
     * @param context
     * @param code
     */
    @Override
    public void queryToken(Context context, String code) {
        mobilePhoneLoginModel.onQueryToken(context, code, new RequestCallback<TokenModel>() {

            @Override
            public void onRequestSuccess(TokenModel tokenModel) {
                mobilePhoneLoginView.onQueryTokenSuccess(tokenModel);
            }

            @Override
            public void onRequestFailture(String errorCode, String errorMessage) {
                mobilePhoneLoginView.onQueryTokenFailture(errorCode, errorMessage);
            }
        });

    }

    @Override
    public void gameLogin(Context context, TokenModel tokenModel) {
        mobilePhoneLoginModel.onGameLogin(context, tokenModel, new RequestCallback<GameLogin>() {

            @Override
            public void onRequestSuccess(GameLogin gameLogin) {
                mobilePhoneLoginView.onGameLoginSuccess(gameLogin);
            }

            @Override
            public void onRequestFailture(String errorCode, String errorMessage) {
                mobilePhoneLoginView.onGameLoginFailture(errorCode, errorMessage);
            }
        });

    }

}

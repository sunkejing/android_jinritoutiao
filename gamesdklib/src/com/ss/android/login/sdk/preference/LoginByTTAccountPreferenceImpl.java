package com.ss.android.login.sdk.preference;

import android.content.Context;

import com.ss.android.gamecommon.model.Authorize;
import com.ss.android.gamecommon.model.GameLogin;
import com.ss.android.gamecommon.model.TicketModel;
import com.ss.android.gamecommon.model.TokenModel;
import com.ss.android.gamecommon.model.Verified;
import com.ss.android.login.sdk.model.LoginByTTAccountModel;
import com.ss.android.login.sdk.model.LoginByTTAccountModelImpl;
import com.ss.android.login.sdk.model.RequestCallback;
import com.ss.android.login.sdk.view.LoginByTTAccountView;

public class LoginByTTAccountPreferenceImpl implements LoginByTTAccountPreference {
    private LoginByTTAccountView loginByTTAccountView;
    private LoginByTTAccountModel loginByTTAccountModel;

    public LoginByTTAccountPreferenceImpl(LoginByTTAccountView loginByTTAccountView) {
        this.loginByTTAccountView = loginByTTAccountView;
        loginByTTAccountModel = new LoginByTTAccountModelImpl();
    }

    @Override
    public void enterGameForTicker(Context context, String account, String pwd) {
        loginByTTAccountModel.onEnterGameForTicker(context, account, pwd, new RequestCallback<TicketModel>() {

            @Override
            public void onRequestSuccess(TicketModel ticketModel) {
                loginByTTAccountView.onEnterGameForTickerSuccess(ticketModel);
            }

            @Override
            public void onRequestFailture(String errorCode, String message) {
                loginByTTAccountView.onEnterGameForTickerFailture(errorCode, message);
            }
        });
    }

    @Override
    public void enterGameForCode(Context context) {
        loginByTTAccountModel.onEnterGameForCode(context, new RequestCallback<Authorize>() {

            @Override
            public void onRequestSuccess(Authorize authorize) {
                loginByTTAccountView.onRequestCodeSuccess(authorize);
            }

            @Override
            public void onRequestFailture(String errorCode, String message) {
                loginByTTAccountView.onRequestCodeFailture(errorCode, message);
            }
        });
    }


    @Override
    public void enterGameForToken(Context context, String code) {
        loginByTTAccountModel.onEnterGameForToken(context, code, new RequestCallback<TokenModel>() {

            @Override
            public void onRequestSuccess(TokenModel tokenModel) {
                loginByTTAccountView.onEnterGameForTokenSuccess(tokenModel);
            }

            @Override
            public void onRequestFailture(String errorCode, String message) {
                loginByTTAccountView.onEnterGameForTokenFailture(errorCode, message);
            }
        });
    }

    @Override
    public void onGameLogin(Context context, TokenModel tokenModel) {
        loginByTTAccountModel.onGameLogin(context, tokenModel, new RequestCallback<GameLogin>() {

            @Override
            public void onRequestSuccess(GameLogin gameLogin) {
                loginByTTAccountView.onGameLoginSuccess(gameLogin);
            }

            @Override
            public void onRequestFailture(String errorCode, String message) {
                loginByTTAccountView.onGameLoginFailture(errorCode, message);
            }
        });
    }

}

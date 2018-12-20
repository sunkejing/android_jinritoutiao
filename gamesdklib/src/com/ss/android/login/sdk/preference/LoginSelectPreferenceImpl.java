package com.ss.android.login.sdk.preference;

import android.content.Context;
import com.ss.android.gamecommon.model.GameLogin;
import com.ss.android.gamecommon.model.VisitorLogin;
import com.ss.android.gamecommon.util.SharedPreferenceUtil;
import com.ss.android.login.sdk.model.LoginSelectModel;
import com.ss.android.login.sdk.model.LoginSelectModelmpl;
import com.ss.android.login.sdk.model.RequestCallback;
import com.ss.android.login.sdk.view.LoginSelectView;

public class LoginSelectPreferenceImpl implements LoginSelectPreference {
    private LoginSelectView loginSelectView;
    private LoginSelectModel loginSelectModel;

    public LoginSelectPreferenceImpl(LoginSelectView loginSelectView) {
        this.loginSelectView = loginSelectView;
        this.loginSelectModel = new LoginSelectModelmpl();
    }


    @Override
    public void hasBound(Context context) {
        loginSelectModel.hasBoundAccount(context, new RequestCallback<Integer>() {

            @Override
            public void onRequestSuccess(Integer result) {
                loginSelectView.isBound(result == 1);
            }

            @Override
            public void onRequestFailture(String errorCode, String message) {
                loginSelectView.onBoundFailture(errorCode, message);
            }
        });
    }

    //游客登录
    @Override
    public void visitorLogin(Context context) {
        loginSelectModel.visitorLogin(context, new RequestCallback<VisitorLogin>() {
            @Override
            public void onRequestSuccess(VisitorLogin visitorLogin) {
                loginSelectView.onVisitorLoginSuccess(visitorLogin);
            }

            @Override
            public void onRequestFailture(String errorCode, String message) {
                loginSelectView.onVisitorLoginFailture(errorCode, message);
            }
        });

    }

    @Override
    public void gameLogin(final Context context, VisitorLogin visitorLogin) {
        final SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance(context);
        loginSelectModel.gameLogin(context, visitorLogin, new RequestCallback<GameLogin>() {
            @Override
            public void onRequestSuccess(GameLogin gameLogin) {
                loginSelectView.onGameLoginSuccess(gameLogin);
                sharedPreferenceUtil.setHasLogin(true);
            }

            @Override
            public void onRequestFailture(String errorCode, String message) {
                loginSelectView.onGameLoginFailture(errorCode, message);
                sharedPreferenceUtil.setHasLogin(false);

            }
        });

    }
}

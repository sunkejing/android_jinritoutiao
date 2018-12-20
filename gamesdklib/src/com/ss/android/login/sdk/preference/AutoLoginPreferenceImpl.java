package com.ss.android.login.sdk.preference;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.ss.android.gamecommon.model.AutoLogin;
import com.ss.android.gamecommon.model.Verified;
import com.ss.android.gamecommon.util.LogUtil;
import com.ss.android.login.sdk.model.AutoLoginModel;
import com.ss.android.login.sdk.model.AutoLoginModelImpl;
import com.ss.android.login.sdk.model.RequestCallback;
import com.ss.android.login.sdk.view.AutoLoginView;

public class AutoLoginPreferenceImpl implements AutoLoginPreference {
    private AutoLoginView autoLoginView;
    private AutoLoginModel autoLoginModel;

    public AutoLoginPreferenceImpl(AutoLoginView autoLoginView) {
        this.autoLoginView = autoLoginView;
        this.autoLoginModel = new AutoLoginModelImpl();
    }


    @Override
    public void autoLogin(Context context, String loginId) {
        autoLoginModel.onAutoLogin(context, loginId, new RequestCallback<AutoLogin>() {

            @Override
            public void onRequestSuccess(AutoLogin autoLogin) {
                autoLoginView.onAutoLoginSuccess(autoLogin);
            }

            @Override
            public void onRequestFailture(String errorCode, String errorMessage) {
                autoLoginView.onAutoLoginFailture(errorCode, errorMessage);
            }
        });

    }

}

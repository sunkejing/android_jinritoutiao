package com.ss.android.login.sdk.view;

import com.ss.android.gamecommon.model.AutoLogin;
import com.ss.android.gamecommon.model.Verified;

public interface AutoLoginView {
    void onAutoLoginSuccess(AutoLogin autoLogin);

    void onAutoLoginFailture(String errorCode, String errorMessage);

}

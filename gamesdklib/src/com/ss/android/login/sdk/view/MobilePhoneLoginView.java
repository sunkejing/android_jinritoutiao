package com.ss.android.login.sdk.view;

import com.ss.android.gamecommon.model.Authorize;
import com.ss.android.gamecommon.model.GameLogin;
import com.ss.android.gamecommon.model.QuickLogin;
import com.ss.android.gamecommon.model.SendCode;
import com.ss.android.gamecommon.model.TokenModel;
import com.ss.android.gamecommon.model.Verified;

public interface MobilePhoneLoginView {
    void onGetCodeSuccess(SendCode sendCode);

    void onGetCodeFailture(String errorCode, String errorMsg);

    void onQuickLoginSuccess(QuickLogin quickLogin);

    void onQuickLoginFailture(String errorCode, String message);

    void onQueryOAuthCodeSuccess(Authorize authorize);

    void onQueryOAuthCodeFailture(String errorCode, String errorMessage);

    void onQueryTokenSuccess(TokenModel tokenModel);

    void onQueryTokenFailture(String errorCode, String errorMessage);

    void onGameLoginSuccess(GameLogin gameLogin);

    void onGameLoginFailture(String errorCode, String errorMessage);

}

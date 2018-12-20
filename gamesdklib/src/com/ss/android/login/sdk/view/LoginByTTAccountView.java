package com.ss.android.login.sdk.view;

import com.ss.android.gamecommon.model.Authorize;
import com.ss.android.gamecommon.model.GameLogin;
import com.ss.android.gamecommon.model.TicketModel;
import com.ss.android.gamecommon.model.TokenModel;
import com.ss.android.gamecommon.model.Verified;

public interface LoginByTTAccountView {
    void onEnterGameForTickerSuccess(TicketModel ticketModel);

    void onEnterGameForTickerFailture(String errorCode, String message);

    void onRequestCodeSuccess(Authorize authorize);

    void onRequestCodeFailture(String errorCode, String message);

    void onEnterGameForTokenSuccess(TokenModel tokenModel);

    void onEnterGameForTokenFailture(String errorCode, String message);

    void onGameLoginSuccess(GameLogin gameLogin);

    void onGameLoginFailture(String errorCode, String message);

}

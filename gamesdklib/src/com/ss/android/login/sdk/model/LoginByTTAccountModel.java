package com.ss.android.login.sdk.model;

import android.content.Context;

import com.ss.android.gamecommon.model.Authorize;
import com.ss.android.gamecommon.model.GameLogin;
import com.ss.android.gamecommon.model.TicketModel;
import com.ss.android.gamecommon.model.TokenModel;
import com.ss.android.gamecommon.model.Verified;

public interface LoginByTTAccountModel {
    void onEnterGameForTicker(Context context, String account, String pwd, RequestCallback<TicketModel> requestCallback);

    void onEnterGameForCode(Context context, RequestCallback<Authorize> requestCallback);

    void onEnterGameForToken(Context context, String code, RequestCallback<TokenModel> requestCallback);

    void onGameLogin(Context context, TokenModel tokenModel, RequestCallback<GameLogin> requestCallback);

}

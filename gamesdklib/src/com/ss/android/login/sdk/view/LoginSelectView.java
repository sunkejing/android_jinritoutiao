package com.ss.android.login.sdk.view;

import android.content.Context;

import com.ss.android.gamecommon.model.GameLogin;
import com.ss.android.gamecommon.model.VisitorLogin;

public interface LoginSelectView {
    //绑定账号请求成功
    void isBound(boolean isBound);

    //绑定账号请求失败
    void onBoundFailture(String errorCode, String message);

    //visitorLogin接口成功
    void onVisitorLoginSuccess(VisitorLogin visitorLogin);

    //visitorLogin接口失败
    void onVisitorLoginFailture(String errorCode, String message);

    //gameLogin接口成功
    void onGameLoginSuccess(GameLogin gameLogin);

    //gameLogin接口失败
    void onGameLoginFailture(String errorCode, String message);

}

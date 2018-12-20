package com.ss.android.gamecommon.util;

import android.content.Context;

public class LoginDataUtil {

    public static boolean isAutoLogin(Context context) {
        SharedPreferenceUtil spUtil = SharedPreferenceUtil.getInstance(context);
        return spUtil.isAutoLogin();
    }
}

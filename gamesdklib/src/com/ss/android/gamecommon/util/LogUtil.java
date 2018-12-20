package com.ss.android.gamecommon.util;

import android.util.Log;

public class LogUtil {
    private static final String TAG = "SS_SDK";

    public static void v(String tag, String message) {
        Log.v(tag, message);

    }

    public static void v(String message) {
        v(TAG, message);

    }

    public static void d(String tag, String message) {
        Log.d(tag, message);

    }

    public static void d(String message) {
        d(TAG, message);

    }

    public static void i(String tag, String message) {
        Log.i(tag, message);

    }

    public static void i(String message) {
        i(TAG, message);

    }

    public static void w(String tag, String message) {
        Log.w(tag, message);

    }

    public static void w(String message) {
        w(TAG, message);

    }

    public static void e(String tag, String message) {
        Log.e(tag, message);

    }

    public static void e(String message) {
        e(TAG, message);

    }


}

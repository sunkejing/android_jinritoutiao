package com.ss.android.GameSdkDemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * 监听apk安装成功的广播
 */
public class AppInstallReceiver extends BroadcastReceiver {
    private static final String TAG = "AppInstallReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_INSTALL)) {
            //安装完成
            String packageName = intent.getData().getSchemeSpecificPart();
            Log.d(TAG, "安装完成");
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
            //卸载完成
            Log.d(TAG, "卸载完成");
        }

    }
}

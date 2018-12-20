package com.ss.android.gamecommon.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.ss.android.gamecommon.thread.RetrofitManager;
import com.ss.android.gamecommon.util.LogUtil;
import com.ss.android.gamecommon.util.NetworkType;
import com.ss.android.gamecommon.util.RomUtil;
import com.ss.android.gamecommon.util.SharedPreferenceUtil;
import com.ss.android.gamecommon.util.ToolUtils;
import org.json.JSONObject;
import static android.media.MediaFormat.KEY_LANGUAGE;
import static com.ss.android.gamecommon.applog.BaseAppLog.KEY_ACCESS;
import static com.ss.android.gamecommon.applog.BaseAppLog.KEY_DEVICE_MODEL;
import static com.ss.android.gamecommon.applog.BaseAppLog.KEY_DISPLAY_DENSITY;
import static com.ss.android.gamecommon.applog.BaseAppLog.KEY_HEADER;
import static com.ss.android.gamecommon.applog.BaseAppLog.KEY_MAGIC_TAG;
import static com.ss.android.gamecommon.applog.BaseAppLog.KEY_MC;
import static com.ss.android.gamecommon.applog.BaseAppLog.KEY_OS;
import static com.ss.android.gamecommon.applog.BaseAppLog.KEY_OS_API;
import static com.ss.android.gamecommon.applog.BaseAppLog.KEY_OS_VERSION;
import static com.ss.android.gamecommon.applog.BaseAppLog.KEY_PACKAGE;
import static com.ss.android.gamecommon.applog.BaseAppLog.KEY_RESOLUTION;
import static com.ss.android.gamecommon.applog.BaseAppLog.KEY_TIMEZONE;
import static com.ss.android.gamecommon.applog.BaseAppLog.KEY_VERSION_CODE;
import static com.ss.android.gamecommon.util.ConstantUtil.KEY_APP_VERSION;
import static com.ss.android.gamecommon.util.ConstantUtil.KEY_CARRIER;
import static com.ss.android.gamecommon.util.ConstantUtil.KEY_CLIENTUDID;
import static com.ss.android.gamecommon.util.ConstantUtil.KEY_DISPLAY_NAME;
import static com.ss.android.gamecommon.util.ConstantUtil.KEY_OPENUDID;
import static com.ss.android.gamecommon.util.ConstantUtil.KEY_ROM;
import static com.ss.android.gamecommon.util.ConstantUtil.KEY_SDK_VERSION;
import static com.ss.android.gamecommon.util.ConstantUtil.KEY_UDID;
import static com.ss.android.gamecommon.util.ConstantUtil.SDK_VERSION_CODE;

public class NetStateChangeReceiver extends BroadcastReceiver {
    private static NetStateChangeReceiver sInstance;
    private static boolean hasRegistReceiver = false;

    public static NetStateChangeReceiver getInstance() {
        if (sInstance == null) {
            synchronized (NetStateChangeReceiver.class) {
                if (sInstance == null) {
                    sInstance = new NetStateChangeReceiver();
                }
            }
        }
        return sInstance;
    }

    public NetStateChangeReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            NetworkType networkType = ToolUtils.getNetWorkType(context);
            notifyObservers(context, networkType);
        }
    }

    /**
     * 注册网络监听
     */
    public void registerReceiver(@NonNull Context context) {
        hasRegistReceiver = true;
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(sInstance, intentFilter);
    }

    /**
     * 取消网络监听
     */
    public void unregisterReceiver(@NonNull Context context) {
        if (hasRegistReceiver) {
            context.unregisterReceiver(sInstance);
            hasRegistReceiver = false;
        }

    }


    /**
     * 通知所有的Observer网络状态变化
     */
    private void notifyObservers(Context context, NetworkType networkType) {
        if (networkType == NetworkType.NONE) {
            //没有网络
            LogUtil.e("网络已断开");
        } else {
            LogUtil.e("网络已连接");
            //开启子线程获取iid
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(KEY_HEADER, getHeaders(context));
                jsonObject.put(KEY_MAGIC_TAG, "ss_app_log");
            } catch (Exception e) {
                e.printStackTrace();
            }
            SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance(context);
            String iid = sharedPreferenceUtil.getString("iid", "");
            if (TextUtils.isEmpty(iid)) {
                RetrofitManager.getInstance().obtainInstallId(context, jsonObject.toString());
            }

        }
    }


    public JSONObject getHeaders(Context context) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(KEY_OPENUDID, ToolUtils.openUDID(context.getApplicationContext()));
            jsonObject.put(KEY_OS, "Android");
            jsonObject.put(KEY_ROM, RomUtil.getRomAndVersion());
            jsonObject.put(KEY_CLIENTUDID, ToolUtils.getUDID(context.getApplicationContext()));
            jsonObject.put(KEY_OS_API, android.os.Build.VERSION.SDK_INT);
            jsonObject.put(KEY_PACKAGE, context.getPackageName());
            jsonObject.put(KEY_APP_VERSION, ToolUtils.getAppVersion(context.getApplicationContext()));
            jsonObject.put(KEY_SDK_VERSION, SDK_VERSION_CODE);
            jsonObject.put("ut", 0);
            jsonObject.put("aid", 0);
            jsonObject.put(KEY_RESOLUTION, ToolUtils.getScreenWidthAndHeight(context));
            jsonObject.put(KEY_UDID, ToolUtils.getUDID(context.getApplicationContext()));
            jsonObject.put(KEY_ACCESS, ToolUtils.getNetworkState(context.getApplicationContext()));
            jsonObject.put(KEY_OS_VERSION, android.os.Build.VERSION.RELEASE);
            jsonObject.put(KEY_VERSION_CODE, ToolUtils.getVersionCode(context));
            jsonObject.put(KEY_DEVICE_MODEL, android.os.Build.MODEL);
            jsonObject.put(KEY_DISPLAY_NAME, ToolUtils.getAppName(context));
            jsonObject.put(KEY_TIMEZONE, ToolUtils.getTimeZoneOffSet());
            jsonObject.put(KEY_MC, ToolUtils.getAdresseMAC(context.getApplicationContext()));
            jsonObject.put(KEY_DISPLAY_DENSITY, ToolUtils.getDensity(context.getApplicationContext()));
            jsonObject.put(KEY_CARRIER, ToolUtils.getOperators(context.getApplicationContext()));
            jsonObject.put(KEY_LANGUAGE, ToolUtils.getLanguage());
            jsonObject.put("device_id", ToolUtils.getDeviceId(context));

            return jsonObject;

        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }


}

package com.ss.android.gamecommon.applog;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.ss.android.game.sdk.SsGameApi;
import com.ss.android.gamecommon.db.DbManager;
import com.ss.android.gamecommon.db.TableOperate;
import com.ss.android.gamecommon.db.tablebean.TableQueue;
import com.ss.android.gamecommon.db.util.LogType;
import com.ss.android.gamecommon.thread.RetrofitManager;
import com.ss.android.gamecommon.util.LogUtil;
import com.ss.android.gamecommon.util.RomUtil;
import com.ss.android.gamecommon.util.SharedPreferenceUtil;
import com.ss.android.gamecommon.util.ToolUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.ss.android.gamecommon.util.ConstantUtil.KEY_ACCESS_TOKEN;
import static com.ss.android.gamecommon.util.ConstantUtil.KEY_CARRIER;
import static com.ss.android.gamecommon.util.ConstantUtil.KEY_CLIENT;
import static com.ss.android.gamecommon.util.ConstantUtil.KEY_CLIENTUDID;
import static com.ss.android.gamecommon.util.ConstantUtil.KEY_DISPLAY_NAME;
import static com.ss.android.gamecommon.util.ConstantUtil.KEY_OPENUDID;
import static com.ss.android.gamecommon.util.ConstantUtil.KEY_ROM;
import static com.ss.android.gamecommon.util.ConstantUtil.KEY_SDK_VERSION;
import static com.ss.android.gamecommon.util.ConstantUtil.KEY_UDID;
import static com.ss.android.gamecommon.util.ConstantUtil.SDK_VERSION_CODE;

public class BaseAppLog implements Thread.UncaughtExceptionHandler {
    public static final String KEY_PACKAGE = "package";
    public static final String KEY_APP_VERSION = "app_version";
    public static final String KEY_AID = "aid";
    public static final String KEY_VERSION_CODE = "version_code";
    public static final String KEY_OS = "os";
    public static final String KEY_OS_VERSION = "os_version";
    public static final String KEY_OS_API = "os_api";
    public static final String KEY_DEVICE_MODEL = "device_model";
    public static final String KEY_HEADER = "header";
    public static final String KEY_MAGIC_TAG = "magic_tag";
    public static final String MAGIC_TAG = "ss_app_log";
    public static final String KEY_DISPLAY_DENSITY = "display_density";//手机分辨率信息
    public static final String KEY_RESOLUTION = "resolution";//获取手机的分辨率 width*height
    public static final String KEY_LANGUAGE = "language";//获取到手机的语言
    public static final String KEY_MC = "mc";//获取到当前的mac地址
    public static final String KEY_TIMEZONE = "timezone";//获取时区
    public static final String KEY_ACCESS = "access";//获取当前的网络连接类型
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private static BaseAppLog sInstance;
    private Context context;
    private JSONObject jsonObject;

    public static BaseAppLog getInstance(Context context) {
        if (sInstance == null) {
            synchronized (BaseAppLog.class) {
                if (sInstance == null) {
                    sInstance = new BaseAppLog(context);
                }
            }
        }
        return sInstance;
    }

    private BaseAppLog(Context context) {
        this.context = context.getApplicationContext();
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        //创建数据库
        DbManager.getInstance(context.getApplicationContext());
        //开启子线程获取iid
        jsonObject = new JSONObject();
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


    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        if (!handleException(thread, throwable) && mDefaultHandler != null) {
            //如果用户不处理，让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, throwable);
        } else {
            //两秒以后退出程序
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.exit(0);

        }

    }

    private boolean handleException(final Thread thread, final Throwable throwable) {
        if (thread == null) {
            return false;
        }
        new Thread(new Runnable() {

            @Override
            public void run() {
                Looper.prepare();
                Log.d("sdk", "正在写入崩溃日志");
                try {
                    //开始处理崩溃信息
                    JSONObject jsonObject = CrashUtil.getCrashInfo(context, throwable);
                    if (jsonObject == null) {
                        jsonObject = new JSONObject();
                    }
                    jsonObject.put("last_create_activity", ToolUtils.getTopActivity(context.getApplicationContext()));
                    jsonObject.put("last_resume_activity", ToolUtils.getTopActivity(context.getApplicationContext()));
                    jsonObject.put(KEY_HEADER, initHeader(null, context));
                    jsonObject.put(KEY_MAGIC_TAG, MAGIC_TAG);
                    LogUtil.e("崩溃信息：" + jsonObject.toString());
                    //将jsonObject插入到数据库
                    TableOperate tableOperate = new TableOperate(context.getApplicationContext());
                    TableQueue tableQueue = new TableQueue();
                    tableQueue.value = jsonObject.toString();
                    tableQueue.is_crash = "1";
                    tableQueue.log_type = LogType.LOG_CTASH;
                    tableQueue.timestamp = String.valueOf(System.currentTimeMillis());
                    tableQueue.retry_time = "0";
                    tableQueue.retry_count = "0";
                    tableOperate.insert(DbManager.TABLE_NAME, tableQueue);


                } catch (Exception e) {
                    e.printStackTrace();
                }
                Looper.loop();

            }
        }).start();
        return true;

    }

    private JSONObject initHeader(JSONObject header, Context context) {
        try {
            if (header == null) {
                header = new JSONObject();
            }
            header.put(KEY_PACKAGE, ToolUtils.getPackName(context));//packageName
            header.put(KEY_APP_VERSION, ToolUtils.getVersionName(context));//versionName
            header.put(KEY_AID, 0);
            header.put(KEY_VERSION_CODE, ToolUtils.getVersionCode(context));//versionCode
            header.put(KEY_OS, "Android");//操作系统
            header.put(KEY_OS_VERSION, android.os.Build.VERSION.RELEASE);//获取系统版本（如：4.1.2或4.4.4）
            header.put(KEY_OS_API, android.os.Build.VERSION.SDK_INT);//获取系统的API级别
            header.put(KEY_DEVICE_MODEL, android.os.Build.MODEL);//获取手机的型号（如三星SM-N9100）
            header.put(KEY_DISPLAY_DENSITY, ToolUtils.getDensity(context.getApplicationContext()));
            header.put(KEY_RESOLUTION, ToolUtils.getScreenWidthAndHeight(context));
            header.put(KEY_LANGUAGE, ToolUtils.getLanguage());
            header.put(KEY_MC, ToolUtils.getAdresseMAC(context.getApplicationContext()));
            header.put(KEY_TIMEZONE, ToolUtils.getTimeZoneOffSet());
            header.put(KEY_ACCESS, ToolUtils.getNetworkState(context.getApplicationContext()));

            return header;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return header;

    }

    public JSONObject getHeaders(Context context) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(KEY_OPENUDID, ToolUtils.openUDID(context.getApplicationContext()));
            jsonObject.put(KEY_OS, "Android");
            jsonObject.put(KEY_CLIENT, SsGameApi.CLIENTID);
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
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(1);
            jsonArray.put(2);
            jsonArray.put(4);
            jsonObject.put("push_sdk", jsonArray);
            jsonObject.put(KEY_DISPLAY_DENSITY, ToolUtils.getDensity(context.getApplicationContext()));
            jsonObject.put(KEY_CARRIER, ToolUtils.getOperators(context.getApplicationContext()));
            jsonObject.put(KEY_LANGUAGE, ToolUtils.getLanguage());
            jsonObject.put(KEY_ACCESS_TOKEN, "");
            //jsonObject.put("install_id", SharedPreferenceUtil.getInstance(SSApplication.getContext()).getString("iid", ""));
            jsonObject.put("device_id", ToolUtils.getDeviceId(context));

            return jsonObject;

        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }


}

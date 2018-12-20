package com.ss.android.gamecommon.util;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.net.NetworkInterface;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import static com.ss.android.gamecommon.util.ConstantUtil.PREFS_DEVICE_ID;

public class ToolUtils {
    /**
     * 获取到当前进程的名字
     *
     * @param context
     * @return
     */
    public static String getCurProcessName(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcessInfo : activityManager.getRunningAppProcesses()) {
            if (appProcessInfo.pid == android.os.Process.myPid()) {
                return appProcessInfo.processName;
            }
        }
        return null;
    }

    /**
     * 获取到顶部的activity
     *
     * @param context
     * @return
     */
    public static Class<?> getTopActivity(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String className = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        Class<?> cls = null;
        try {
            cls = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return cls;
    }

    /**
     * 获取到包名
     *
     * @param context
     * @return
     */
    public static String getPackName(Context context) {
        return context.getApplicationContext().getPackageName().toString();
    }

    /**
     * 获取到versionName
     */
    public static String getVersionName(Context context) {
        PackageManager packageManager = context.getApplicationContext().getPackageManager();
        PackageInfo packageInfo;
        String versionName = "";
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 获取VersionCode
     */
    public static String getVersionCode(Context context) {
        PackageManager packageManager = context.getApplicationContext().getPackageManager();
        PackageInfo packageInfo;
        int versionCode = -1;
        try {
            packageInfo = packageManager.getPackageInfo(context.getApplicationContext().getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode + "";
    }

    public static String getDensity(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        int screenSize = dm.densityDpi;
        String density = "";
        switch (screenSize) {
            case DisplayMetrics.DENSITY_LOW:
                //低分辨率
                density = "ldpi";
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                density = "mdpi";
                break;
            case DisplayMetrics.DENSITY_HIGH:
                density = "hdpi";
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                density = "xhdpi";
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                density = "xxhdpi";
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                density = "xxxhdpi";
                break;
        }
        return density;
    }

    public static String getScreenWidthAndHeight(Context context) {
        WindowManager wm = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int dispWidth = display.getWidth();
        int dispHeight = display.getHeight();
        return dispWidth + "*" + dispHeight;
    }


    public static String getLanguage() {
        Locale locale;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            locale = LocaleList.getDefault().get(0);
//        } else {
        locale = Locale.getDefault();
        //  }
        String language = locale.getLanguage() + "-" + locale.getCountry();
        return language;
    }

    public static String getAdresseMAC(Context context) {
        WifiManager wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();
        String marshmallowMacAddress = "02:00:00:00:00:00";
        if (wifiInf != null && marshmallowMacAddress.equals(wifiInf.getMacAddress())) {
            String result = null;
            try {
                result = getAdressMacByInterface();
                if (result != null) {
                    return result;
                } else {
                    result = getAddressMacByFile(context, wifiMan);
                    return result;
                }
            } catch (IOException e) {
                e.printStackTrace();
                LogUtil.e("Erreur lecture propriete Adresse MAC");
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e("Erreur lecture propriete Adresse MAC");
            }
        } else {
            if (wifiInf != null && wifiInf.getMacAddress() != null) {
                return wifiInf.getMacAddress();
            } else {
                return "";
            }
        }
        return marshmallowMacAddress;
    }

    private static String getAdressMacByInterface() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (nif.getName().equalsIgnoreCase("wlan0")) {
                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) {
                        return "";
                    }

                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        res1.append(String.format("%02X:", b));
                    }

                    if (res1.length() > 0) {
                        res1.deleteCharAt(res1.length() - 1);
                    }
                    return res1.toString();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("Erreur lecture propriete Adresse MAC");
        }
        return null;
    }

    private static String getAddressMacByFile(Context context, WifiManager wifiMan) throws Exception {
        String ret = "get mac address fail no permission";
        int wifiState = wifiMan.getWifiState();
        if (context.getPackageManager().checkPermission(Manifest.permission.CHANGE_WIFI_STATE, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
            wifiMan.setWifiEnabled(true);
            String fileAddressMac = "/sys/class/net/wlan0/address";
            File fl = new File(fileAddressMac);
            FileInputStream fin = new FileInputStream(fl);
            ret = crunchifyGetStringFromStream(fin);
            fin.close();
            boolean enabled = WifiManager.WIFI_STATE_ENABLED == wifiState;
            wifiMan.setWifiEnabled(enabled);
            return ret;
        }
        return ret;
    }

    private static String crunchifyGetStringFromStream(InputStream crunchifyStream) throws IOException {
        if (crunchifyStream != null) {
            Writer crunchifyWriter = new StringWriter();

            char[] crunchifyBuffer = new char[2048];
            try {
                Reader crunchifyReader = new BufferedReader(new InputStreamReader(crunchifyStream, "UTF-8"));
                int counter;
                while ((counter = crunchifyReader.read(crunchifyBuffer)) != -1) {
                    crunchifyWriter.write(crunchifyBuffer, 0, counter);
                }
            } finally {
                crunchifyStream.close();
            }
            return crunchifyWriter.toString();
        } else {
            return "No Contents";
        }
    }

    /**
     * 获取当前时区与手机时区的便宜量，单位小时
     *
     * @return
     */
    public static int getTimeZoneOffSet() {
        Calendar c = Calendar.getInstance();
        TimeZone tz = c.getTimeZone();
        int offset = tz.getOffset(0) / 1000 / 3600;//单位是小时
        return offset;
    }

    /**
     * 获取到当前的网络连接类型
     *
     * @param context
     * @return
     */
    public static String getNetworkState(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (null == connectivityManager) {
                return "no_network";
            }
            NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
                return "no_network";
            }
            //判断当前连接的是否是wifi
            NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (null != wifiInfo) {
                NetworkInfo.State state = wifiInfo.getState();
                if (null != state) {
                    if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                        return "wifi";
                    }
                }

            }
            //如果不是wifi，则判断当前连接的是运营商的哪种网络2g、3g、4g等
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (null != networkInfo) {
                NetworkInfo.State state = networkInfo.getState();
                String strSubTypeName = networkInfo.getSubtypeName();
                if (null != state) {
                    if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                        switch (activeNetInfo.getSubtype()) {
                            //如果是2g类型
                            case TelephonyManager.NETWORK_TYPE_GPRS:
                            case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2g
                            case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2g
                            case TelephonyManager.NETWORK_TYPE_1xRTT:
                            case TelephonyManager.NETWORK_TYPE_IDEN:
                                return "2g";

                            //如果是3g类型
                            case TelephonyManager.NETWORK_TYPE_EVDO_A: // 电信3g
                            case TelephonyManager.NETWORK_TYPE_UMTS:
                            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                            case TelephonyManager.NETWORK_TYPE_HSDPA:
                            case TelephonyManager.NETWORK_TYPE_HSUPA:
                            case TelephonyManager.NETWORK_TYPE_HSPA:
                            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                            case TelephonyManager.NETWORK_TYPE_EHRPD:
                            case TelephonyManager.NETWORK_TYPE_HSPAP:
                                return "3g";
                            //如果是4g类型
                            case TelephonyManager.NETWORK_TYPE_LTE:
                                return "4g";
                            default:
                                if (strSubTypeName.equalsIgnoreCase("TD-SCDMA") || strSubTypeName.equalsIgnoreCase("WCDMA") || strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                                    return "3g";
                                } else {
                                    return "network_mobile";
                                }


                        }

                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public static String openUDID(Context context) {
        String openUDID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (null == openUDID || openUDID.equals("9774d56d682e549c") || openUDID.length() < 15) {
            SecureRandom secureRandom = new SecureRandom();
            openUDID = new BigInteger(64, secureRandom).toString();
        }
        return openUDID;
    }


    public static String getUDID(Context mContext) {
        String udid = null;
        if (udid == null) {
            if (udid == null) {
                SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance(mContext);
                final String id = sharedPreferenceUtil.getString(PREFS_DEVICE_ID, null);
                if (id != null) {
                    // Use the ids previously computed and stored in the prefs file
                    udid = id;
                } else {

                    final String androidId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
                    // Use the Android ID unless it's broken, in which case fallback on deviceId,
                    // unless it's not available, then fallback on a random number which we store
                    // to a prefs file
                    try {
                        if (!"9774d56d682e549c".equals(androidId)) {
                            udid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8")).toString();
                        } else {
                            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                                final String deviceId = ((TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                                udid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")).toString() : UUID.randomUUID().toString();
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // Write the value out to the prefs file
                    sharedPreferenceUtil.putString(PREFS_DEVICE_ID, udid);
                }
            }
        }
        return udid;
    }

    public static String getAppVersion(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        String versionName = "";
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * get App versionCode
     *
     * @param context
     * @return
     */
    public String getAppVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        String versionCode = "";
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode + "";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取appName
     *
     * @param context
     * @return
     */
    public static String getAppName(Context context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        String appName = (String) packageManager.getApplicationLabel(applicationInfo);
        return appName;

    }

    /**
     * 返回运营商 需要加入权限 <uses-permission android:name="android.permission.READ_PHONE_STATE"/> <BR>
     *
     * @return 1, 代表中国移动，2，代表中国联通，3，代表中国电信，0，代表未知
     * @author youzc@yiche.com
     */
    public static String getOperators(Context context) {
        // 移动设备网络代码（英语：Mobile Network Code，MNC）是与移动设备国家代码（Mobile Country Code，MCC）（也称为“MCC /
        // MNC”）相结合, 例如46000，前三位是MCC，后两位是MNC 获取手机服务商信息
        String OperatorsName = "unkonwn";
        if (context.getPackageManager().checkPermission(Manifest.permission.READ_PHONE_STATE, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
            String IMSI = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
            // IMSI号前面3位460是国家，紧接着后面2位00 运营商代码
            if (TextUtils.isEmpty(IMSI)) {
                return OperatorsName;
            }
            if (IMSI.startsWith("46000") || IMSI.startsWith("46002") || IMSI.startsWith("46007")) {
                OperatorsName = "中国移动";
            } else if (IMSI.startsWith("46001") || IMSI.startsWith("46006")) {
                OperatorsName = "中国联通";
            } else if (IMSI.startsWith("46003") || IMSI.startsWith("46005")) {
                OperatorsName = "中国电信";
            }
        }
        return OperatorsName;
    }

    public static NetworkType getNetWorkType(Context context) {
        String netWorkState = getNetworkState(context);
        switch (netWorkState) {
            case "no_network":
                return NetworkType.NONE;
            case "wifi":
                return NetworkType.WIFI;
            case "2g":
                return NetworkType.MOBILE_2G;
            case "3g":
                return NetworkType.MOBILE_3G;
            case "4g":
                return NetworkType.MOBILE_4G;
            case "network_mobile":
                return NetworkType.MOBILE;

        }
        return null;
    }

    /**
     * 获取设备唯一标识码
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            String DEVICE_ID = tm.getDeviceId();
            if (!TextUtils.isEmpty(DEVICE_ID) && !"9774d56d682e549c".equals(DEVICE_ID)) {
                return DEVICE_ID;
            }

            String MAC_ADDRESS = ToolUtils.getAdresseMAC(context);
            if (!TextUtils.isEmpty(MAC_ADDRESS)) {
                return MAC_ADDRESS;
            }

            String ANDROID_ID = android.provider.Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            if (!TextUtils.isEmpty(ANDROID_ID)) {
                return ANDROID_ID;
            }
            return UUID.randomUUID().toString();
        } else {
            return "UNKNOWN";
        }

    }


    /**
     * 获取到手机的唯一识别码
     *
     * @param context
     * @return
     */
    public static String getUniqueID(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String tmDevice, tmSerial, androidId;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
            androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
            return deviceUuid.toString();
        }
        return "UNKNOWN";

    }

    /**
     * 将dp转换成px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


}




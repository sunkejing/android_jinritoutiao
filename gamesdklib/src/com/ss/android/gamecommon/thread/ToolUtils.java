
package com.ss.android.gamecommon.thread;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.text.TextUtils;

import org.w3c.dom.Text;

public class ToolUtils {
    public final static String MESSAGE_PROCESS_SUFFIX = ":push";
    private static String sCurProcessName = null;

    public static boolean isMessageProcess(Context context) {
        String processName = getCurProcessName(context);
        if (processName != null && processName.endsWith(MESSAGE_PROCESS_SUFFIX)) {
            return true;
        }
        return false;
    }

    public static String getCurProcessName(Context context) {
        String procName = sCurProcessName;
        if (!TextUtils.isEmpty(procName)) {
            return procName;
        }
        try {
            int pid = android.os.Process.myPid();
            ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
                if (appProcess.pid == pid) {

                    procName = appProcess.processName;
                    sCurProcessName = procName;
                    return procName;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static boolean isMainProcess(Context context) {
        String processName = getCurProcessName(context);
        if (processName != null && processName.contains(":")) {
            return false;
        }
        return (processName != null && processName.equals(context.getPackageName()));
    }
}

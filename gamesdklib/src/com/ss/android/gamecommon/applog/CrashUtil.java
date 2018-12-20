package com.ss.android.gamecommon.applog;

import android.content.Context;
import android.os.Debug;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import com.ss.android.gamecommon.util.ToolUtils;

public class CrashUtil {
    public static JSONObject getCrashInfo(Context context, Throwable exc) {
        PrintWriter pw = null;
        Writer writer = new StringWriter();
        JSONObject jsonObject = new JSONObject();
        try {
            pw = new PrintWriter(writer);
            if (exc != null) {
                exc.printStackTrace(pw);
                Throwable excCause = exc.getCause();
                while (excCause != null) {
                    excCause.printStackTrace(pw);
                    excCause = excCause.getCause();
                }
                pw.close();
                String result = writer.toString();
                jsonObject.put("data", result);
            }
            jsonObject.put("crash_time", System.currentTimeMillis());
            jsonObject.put("process_name", ToolUtils.getCurProcessName(context));
            //获取到内存信息
            Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
            Debug.getMemoryInfo(memoryInfo);
            JSONObject memoryInfoJson = new JSONObject();
            memoryInfoJson.put("dalvikPrivateDirty", memoryInfo.dalvikPrivateDirty);//dalvik使用的私有内存
            memoryInfoJson.put("dalvikPss", memoryInfo.dalvikPss);//
            memoryInfoJson.put("dalvikSharedDirty", memoryInfo.dalvikSharedDirty);
            memoryInfoJson.put("nativePrivateDirty", memoryInfo.nativePrivateDirty);
            memoryInfoJson.put("nativePss", memoryInfo.nativePss);
            memoryInfoJson.put("nativeSharedDirty", memoryInfo.nativeSharedDirty);
            memoryInfoJson.put("otherPrivateDirty", memoryInfo.otherPrivateDirty);
            memoryInfoJson.put("otherPss", memoryInfo.otherPss);
            memoryInfoJson.put("otherSharedDirty", memoryInfo.otherSharedDirty);
            jsonObject.put("sys_memory_info", memoryInfoJson);


        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (pw != null) {
                pw.close();
            }

        }
        return jsonObject;


    }

}

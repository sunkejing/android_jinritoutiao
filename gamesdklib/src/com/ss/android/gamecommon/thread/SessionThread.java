package com.ss.android.gamecommon.thread;

import android.content.ContentValues;
import android.content.Context;

import com.ss.android.gamecommon.applog.LogReport;
import com.ss.android.gamecommon.db.DbManager;
import com.ss.android.gamecommon.db.TableOperate;
import com.ss.android.gamecommon.db.tablebean.TableSession;
import com.ss.android.gamecommon.db.util.TablePropertyUtil;
import com.ss.android.gamecommon.util.ToolUtils;

import java.util.UUID;

public class SessionThread implements Runnable {
    private long timeStamp;
    private Context context;
    private TableSession oldTableSession;

    public SessionThread(long timeStamp, Context context, TableSession tableSession) {
        this.timeStamp = timeStamp;
        this.context = context;
        this.oldTableSession = tableSession;
    }

    @Override
    public void run() {
        TableOperate tableOperate = new TableOperate(context);
        TableSession newSession = new TableSession();
        newSession.value = createSession();
        LogReport.sSessionId = newSession.value;
        newSession.timestamp = timeStamp;
        newSession.pausetime = newSession.timestamp;
        newSession.duration = 0;
        newSession.app_version = ToolUtils.getAppVersion(context);
        newSession.version_code = ToolUtils.getVersionCode(context);
        newSession.non_page = "0";
        //将session插入到数据库中
        int index = tableOperate.insert(TablePropertyUtil.TABLE_SESSION, newSession);
    }

    private String createSession() {
        return UUID.randomUUID().toString();
    }
}

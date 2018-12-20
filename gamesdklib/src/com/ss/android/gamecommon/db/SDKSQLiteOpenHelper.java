package com.ss.android.gamecommon.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ss.android.gamecommon.db.util.TablePropertyUtil;
import com.ss.android.gamecommon.util.LogUtil;

import static com.ss.android.gamecommon.db.util.TablePropertyUtil.TABLE_SESSION;

public class SDKSQLiteOpenHelper extends SQLiteOpenHelper {
    private static SDKSQLiteOpenHelper helper;
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "ss_app_log.db";

    //创建的是日志表
    private static final String createTableQueue = "create table if not exists " + TablePropertyUtil.TABLE_NAME + " ("
            + TablePropertyUtil.COL_ID + " integer not null primary key autoincrement,"
            + TablePropertyUtil.COL_VALUE + " text,"
            + TablePropertyUtil.COL_IS_CRASH + " integer not null default 0,"
            + TablePropertyUtil.COL_LOG_TYPE + " integer not null default 0,"
            + TablePropertyUtil.COL_TIMESTAMP + " integer,"
            + TablePropertyUtil.COL_RETRY_TIME + " integer,"
            + TablePropertyUtil.COL_RETRY_COUNT + " integer" + ")";

    //创建sesssion表
    private static final String createTableSession = "create table if not exists " + TABLE_SESSION + " ("
            + TablePropertyUtil.SESSION_COL_ID + " integer not null primary key autoincrement,"
            + TablePropertyUtil.SESSION_COL_VALUE + " text,"
            + TablePropertyUtil.SESSION_COL_TIMESTAMP + " integer,"
            + TablePropertyUtil.SESSION_COL_DURATION + " integer,"
            + TablePropertyUtil.SESSION_COL_NON_PAGE + " text,"
            + TablePropertyUtil.SESSION_COL_APP_VERSION + " text,"
            + TablePropertyUtil.SESSION_COL_VERSION_CODE + " text,"
            + TablePropertyUtil.SESSION_COL_PAUSETIME + " integer,"
            + TablePropertyUtil.SESSION_COL_LAUNCH_SENT + " integer" + ")";


    public SDKSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //为了简化构造器的使用，我们自定义一个构造器
    private SDKSQLiteOpenHelper(Context context, String name) {
        this(context, name, null, DB_VERSION);//传入Context和数据库的名称，调用上面那个构造器
    }

    public static SDKSQLiteOpenHelper getInstance(Context context) {
        if (helper == null) {
            synchronized (SDKSQLiteOpenHelper.class) {
                if (helper == null) {
                    helper = new SDKSQLiteOpenHelper(context, DB_NAME);
                }
            }
        }
        return helper;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //在创建数据库时，初始化创建数据库中包含的表
        LogUtil.d("start to create table");
        sqLiteDatabase.execSQL(createTableQueue);//创建日志表
        sqLiteDatabase.execSQL(createTableSession);//创建session表
        LogUtil.d("end to create table");

    }

    //数据库的升级
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

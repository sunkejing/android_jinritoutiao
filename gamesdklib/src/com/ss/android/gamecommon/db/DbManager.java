package com.ss.android.gamecommon.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DbManager {
    public static String TABLE_NAME = "queue";//crash表名
    private static DbManager sInstance;
    private SDKSQLiteOpenHelper helper;
    private SQLiteDatabase db;

    /**
     * 获取DbManager的单利对象
     *
     * @return
     */
    public static DbManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (DbManager.class) {
                if (sInstance == null) {
                    sInstance = new DbManager(context);
                }

            }
        }
        return sInstance;

    }

    private DbManager(Context context) {
        helper = SDKSQLiteOpenHelper.getInstance(context.getApplicationContext());
    }

    /**
     * 获取数据库对象
     *
     * @return
     */
    public SQLiteDatabase getDb() {
        if (db == null) {
            db = helper.getWritableDatabase();
        }
        return db;
    }
}

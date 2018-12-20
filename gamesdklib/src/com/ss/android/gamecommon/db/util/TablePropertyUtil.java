package com.ss.android.gamecommon.db.util;

public class TablePropertyUtil {
    //日志表名
    public static String TABLE_NAME = "queue";
    //日志字段
    public static String COL_ID = "_id";//主键id
    public static String COL_VALUE = "value";//日志内容
    public static String COL_IS_CRASH = "is_crash";//是否是崩溃日zhi
    public static String COL_LOG_TYPE = "log_type";//日志类型
    public static String COL_TIMESTAMP = "timestamp";//时间戳
    public static String COL_RETRY_TIME = "retry_time";//重试时间
    public static String COL_RETRY_COUNT = "retry_count";//重试次数

    //session表
    public static String TABLE_SESSION = "session";
    public static String SESSION_COL_ID = "_id";//主键
    public static String SESSION_COL_VALUE = "value";//值
    public static String SESSION_COL_TIMESTAMP = "timestamp";//时间戳
    public static String SESSION_COL_DURATION = "duration";
    public static String SESSION_COL_NON_PAGE = "non_page";
    public static String SESSION_COL_APP_VERSION = "app_version";
    public static String SESSION_COL_VERSION_CODE = "version_code";
    public static String SESSION_COL_PAUSETIME = "pausetime";
    public static String SESSION_COL_LAUNCH_SENT = "launch_sent";



}

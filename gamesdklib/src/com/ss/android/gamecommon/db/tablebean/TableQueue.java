package com.ss.android.gamecommon.db.tablebean;

/**
 *
 */
public class TableQueue {
    public String _id;//主键id
    public String value;//日志内容
    public String is_crash;//是否是崩溃日志
    public String log_type;//日志类型
    public String timestamp;//时间戳
    public String retry_time;//重试时间
    public String retry_count;//重试次数

}

package com.ss.android.gamecommon.db.tablebean;

public class TableSession {
    private int _id;//主键
    public String value;//值
    public long timestamp;//时间戳
    public int duration;
    public String non_page;
    public String app_version;
    public String version_code;
    public long pausetime;
    public int launch_sent;

    public TableSession(int _id, String value, long timestamp, int duration, String non_page, String app_version, String version_code, long pausetime, int
            launch_sent) {
        this._id = _id;
        this.value = value;
        this.timestamp = timestamp;
        this.duration = duration;
        this.non_page = non_page;
        this.app_version = app_version;
        this.version_code = version_code;
        this.pausetime = pausetime;
        this.launch_sent = launch_sent;
    }

    public TableSession() {
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setNon_page(String non_page) {
        this.non_page = non_page;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public void setVersion_code(String version_code) {
        this.version_code = version_code;
    }

    public void setPausetime(long pausetime) {
        this.pausetime = pausetime;
    }

    public void setLaunch_sent(int launch_sent) {
        this.launch_sent = launch_sent;
    }

    public int get_id() {
        return _id;
    }

    public String getValue() {
        return value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getDuration() {
        return duration;
    }

    public String getNon_page() {
        return non_page;
    }

    @Override
    public String toString() {
        return "TableSession{" +
                "_id=" + _id +
                ", value='" + value + '\'' +
                ", timestamp=" + timestamp +
                ", duration=" + duration +
                ", non_page='" + non_page + '\'' +
                ", app_version='" + app_version + '\'' +
                ", version_code=" + version_code +
                ", pausetime=" + pausetime +
                ", launch_sent=" + launch_sent +
                '}';
    }

    public String getApp_version() {
        return app_version;
    }

    public String getVersion_code() {
        return version_code;
    }

    public long getPausetime() {
        return pausetime;
    }

    public int getLaunch_set() {
        return launch_sent;
    }


}

package com.ss.android.gamecommon.model;

public class AutoLogin {
    private int error_code;
    private String open_id;
    private long user_id;
    private String description;//描述信息

    public int getError_code() {
        return error_code;
    }

    public String getOpen_id() {
        return open_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public String getDescription() {
        return description;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public void setOpen_id(String open_id) {
        this.open_id = open_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "AutoLogin{" +
                "error_code=" + error_code +
                ", open_id='" + open_id + '\'' +
                ", user_id=" + user_id +
                ", description='" + description + '\'' +
                '}';
    }
}

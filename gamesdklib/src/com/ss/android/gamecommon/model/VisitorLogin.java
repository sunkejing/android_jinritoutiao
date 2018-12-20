package com.ss.android.gamecommon.model;

public class VisitorLogin {
    private int error_code;
    private int expires_in;
    private String open_id;
    private long user_id;
    private int user_type;

    public VisitorLogin() {
    }

    public VisitorLogin(int error_code, int expires_in, String open_id, long user_id, int user_type) {
        this.error_code = error_code;
        this.expires_in = expires_in;
        this.open_id = open_id;
        this.user_id = user_id;
        this.user_type = user_type;
    }

    public int getError_code() {
        return error_code;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public String getOpen_id() {
        return open_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public void setOpen_id(String open_id) {
        this.open_id = open_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    @Override
    public String toString() {
        return "VisitorLogin{" +
                "error_code=" + error_code +
                ", expires_in=" + expires_in +
                ", open_id='" + open_id + '\'' +
                ", user_id=" + user_id +
                ", user_type=" + user_type +
                '}';
    }
}

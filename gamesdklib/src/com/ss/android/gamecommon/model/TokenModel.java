package com.ss.android.gamecommon.model;

public class TokenModel {
    private String access_token;
    private String description;
    private int error_code;
    private int expires_in;
    private String open_id;
    private String refresh_token;
    private String scope;
    private String user_id;

    public String getAccess_token() {
        return access_token;
    }

    public String getDescription() {
        return description;
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

    public String getRefresh_token() {
        return refresh_token;
    }

    public String getScope() {
        return scope;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "TokenModel{" +
                "access_token='" + access_token + '\'' +
                ", description='" + description + '\'' +
                ", error_code=" + error_code +
                ", expires_in=" + expires_in +
                ", open_id='" + open_id + '\'' +
                ", refresh_token='" + refresh_token + '\'' +
                ", scope='" + scope + '\'' +
                ", user_id='" + user_id + '\'' +
                '}';
    }
}

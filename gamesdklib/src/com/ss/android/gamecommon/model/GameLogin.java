package com.ss.android.gamecommon.model;

public class GameLogin {
    private int error_code;
    private int has_played;
    private String open_id;
    private long user_id;
    private String login_id;

    public GameLogin(int error_code, int has_played, String open_id, long user_id, String login_id) {
        this.error_code = error_code;
        this.has_played = has_played;
        this.open_id = open_id;
        this.user_id = user_id;
        this.login_id = login_id;
    }

    public GameLogin() {
    }

    public int getError_code() {
        return error_code;
    }

    public int getHas_played() {
        return has_played;
    }

    public String getOpen_id() {
        return open_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public String getLogin_id() {
        return login_id;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public void setHas_played(int has_played) {
        this.has_played = has_played;
    }

    public void setOpen_id(String open_id) {
        this.open_id = open_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public void setLogin_id(String login_id) {
        this.login_id = login_id;
    }

    @Override
    public String toString() {
        return "GameLogin{" +
                "error_code=" + error_code +
                ", has_played=" + has_played +
                ", open_id='" + open_id + '\'' +
                ", user_id=" + user_id +
                ", login_id='" + login_id + '\'' +
                '}';
    }
}

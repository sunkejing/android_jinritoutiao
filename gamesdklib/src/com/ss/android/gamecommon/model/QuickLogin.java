package com.ss.android.gamecommon.model;

public class QuickLogin {
    private String captcha;//图片验证码
    private String description;//描述
    private int error_code;//错误码

    public String getCaptcha() {
        return captcha;
    }

    public String getDescription() {
        return description;
    }

    public int getError_code() {
        return error_code;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public QuickLogin(String captcha, String description, int error_code) {
        this.captcha = captcha;
        this.description = description;
        this.error_code = error_code;
    }

    public QuickLogin() {

    }

    @Override
    public String toString() {
        return "QuickLogin{" +
                "captcha='" + captcha + '\'' +
                ", description='" + description + '\'' +
                ", error_code='" + error_code + '\'' +
                '}';
    }
}

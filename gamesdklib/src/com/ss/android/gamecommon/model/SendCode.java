package com.ss.android.gamecommon.model;

public class SendCode {
    private String mobile;//手机号
    private int retry_time;//倒计时时间
    private String captcha;
    private String description;
    private int error_code;


    public String getMobile() {
        return mobile;
    }

    public int getRetry_time() {
        return retry_time;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setRetry_time(int retry_time) {
        this.retry_time = retry_time;
    }

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

    public SendCode() {

    }

    public SendCode(String mobile, int retry_time) {
        this.mobile = mobile;
        this.retry_time = retry_time;
    }

    @Override
    public String toString() {
        return "SendCode{" +
                "mobile='" + mobile + '\'' +
                ", retry_time=" + retry_time +
                ", captcha='" + captcha + '\'' +
                ", description='" + description + '\'' +
                ", error_code=" + error_code +
                '}';
    }
}

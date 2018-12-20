package com.ss.android.gamecommon.model;

public class Authorize {
    private String captcha;//验证码
    private String description;//描述信息
    private int error_code;//错误码
    private String code;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Authorize() {
    }


    @Override
    public String toString() {
        return "Authorize{" +
                "captcha='" + captcha + '\'' +
                ", description='" + description + '\'' +
                ", error_code=" + error_code +
                ", code='" + code + '\'' +
                '}';
    }
}

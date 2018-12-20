package com.ss.android.gamecommon.model;

public class TicketModel {
    private String ticket;
    private String captcha;//验证码
    private String description;//描述信息
    private int error_code;//错误码

    public String getTicket() {
        return ticket;
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

    public void setTicket(String ticket) {
        this.ticket = ticket;
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
}

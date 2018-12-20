package com.ss.android.gamecommon.model;

public class RegistrationMobilePhone {
    private int new_user;
    private long install_id;
    private long server_time;
    private String ssid;
    private long device_id;

    public RegistrationMobilePhone(int new_user, long install_id, long server_time, String ssid, long device_id) {
        this.new_user = new_user;
        this.install_id = install_id;
        this.server_time = server_time;
        this.ssid = ssid;
        this.device_id = device_id;
    }

    public RegistrationMobilePhone() {
    }

    public void setNew_user(int new_user) {
        this.new_user = new_user;
    }

    public void setInstall_id(long install_id) {
        this.install_id = install_id;
    }

    public void setServer_time(long server_time) {
        this.server_time = server_time;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public void setDevice_id(long device_id) {
        this.device_id = device_id;
    }

    public int getNew_user() {
        return new_user;
    }

    public long getInstall_id() {
        return install_id;
    }

    public long getServer_time() {
        return server_time;
    }

    public String getSsid() {
        return ssid;
    }

    public long getDevice_id() {
        return device_id;
    }

    @Override
    public String toString() {
        return "RegistrationMobilePhone{" +
                "new_user=" + new_user +
                ", install_id=" + install_id +
                ", server_time=" + server_time +
                ", ssid='" + ssid + '\'' +
                ", device_id=" + device_id +
                '}';
    }
}

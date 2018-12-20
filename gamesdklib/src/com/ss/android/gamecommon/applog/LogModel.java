package com.ss.android.gamecommon.applog;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LogModel{


    /**
     * magic_tag : ss_app_log
     * header : {"udid":"352105061736667","openudid":"9a72ef359c705f5e","sdk_version":334,"package":"com.ss.android.GameSdkDemo","display_name":"GameSdk","app_version":"3.3.4","version_code":334,"timezone":8,"access":"wifi","os":"Android","os_version":"6.0.1","os_api":23,"device_model":"SM-G9008W","language":"zh","resolution":"1920x1080","display_density":"mdpi","mc":"F4:09:D8:11:B9:69","clientudid":"48a14e03-ae78-4254-9e25-0804e91397d9","install_id":"52669568942","device_id":"54160797054","sig_hash":"ba149aded052a345f5b96dca34eea7ae","aid":0,"push_sdk":[1,2,4],"rom":"G9008WZMU1CQB1","access_token":"","client_key":"tt","ut":0}
     * event : [{"event_type_value":"window_show","nt":4,"category":"umeng","tag":"SDK_GAME","label":"init_window","value":0,"session_id":"871e2bcb-fd42-4858-a490-1dc4acf1a786","datetime":"2018-11-30 20:33:38"}]
     * launch : {"session_id":"871e2bcb-fd42-4858-a490-1dc4acf1a786","datetime":"2018-11-30 20:33:35"}
     */

    private String magic_tag;
    private HeaderBean header;
    private LaunchBean launch;
    private List<EventBean> event;

    public String getMagic_tag() {
        return magic_tag;
    }

    public void setMagic_tag(String magic_tag) {
        this.magic_tag = magic_tag;
    }

    public HeaderBean getHeader() {
        return header;
    }

    public void setHeader(HeaderBean header) {
        this.header = header;
    }

    public LaunchBean getLaunch() {
        return launch;
    }

    public void setLaunch(LaunchBean launch) {
        this.launch = launch;
    }

    public List<EventBean> getEvent() {
        return event;
    }

    public void setEvent(List<EventBean> event) {
        this.event = event;
    }

    public static class HeaderBean {
        /**
         * udid : 352105061736667
         * openudid : 9a72ef359c705f5e
         * sdk_version : 334
         * package : com.ss.android.GameSdkDemo
         * display_name : GameSdk
         * app_version : 3.3.4
         * version_code : 334
         * timezone : 8
         * access : wifi
         * os : Android
         * os_version : 6.0.1
         * os_api : 23
         * device_model : SM-G9008W
         * language : zh
         * resolution : 1920x1080
         * display_density : mdpi
         * mc : F4:09:D8:11:B9:69
         * clientudid : 48a14e03-ae78-4254-9e25-0804e91397d9
         * install_id : 52669568942
         * device_id : 54160797054
         * sig_hash : ba149aded052a345f5b96dca34eea7ae
         * aid : 0
         * push_sdk : [1,2,4]
         * rom : G9008WZMU1CQB1
         * access_token :
         * client_key : tt
         * ut : 0
         */

        private String udid;
        private String openudid;
        private int sdk_version;
        @SerializedName("package")
        private String packageX;
        private String display_name;
        private String app_version;
        private int version_code;
        private int timezone;
        private String access;
        private String os;
        private String os_version;
        private int os_api;
        private String device_model;
        private String language;
        private String resolution;
        private String display_density;
        private String mc;
        private String clientudid;
        private String install_id;
        private String device_id;
        private String sig_hash;
        private int aid;
        private String rom;
        private String access_token;
        private String client_key;
        private int ut;
        //private List<Integer> push_sdk;

        public String getUdid() {
            return udid;
        }

        public void setUdid(String udid) {
            this.udid = udid;
        }

        public String getOpenudid() {
            return openudid;
        }

        public void setOpenudid(String openudid) {
            this.openudid = openudid;
        }

        public int getSdk_version() {
            return sdk_version;
        }

        public void setSdk_version(int sdk_version) {
            this.sdk_version = sdk_version;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getDisplay_name() {
            return display_name;
        }

        public void setDisplay_name(String display_name) {
            this.display_name = display_name;
        }

        public String getApp_version() {
            return app_version;
        }

        public void setApp_version(String app_version) {
            this.app_version = app_version;
        }

        public int getVersion_code() {
            return version_code;
        }

        public void setVersion_code(int version_code) {
            this.version_code = version_code;
        }

        public int getTimezone() {
            return timezone;
        }

        public void setTimezone(int timezone) {
            this.timezone = timezone;
        }

        public String getAccess() {
            return access;
        }

        public void setAccess(String access) {
            this.access = access;
        }

        public String getOs() {
            return os;
        }

        public void setOs(String os) {
            this.os = os;
        }

        public String getOs_version() {
            return os_version;
        }

        public void setOs_version(String os_version) {
            this.os_version = os_version;
        }

        public int getOs_api() {
            return os_api;
        }

        public void setOs_api(int os_api) {
            this.os_api = os_api;
        }

        public String getDevice_model() {
            return device_model;
        }

        public void setDevice_model(String device_model) {
            this.device_model = device_model;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getResolution() {
            return resolution;
        }

        public void setResolution(String resolution) {
            this.resolution = resolution;
        }

        public String getDisplay_density() {
            return display_density;
        }

        public void setDisplay_density(String display_density) {
            this.display_density = display_density;
        }

        public String getMc() {
            return mc;
        }

        public void setMc(String mc) {
            this.mc = mc;
        }

        public String getClientudid() {
            return clientudid;
        }

        public void setClientudid(String clientudid) {
            this.clientudid = clientudid;
        }

        public String getInstall_id() {
            return install_id;
        }

        public void setInstall_id(String install_id) {
            this.install_id = install_id;
        }

        public String getDevice_id() {
            return device_id;
        }

        public void setDevice_id(String device_id) {
            this.device_id = device_id;
        }

        public String getSig_hash() {
            return sig_hash;
        }

        public void setSig_hash(String sig_hash) {
            this.sig_hash = sig_hash;
        }

        public int getAid() {
            return aid;
        }

        public void setAid(int aid) {
            this.aid = aid;
        }

        public String getRom() {
            return rom;
        }

        public void setRom(String rom) {
            this.rom = rom;
        }

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getClient_key() {
            return client_key;
        }

        public void setClient_key(String client_key) {
            this.client_key = client_key;
        }

        public int getUt() {
            return ut;
        }

        public void setUt(int ut) {
            this.ut = ut;
        }

//        public List<Integer> getPush_sdk() {
//            return push_sdk;
//        }

//        public void setPush_sdk(List<Integer> push_sdk) {
//            this.push_sdk = push_sdk;
//        }


        @Override
        public String toString() {
            return "HeaderBean{" +
                    "udid='" + udid + '\'' +
                    ", openudid='" + openudid + '\'' +
                    ", sdk_version=" + sdk_version +
                    ", packageX='" + packageX + '\'' +
                    ", display_name='" + display_name + '\'' +
                    ", app_version='" + app_version + '\'' +
                    ", version_code=" + version_code +
                    ", timezone=" + timezone +
                    ", access='" + access + '\'' +
                    ", os='" + os + '\'' +
                    ", os_version='" + os_version + '\'' +
                    ", os_api=" + os_api +
                    ", device_model='" + device_model + '\'' +
                    ", language='" + language + '\'' +
                    ", resolution='" + resolution + '\'' +
                    ", display_density='" + display_density + '\'' +
                    ", mc='" + mc + '\'' +
                    ", clientudid='" + clientudid + '\'' +
                    ", install_id='" + install_id + '\'' +
                    ", device_id='" + device_id + '\'' +
                    ", sig_hash='" + sig_hash + '\'' +
                    ", aid=" + aid +
                    ", rom='" + rom + '\'' +
                    ", access_token='" + access_token + '\'' +
                    ", client_key='" + client_key + '\'' +
                    ", ut=" + ut +
                    '}';
        }
    }

    public static class LaunchBean {
        /**
         * session_id : 871e2bcb-fd42-4858-a490-1dc4acf1a786
         * datetime : 2018-11-30 20:33:35
         */

        private String session_id;
        private String datetime;

        public String getSession_id() {
            return session_id;
        }

        public void setSession_id(String session_id) {
            this.session_id = session_id;
        }

        public String getDatetime() {
            return datetime;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }

        @Override
        public String toString() {
            return "LaunchBean{" +
                    "session_id='" + session_id + '\'' +
                    ", datetime='" + datetime + '\'' +
                    '}';
        }
    }

    public static class EventBean {
        /**
         * event_type_value : window_show
         * nt : 4
         * category : umeng
         * tag : SDK_GAME
         * label : init_window
         * value : 0
         * session_id : 871e2bcb-fd42-4858-a490-1dc4acf1a786
         * datetime : 2018-11-30 20:33:38
         */

        private String event_type_value;
        private int nt;
        private String category;
        private String tag;
        private String label;
        private int value;
        private String session_id;
        private String datetime;

        public String getEvent_type_value() {
            return event_type_value;
        }

        public void setEvent_type_value(String event_type_value) {
            this.event_type_value = event_type_value;
        }

        public int getNt() {
            return nt;
        }

        public void setNt(int nt) {
            this.nt = nt;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getSession_id() {
            return session_id;
        }

        public void setSession_id(String session_id) {
            this.session_id = session_id;
        }

        public String getDatetime() {
            return datetime;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }
    }

    @Override
    public String toString() {
        return "LogModel{" +
                "magic_tag='" + magic_tag + '\'' +
                ", header=" + header +
                ", launch=" + launch +
                ", event=" + event +
                '}';
    }
}

package com.ss.android.gamecommon.util;

public class ConstantUtil {
    public static final String SDK_VERSION_CODE = "334";
    public static final String BASE_URL = "https://i.snssdk.com/";
    public static final String BASE_URL_OPEN = "https://open.snssdk.com/";
    //public static final String BASE_URL = "http://10.8.123.25:9001";
    public static final String APP_LOG_CONFIG_URL = "https://i.snssdk.com/service/2/device_register/";
    public static final String KEY_OPENUDID = "openudid";
    public static final String KEY_CLIENT = "client_key";
    public static final String KEY_ROM = "rom";
    public static final String KEY_CLIENTUDID = "clientudid";
    public static final String KEY_UDID = "udid";
    public static final String PREFS_DEVICE_ID = "gank_device_id";
    public static final String KEY_PACKAGE = "package";
    public static final String KEY_APP_VERSION = "app_version";
    public static final String KEY_SDK_VERSION = "sdk_version";
    public static final String KEY_VERSION_CODE = "version_code";
    public static final String KEY_DISPLAY_NAME = "display_name";
    public static final String KEY_CARRIER = "carrier";
    public static final String KEY_ACCESS_TOKEN = "access_token";
    public static final String KEY_HEADER = "header";
    public static final String KEY_MAGIC_TAG = "magic_tag";
    public static final String DEVICE_REGISTER = "device_register/";
    public static final String LOG_URL = "game_sdk/log/";
    public static final String HAS_BOUND = "game_sdk/has_bound/";
    public static final String VISITOR_LOGIN = "game_sdk/visitor_login/";
    public static final String GAME_LOGIN = "game_sdk/game_login/";
    public static final String SEND_CODE = "oauth/send_code/";
    public static final String GET_TOKEN = "game_sdk/access_token/";
    public static final int ACCOUNT_NUMBER = 12;//账号登录
    public static final int TOURIST = 14;//游客
    public static final int LOGIN_FAIL_BECAUSE_ACTIVITY_IS_NULL = 10000;//activity为空，
    public static final int LOGIN_FAIL_BECAUSE_USERTYPE_IS_INVALID = 10001;//usertype不合法
    public static final String TARGET_FRAGMENT = "target_fragment";//目标fragment
    public static final int LoginSelectFragment = 20001;//登录选择fragment
    public static final int AutoLoginFragment = 20002;//自动登录fragment


    public static final int PAY_METHOD_ALI = 30001;//支付宝支付
    public static final int PAY_METHOD_WX = 30002;//微信H5支付
    public static final int PAY_FAIL_NOT_LOGIN = 30003;//没有登录成功导致支付失败
    public static final int PAY_FAIL_BECAUSE_CREATE_ORDER_FAIL = 30004;//创建订单失败，导致支付失败
    public static final int PAY_CANCEL = 30005;//支付取消
    public static final int PAY_FAIL = 30006;
    public static final int PAY_SUCCESS = 30007;
    public static final int PAY_FAIL_BECAUSE_ORDERINFO_IS_NULL = 30008;
    public static final int IdentityVerificationFragment = 30009;//身份验证fragment
    public static final int PaymentSelectFragment = 30010;//支付方式选择fragment

    public static final String PAY_INFO = "payInfo";
    public static final String PAY_REQUEST_INFO = "payRequestInfo";
    public static final String OUT_TRADE_NO = "outTradeNo";
    public static final String PAY_EXT_PARMAS = "payExtParmas";
    public static final String PAY_WAY = "way";
    public static final String WX_H5_PAY_HOST = "http://www.toutiao.com";//申请微信支付时填写的域名
    public static final String MWEB_URL = "mweb_url";
    public static final int NEED_READ_PHONE_STATE = 40000;
    //Log相关
    public static final String EVENT_TYPE_WINDOW_SHOW = "window_show";
    public static final String EVENT_LABEL_INIT_WINDOW = "init_window";

    public static final String LOG_URL_HEADER = "https://i.snssdk.com/game_sdk/log/";


}

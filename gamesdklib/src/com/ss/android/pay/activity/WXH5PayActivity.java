package com.ss.android.pay.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ss.android.game.sdk.GameSdk;
import com.ss.android.gamecommon.util.ConstantUtil;
import com.ss.android.gamecommon.util.RUtil;
import com.ss.android.gamecommon.util.ToastUtil;
import com.ss.android.pay.SSPayManager;
import com.ss.android.pay.preference.WXH5PayPreference;
import com.ss.android.pay.preference.WXH5PayPreferenceImpl;
import com.ss.android.pay.view.WXH5PayView;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信H5支付
 */
public class WXH5PayActivity extends Activity implements WXH5PayView {
    private boolean isStop = false;
    private WebView webView;
    private String mWeb_url;
    private String outTradeNo;
    private int way;
    private WXH5PayPreference wxh5PayPreference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(RUtil.getLayout(this, "activity_wxh5_pay"));
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mWeb_url = bundle.getString(ConstantUtil.MWEB_URL);
                outTradeNo = bundle.getString(ConstantUtil.OUT_TRADE_NO);
                way = bundle.getInt(ConstantUtil.PAY_WAY);
            }
        }
        initView();
        wxh5PayPreference = new WXH5PayPreferenceImpl(this);

    }

    private void initView() {
        webView = (WebView) findViewById(RUtil.getId(this, "webview"));
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(false);
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //可以让webview处理https请求
                handler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("weixin://wap/pay?")) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    return true;
                } else {
                    if (("4.4.3".equals(android.os.Build.VERSION.RELEASE))
                            || ("4.4.4".equals(android.os.Build.VERSION.RELEASE))) {
                        return false;
                    } else {
                        Map<String, String> extraHeaders = new HashMap<>();
                        extraHeaders.put("Referer", "http://www.toutiao.com");
                        webView.loadUrl(mWeb_url, extraHeaders);
                    }
                }
                return true;
            }
        });

        if (("4.4.3".equals(android.os.Build.VERSION.RELEASE))
                || ("4.4.4".equals(android.os.Build.VERSION.RELEASE))) {
            //兼容这两个版本设置referer无效的问题
            webView.loadDataWithBaseURL("http://www.toutiao.com",
                    "<script>window.location.href=\"" + mWeb_url + "\";</script>",
                    "text/html", "utf-8", null);
        } else {
            Map<String, String> extraHeaders = new HashMap<>();
            extraHeaders.put("Referer", "http://www.toutiao.com");
            webView.loadUrl(mWeb_url, extraHeaders);
        }

    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
            // destory()
            ViewParent parent = webView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(webView);
            }
            webView.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            webView.getSettings().setJavaScriptEnabled(false);
            webView.clearHistory();
            webView.clearView();
            webView.removeAllViews();
            webView.destroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        isStop = true;
        super.onStop();

    }

    @Override
    protected void onPause() {
        super.onPause();
        isStop = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isStop) {
            //查询支付结果
            HashMap<String, Object> map = new HashMap<>();
            map.put(ConstantUtil.OUT_TRADE_NO, outTradeNo);
            map.put(ConstantUtil.PAY_WAY, way);
            map.put("client_id", GameSdk.clientId);
            wxh5PayPreference.queryPaymentResults(this, map);
        }

    }


    @Override
    public void onQueryPaymentResultSuccess(Boolean isSuccess) {
        if (isSuccess) {
            //支付成功
            ToastUtil.showToast(this, "支付成功");
            isStop = false;
            if (SSPayManager.sPayCallback != null) {
                SSPayManager.sPayCallback.onPaySuccess(ConstantUtil.PAY_SUCCESS, "微信支付成功");
            }
            finish();

        } else {
            //支付失败
            if (SSPayManager.sPayCallback != null) {
                SSPayManager.sPayCallback.onPayCancel(ConstantUtil.PAY_CANCEL, "微信支付取消");
            }
            isStop = false;
            finish();
        }

    }

    @Override
    public void onQueryPaymentResultFailture(String errorCode, String message) {
        //支付失败
        isStop = false;
        if (SSPayManager.sPayCallback != null) {
            SSPayManager.sPayCallback.onPayFail(ConstantUtil.PAY_FAIL, "微信支付失败");
        }
        finish();
    }
}

package com.ss.android.login.sdk.activity;

import android.net.http.SslError;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.ss.android.gamecommon.customview.ProgressWebView;
import com.ss.android.gamecommon.util.RUtil;

public class UserProtocolActivity extends BaseActivity {
    private ProgressWebView webView;
    private LinearLayout ll_back;

    @Override
    protected void initData() {
        String url = "https://www.toutiao.com/user_agreement/?hideAll=1";
        webView.loadUrl(url);

    }

    @Override
    public void initView() {
        ll_back = (LinearLayout) findViewById(RUtil.getId(this, "ll_back"));
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        webView = (ProgressWebView) findViewById(RUtil.getId(this, "wb_userprotocol"));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true); //设置适应Html5
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();// 接受所有网站的证书
            }
        });

        // android 5.0以上默认不支持Mixed Content
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(
                    WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }


    }

    @Override
    public int getLayout() {
        return RUtil.getLayout(this, "activity_user_protocol");
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.removeAllViews();
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.setTag(null);
            webView.clearHistory();
            webView.destroy();
            webView = null;
        }
    }
}

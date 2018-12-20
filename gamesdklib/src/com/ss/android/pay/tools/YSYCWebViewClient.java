package com.ss.android.pay.tools;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class YSYCWebViewClient extends WebViewClient {
    private WebView webView;
    private Context context;

    public YSYCWebViewClient(Context context, WebView webView) {
        this.webView = webView;
        this.context = context;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        System.out.println("shouldOverrideUrlLoading");
        startPay(url);
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
    }

    @SuppressLint("ObsoleteSdkInt")
    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        //根据业务场景自定义错误处理

    }


    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed();//处理https证书问题
    }

    private boolean startPay(String url) {
        try {
            if (url.startsWith("weixin://")) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                context.startActivity(intent);
                return true;
            } else if (parseScheme(url)) {
                Intent intent;
                intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                intent.addCategory("android.intent.category.BROWSABLE");
                intent.setComponent(null);
                context.startActivity(intent);
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }

    public boolean parseScheme(String url) {
        if (url.contains("platformapi/startApp") || url.contains("platformapi/startapp")) {
            return true;
        } else {
            return false;
        }
    }


}

package com.ss.android.gamecommon.customview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.ss.android.gamecommon.util.RUtil;


public class ProgressWebView extends WebView {
    private ProgressBar progressBar;

    public ProgressWebView(Context context, AttributeSet attributeSet) {
        super(context.getApplicationContext(), attributeSet);
        progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 5);
        progressBar.setLayoutParams(layoutParams);
        Drawable drawable = context.getResources().getDrawable(RUtil.getDrawable(context, "progress_bar_states"));
        progressBar.setProgressDrawable(drawable);
        addView(progressBar);
        setWebChromeClient(new WebChromeClient());


    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) progressBar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressBar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressBar.setVisibility(GONE);
            } else {
                if (progressBar.getVisibility() == GONE) {
                    progressBar.setVisibility(VISIBLE);
                }
                progressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }

}

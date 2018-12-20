package com.ss.android.gamecommon.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;


public class LoadProgressDialog extends Dialog {
    private Context context;

    public LoadProgressDialog(Context context) {
        super(context);
        this.context = context;
    }

    public LoadProgressDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }


    /**
     * 给Dialog设置提示信息
     *
     * @param message
     */
    public void setMessage(CharSequence message) {
        if (message != null && message.length() > 0) {
            int messageId = RUtil.getId(context, "message");
            TextView txt = (TextView) findViewById(messageId);
            txt.setText(message);
        }
    }

    /**
     * 弹出自定义ProgressDialog
     *
     * @param context        上下文
     * @param message        提示
     * @param cancelable     是否按返回键取消
     * @param cancelListener 按下返回键监听
     * @return
     */
    public static LoadProgressDialog show(Context context, CharSequence message, boolean cancelable, OnCancelListener cancelListener) {
        int styleId = RUtil.getStyle(context, "add_dialog");
        LoadProgressDialog dialog = new LoadProgressDialog(context, styleId);
        dialog.setTitle("");
        int layoutId = RUtil.getLayout(context, "progress_dialog_layout");
        dialog.setContentView(layoutId);
        if (message == null || message.length() == 0) {
            int messageId = RUtil.getId(context, "message");
            dialog.findViewById(messageId).setVisibility(View.GONE);
        } else {
            int messageId = RUtil.getId(context, "message");
            TextView txt = (TextView) dialog.findViewById(messageId);
            txt.setText(message);
        }
        // 按返回键是否取消
        dialog.setCanceledOnTouchOutside(false);
        //dialog.setCancelable(cancelable);
        // 监听返回键处理
        dialog.setOnCancelListener(cancelListener);
        // 设置居中
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        // 设置背景层透明度
        lp.dimAmount = 0.6f;
        dialog.getWindow().setAttributes(lp);
        // dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.show();
        return dialog;
    }


}

package com.ss.android.gamecommon.util;

import android.content.Context;

public class RUtil {
    public static int getString(Context context, String name) {
        return getResId(context, name, "string");
    }

    public static int getDrawable(Context context, String name) {
        return getResId(context, name, "drawable");
    }

    public static int getId(Context context, String name) {
        return getResId(context, name, "id");
    }

    public static int getLayout(Context context, String name) {
        return getResId(context, name, "layout");
    }

    public static int getStyle(Context context, String name) {
        return getResId(context, name, "style");
    }

    public static int getAnim(Context context, String name) {
        return getResId(context, name, "anim");
    }

    public static int getColor(Context context, String name) {
        return getResId(context, name, "color");
    }

    private static int getResId(Context context, String name, String type) {
        return context.getResources().getIdentifier(name, type, context.getPackageName());
    }


}

package com.kangjj.skin.lib.utils;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class StatusBarUtils {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void forStatusBar(Activity activity){
        TypedArray a = activity.getTheme().obtainStyledAttributes(0,new int[]{
                android.R.attr.statusBarColor
        });
        int color =a.getColor(0,0);
        forStatusBar(activity,color);
        a.recycle();
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void forStatusBar(Activity activity, int skinColor){
        activity.getWindow().setStatusBarColor(skinColor);
    }
}

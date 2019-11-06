package com.kangjj.skin.lib.utils;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class NavigationUtils {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void forNavigation(Activity activity){
        TypedArray a = activity.getTheme().obtainStyledAttributes(0,new int[]{
                android.R.attr.statusBarColor
        });
        int color = a.getColor(0,0);
        forNavigation(activity,color);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void forNavigation(Activity activity, int skinColor) {
        activity.getWindow().setNavigationBarColor(skinColor);
    }
}

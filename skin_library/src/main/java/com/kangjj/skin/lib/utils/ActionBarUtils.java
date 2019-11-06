package com.kangjj.skin.lib.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ActionBarUtils {

    public static void forActionBar(AppCompatActivity activity){
        TypedArray a = activity.getTheme().obtainStyledAttributes(0,new int[]{
                android.R.attr.colorPrimary
        });
        int color = a.getColor(0,0);
        a.recycle();
        forActionBar(activity,color);
    }

    public static void forActionBar(AppCompatActivity activity, int skinColor) {
        ActionBar actionBar = activity.getSupportActionBar();
        if(actionBar!=null){
            actionBar.setBackgroundDrawable(new ColorDrawable(skinColor));
        }
    }
}

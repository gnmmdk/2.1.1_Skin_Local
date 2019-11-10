package com.kangjj.skin.base;

import android.app.Application;

import com.kangjj.skin.lib.SkinManager;


public class SkinApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.init(this);
    }
}

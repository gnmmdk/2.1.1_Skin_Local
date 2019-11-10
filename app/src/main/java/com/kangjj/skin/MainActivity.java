package com.kangjj.skin;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;

import com.kangjj.skin.lib.base.SkinActivity;
import com.kangjj.skin.lib.utils.PreferencesUtils;

import java.io.File;

/**
 * 如果图标有固定的尺寸，不需要更改，那么drawable更加适合
 * 如果需要变大变小变大变小的，有动画的，放在mipmap中能有更高的质量
 */
public class MainActivity extends SkinActivity {
    private String skinPath;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//
//        boolean isNight = PreferencesUtils.getBoolean(this, "isNight");
//        if (isNight) {
//            setDayNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//        } else {
//            setDayNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//        }

        // File.separator含义：拼接 /
        skinPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "kangjj.skin";

        // 运行时权限申请（6.0+）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (checkSelfPermission(perms[0]) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(perms, 200);
            }
        }

        if (("kangjj").equals(PreferencesUtils.getString(this, "currentSkin"))) {
            skinDynamic(skinPath, R.color.skin_item_color);
        } else {
            defaultSkin(R.color.colorPrimary);
        }
    }
    
    // 换肤按钮（api限制：5.0版本）
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void skinDynamic(View view) {
        // 真实项目中：需要先判断当前皮肤，避免重复操作！
        if (!("kangjj").equals(PreferencesUtils.getString(this, "currentSkin"))) {
            Log.e("kangjj >>> ", "-------------start-------------");
            long start = System.currentTimeMillis();

            skinDynamic(skinPath, R.color.skin_item_color);
            PreferencesUtils.putString(this, "currentSkin", "kangjj");

            long end = System.currentTimeMillis() - start;
            Log.e("kangjj >>> ", "换肤耗时（毫秒）：" + end);
            Log.e("kangjj >>> ", "-------------end---------------");
        }
    }

    // 默认按钮（api限制：5.0版本）
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void skinDefault(View view) {
        if (!("default").equals(PreferencesUtils.getString(this, "currentSkin"))) {
            Log.e("kangjj >>> ", "-------------start-------------");
            long start = System.currentTimeMillis();

            defaultSkin(R.color.colorPrimary);
            PreferencesUtils.putString(this, "currentSkin", "default");

            long end = System.currentTimeMillis() - start;
            Log.e("kangjj >>> ", "还原耗时（毫秒）：" + end);
            Log.e("kangjj >>> ", "-------------end---------------");
        }
    }
    
//    // 点击事件
//    public void dayOrNight(View view) {
//        int uiMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
//
//        switch (uiMode) {
//            case Configuration.UI_MODE_NIGHT_NO:
//                setDayNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                PreferencesUtils.putBoolean(this, "isNight", true);
//                break;
//            case Configuration.UI_MODE_NIGHT_YES:
//                setDayNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                PreferencesUtils.putBoolean(this, "isNight", false);
//                break;
//            default:
//                break;
//        }
//    }

    @Override
    protected boolean openChangeSkin() {
        return true;
    }

    // 跳转按钮
    public void jumpSelf(View view) {
        startActivity(new Intent(this, this.getClass()));
    }
}

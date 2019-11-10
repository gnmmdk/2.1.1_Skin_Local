package com.kangjj.skin.lib.model;

import android.content.res.Resources;

public class SkinCache {

    private Resources skinResources;        //用于加载皮肤包资源
    private String skinPackagenName;        //皮肤包资源所在包名（注：皮肤包不在app内，也不限包名)

    public SkinCache(Resources skinResources, String skinPackagenName) {
        this.skinResources = skinResources;
        this.skinPackagenName = skinPackagenName;
    }

    public Resources getSkinResources() {
        return skinResources;
    }

    public void setSkinResources(Resources skinResources) {
        this.skinResources = skinResources;
    }

    public String getSkinPackagenName() {
        return skinPackagenName;
    }

    public void setSkinPackagenName(String skinPackagenName) {
        this.skinPackagenName = skinPackagenName;
    }
}

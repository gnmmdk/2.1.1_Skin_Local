package com.kangjj.skin.lib;

import android.app.Application;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;

import com.kangjj.skin.lib.model.SkinCache;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * 皮肤资源管理器
 * 加载应用资源（app内置：res/xxx） or 存储资源（下载皮肤包)
 *
 * App中创建属于自己的Resources类，给自定义的Resources类创建新的AssetManager，并重定向资源文件，就可以实现换肤的功能
 */
public class SkinManager {
    private static SkinManager instance;
    private Application application;
    private Resources appResources;     //用于加载app内置资源
    private Resources skinResources;    //用于加载皮肤包资源
    private String skinPackageName;     //皮肤包资源所在包名（注：皮肤包不在app内，也不限包名）
    private HashMap<String, SkinCache> cacheSkin;
    private static final String ADD_ASSET_PATH = "addAssetPath";//方法名
    private boolean isDefaultSkin;      //应用默认皮肤（app内置）

    private SkinManager(Application application){
        this.application = application;
        appResources = application.getResources();
        cacheSkin = new HashMap<>();
    }

    /**
     * 单例方法，目的是初始化app内置资源（越早越好，用户的操作可能是：换肤后的第2次冷启动）
     */
    public static void init(Application application){
        if(instance==null){
            synchronized (SkinManager.class){
                if(instance==null){
                    instance = new SkinManager(application);
                }
            }
        }
    }

    public static SkinManager getInstance(){
        return instance;
    }

    /**
     * todo 6.1 加载皮肤包资源
     * @param skinPath 皮肤包路径，为空则在家app内置资源
     */
    public void loaderSkinRecources(String skinPath){
        // 优化：如果没有皮肤包或者没做换肤动作，方法不执行直接返回！
        if(TextUtils.isEmpty(skinPath)){
            isDefaultSkin = true;
            return;
        }
        //todo 6.2 从缓存中读取 HashMap<String, SkinCache>
        //优化：app冷启动、热启动可以缓存对象
        if(cacheSkin.containsKey(skinPath)){
            isDefaultSkin = false;
            SkinCache skinCache = cacheSkin.get(skinPath);
            if(null !=skinCache){
                skinResources = skinCache.getSkinResources();
                skinPackageName = skinCache.getSkinPackagenName();
                return;
            }
        }
        try {
            //todo 6.3 反射AssetManager内部的addAssetPath方法设置皮肤包路径
            //创建资源管理器（此处不能用：application.getAssets()）
            AssetManager assetManager  = AssetManager.class.newInstance();
            //由于AssetManager中的assAssetPath和setApkAssets方法都被@hide，目前只能通过反射区执行方法 addAssetPath
            Method addAssetPath = assetManager.getClass().getDeclaredMethod(ADD_ASSET_PATH, String.class);
            //设置私有方法可访问
            addAssetPath.setAccessible(true);
            //执行addAssetPatch方法
            addAssetPath.invoke(assetManager,skinPath);
            //==============================================================================
            // 如果还是担心@hide限制，可以反射addAssetPathInternal()方法，参考源码366行 + 387行
            //==============================================================================

            //todo 6.4 构造外部皮肤包的Resources 传入上方的assetManager和配置信息
            // 创建外部加载的皮肤包文件Resources（注：依然是本应用加载）
            skinResources = new Resources(assetManager,appResources.getDisplayMetrics(),appResources.getConfiguration());


            //todo 6.5 通过下方的方法可以获取皮肤包的包名 getPackageArchiveInfo
            //  根据apk文件路径（皮肤包也是apk文件），获取应用的包名，从皮肤包的AndroidManifest.xml文件获取，所以这个文件不能少。
            // 而classex.dex可以去掉（未测试）。兼容5.0-9.0
            skinPackageName = application.getPackageManager().getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES).packageName;

            //无法获取皮肤包应用的包名，则加载app内置资源
            isDefaultSkin = TextUtils.isEmpty(skinPackageName);
            if(!isDefaultSkin){
                //todo 6.6 放入key是路径，value是SkinCache的缓存中
                cacheSkin.put(skinPath,new SkinCache(skinResources,skinPackageName));
            }
            Log.e("skinPackageName >>> ", skinPackageName);
        } catch (Exception e) {
            e.printStackTrace();
            // 发生异常，预判：通过skinPath获取skinPacakageName失败！
            isDefaultSkin = true;
        }
    }

    /**
     * todo 7 参考resources.arsc资源映射表
     *  通过resourceID获取到Name和Type，然后插件的皮肤包通过Name和type找到对应的resourceID
     * @param resourceId 资源ID值
     * @return 如果没有皮肤包则加载app内置资源ID，反之加载皮肤包指定资源ID
     */
    private int getSkinResourceIds(int resourceId){
        // 优化：如果没有皮肤包或者没做换肤动作，直接返回app内置资源！
        if(isDefaultSkin) {
            return resourceId;
        }

        // todo 7.1 使用app内置资源加载，是因为内置资源与皮肤包资源一一对应（“netease_bg”, “drawable”）
        String resourceName = appResources.getResourceEntryName(resourceId);
        String resourceType = appResources.getResourceTypeName(resourceId);

        // todo 7.2 动态获取皮肤包内的指定资源ID
        // getResources().getIdentifier(“netease_bg”, “drawable”, “com.netease.skin.packages”);
        int skinResourceId = skinResources.getIdentifier(resourceName,resourceType,skinPackageName);

        isDefaultSkin = skinResourceId==0;
        return isDefaultSkin ? resourceId:skinResourceId;
    }

    public boolean isDefaultSkin(){
        return isDefaultSkin;
    }

    //==============================================================================================
    public int getColor(int resourceId){
        int ids = getSkinResourceIds(resourceId);
        return isDefaultSkin?appResources.getColor(ids):skinResources.getColor(ids);
    }

    public ColorStateList getColorStateList(int resourceId){
        int ids =getSkinResourceIds(resourceId);
        return isDefaultSkin?appResources.getColorStateList(ids):skinResources.getColorStateList(ids);
    }

    public Drawable getDrawableOrMipMap(int resourceId){
        int ids = getSkinResourceIds(resourceId);
        return isDefaultSkin ? appResources.getDrawable(ids):skinResources.getDrawable(ids);
    }

    public String getString(int resourceId){
        int ids = getSkinResourceIds(resourceId);
        return isDefaultSkin?appResources.getString(ids):skinResources.getString(ids);
    }

    // 返回值特殊情况：可能是color / drawable / mipmap
    public Object getBackgroundOrSrc(int resourceId){
        // 需要获取当前属性的类型名Resources.getResourceTypeName(resourceId)再判断
        String resourceTypeName = appResources.getResourceTypeName(resourceId);
        switch (resourceTypeName) {
            case "color":
                return getColor(resourceId);
            case "mipmap":
            case "drawable":
                return getDrawableOrMipMap(resourceId);
        }
        return null;
    }

    //获得字体
    public Typeface getTypeface(int resourceId){
        //通过资源ID获取资源path
        String skinTypefacePath = getString(resourceId);
        if(TextUtils.isEmpty(skinTypefacePath)){
            return Typeface.DEFAULT;
        }
        return isDefaultSkin?
                Typeface.createFromAsset(appResources.getAssets(),skinTypefacePath):
                Typeface.createFromAsset(skinResources.getAssets(),skinTypefacePath);
    }
}

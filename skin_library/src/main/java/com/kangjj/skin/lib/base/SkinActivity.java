package com.kangjj.skin.lib.base;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.LayoutInflaterCompat;

import com.kangjj.skin.lib.SkinManager;
import com.kangjj.skin.lib.core.CustomAppCompatViewInflater;
import com.kangjj.skin.lib.core.ViewsMatch;
import com.kangjj.skin.lib.utils.ActionBarUtils;
import com.kangjj.skin.lib.utils.NavigationUtils;
import com.kangjj.skin.lib.utils.StatusBarUtils;

/**
 * 看源码的时候知道view的创建 假如 LayoutInflater.Factory2！=null, 就會有view = mFactory2.onCreateView(parent, name, context, attrs);
 * 而Activity已经实现了LayoutInflater.Factory2,所以只要在BaseActivity重写onCreateView方法，就可以创建控件
 * onCreateView创建控件的时候 根据名字（例如：LinearLayout->SkinnableLinearLayout、ImageView->SkinnableImageView）
 * 生成对应的自定义控件（偷梁换柱，其实源码就是这样处理）注意这里的自定义控件需要继承LinearLayoutCompat、AppCompatTextView等对应的兼容控件，
 * 并且这些自定义控件都实现了ViewsMatch接口。另外需要在attribute.xml文件中配置需要的自定义属性
 * （例如<declare-styleable name="SkinnableTextView">
 *         <attr name="android:background" />
 *         <attr name="android:textColor" />
 *     </declare-styleable> ）。
 * 在自定义控件实例化的时候将自定义属性保存在key为自定义属性，value为resourceId的Map中。
 * 当用户点击切换黑夜白天模式的的时候，通过getDelegate().setLocalNightMode(nightMode)设置当前是白天还是黑夜模式，
 * （<activity android:name=".MainActivity"  android:configChanges="uiMode">  需要配置此属性=uiMode，才能更换navigation ）
 * 这样后面控件获取到背景或者字体颜色就会适配到values或者values-night的颜色
 * 然后getWindow().getDecorView()通知每个子控件控件需要换肤了（前面每个控件都进行了焕肤的监听，这里用到观察者模式）。
 * 每个换肤的控件 根据styleable获取控件某属性的resourceId进行重新设置背景或者设置字体颜色（注意：
 * 考虑到各个版本的兼容，这里需要使用ContextCompat获取背景或者颜色等）
 */
public class SkinActivity extends AppCompatActivity {

    private CustomAppCompatViewInflater viewInflater;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        LayoutInflaterCompat.setFactory2(layoutInflater,this);
        super.onCreate(savedInstanceState);
    }

    //每个控件创建都会走这里
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        if(openChangeSkin()){
            if(viewInflater == null){
                viewInflater = new CustomAppCompatViewInflater(context);
            }
            viewInflater.setName(name);
            viewInflater.setAttrs(attrs);
            //这里如果返回空没关系，系统会自己处理
            return viewInflater.autoMatch();
        }
        return super.onCreateView(name, context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void defaultSkin(int themeColorId){
        this.skinDynamic(null,themeColorId);
    }

    /**
     * 动态换肤（api限制：5.0版本）
     * @param skinPath
     * @param themeColorId
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void skinDynamic(String skinPath, int themeColorId){
        SkinManager.getInstance().loaderSkinRecources(skinPath);
        if(themeColorId!=0){
            int themeColor = SkinManager.getInstance().getColor(themeColorId);
            StatusBarUtils.forStatusBar(this,themeColor);
            NavigationUtils.forNavigation(this,themeColor);
            ActionBarUtils.forActionBar(this,themeColor);
        }
        applyViews(getWindow().getDecorView());
    }

    /**
     * 控件回调监听，匹配上则给控件执行换肤方法
     */
    protected void applyViews(View view) {
        if (view instanceof ViewsMatch) {
            ViewsMatch viewsMatch = (ViewsMatch) view;
            viewsMatch.skinnableView();
        }

        if (view instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) view;
            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                applyViews(parent.getChildAt(i));
            }
        }
    }

//    protected void setDayNightMode(@AppCompatDelegate.NightMode int nightMode){
//        getDelegate().setLocalNightMode(nightMode);
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//            //换状态栏
//            StatusBarUtils.forStatusBar(this);
//            //换标题栏
//            ActionBarUtils.forActionBar(this);
//            //换底部导航栏
//            NavigationUtils.forNavigation(this);
//        }
//        View decorView = getWindow().getDecorView();
//        applyDayNightForView(decorView);
//
//    }
//
//    /**
//     * 回调接口 给具体控件换肤操作
//     */
//    protected void applyDayNightForView(View view) {
//        if(view instanceof ViewsMatch){
//            ViewsMatch viewsMatch = (ViewsMatch) view;
//            viewsMatch.skinnableView();
//        }
//        if(view instanceof ViewGroup){
//            ViewGroup parent = (ViewGroup) view;
//            int childCount = parent.getChildCount();
//            for (int i = 0; i < childCount; i++) {
//                applyDayNightForView(parent.getChildAt(i));
//            }
//        }
//    }

    /**
     * @return 是否开启换肤，增加此开关是为了避免开发者误继承此父类，导致未知bug
     */
    protected boolean openChangeSkin() {
        return false;
    }
}

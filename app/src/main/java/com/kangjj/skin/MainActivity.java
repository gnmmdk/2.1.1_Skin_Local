package com.kangjj.skin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

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
 * 这样后面控件获取到背景或者字体颜色就会适配到values或者values-night的颜色
 * 然后getWindow().getDecorView()通知每个子控件控件需要换肤了（前面每个控件都进行了焕肤的监听，这里用到观察者模式）。
 * 每个换肤的控件 根据styleable获取控件某属性的resourceId进行重新设置背景或者设置字体颜色（注意：
 * 考虑到各个版本的兼容，这里需要使用ContextCompat获取背景或者颜色等）
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
